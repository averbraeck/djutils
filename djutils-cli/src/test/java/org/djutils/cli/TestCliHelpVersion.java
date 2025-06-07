package org.djutils.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Program to test the CLI. <br>
 * <br>
 * Copyright (c) 2018-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
        CliUtil.testMode = true;

        // System.setSecurityManager(new ExitHelper.NoExitSecurityManager());
        String[] args = new String[] {"--help"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");

        PrintStream oldPrintStream = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        try
        {
            CliUtil.execute(options, args);
            fail("calling CliUtil.execute did not exit when it should");
        }
        catch (CliRuntimeException e)
        {
            assertTrue(outContent.toString().contains("Program2"));
            assertTrue(outContent.toString().contains("2nd version of program"));
        }
        catch (Exception e)
        {
            fail("CliUtil.execute caused exception", e);
        }
        finally
        {
            System.setOut(oldPrintStream);
        }

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
        CliUtil.testMode = true;

        String[] args = new String[] {"-V"};
        Options options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");

        PrintStream oldPrintStream = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        try
        {
            CliUtil.execute(options, args);
            fail("calling CliUtil.execute did not exit when it should");
        }
        catch (CliRuntimeException e)
        {
            assertTrue(outContent.toString().contains("2.0"));
        }
        catch (Exception e)
        {
            fail("CliUtil.execute caused exception", e);
        }
        finally
        {
            System.setOut(oldPrintStream);
        }

        // clean the override map
        CliUtil.overrideMap.clear();
    }

    /**
     * Test the CliUtil methods with a wrong port.
     */
    @Test
    public void testCliWrongValue()
    {
        CliUtil.testMode = true;

        String[] args = new String[] {"-p", "120000"};
        Options options = new Options();

        PrintStream oldPrintStream = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        try
        {
            CliUtil.execute(options, args);
            fail("calling CliUtil.execute did not exit when it should");
        }
        catch (CliRuntimeException e)
        {
            assertTrue(errContent.toString().startsWith("Port should be between 1 and 65535"));
        }
        finally
        {
            System.setErr(oldPrintStream);
        }
    }

    /**
     * Test the CliUtil methods with a wrong option.
     */
    @Test
    public void testCliWrongOption()
    {
        CliUtil.testMode = true;

        String[] args = new String[] {"--wrongOption=50"};
        Options options = new Options();

        PrintStream oldPrintStream = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        try
        {
            CliUtil.execute(options, args);
            fail("calling CliUtil.execute did not exit when it should");
        }
        catch (CliRuntimeException e)
        {
            assertTrue(errContent.toString().contains("Unknown option:"));
            assertTrue(errContent.toString().contains("--wrongOption=50"));
        }
        finally
        {
            System.setErr(oldPrintStream);
        }
    }

}
