package org.djutils.cli;

import java.lang.reflect.Field;
import java.util.List;

import org.djutils.reflection.ClassUtil;

import picocli.CommandLine;
import picocli.CommandLine.Command;
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
 * Copyright (c) 2019-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class CliUtil
{
    /**
     * Register Unit converters, parse the command line, catch --help, --version and errors. If the program implements the
     * Checkable interface, it calls the "check" method of the class that can take care of further checks of the CLI arguments.
     * Potentially, check() can also provide other initialization of the program to be executed, but this can better be provided
     * by other methods in main(). The method will exit on requesting help or version information, or when the arguments are not
     * complete or not correct.
     * @param program Checkable; the checkable program with the &#64;Option information
     * @param args String[]; the arguments from the command line
     */
    public static void execute(final Object program, final String[] args)
    {
        CommandLine cmd = new CommandLine(program);
        CliUnitConverters.registerAll(cmd);
        cmd.getCommandSpec().parser().collectErrors(true);
        ParseResult parseResult = cmd.parseArgs(args);
        List<Exception> parseErrors = parseResult.errors();
        if (parseErrors.size() > 0)
        {
            for (Exception e : parseErrors)
            {
                System.err.println(e.getMessage());
            }
            System.exit(-1);
        }
        if (parseResult.isUsageHelpRequested())
        {
            cmd.usage(System.out);
            System.exit(0);
        }
        else if (parseResult.isVersionHelpRequested())
        {
            cmd.printVersionHelp(System.out);
            System.exit(0);
        }
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
     * Change the value of a property of an already present &#64;Option annotation.
     * @param program Object; the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue String; the new value of the property
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeOptionProperty(final Object program, final String fieldName, final String propertyName,
            final Object newValue) throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        Field field = ClassUtil.resolveField(program, fieldName);
        Option option = field.getAnnotation(Option.class);
        ClassUtil.changeAnnotationValue(option, propertyName, newValue);
    }

    /**
     * Change the default value of an already present &#64;Option annotation.
     * @param program Object; the program for which the options should be changed
     * @param fieldName String; the field for which the defaultValue in &#64;Option should be changed
     * @param newDefaultValue Object; the new value of the defaultValue
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeOptionDefault(final Object program, final String fieldName, final String newDefaultValue)
            throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        changeOptionProperty(program, fieldName, "defaultValue", newDefaultValue);
    }

    /**
     * Change the value of a property of an already present &#64;Command annotation.
     * @param program Object; the program for which the cli property should be changed
     * @param propertyName String; the name of the property to change the value of
     * @param newValue Object; the new value of the property
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeCommandProperty(final Object program, final String propertyName, final Object newValue)
            throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        Command command = program.getClass().getAnnotation(Command.class);
        ClassUtil.changeAnnotationValue(command, propertyName, newValue);
    }

    /**
     * Change the value of the 'name' property of an already present &#64;Command annotation.
     * @param program Object; the program for which the cli property should be changed
     * @param newName String; the new value of the name
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeCommandName(final Object program, final String newName)
            throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        changeCommandProperty(program, "name", newName);
    }

    /**
     * Change the value of the 'description' property of an already present &#64;Command annotation.
     * @param program Object; the program for which the cli property should be changed
     * @param newDescription String; the new value of the description
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeCommandDescription(final Object program, final String newDescription)
            throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        changeCommandProperty(program, "description", new String[] {newDescription});
    }

    /**
     * Change the value of the 'version' property of an already present &#64;Command annotation.
     * @param program Object; the program for which the cli property should be changed
     * @param newVersion String; the new value of the version
     * @throws CliException when the field cannot be found, or when the &#64;Option annotation is not present in the field
     * @throws NoSuchFieldException when the field with the name does not exist in the program object
     * @throws IllegalStateException when the annotation has no member values or access to the member values is denied
     * @throws IllegalArgumentException when the value that is changed is of a different type than the type of the newValue
     */
    public static void changeCommandVersion(final Object program, final String newVersion)
            throws CliException, NoSuchFieldException, IllegalStateException, IllegalArgumentException
    {
        changeCommandProperty(program, "version", new String[] {newVersion});
    }

}
