package org.djutils.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.junit.jupiter.api.Test;

import com.github.stefanbirkner.systemlambda.SystemLambda;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * TestCliLocale tests whether values with units are correctly parsed depending on the locale.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestCliLocale
{
    /** */
    @Command(description = "Test program for CLI", name = "TestCliLocale", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options
    {
        /** */
        @Option(names = {"-d", "--duration"}, description = "Duration of the call", defaultValue = "0.5s")
        private Duration duration;

        /**
         * @return duration
         */
        public Duration getDuration()
        {
            return this.duration;
        }
    }

    /**
     * Test the parsing with different locales.
     * @throws CliException on error
     */
    @Test
    public void testCli() throws CliException
    {
        String[] args;
        Options options = new Options();
        Locale saveLocale = Locale.getDefault();
        Locale locale;

        // check US as default language and explicitly
        locale = Locale.US;
        Locale.setDefault(locale);
        args = new String[] {};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0.5s"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0.5s", "--locale", "en"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "2h", "--locale", "en"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(2.0, DurationUnit.HOUR), options.getDuration());
        args = new String[] {"--duration", "0.5s", "--locale", "en_US"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0.5s", "--locale", "en-US"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        assertEquals(locale, Locale.getDefault());

        // check NL as default language and explicitly
        locale = new Locale("nl", "NL");
        Locale.setDefault(locale);
        args = new String[] {"--duration", "0.5s"}; // note that en_US is ALWAYS the default
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "nl"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "2uur", "--locale", "nl"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(2.0, DurationUnit.HOUR), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "nl_NL"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "nl-NL"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        assertEquals(locale, Locale.getDefault());

        // check DE as locale
        locale = Locale.GERMAN;
        Locale.setDefault(locale);
        args = new String[] {"--duration", "0.5s"}; // note that en_US is ALWAYS the default
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "de"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "de_DE"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "de-DE"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        assertEquals(locale, Locale.getDefault());

        // check NO as locale (a locale with a variant)
        locale = Locale.US;
        Locale.setDefault(locale);
        args = new String[] {"--duration", "0.5s"}; // note that en_US is ALWAYS the default
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "no"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0,5s", "--locale", "no_NO"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0.5s", "--locale", "no_NO_NY"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        args = new String[] {"--duration", "0.5s", "--locale", "no-NO-NY"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(0.5, DurationUnit.SECOND), options.getDuration());
        assertEquals(locale, Locale.getDefault());

        Locale.setDefault(saveLocale);
    }

    /**
     * Test the parsing with an error for a unit-variable.
     * @throws Exception on error
     */
    @Test
    public void testCliError() throws Exception
    {
        Options options = new Options();
        Locale saveLocale = Locale.getDefault();

        // errors with locale and unit that should be caught
        Locale locale = Locale.US;
        Locale.setDefault(locale);
        var argsErr = new String[] {"--duration", "0.5sx"};
        String errorText = "";
        errorText = SystemLambda.tapSystemErr(() ->
        {
            SystemLambda.catchSystemExit(() ->
            { CliUtil.execute(options, argsErr); });
        });
        assertTrue(errorText.startsWith("Invalid value"), "Error text should start with 'Invalid value': " + errorText);
        assertTrue(errorText.contains("cannot convert"), "Error text should contain 'cannot convert': " + errorText);
        assertTrue(errorText.contains("IllegalArgumentException"),
                "Error text should contain 'IllegalArgumentException': " + errorText);

        Locale.setDefault(saveLocale);
    }
}
