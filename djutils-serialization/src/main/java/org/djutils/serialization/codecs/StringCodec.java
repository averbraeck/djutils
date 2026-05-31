package org.djutils.serialization.codecs;

import java.nio.charset.Charset;

import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * StringCodec is responsible for the serialization of Strings, which can be coded as UTF8 or UTF16 (big-endian).
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class StringCodec extends BasicCodec<String>
{
    /** the UTF-8 charset. */
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /** the UTF-16 charset, big endian variant. */
    public static final Charset UTF16 = Charset.forName("UTF-16BE");

    /**
     * Construct a StringCodec.
     * @param fieldType the field type as defined by the {@link FieldTypes} class
     * @param shortName the short name of the codec
     */
    public StringCodec(final byte fieldType, final String shortName)
    {
        super(fieldType, shortName);
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfDimensions()
    {
        return 0;
    }

    /** Converter for String. */
    public static final StringCodec STRING16 = new StringCodec(FieldTypes.STRING_UTF16, "String_16")
    {
        @Override
        public int size(final String object)
        {
            return 4 + object.getBytes(UTF16).length;
        }

        @Override
        public void serialize(final String string, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            // Note that according to https://stackoverflow.com/questions/74887443, String.length returns the number of
            // code units (i.e. the number of 16-bit char values) needed to make up the String and not the number of
            // Unicode codepoints.
            char[] chars = new char[string.length()];
            string.getChars(0, chars.length, chars, 0);
            endianness.encodeInt(chars.length, buffer, pointer.getAndIncrement(4));
            for (char c : chars)
            {
                endianness.encodeChar(c, buffer, pointer.getAndIncrement(2));
            }
        }

        @Override
        public String deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            String s = endianness.decodeUTF16String(buffer, pointer.get());
            pointer.getAndIncrement(4 + s.length() * 2);
            return s;
        }
    };

    /** Converter for String. */
    public static final StringCodec STRING8 = new StringCodec(FieldTypes.STRING_UTF8, "String_8")
    {
        @Override
        public int size(final String string)
        {
            return 4 + string.getBytes(UTF8).length;
        }

        @Override
        public void serialize(final String string, final byte[] buffer, final Pointer pointer, final Endianness endianness)
        {
            byte[] s = string.getBytes(UTF8);
            endianness.encodeInt(s.length, buffer, pointer.getAndIncrement(4));
            for (byte b : s)
            {
                buffer[pointer.getAndIncrement(1)] = b;
            }
        }

        @Override
        public String deserialize(final byte[] buffer, final Pointer pointer, final Endianness endianness)
                throws SerializationException
        {
            int bytesUsed = endianness.decodeInt(buffer, pointer.get());
            String s = endianness.decodeUTF8String(buffer, pointer.get());
            pointer.getAndIncrement(4 + bytesUsed);
            return s;
        }
    };

}
