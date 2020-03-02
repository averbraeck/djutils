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
    /** Utility class constructor. */
    private PrimitiveSerializer()
    {
        // utility class
    }

    /** Serializer and deserializer for int. */
    public static class Int
    {
        /**
         * Serialize an int value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value int; the value to serialize
         * @return String; a string representation of the int value that can later be deserialized
         */
        public String serialize(final int value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an int value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return int; the int corresponding to the String
         */
        public int deserialize(final String text)
        {
            return Integer.valueOf(text).intValue();
        }
    }

    /** Serializer and deserializer for double. */
    public static class Double
    {
        /**
         * Serialize an double value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value double; the value to serialize
         * @return String; a string representation of the double value that can later be deserialized
         */
        public String serialize(final double value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an double value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return double; the double corresponding to the String
         */
        public double deserialize(final String text)
        {
            return java.lang.Double.valueOf(text).doubleValue();
        }
    }

    /** Serializer and deserializer for float. */
    public static class Float
    {
        /**
         * Serialize an float value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value float; the value to serialize
         * @return String; a string representation of the float value that can later be deserialized
         */
        public String serialize(final float value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an float value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return float; the float corresponding to the String
         */
        public float deserialize(final String text)
        {
            return java.lang.Float.valueOf(text).floatValue();
        }
    }

    /** Serializer and deserializer for long. */
    public static class Long
    {
        /**
         * Serialize an long value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value long; the value to serialize
         * @return String; a string representation of the long value that can later be deserialized
         */
        public String serialize(final long value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an long value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return long; the long corresponding to the String
         */
        public long deserialize(final String text)
        {
            return java.lang.Long.valueOf(text).longValue();
        }
    }

    /** Serializer and deserializer for short. */
    public static class Short
    {
        /**
         * Serialize an short value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value short; the value to serialize
         * @return String; a string representation of the short value that can later be deserialized
         */
        public String serialize(final short value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an short value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return short; the short corresponding to the String
         */
        public short deserialize(final String text)
        {
            return java.lang.Short.valueOf(text).shortValue();
        }
    }

    /** Serializer and deserializer for byte. */
    public static class Byte
    {
        /**
         * Serialize an byte value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value byte; the value to serialize
         * @return String; a string representation of the byte value that can later be deserialized
         */
        public String serialize(final byte value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an byte value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return byte; the byte corresponding to the String
         */
        public byte deserialize(final String text)
        {
            return java.lang.Byte.valueOf(text).byteValue();
        }
    }

    /** Serializer and deserializer for boolean. */
    public static class Boolean
    {
        /**
         * Serialize an boolean value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value boolean; the value to serialize
         * @return String; a string representation of the boolean value that can later be deserialized
         */
        public String serialize(final boolean value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an boolean value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return boolean; the boolean corresponding to the String
         */
        public boolean deserialize(final String text)
        {
            return java.lang.Boolean.valueOf(text).booleanValue();
        }
    }

    /** Serializer and deserializer for char. */
    public static class Char
    {
        /**
         * Serialize an char value to text in such a way that it can be deserialized with the corresponding deserializer.
         * @param value char; the value to serialize
         * @return String; a string representation of the char value that can later be deserialized
         */
        public String serialize(final char value)
        {
            return String.valueOf(value);
        }

        /**
         * Deserialize an char value from text that has been created with the corresponding serializer.
         * @param text String; the string to deserialize
         * @return char; the char corresponding to the String
         */
        public char deserialize(final String text)
        {
            return text.charAt(0);
        }
    }

}
