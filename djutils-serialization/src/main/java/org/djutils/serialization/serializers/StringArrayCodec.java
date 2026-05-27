package org.djutils.serialization.serializers;

import java.nio.charset.Charset;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * StringArrayCodec is responsible for the serialization of arrays of Strings, which can be coded as UTF8 or UTF16 (big-endian).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class StringArrayCodec extends BasicCodec<String[]>
{
    /** the UTF-8 charset. */
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset, big endian variant. */
    protected static final Charset UTF16 = Charset.forName("UTF-16BE");

    /**
     * Construct a StringArrayCodec.
     * @param fieldType the field type as defined by the {@link FieldTypes} class
     */
    public StringArrayCodec(final byte fieldType)
    {
        super(fieldType);
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfDimensions()
    {
        return 1;
    }

    /** Converter for String UTF-8 array. */
    protected static final StringArrayCodec CONVERT_STRING8_ARRAY = new StringArrayCodec(FieldTypes.STRING_UTF8_ARRAY)
    {
        @Override
        public int size(final String[] stringArray)
        {
            int size = 4;
            for (String string : stringArray)
            {
                size += 4 + string.getBytes(UTF8).length;
            }
            return size;
        }

        @Override
        public void serialize(final String[] stringArray, final byte[] buffer, final Pointer pointer,
                final Endianness endianness)
        {
            endianness.encodeInt(stringArray.length, buffer, pointer.getAndIncrement(4));
            for (String string : stringArray)
            {
                byte[] s = string.getBytes(UTF8);
                endianness.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
                for (byte b : s)
                {
                    buffer[pointer.getAndIncrement(1)] = b;
                }
            }
        }

        @Override
        public String[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            String[] result = new String[size];
            for (int i = 0; i < size; i++)
            {
                int bytesUsed = endianness.decodeInt(buffer, pointer.get());
                result[i] = endianness.decodeUTF8String(buffer, pointer.get());
                pointer.getAndIncrement(4 + bytesUsed);
            }
            return result;
        }

    };

    /** Converter for String UTF-16 array. */
    protected static final StringArrayCodec CONVERT_STRING16_ARRAY = new StringArrayCodec(FieldTypes.STRING_UTF16_ARRAY)
    {
        @Override
        public int size(final String[] stringArray)
        {
            int size = 4;
            for (String string : stringArray)
            {
                size += 4 + string.getBytes(UTF16).length;
            }
            return size;
        }

        @Override
        public void serialize(final String[] stringArray, final byte[] buffer, final Pointer pointer,
                final Endianness endianness)
        {
            endianness.encodeInt(stringArray.length, buffer, pointer.getAndIncrement(4));
            for (String string : stringArray)
            {
                // Note that according to https://stackoverflow.com/questions/74887443, String.length returns
                // the number of code units (i.e. the number of 16-bit char values) needed to make up the String
                // and not the number of Unicode codepoints.
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                endianness.encodeInt(chars.length, buffer, pointer.getAndIncrement(4));
                for (char c : chars)
                {
                    endianness.encodeChar(c, buffer, pointer.getAndIncrement(2));
                }
            }
        }

        @Override
        public String[] deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            int size = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
            String[] result = new String[size];
            for (int i = 0; i < size; i++)
            {
                result[i] = endianness.decodeUTF16String(buffer, pointer.get());
                pointer.getAndIncrement(4 + result[i].length() * 2);
            }
            return result;
        }
    };

}
