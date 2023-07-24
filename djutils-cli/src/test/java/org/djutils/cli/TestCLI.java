package org.djutils.cli;

import static org.junit.Assert.assertEquals;

import org.djunits.value.vdouble.scalar.Duration;
import org.djutils.exceptions.Throw;
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
public class TestCLI
{
    /** */
    @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options implements Checkable
    {
        /** */
        @Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "80")
        private int port;

        /** */
        private double noOption = 1.0;

        /** @return the port number */
        public int getPort()
        {
            return this.port;
        }

        /** @return noOption field. */
        public double getNoOption()
        {
            return this.noOption;
        }

        /** {@inheritDoc} */
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
    public static class NoCommandOptions implements Checkable
    {
        /** */
        @Option(names = {"-t", "--timeout"}, description = "Timeout for network", defaultValue = "1min")
        private Duration timeout;


        /** @return timeout for network. */
        public Duration getTimeout()
        {
            return this.timeout;
        }

        /** {@inheritDoc} */
        @Override
        public void check() throws Exception
        {
            Throw.when(this.timeout.si <= 0.0, IllegalArgumentException.class, "timeout should be positive");
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
        assertEquals("1.0", CliUtil.getCommandVersion(options)[0]);
        assertEquals("Program", CliUtil.getCommandName(options));
        assertEquals("Test program for CLI", CliUtil.getCommandDescription(options)[0]);
        CliUtil.execute(options, args);
        assertEquals(1200, options.getPort());
        assertEquals("1.0", options.getClass().getAnnotation(Command.class).version()[0]);
        assertEquals("Program", options.getClass().getAnnotation(Command.class).name());
        assertEquals("Test program for CLI", options.getClass().getAnnotation(Command.class).description()[0]);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(80, options.getPort());

        args = new String[] {};
        options = new Options();
        CliUtil.changeOptionDefault(options, "port", "8080");
        CliUtil.execute(options, args);
        assertEquals(8080, options.getPort());

        // change the @Command annotation
        args = new String[] {};
        options = new Options();
        CliUtil.changeCommandVersion(options, "2.0");
        CliUtil.changeCommandName(options, "Program2");
        CliUtil.changeCommandDescription(options, "2nd version of program");
        CliUtil.execute(options, args);
        assertEquals("2.0", CliUtil.getCommandVersion(options)[0]);
        assertEquals("Program2", CliUtil.getCommandName(options));
        assertEquals("2nd version of program", CliUtil.getCommandDescription(options)[0]);
        
        // The annotation itself should NOT have changed
        Command command = CliUtil.getCommandAnnotation(Options.class);
        assertEquals("1.0", command.version()[0]);
        assertEquals("Program", command.name());
        assertEquals("Test program for CLI", command.description()[0]);

        // clean the override map
        CliUtil.overrideMap.clear();

        // test field without @Option
        try
        {
            CliUtil.changeOptionDefault(Options.class, "noOption", "3.0");
        }
        catch (CliException e)
        {
            // ok
        }

        // test class without @Command
        try
        {
            NoCommandOptions nco = new NoCommandOptions();
            CliUtil.changeCommandName(nco, "NoCommand");
        }
        catch (CliException e)
        {
            // ok
        }
        
        // change the @Command annotations to other object types
        args = new String[] {};
        options = new Options();
        Class<?> declaringClass = CliUtil.getCommandAnnotationClass(Options.class);
        String key = CliUtil.makeOverrideKeyCommand(declaringClass, "version");
        CliUtil.overrideMap.put(key, "6.0");
        key = CliUtil.makeOverrideKeyCommand(declaringClass, "name");
        CliUtil.overrideMap.put(key, "Prog60");
        key = CliUtil.makeOverrideKeyCommand(declaringClass, "description");
        CliUtil.overrideMap.put(key, "Program v60");
        CliUtil.execute(options, args);
        assertEquals("6.0", CliUtil.getCommandVersion(options)[0]);
        assertEquals("Prog60", CliUtil.getCommandName(options));
        assertEquals("Program v60", CliUtil.getCommandDescription(options)[0]);

        // clean the override map
        CliUtil.overrideMap.clear();        
    }
}
