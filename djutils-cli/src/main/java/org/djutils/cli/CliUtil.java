package org.djutils.cli;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.djutils.reflection.ClassUtil;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.IHelpSectionRenderer;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

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
 * Copyright (c) 2019-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class CliUtil
{
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

    /**
     * Parse the command line for the program. Register Unit converters, parse the command line, catch --help, --version and
     * errors. If the program implements the Checkable interface, it calls the "check" method of the class that can take care of
     * further checks of the CLI arguments. Potentially, check() can also provide other initialization of the program to be
     * executed, but this can better be provided by other methods in main(). The method will exit on requesting help or version
     * information, or when the arguments are not complete or not correct.
     * @param program Object; the potentially checkable program with the &#64;Option information
     * @param args String[]; the arguments from the command line
     */
    public static void execute(final Object program, final String[] args)
    {
        execute(new CommandLine(program), args);
    }

    /**
     * Parse the given CommandLine object, that has been generated for a program. Register Unit converters, parse the command
     * line, catch --help, --version and errors. If the program implements the Checkable interface, it calls the "check" method
     * of the class that can take care of further checks of the CLI arguments. Potentially, check() can also provide other
     * initialization of the program to be executed, but this can better be provided by other methods in main(). The method will
     * exit on requesting help or version information, or when the arguments are not complete or not correct.
     * @param commandLine CommandLine; the CommandLine object for the program with the &#64;Option information
     * @param args String[]; the arguments from the command line
     */
    public static void execute(final CommandLine commandLine, final String[] args)
    {
        // set-up a new provider for default @Option values that can be overridden
        CommandLine.IDefaultValueProvider vp = new CommandLine.IDefaultValueProvider()
        {
            @Override
            public String defaultValue(final ArgSpec argSpec) throws Exception
            {
                String fieldName = ((Field) argSpec.userObject()).getName();
                Class<?> fieldClass = null;
                try
                {
                    Field field = ClassUtil.resolveField(commandLine.getCommand().getClass(), fieldName);
                    fieldClass = field.getDeclaringClass();
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

        // set-up the version provider that provides a version number that can be overridden
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

        // parse the command line arguments and handle errors
        commandLine.getCommandSpec().parser().collectErrors(true);
        ParseResult parseResult = commandLine.parseArgs(args);
        List<Exception> parseErrors = parseResult.errors();
        if (parseErrors.size() > 0)
        {
            for (Exception e : parseErrors)
            {
                System.err.println(e.getMessage());
            }
            System.exit(-1);
        }

        // process help and usage (using overridden values)
        if (parseResult.isUsageHelpRequested())
        {
            commandLine.usage(System.out);
            System.exit(0);
        }
        else if (parseResult.isVersionHelpRequested())
        {
            commandLine.printVersionHelp(System.out);
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
                System.exit(-1);
            }
        }
    }

    /**
     * Change the value of a property of an already present &#64;Option annotation of a field in a class or superclass.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue Object; the new value of the property
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     */
    public static void changeOptionProperty(final Class<?> programClass, final String fieldName, final String propertyName,
            final Object newValue) throws CliException, NoSuchFieldException
    {
        Field field = ClassUtil.resolveField(programClass, fieldName);
        Option optionAnnotation = field.getAnnotation(Option.class);
        if (optionAnnotation == null)
        {
            throw new CliException(
                    String.format("@Option annotation not found for field %s in class %s", fieldName, programClass.getName()));
        }
        String key = makeOverrideKeyProperty(field.getDeclaringClass(), fieldName, propertyName);
        overrideMap.put(key, newValue);
    }

    /**
     * Change the value of a property of an already present &#64;Option annotation of a field in a class or superclass.
     * @param program Object; the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue Object; the new value of the property
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
     * @param program Object; the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param newDefaultValue String; the new value of the defaultValue
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
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param newDefaultValue String; the new value of the defaultValue
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
     * @param program Object; the program for which the cli property should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue Object; the new value of the property
     * @throws CliException when the class is not annotated with &#64;Command
     */
    private static void changeCommandProperty(final Object program, final String propertyName, final Object newValue)
            throws CliException
    {
        changeCommandProperty(program.getClass(), propertyName, newValue);
    }

    /**
     * Change the value of a property of an already present &#64;Command annotation in a class or superclass of that class.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue Object; the new value of the property
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
     * @param program Object; the program for which the cli property should be changed
     * @param newName String; the new value of the name
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandName(final Object program, final String newName) throws CliException
    {
        changeCommandProperty(program, "name", newName);
    }

    /**
     * Change the value of the 'name' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param newName String; the new value of the name
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandName(final Class<?> programClass, final String newName) throws CliException
    {
        changeCommandProperty(programClass, "name", newName);
    }

    /**
     * Change the value of the 'description' property of an already present &#64;Command annotation in a class or superclass of
     * that class.
     * @param program Object; the program for which the cli property should be changed
     * @param newDescription String; the new value of the description
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandDescription(final Object program, final String newDescription) throws CliException
    {
        changeCommandProperty(program, "description", new String[] {newDescription});
    }

    /**
     * Change the value of the 'description' property of an already present &#64;Command annotation in a class or superclass of
     * that class.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param newDescription String; the new value of the description
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandDescription(final Class<?> programClass, final String newDescription) throws CliException
    {
        changeCommandProperty(programClass, "description", new String[] {newDescription});
    }

    /**
     * Change the value of the 'version' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param program Object; the program for which the cli property should be changed
     * @param newVersion String; the new value of the version
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandVersion(final Object program, final String newVersion) throws CliException
    {
        changeCommandProperty(program, "version", new String[] {newVersion});
    }

    /**
     * Change the value of the 'version' property of an already present &#64;Command annotation in a class or superclass of that
     * class.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param newVersion String; the new value of the version
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static void changeCommandVersion(final Class<?> programClass, final String newVersion) throws CliException
    {
        changeCommandProperty(programClass, "version", new String[] {newVersion});
    }

    /**
     * Return the &#64;Command annotation of a class or one of its superclasses.
     * @param programClass Class&lt;?&gt;; the class of the program for which the annotation should be retrieved
     * @return Command; the &#64;Command annotation of the class or one of its superclasses
     * @throws CliException when the class or one of its superclasses is not annotated with &#64;Command
     */
    public static Command getCommandAnnotation(final Class<?> programClass) throws CliException
    {
        return getCommandAnnotationClass(programClass).getDeclaredAnnotation(Command.class);
    }

    /**
     * Return the &#64;Command annotation of a class or one of its superclasses.
     * @param programClass Class&lt;?&gt;; the class of the program for which the annotation should be retrieved
     * @return Class&lt;?&gt;; the class or superclass in which the &#64;Command annotation was found
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
     * @param programClass Class&lt;?&gt;; the class for which to retrieve the version. The class should be annotated with
     *            &#64;Command
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
     * @param program Object; the program for which to retrieve the version. The program's class should be annotated with
     *            &#64;Command
     * @return String[] the version string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String[] getCommandVersion(final Object program) throws CliException
    {
        return getCommandVersion(program.getClass());
    }

    /**
     * @param programClass Class&lt;?&gt;; the class for which to retrieve the program name. The class should be annotated with
     *            &#64;Command
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
     * @param program Object; the program for which to retrieve the program name. The program's class should be annotated with
     *            &#64;Command
     * @return String the name string
     * @throws CliException when the class is not annotated with &#64;Command
     */
    public static String getCommandName(final Object program) throws CliException
    {
        return getCommandName(program.getClass());
    }

    /**
     * @param programClass Class&lt;?&gt;; the class for which to retrieve the description. The class should be annotated with
     *            &#64;Command
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
     * @param program Object; the program for which to retrieve the description. The program's class should be annotated with
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
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName String; the name of the property to change the value of
     * @return String; the override key for an option property
     */
    static String makeOverrideKeyProperty(final Class<?> programClass, final String fieldName, final String propertyName)
    {
        return programClass.getName() + "%" + fieldName + "%" + propertyName;
    }

    /**
     * Make the override key for the Command annotation.
     * @param programClass Class&lt;?&gt;; the class of the program for which the options should be changed
     * @param propertyName String; the name of the annotation property to change the value of
     * @return String; the override key for an option property
     */
    static String makeOverrideKeyCommand(final Class<?> programClass, final String propertyName)
    {
        return programClass.getName() + "%" + propertyName;
    }
}
