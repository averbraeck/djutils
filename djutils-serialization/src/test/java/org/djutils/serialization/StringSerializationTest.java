package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import org.djutils.decoderdumper.HexDumper;
import org.djutils.exceptions.Try;
import org.djutils.serialization.util.SerialDataDumper;
import org.junit.jupiter.api.Test;

/**
 * StringSerializationTest tests the encoding and decoding of strings and string arrays/matrices.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class StringSerializationTest extends AbstractSerializationTest
{

    /**
     * Test encoding and decoding of Strings with more exotic characters for UTF-8 and UTF-16.
     * @throws SerializationException when that happens uncaught this test has failed
     * @throws UnsupportedEncodingException when UTF-8 en/decoding fails
     */
    @Test
    public void testStrings() throws SerializationException, UnsupportedEncodingException
    {
        String abc = "abc";
        String copyright = "" + '\u00A9';
        String xi = "" + '\u03BE';
        String permille = "" + '\u2030';
        String smiley = "\uD83D\uDE00";
        String complex = smiley + copyright + xi + permille;

        testString(3, 6, abc);
        testString(2, 2, copyright);
        testString(3, 2, permille);
        testString(2, 2, xi);
        testString(4, 4, smiley);

        compare(TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, permille),
                new byte[] {9, 0, 0, 0, 3, (byte) 0xE2, (byte) 0x80, (byte) 0xB0});
        compare(TypedMessage.encodeUTF16(EndianUtil.BIG_ENDIAN, permille),
                new byte[] {10, 0, 0, 0, 1, (byte) 0x20, (byte) 0x30});

        compare(TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, smiley),
                new byte[] {9, 0, 0, 0, 4, (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80});
        compare(TypedMessage.encodeUTF16(EndianUtil.BIG_ENDIAN, smiley),
                new byte[] {10, 0, 0, 0, 2, (byte) 0xD8, (byte) 0x3D, (byte) 0xDE, (byte) 0x00});

        compare(TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, permille),
                new byte[] {-119, 3, 0, 0, 0, (byte) 0xE2, (byte) 0x80, (byte) 0xB0});
        compare(TypedMessage.encodeUTF16(EndianUtil.LITTLE_ENDIAN, permille),
                new byte[] {-118, 1, 0, 0, 0, (byte) 0x30, (byte) 0x20});

        compare(TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, smiley),
                new byte[] {-119, 4, 0, 0, 0, (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x80});
        compare(TypedMessage.encodeUTF16(EndianUtil.LITTLE_ENDIAN, smiley),
                new byte[] {-118, 2, 0, 0, 0, (byte) 0x3D, (byte) 0xD8, (byte) 0x00, (byte) 0xDE});

        Object[] objects = new Object[] {copyright, xi, permille, smiley, abc, complex};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                HexDumper.hexDumper(serialized);
                SerialDataDumper.serialDataDumper(endianUtil, serialized);
                Object[] decodedObjects = TypedMessage.decodeToObjectDataTypes(serialized);
                assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                for (int i = 0; i < objects.length; i++)
                {
                    assertEquals(objects[i], decodedObjects[i],
                            "decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input");
                }
            }
        }
    }

    /**
     * Test encoding and decoding of one String for UTF-8 and UTF-16.
     * @param expected8 expected length of UTF-8 encoding
     * @param expected16 expected length of UTF-16 encoding
     * @param s the string to test
     * @throws SerializationException when that happens uncaught this test has failed
     * @throws UnsupportedEncodingException when UTF-8 en/decoding fails
     */
    private void testString(final int expected8, final int expected16, final String s)
            throws SerializationException, UnsupportedEncodingException
    {
        assertEquals(expected8, s.getBytes("UTF-8").length);
        assertEquals(expected16, s.getBytes("UTF-16BE").length);
        assertEquals(expected16, s.getBytes("UTF-16LE").length);

        byte[] b8BE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, s);
        byte[] b8LE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, s);
        byte[] b16BE = TypedMessage.encodeUTF16(EndianUtil.BIG_ENDIAN, s);
        byte[] b16LE = TypedMessage.encodeUTF16(EndianUtil.LITTLE_ENDIAN, s);

        assertEquals(expected8, b8BE.length - 5);
        assertEquals(expected8, b8LE.length - 5);
        assertEquals(expected16, b16BE.length - 5);
        assertEquals(expected16, b16LE.length - 5);

        // get the number from the byte arrays
        assertEquals(9, b8BE[0]);
        assertEquals(expected8, EndianUtil.BIG_ENDIAN.decodeInt(b8BE, 1));
        assertEquals(-119, b8LE[0]);
        assertEquals(expected8, EndianUtil.LITTLE_ENDIAN.decodeInt(b8LE, 1));
        assertEquals(10, b16BE[0]);
        assertEquals(expected16 / 2, EndianUtil.BIG_ENDIAN.decodeInt(b16BE, 1));
        assertEquals(-118, b16LE[0]);
        assertEquals(expected16 / 2, EndianUtil.LITTLE_ENDIAN.decodeInt(b16LE, 1));
    }

    /**
     * Test String array en/decoding.
     * @throws SerializationException on error
     */
    @Test
    public void testStringArray() throws SerializationException
    {
        String abc = "abc123/?";
        String copyright = "" + '\u00A9';
        String xi = "" + '\u03BE';
        String permille = "" + '\u2030';
        String smiley = "\uD83D\uDE00";
        String complex = "12%c" + smiley + copyright + xi + permille + "[]@";

        String[] sa = new String[] {abc, smiley, complex};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized =
                        encodeUTF8 ? TypedObject.encodeUTF8(endianUtil, sa) : TypedObject.encodeUTF16(endianUtil, sa);
                HexDumper.hexDumper(serialized);
                // SerialDataDumper.serialDataDumper(endianUtil, serialized);
                String[] decodedObjects = (String[]) TypedObject.decodeToObjectDataTypes(serialized);
                assertEquals(sa.length, decodedObjects.length, "Size of decoded matches");
                for (int i = 0; i < sa.length; i++)
                {
                    assertEquals(sa[i], decodedObjects[i], "decoded object at index " + i + "(" + decodedObjects[i]
                            + ") equals corresponding object in input");
                }
            }
        }
    }

    /**
     * Test String matrix en/decoding.
     * @throws SerializationException on error
     */
    @Test
    public void testStringMatrix() throws SerializationException
    {
        String abc = "abc123/?";
        String copyright = "" + '\u00A9';
        String xi = "" + '\u03BE';
        String permille = "" + '\u2030';
        String smiley = "\uD83D\uDE00";
        String complex = "12%c" + smiley + copyright + xi + permille + "[]@";

        String[][] sm = new String[][] {{abc, smiley, complex}, {xi, permille, smiley}};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized =
                        encodeUTF8 ? TypedObject.encodeUTF8(endianUtil, sm) : TypedObject.encodeUTF16(endianUtil, sm);
                HexDumper.hexDumper(serialized);
                // SerialDataDumper.serialDataDumper(endianUtil, serialized);
                String[][] decodedObjects = (String[][]) TypedObject.decodeToObjectDataTypes(serialized);
                assertEquals(sm.length, decodedObjects.length, "Row size of decoded matches");
                for (int i = 0; i < sm.length; i++)
                {
                    assertEquals(sm[i].length, decodedObjects[i].length, "Column size of decoded matches");
                    for (int j = 0; j < sm[i].length; j++)
                    {
                        assertEquals(sm[i][j], decodedObjects[i][j], "decoded object at index (" + i + "," + j + ") (value "
                                + decodedObjects[i][j] + ") equals corresponding object in input");
                    }
                }
            }
        }

        // test jagged matrix
        final String[][] smRagged = new String[][] {{abc, smiley, complex}, {xi, smiley}};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                Try.testFail(() -> encodeUTF8 ? TypedObject.encodeUTF8(endianUtil, smRagged)
                        : TypedObject.encodeUTF16(endianUtil, smRagged));
            }
        }
    }

}
