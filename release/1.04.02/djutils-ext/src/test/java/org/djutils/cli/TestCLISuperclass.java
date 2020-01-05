package org.djutils.cli;

import static org.junit.Assert.assertEquals;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.junit.Test;

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
public class TestCLISuperclass
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

    /** */
    public static class OptionsSub extends Options
    {
        /** */
        @Option(names = {"-t", "--timeout"}, description = "timeout", defaultValue = "10min")
        private Duration timeout;

        /** @return the timeout */
        public Duration getTimeout()
        {
            return this.timeout;
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
        String[] args = new String[] {"-p", "1200"};
        Options options = new Options();
        CliUtil.execute(options, args);
        assertEquals(1200, options.getPort());
        assertEquals("1.0", options.getClass().getAnnotation(Command.class).version()[0]);
        assertEquals("Program", options.getClass().getAnnotation(Command.class).name());
        assertEquals("Test program for CLI", options.getClass().getAnnotation(Command.class).description()[0]);

        args = new String[] {"-p", "2400", "--timeout", "60s"};
        OptionsSub optionsSub = new OptionsSub();
        CliUtil.execute(optionsSub, args);
        assertEquals(2400, optionsSub.getPort());
        assertEquals(new Duration(60.0, DurationUnit.SECOND), optionsSub.getTimeout());
        assertEquals("1.0", optionsSub.getClass().getSuperclass().getAnnotation(Command.class).version()[0]);
        assertEquals("Program", optionsSub.getClass().getSuperclass().getAnnotation(Command.class).name());
        assertEquals("Test program for CLI",
                optionsSub.getClass().getSuperclass().getAnnotation(Command.class).description()[0]);

        CliUtil.changeCommandName(OptionsSub.class, "NewName");
        CliUtil.changeCommandDescription(OptionsSub.class, "NewDescription");
        CliUtil.changeCommandVersion(OptionsSub.class, "1.1");
        CliUtil.execute(optionsSub, args);
        assertEquals("1.1", CliUtil.getCommandVersion(options)[0]);
        assertEquals("NewName", CliUtil.getCommandName(options));
        assertEquals("NewDescription", CliUtil.getCommandDescription(options)[0]);
    }
}
