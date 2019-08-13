package org.djutils.cli;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

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
public class TestCLIHelpVersion
{
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

    /** catch the System.exit() call and prevent exiting. */
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    /** store the System.out.print() information in a log. */
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    /**
     * Test the CliUtil "--help" option (that calls System.exit).
     * @throws CliException on error
     * @throws IllegalAccessException on error
     * @throws IllegalArgumentException on error
     * @throws NoSuchFieldException on error
     */
    @Test
    public void testCliHelp() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, CliException
    {
        this.exit.expectSystemExit();
        String[] args = new String[] {"--help"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");
        CliUtil.execute(options, args);
        String helpText = this.systemOutRule.getLog();
        assertTrue(helpText.contains("Program2"));
        assertTrue(helpText.contains("2nd version of program"));
    }

    /**
     * Test the CliUtil "-V" option (that calls System.exit).
     * @throws CliException on error
     * @throws IllegalAccessException on error
     * @throws IllegalArgumentException on error
     * @throws NoSuchFieldException on error
     */
    @Test
    public void testCliVersion() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, CliException
    {
        this.exit.expectSystemExit();
        String[] args = new String[] {"-V"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");
        CliUtil.execute(options, args);
        String versionText = this.systemOutRule.getLog();
        assertTrue(versionText.contains("2.0"));
    }
    
    /**
     * Test the CliUtil methods with a wrong port.
     */
    @Test
    public void testCliWrongValue()
    {
        this.exit.expectSystemExit();
        String[] args = new String[] {"-p", "120000"};
        Options options = new Options();
        CliUtil.execute(options, args);
        fail("the program should exit with an error message when a wrong port is provided");
    }
    
    /**
     * Test the CliUtil methods with a wrong option.
     */
    @Test
    public void testCliWrongOption()
    {
        this.exit.expectSystemExit();
        String[] args = new String[] {"--wrongOption=50"};
        Options options = new Options();
        CliUtil.execute(options, args);
        fail("the program should exit with an error message when a wrong option is provided");
    }

}
