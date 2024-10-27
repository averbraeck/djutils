package org.djutils.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.github.stefanbirkner.systemlambda.SystemLambda;

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
public class TestCliHelpVersion
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
        // System.setSecurityManager(new ExitHelper.NoExitSecurityManager());
        String[] args = new String[] {"--help"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");
        String helpText = "";
        try
        {
            helpText = SystemLambda.tapSystemOut(() ->
            {
                SystemLambda.catchSystemExit(() ->
                { CliUtil.execute(options, args); });
            });
        }
        catch (Exception e)
        {
            fail("Requesting help caused exception", e);
        }
        assertTrue(helpText.contains("Program2"));
        assertTrue(helpText.contains("2nd version of program"));

        // clean the override map
        CliUtil.overrideMap.clear();
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
        String[] args = new String[] {"-V"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");
        String versionText = "";
        try
        {
            versionText = SystemLambda.tapSystemOut(() ->
            {
                SystemLambda.catchSystemExit(() ->
                { CliUtil.execute(options, args); });
            });
        }
        catch (Exception e)
        {
            fail("Requesting help caused exception", e);
        }
        assertTrue(versionText.contains("2.0"));

        // clean the override map
        CliUtil.overrideMap.clear();
    }

    /**
     * Test the CliUtil methods with a wrong port.
     */
    @Test
    public void testCliWrongValue()
    {
        String[] args = new String[] {"-p", "120000"};
        Options options = new Options();
        String errorText = "";
        try
        {
            errorText = SystemLambda.tapSystemErr(() ->
            {
                SystemLambda.catchSystemExit(() ->
                { CliUtil.execute(options, args); });
            });
        }
        catch (Exception e)
        {
            fail("calling CliUtil.execute caused exception", e);
        }
        assertTrue(errorText.startsWith("Port should be between 1 and 65535"));
    }

    /**
     * Test the CliUtil methods with a wrong option.
     */
    @Test
    public void testCliWrongOption()
    {
        String[] args = new String[] {"--wrongOption=50"};
        Options options = new Options();
        String errorText = "";
        try
        {
            errorText = SystemLambda.tapSystemErr(() ->
            {
                SystemLambda.catchSystemExit(() ->
                { CliUtil.execute(options, args); });
            });
        }
        catch (Exception e)
        {
            fail("calling CliUtil.execute caused exception", e);
        }
        assertTrue(errorText.contains("Unknown option:"));
        assertTrue(errorText.contains("--wrongOption=50"));
    }

}
