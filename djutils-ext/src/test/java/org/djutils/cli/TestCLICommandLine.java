package org.djutils.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Program to test the CLI. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class TestCLICommandLine
{
    // /** catch the System.exit() call and prevent exiting. */
    // @Rule
    // public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    /** */
    @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options implements Checkable
    {
        /** */
        @Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "80")
        private int port;

        /** @return the port number */
        public int getPort()
        {
            return this.port;
        }

        @Override
        public void check() throws Exception
        {
            if (this.port <= 0 || this.port > 65535)
            {
                throw new Exception("Port should be between 1 and 65535");
            }
        }
    }

    /**
     * Test the CliUtil methods.
     * @throws CliException on error
     * @throws IllegalAccessException on error
     * @throws IllegalArgumentException on error
     * @throws NoSuchFieldException on error
     */
    @Test
    public void testCli() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, CliException
    {
        // prevent exit to really exit
        System.setSecurityManager(new ExitHelper.NoExitSecurityManager());

        // test CliUtil with CommandLine
        String[] args = new String[] {"-p", "2400"};
        Options options = new Options();
        CommandLine cmd = new CommandLine(options);
        CliUtil.execute(cmd, args);
        assertEquals(2400, options.getPort());
        assertEquals("1.0", options.getClass().getAnnotation(Command.class).version()[0]);
        assertEquals("Program", options.getClass().getAnnotation(Command.class).name());
        assertEquals("Test program for CLI", options.getClass().getAnnotation(Command.class).description()[0]);

        // test CliUtil with CommandLine and check() method
        // this.exit.expectSystemExit();
        args = new String[] {"-p", "240000"};
        options = new Options();
        cmd = new CommandLine(options);
        try
        {
            CliUtil.execute(cmd, args);
            fail("check() is not called: the program should exit with an error message when a wrong port is provided");
        }
        catch (ExitHelper.ExitException e)
        {
            // ok!
        }

        System.setSecurityManager(null);
    }
}
