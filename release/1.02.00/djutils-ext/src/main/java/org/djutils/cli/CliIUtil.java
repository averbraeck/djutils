package org.djutils.cli;

import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

/**
 * CliIUtil offers a helper method to display --help and --version without starting the program. The method is used as follows:
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
public class CliIUtil
{
    /**
     * Register Unit converters, parse the command line, catch --help, --version and errors. Calls the "check" method of the
     * class that can take care of further checks of the CLI arguments. Potentially, check() can also provide other
     * initialization of the program to be executed, but this can better be provided by other methods in main(). The method will
     * exit on requesting help or version information, or when the arguments are not complete or not correct.
     * @param program Checkable; the checkable program with the &#64;Option information
     * @param args String[]; the arguments from the command line
     */
    public static void execute(final Checkable program, final String[] args)
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
        try
        {
            program.check();
        }
        catch (Exception exception)
        {
            System.err.println(exception.getMessage());
            System.exit(-1);
        }
    }
}
