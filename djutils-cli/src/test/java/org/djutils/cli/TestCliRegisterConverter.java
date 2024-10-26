package org.djutils.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.djutils.exceptions.Throw;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

/**
 * Program to test the CLI. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class TestCliRegisterConverter
{
    /** lat/lon object that can be parsed. */
    public static class LatLon
    {
        /** lat. */
        private final double lat;

        /** lon. */
        private final double lon;

        /**
         * @param lat lat
         * @param lon lon
         */
        public LatLon(final double lat, final double lon)
        {
            this.lat = lat;
            this.lon = lon;
        }

        /**
         * @return lat
         */
        public double getLat()
        {
            return this.lat;
        }

        /**
         * @return lon
         */
        public double getLon()
        {
            return this.lon;
        }

        /**
         * parse a string of the form (lat, lon).
         * @param latlon the string to parse
         * @return the LatLon object
         * @throws RuntimeException on error
         */
        public static LatLon of(final String latlon) throws RuntimeException
        {
            String ll = latlon;
            Throw.when(!ll.startsWith("("), RuntimeException.class, "string does not contain '('");
            Throw.when(!ll.endsWith(")"), RuntimeException.class, "string does not contain ')'");
            ll = ll.replaceAll("\\(", "").replaceAll("\\)", "");
            String[] llArr = ll.split("\\,");
            Throw.when(llArr.length != 2, RuntimeException.class, "string does not contain one ','");
            double lat = Double.parseDouble(llArr[0].trim());
            double lon = Double.parseDouble(llArr[1].trim());
            return new LatLon(lat, lon);
        }
    }

    /** */
    @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options
    {
        /** */
        @Option(names = {"-l", "--latlon"}, description = "Latitude and longitude of the form '(lat, lon)'")
        private LatLon latLon;

        /** @return the lat/lon */
        public LatLon getLatLon()
        {
            return this.latLon;
        }
    }

    /**
     * Convert a LatLon string to a LatLon object.
     */
    public static class LatLonConverter implements ITypeConverter<LatLon>
    {
        /** {@inheritDoc} */
        @Override
        public LatLon convert(final String value) throws Exception
        {
            return LatLon.of(value);
        }
    }

    /**
     * Test the CliUtil methods for registering user-defined converters.
     * @throws CliException on error
     * @throws IllegalAccessException on error
     * @throws IllegalArgumentException on error
     * @throws NoSuchFieldException on error
     */
    @Test
    public void testCliRegisterConverters()
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, CliException
    {
        // test CliUtil with CommandLine
        String[] args = new String[] {"-l", "(50.2,71.3)"};
        Options options = new Options();
        CommandLine cmd = new CommandLine(options);
        cmd.registerConverter(LatLon.class, new LatLonConverter());
        CliUtil.execute(cmd, args);
        assertEquals(50.2, options.getLatLon().getLat(), 0.001);
        assertEquals(71.3, options.getLatLon().getLon(), 0.001);
    }
}
