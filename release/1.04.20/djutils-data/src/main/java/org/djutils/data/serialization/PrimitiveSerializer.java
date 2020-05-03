package org.djutils.data.serialization;

/**
 * PrimitiveSerializer takes care of (de)serialization of primitive values. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class PrimitiveSerializer
{
    /** Utility class cannot be initialized. */
    private PrimitiveSerializer()
    {
        // utility class.
    }

    /** Serializer and deserializer for int. */
    public static class Int implements TextSerializer<java.lang.Integer>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return Integer.valueOf(text).intValue();
        }
    }

    /** Serializer and deserializer for double. */
    public static class Double implements TextSerializer<java.lang.Double>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Double.valueOf(text).doubleValue();
        }
    }

    /** Serializer and deserializer for float. */
    public static class Float implements TextSerializer<java.lang.Float>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Float.valueOf(text).floatValue();
        }
    }

    /** Serializer and deserializer for long. */
    public static class Long implements TextSerializer<java.lang.Long>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Long.valueOf(text).longValue();
        }
    }

    /** Serializer and deserializer for short. */
    public static class Short implements TextSerializer<java.lang.Short>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Short.valueOf(text).shortValue();
        }
    }

    /** Serializer and deserializer for byte. */
    public static class Byte implements TextSerializer<java.lang.Byte>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Byte.valueOf(text).byteValue();
        }
    }

    /** Serializer and deserializer for boolean. */
    public static class Boolean implements TextSerializer<java.lang.Boolean>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return java.lang.Boolean.valueOf(text).booleanValue();
        }
    }

    /** Serializer and deserializer for char. */
    public static class Char implements TextSerializer<java.lang.Character>
    {
        /** {@inheritDoc} */
        @Override
        public String serialize(final Object value)
        {
            return String.valueOf(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object deserialize(final String text)
        {
            return text.charAt(0);
        }
    }

}
