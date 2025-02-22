package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djutils.decoderdumper.HexDumper;
import org.djutils.serialization.util.SerialDataDumper;
import org.junit.jupiter.api.Test;

/**
 * MatrixSerializationTest tests the encoding and decoding of matrices.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MatrixSerializationTest extends AbstractSerializationTest
{

    /**
     * Test encoding and decoding of arrays.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void testMatrices() throws SerializationException
    {
        int[][] integer = new int[][] {{1, 2, 3}, {4, 5, 6}};
        Integer[][] integerValues2 = new Integer[][] {{-1, -2, -3}, {-4, -5, -6}};
        short[][] shortValues = new short[][] {{10, 20, 30}, {40, 50, 60}};
        Short[][] shortValues2 = new Short[][] {{-10, -20, -30}, {-40, -50, -60}};
        long[][] longValues = new long[][] {{1000, 2000, 3000}, {3000, 4000, 5000}};
        Long[][] longValues2 = new Long[][] {{-1000L, -2000L, -3000L}, {-3000L, -4000L, -5000L}};
        byte[][] byteValues = new byte[][] {{12, 13, 14}, {15, 16, 17}};
        Byte[][] byteValues2 = new Byte[][] {{-12, -13, -14}, {-15, -16, -17}};
        boolean[][] boolValues = new boolean[][] {{false, true, true}, {false, false, false}};
        Boolean[][] boolValues2 = new Boolean[][] {{true, true, false}, {true, true, true}};
        float[][] floatValues = new float[][] {{12.3f, 23.4f, 34.5f}, {44.4f, 55.5f, 66.6f}};
        Float[][] floatValues2 = new Float[][] {{-12.3f, -23.4f, -34.5f}, {-11.1f, -22.2f, -33.3f}};
        double[][] doubleValues = new double[][] {{23.45, 34.56, 45.67}, {55.5, 66.6, 77.7}};
        Double[][] doubleValues2 = new Double[][] {{-23.45, -34.56, -45.67}, {-22.2, -33.3, -44.4}};
        Object[] objects = new Object[] {integer, integerValues2, shortValues, shortValues2, longValues, longValues2,
                byteValues, byteValues2, floatValues, floatValues2, doubleValues, doubleValues2, boolValues, boolValues2};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                HexDumper.hexDumper(serialized);
                SerialDataDumper.serialDataDumper(endianUtil, serialized);
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized)
                            : TypedMessage.decodeToObjectDataTypes(serialized);
                    assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                    for (int i = 0; i < objects.length; i++)
                    {
                        assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
                                "decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input");
                    }
                }
            }
        }
    }

}
