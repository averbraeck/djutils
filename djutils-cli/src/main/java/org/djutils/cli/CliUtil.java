package org.djutils.cli;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.djutils.reflection.ClassUtil;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.IHelpSectionRenderer;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.Unmatched;

/**
 * CliUtil offers a helper method to display --help and --version without starting the program. The method is used as follows:
 * 
 * <pre>
 * public static void main(final String[] args) throws Exception
 * {
 *     Program program = new Program(); // initialize the Checkable class with the &#64;Option information
 *     CliUtil.execute(program, args); // register Unit converters, parse the command line, catch --help, --version and error
 *     // do rest of what the main method should do
 * }
 * </pre>
 * 
 * When the program is Checkable, the <code>check()</code> method is called after the arguments have been parsed. Here, further
 * checks on the arguments (i.e., range checks) can be carried out. Potentially, check() can also provide other initialization
 * of the program to be executed, but this can better be provided by other methods in main() . Make sure that expensive
 * initialization is <b>not</b> carried out in the constructor of the program class that is given to the execute method.
 * Alternatively, move the command line options to a separate class, e.g. called Options and initialize that class rather than
 * the real program class. The real program can then take the values of the program from the Options class. An example:
 * 
 * <pre>
 * public class Program
 * {
 *     &#64;Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
 *     public static class Options implements Checkable
 *     {
 *         &#64;Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "80")
 *         private int port;
 * 
 *         public int getPort()
 *         {
 *             return this.port;
 *         }
 * 
 *         &#64;Override
 *         public void check() throws Exception
 *         {
 *             if (this.port &lt;= 0 || this.port &gt; 65535)
 *                 throw new Exception("Port should be between 1 and 65535");
 *         }
 *     }
 * 
 *     public Program()
 *     {
 *         // initialization for the program; avoid really starting things
 *     }
 * 
 *     public static void main(final String[] args)
 *     {
 *         Options options = new Options();
 *         CliUtil.execute(options, args);
 *         System.out.println("port = " + options.getPort());
 *         // you can now call methods on the program, e.g. for real initialization using the CLI parameters in options
 *     }
 * }
 * </pre>
 * 
 * <br>
 * Copyright (c) 2019-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class CliUtil
{
    /** Mixin class to provide a --locale option, and a default locale. */
    public static class InitLocale
    {
        /** the locale. */
        @Option(names = {"--locale"}, defaultValue = "en-US", description = "locale for variables with units.")
        private String locale;
    }

    /** Retrieval class to provide a --locale option, and a default locale. */
    public static class RetrieveLocale
    {
        /** the locale. */
        @Option(names = {"--locale"}, defaultValue = "en-US", description = "locale for variables with units.")
        private String locale;

        /**
         * @return the locale from --locale
         */
        String getLocale()
        {
            return this.locale;
        }

        /** The other options. */
        @Unmatched
        private List<String> remainder;
    }

    /** Utility class constructor. */
    private CliUtil()
    {
        // Utility class
    }

    /**
     * The map with overrides for default values and other Option and Program annotation values. values in the map are:
     * <ul>
     * <li>className%fieldName%propertyName for the &#64;Option annotation for field fieldName within the class named className,
     * and the annotation property propertyName. An example of the propertyName is "defaultValue"</li>
     * <li>className%propertyName for the &#64;Command annotation for the annotation property with propertyName in the named
     * class. Examples of the propertyName are "name", "version", and "description"</li>
     * </ul>
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    static Map<String, Object> overrideMap = new LinkedHashMap<>();

    /** The set locale for the last defaultValue to be parsed. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    private static Locale defaultValueLocale = Locale.US;

    /** The locale as set in the --locale option. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    private static Locale localeOption = null;

    /** Whether we are parsing a default value or not. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    private static boolean parseDefaultValue = false;

    /** Whether we are in test mode and should throw an exception instead of System.exit() with -v or -h. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    static boolean testMode = false;

    /**
     * Parse the command line for the program. Register Unit converters, parse the command line, catch --help, --version and
     * errors. If the program implements the Checkable interface, it calls the "check" method of the class that can take care of
     * further checks of the CLI arguments. Potentially, check() can also provide other initialization of the program to be
     * executed, but this can better be provided by other methods in main(). The method will exit on requesting help or version
     * information, or when the arguments are not complete or not correct.
     * @param program the potentially checkable program with the &#64;Option information
     * @param args the arguments from the command line
     */
    public static void execute(final Object program, final String[] args)
    {
        execute(new CommandLine(program), args);
    }

    /**
     * Parse the given CommandLine object, that has been generated for a program. Register Unit converters, parse the command
     * line, catch --help, --version, --locale, and errors. If the program implements the Checkable interface, it calls the
     * "check" method of the class that can take care of further checks of the CLI arguments. Potentially, check() can also
     * provide other initialization of the program to be executed, but this can better be provided by other methods in main().
     * The method will exit on requesting help or version information, or when the arguments are not complete or not correct.
     * @param commandLine the CommandLine object for the program with the &#64;Option information
     * @param args the arguments from the command line
     */
    @SuppressWarnings("checkstyle:methodlength")
    public static void execute(final CommandLine commandLine, final String[] args)
    {
        // Issue #13. add the --locale option
        var initLocale = new InitLocale();
        commandLine.addMixin("locale", initLocale);

        // set-up a new provider for default @Option values that can be overridden
        CommandLine.IDefaultValueProvider vp = new CommandLine.IDefaultValueProvider()
        {
            @Override
            public String defaultValue(final ArgSpec argSpec) throws Exception
            {
                defaultValueLocale = Locale.US; // set to default if no annotation
                String fieldName = ((Field) argSpec.userObject()).getName();
                Class<?> fieldClass = null;
                try
                {
                    Field field = ClassUtil.resolveField(commandLine.getCommand().getClass(), fieldName);
                    fieldClass = field.getDeclaringClass();
                    if (field.isAnnotationPresent(DefaultValueLocale.class))
                    {
                        var loc = field.getAnnotation(DefaultValueLocale.class).value();
                        parseDefaultValue = true; // the next parse will be an annotated default value
                        defaultValueLocale = parseLocale(loc);
                    }
                }
                catch (NoSuchFieldException nsfe)
                {
                    fieldClass = commandLine.getCommand().getClass();
                }
                String key = CliUtil.makeOverrideKeyProperty(fieldClass, fieldName, "defaultValue");
                if (CliUtil.overrideMap.containsKey(key))
                {
                    return CliUtil.overrideMap.get(key).toString();
                }
                else
                {
                    return argSpec.defaultValue();
                }
            }
        };
        commandLine.setDefaultValueProvider(vp);

        // check @Program name override
        String programKey = makeOverrideKeyCommand(commandLine.getCommand().getClass(), "name");
        if (overrideMap.containsKey(programKey))
        {
            commandLine.setCommandName(overrideMap.get(programKey).toString());
        }

        // set-up the version provider that provides a version number that can be overridden
        String versionKey = makeOverrideKeyCommand(commandLine.getCommand().getClass(), "version");
        if (overrideMap.containsKey(versionKey))
        {
            commandLine.getCommandSpec().versionProvider(new IVersionProvider()
            {
                @Override
                public String[] getVersion() throws Exception
                {
                    if (overrideMap.get(versionKey) instanceof String[])
                    {
                        return (String[]) overrideMap.get(versionKey);
                    }
                    return new String[] {overrideMap.get(versionKey).toString()};
                }
            });
        }

        // set-up the description provider that provides a description that can be overridden
        Map<String, IHelpSectionRenderer> helpMap = commandLine.getHelpSectionMap();
        final IHelpSectionRenderer defaultDescriptionRenderer = helpMap.get("description");
        helpMap.put("description", new IHelpSectionRenderer()
        {
            @Override
            public String render(final Help help)
            {
                String descriptionKey = makeOverrideKeyCommand(commandLine.getCommand().getClass(), "description");
                if (overrideMap.containsKey(descriptionKey))
                {
                    if (overrideMap.get(descriptionKey) instanceof String[])
                    {
                        StringBuilder sb = new StringBuilder();
                        for (String line : (String[]) overrideMap.get(descriptionKey))
                        {
                            sb.append(line);
                            sb.append("\n");
                        }
                        return sb.toString();
                    }
                    return overrideMap.get(descriptionKey).toString();
                }
                return defaultDescriptionRenderer.render(help);
            }
        });
        commandLine.setHelpSectionMap(helpMap);

        // register the DJUNITS converters
        CliUnitConverters.registerAll(commandLine);
        registerLocaleFloatConverters(commandLine);

        // Issue #13. set the locale, and store the old one
        Locale saveLocale = Locale.getDefault();
        RetrieveLocale retrieveLocale = new RetrieveLocale();
        CommandLine cmdLocale = new CommandLine(retrieveLocale);
        cmdLocale.parseArgs(args);
        localeOption =
                new HashSet<String>(Arrays.asList(args)).contains("--locale") ? parseLocale(retrieveLocale.getLocale()) : null;

        // parse the command line arguments and handle errors, now based on the set locale
        commandLine.getCommandSpec().parser().collectErrors(true);
        ParseResult parseResult = commandLine.parseArgs(args);
        List<Exception> parseErrors = parseResult.errors();
        if (parseErrors.size() > 0)
        {
            for (Exception e : parseErrors)
            {
                System.err.println(e.getMessage());
            }
            if (testMode)
            {
                throw new CliRuntimeException("parse errors");
            }
            System.exit(-1);
        }

        // process help and usage (using overridden values)
        if (parseResult.isUsageHelpRequested())
        {
            commandLine.usage(System.out);
            if (testMode)
            {
                throw new CliRuntimeException("usage help requested");
            }
            System.exit(0);
        }
        else if (parseResult.isVersionHelpRequested())
        {
            commandLine.printVersionHelp(System.out);
            if (testMode)
            {
                throw new CliRuntimeException("version help requested");
            }
            System.exit(0);
        }

        // check the values for the variables
        Object program = commandLine.getCommand();
        if (program instanceof Checkable)
        {
            try
            {
                ((Checkable) program).check();
            }
            catch (Exception exception)
            {
                System.err.println(exception.getMessage());
                if (testMode)
                {
                    throw new CliRuntimeException("check error");
                }
                System.exit(-1);
            }
        }

        // Issue #13. reset the locale
        Locale.setDefault(saveLocale);
    }

    /**
     * Change the value of a property of an already present &#64;Option annotation of a field in a class or superclass.
     * @param programClass the class of the program for which the options should be changed
     * @param fieldName the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName the name of the property to change the value of
     * @param newValue the new value of the property
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     */
    public static void changeOptionProperty(final Class<?> programClass, final String fieldName, final String propertyName,
            final Object newValue) throws CliException, NoSuchFieldException
    {
        findOptionFieldIncludingMixins(programClass, fieldName);
        String key = makeOverrideKeyProperty(programClass, fieldName, propertyName);
        overrideMap.put(key, newValue);
    }

    /**
     * Find a field with an &#64;Option annotation in the rootClass or a &#64;Mixin of the rootClass.
     * @param rootClass the root class to start searching
     * @param fieldName the name of the field with an &#64;Option annotation to search for
     * @return the &#64;Option field that was found; when not found or no &#64;Option annotation, an exception is thrown
     * @throws CliException on not finding the &#64;Option field in the rootClass or a &#64;Mixin class
     */
    private static Field findOptionFieldIncludingMixins(final Class<?> rootClass, final String fieldName) throws CliException
    {
        Set<Class<?>> visited = new HashSet<>();
        Field result = findOptionFieldIncludingMixins(rootClass, fieldName, visited);
        if (result == null)
        {
            throw new CliException("No @Option field '" + fieldName + "' in " + rootClass.getName() + " or its @Mixin classes");
        }
        return result;
    }

    /**
     * Recursively find a field with an &#64;Option annotation in 'type' or a mixin of 'type'.
     * @param type the class to inspect
     * @param fieldName the name of the field with an &#64;Option annotation to search for
     * @param visited A Set of classes that were already checked to avoid duplication and looping
     * @return the &#64;Option field that was found; when not found or no &#64;Option annotation, null is returned
     */
    private static Field findOptionFieldIncludingMixins(final Class<?> type, final String fieldName,
            final Set<Class<?>> visited)
    {
        if (type == null || !visited.add(type))
        {
            return null;
        }

        // 1) Walk class hierarchy for a matching @Option field
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass())
        {
            try
            {
                Field f = c.getDeclaredField(fieldName);
                if (f.isAnnotationPresent(Option.class))
                {
                    return f;
                }
            }
            catch (NoSuchFieldException ignored)
            {
            }
        }

        // 2) Recurse into @Mixin fields
        for (Field f : type.getDeclaredFields())
        {
            if (f.isAnnotationPresent(Mixin.class))
            {
                Field candidate = findOptionFieldIncludingMixins(f.getType(), fieldName, visited);
                if (candidate != null)
                {
                    return candidate;
                }
            }
        }

        return null;
    }

    /**
     * Change the value of a property of an already present &#64;Option annotation of a field in a class or superclass.
     * @param program the program for which the options should be changed
     * @param fieldName the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName the name of the property to change the value of
     * @param newValue the new value of the property
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     */
    public static void changeOptionProperty(final Object program, final String fieldName, final String propertyName,
            final Object newValue) throws CliException, NoSuchFieldException
    {
        changeOptionProperty(program.getClass(), fieldName, propertyName, newValue);
    }

    /**
     * Change the default value of an already present &#64;Option annotation of the "defaultValue" field in a class or
     * superclass.
     * @param program the program for which the options should be changed
     * @param fieldName the field for which the defaultValue in &#64;Option should be changed
     * @param newDefaultValue the new value of the defaultValue
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     */
    public static void changeOptionDefault(final Object program, final String fieldName, final String newDefaultValue)
            throws CliException, NoSuchFieldException
    {
        changeOptionProperty(program, fieldName, "defaultValue", newDefaultValue);
    }

    /**
     * Change the default value of an already present &#64;Option annotation of the "defaultValue" field in a class or
     * superclass.
     * @param programClass the class of the program for which the options should be changed
     * @param fieldName the field for which the defaultValue in &#64;Option should be changed
     * @param newDefaultValue the new value of the defaultValue
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     */
    public static void changeOptionDefault(final Class<?> programClass, final String fieldName, final String newDefaultValue)
            throws CliException, NoSuchFieldException
    {
        changeOptionProperty(programClass, fieldName, "defaultValue", newDefaultValue);
    }

    /**
     * Change the value of a property of an already present &#64;Command annotation in a class or superclass of that class.
     * @param program the program for which the cli property should be changed
     * @param propertyName the name of the property to change the value of
     * @param newValue the new value of the property
     * @throws CliException when the class is not annotated with &#64;Command
     */
    private static void changeCommandProperty(final Object program, final String propertyName, final Object newValue)
            throws CliException
    {
        changeCommandProperty(program.getClass(), propertyName, newValue);
    }

    /**
     * Change the value of a property of an already present &#64;Command annotation in a class or superclass of that class.
     * @param programClass the class of the program for which the options should be changed
     * @param propertyName the name of the property to change the value of
     * @param newValue the new value of the property
     * @throws CliException when the class is not annotated with &#64;Command
     */
    private static void changeCommandProperty(final Class<?> programClass, final String propertyName, final Object newValue)
            throws CliException
    {
        Class<?> declaringClass = getCommandAnnotationClass(programClass);
        String key = makeOverrideKeyCommand(declaringClass, propertyName);
        overrideMap.put(key, newValue);
    }

    /**
     * Change the value of the 'name' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param program the program for which the cli property should be changed
     * @param newName the new value of the name
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandName(final Object program, final String newName) throws CliException
    {
        changeCommandProperty(program, "name", newName);
    }

    /**
     * Change the value of the 'name' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param programClass the class of the program for which the options should be changed
     * @param newName the new value of the name
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandName(final Class<?> programClass, final String newName) throws CliException
    {
        changeCommandProperty(programClass, "name", newName);
    }

    /**
     * Change the value of the 'description' property of an already present &#64;Command annotation in a class or superclass of
     * that class.
     * @param program the program for which the cli property should be changed
     * @param newDescription the new value of the description
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandDescription(final Object program, final String newDescription) throws CliException
    {
        changeCommandProperty(program, "description", new String[] {newDescription});
    }

    /**
     * Change the value of the 'description' property of an already present &#64;Command annotation in a class or superclass of
     * that class.
     * @param programClass the class of the program for which the options should be changed
     * @param newDescription the new value of the description
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandDescription(final Class<?> programClass, final String newDescription) throws CliException
    {
        changeCommandProperty(programClass, "description", new String[] {newDescription});
    }

    /**
     * Change the value of the 'version' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param program the program for which the cli property should be changed
     * @param newVersion the new value of the version
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandVersion(final Object program, final String newVersion) throws CliException
    {
        changeCommandProperty(program, "version", new String[] {newVersion});
    }

    /**
     * Change the value of the 'version' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param programClass the class of the program for which the options should be changed
     * @param newVersion the new value of the version
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandVersion(final Class<?> programClass, final String newVersion) throws CliException
    {
        changeCommandProperty(programClass, "version", new String[] {newVersion});
    }

    /**
     * Return the &#64;Command annotation of a class or one of its superclasses.
     * @param programClass the class of the program for which the annotation should be retrieved
     * @return the &#64;Command annotation of the class or one of its superclasses
     * @throws CliException when the class or one of its superclasses is not annotated with &#64;Command
     */
    public static Command getCommandAnnotation(final Class<?> programClass) throws CliException
    {
        return getCommandAnnotationClass(programClass).getDeclaredAnnotation(Command.class);
    }

    /**
     * Return the &#64;Command annotation of a class or one of its superclasses.
     * @param programClass the class of the program for which the annotation should be retrieved
     * @return the class or superclass in which the &#64;Command annotation was found
     * @throws CliException when the class or one of its superclasses is not annotated with &#64;Command
     */
    public static Class<?> getCommandAnnotationClass(final Class<?> programClass) throws CliException
    {
        Class<?> clazz = programClass;
        while (clazz != null)
        {
            Command commandAnnotation = clazz.getDeclaredAnnotation(Command.class);
            if (commandAnnotation != null)
            {
                return clazz;
            }
            clazz = clazz.getSuperclass();
        }
        throw new CliException(
                String.format("@Command annotation not found for class %s or one of its superclasses", programClass.getName()));
    }

    /**
     * @param programClass the class for which to retrieve the version. The class should be annotated with &#64;Command
     * @return String[] the version string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String[] getCommandVersion(final Class<?> programClass) throws CliException
    {
        String versionKey = makeOverrideKeyCommand(programClass, "version");
        if (overrideMap.containsKey(versionKey))
        {
            Object version = overrideMap.get(versionKey);
            if (version instanceof String[])
            {
                return (String[]) version;
            }
            return new String[] {version.toString()};
        }
        return getCommandAnnotation(programClass).version();
    }

    /**
     * @param program the program for which to retrieve the version. The program's class should be annotated with &#64;Command
     * @return String[] the version string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String[] getCommandVersion(final Object program) throws CliException
    {
        return getCommandVersion(program.getClass());
    }

    /**
     * @param programClass the class for which to retrieve the program name. The class should be annotated with &#64;Command
     * @return String the name string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String getCommandName(final Class<?> programClass) throws CliException
    {
        String nameKey = makeOverrideKeyCommand(programClass, "name");
        if (overrideMap.containsKey(nameKey))
        {
            return overrideMap.get(nameKey).toString();
        }
        return getCommandAnnotation(programClass).name();
    }

    /**
     * @param program the program for which to retrieve the program name. The program's class should be annotated with
     *            &#64;Command
     * @return String the name string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String getCommandName(final Object program) throws CliException
    {
        return getCommandName(program.getClass());
    }

    /**
     * @param programClass the class for which to retrieve the description. The class should be annotated with &#64;Command
     * @return String[] the description string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String[] getCommandDescription(final Class<?> programClass) throws CliException
    {
        String descriptionKey = makeOverrideKeyCommand(programClass, "description");
        if (overrideMap.containsKey(descriptionKey))
        {
            Object description = overrideMap.get(descriptionKey);
            if (description instanceof String[])
            {
                return (String[]) description;
            }
            return new String[] {description.toString()};
        }
        return getCommandAnnotation(programClass).description();
    }

    /**
     * @param program the program for which to retrieve the description. The program's class should be annotated with
     *            &#64;Command
     * @return String[] the description string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String[] getCommandDescription(final Object program) throws CliException
    {
        return getCommandDescription(program.getClass());
    }

    /**
     * Make the override key for an option property.
     * @param programClass the class of the program for which the options should be changed
     * @param fieldName the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName the name of the property to change the value of
     * @return the override key for an option property
     */
    static String makeOverrideKeyProperty(final Class<?> programClass, final String fieldName, final String propertyName)
    {
        return programClass.getName() + "%" + fieldName + "%" + propertyName;
    }

    /**
     * Make the override key for the Command annotation.
     * @param programClass the class of the program for which the options should be changed
     * @param propertyName the name of the annotation property to change the value of
     * @return the override key for an option property
     */
    static String makeOverrideKeyCommand(final Class<?> programClass, final String propertyName)
    {
        return programClass.getName() + "%" + propertyName;
    }

    /**
     * Register the locale-dependent converters for double, Double, float, Float.
     * @param cmd the CommandLine for which the DJUNITS converters should be registered
     */
    static void registerLocaleFloatConverters(final CommandLine cmd)
    {
        cmd.registerConverter(Double.class, new DoubleConverter());
        cmd.registerConverter(double.class, new DoubleConverter());
        cmd.registerConverter(Float.class, new FloatConverter());
        cmd.registerConverter(float.class, new FloatConverter());
    }

    /**
     * Convert a Double String on the command line to a Double, taking into account the locale.
     */
    public static class DoubleConverter implements ITypeConverter<Double>
    {
        @Override
        public Double convert(final String value) throws Exception
        {
            prepareLocale();
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number;
            double ret;
            try
            {
                number = format.parse(value);
                ret = number.doubleValue();
            }
            catch (Exception e)
            {
                System.err.println("ERROR parsing double value " + value + ": " + e.getMessage());
                ret = Double.NaN;
            }
            restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a Double String on the command line to a Double, taking into account the locale.
     */
    public static class FloatConverter implements ITypeConverter<Float>
    {
        @Override
        public Float convert(final String value) throws Exception
        {
            prepareLocale();
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number;
            float ret;
            try
            {
                number = format.parse(value);
                ret = number.floatValue();
            }
            catch (Exception e)
            {
                System.err.println("ERROR parsing double value " + value + ": " + e.getMessage());
                ret = Float.NaN;
            }
            restoreLocale();
            return ret;
        }
    }

    /** temporarily save the locale. */
    private static Locale saveLocaleForDefault;

    /**
     * Prepare the setting of a Locale for a default value or a value.
     */
    static void prepareLocale()
    {
        saveLocaleForDefault = Locale.getDefault();
        Locale.setDefault(
                CliUtil.localeOption != null && !CliUtil.parseDefaultValue ? CliUtil.localeOption : CliUtil.defaultValueLocale);
    }

    /**
     * Restore the setting of a Locale for a default value or a value.
     */
    static void restoreLocale()
    {
        Locale.setDefault(saveLocaleForDefault);
        CliUtil.defaultValueLocale = Locale.US;
        CliUtil.parseDefaultValue = false;
    }

    /**
     * Parse a locale string like "en-US" or "en_GB" or "nl" or "nl_NL".
     * @param localeStr the string to parse
     * @return the Locale belonging to the string
     */
    private static Locale parseLocale(final String localeStr)
    {
        var s = localeStr.replaceAll("_", "-");
        var sa = s.split("\\-", 3);
        return sa.length == 3 ? new Locale(sa[0], sa[1], sa[2])
                : sa.length == 2 ? new Locale(sa[0], sa[1]) : sa.length == 1 ? new Locale(sa[0]) : new Locale(s);
    }

}
