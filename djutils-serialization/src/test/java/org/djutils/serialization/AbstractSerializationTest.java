package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

/**
 * AbstractSerializationTest contain a number of helper methods for different tests.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class AbstractSerializationTest
{
    /**
     * Compare two byte arrays.
     * @param actual the calculated byte array
     * @param expected the expected byte array
     */
    protected void compare(final byte[] actual, final byte[] expected)
    {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], actual[i], "byte " + i + " expected: " + expected[i] + ", actual: " + actual[i]);
        }
    }

    /**
     * Convert an array, or matrix of Byte, Short, Integer, etc. to an array/matrix of byte, short, int, etc.
     * @param in the array to convert
     * @return the converted input (if conversion was possible), or the unconverted input.
     */
    protected Object makePrimitive(final Object in)
    {
        if (in instanceof Byte[])
        {
            Byte[] byteIn = (Byte[]) in;
            byte[] result = new byte[byteIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = byteIn[i];
            }
            return result;
        }
        if (in instanceof Short[])
        {
            Short[] shortIn = (Short[]) in;
            short[] result = new short[shortIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = shortIn[i];
            }
            return result;
        }
        if (in instanceof Integer[])
        {
            Integer[] integerIn = (Integer[]) in;
            int[] result = new int[integerIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = integerIn[i];
            }
            return result;
        }
        if (in instanceof Long[])
        {
            Long[] longIn = (Long[]) in;
            long[] result = new long[longIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = longIn[i];
            }
            return result;
        }
        if (in instanceof Float[])
        {
            Float[] floatIn = (Float[]) in;
            float[] result = new float[floatIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = floatIn[i];
            }
            return result;
        }
        if (in instanceof Double[])
        {
            Double[] doubleIn = (Double[]) in;
            double[] result = new double[doubleIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = doubleIn[i];
            }
            return result;
        }
        if (in instanceof Boolean[])
        {
            Boolean[] booleanIn = (Boolean[]) in;
            boolean[] result = new boolean[booleanIn.length];
            for (int i = 0; i < result.length; i++)
            {
                result[i] = booleanIn[i];
            }
            return result;
        }
        if (in instanceof Byte[][])
        {
            Byte[][] byteIn = (Byte[][]) in;
            byte[][] result = new byte[byteIn.length][byteIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = byteIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Short[][])
        {
            Short[][] shortIn = (Short[][]) in;
            short[][] result = new short[shortIn.length][shortIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = shortIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Integer[][])
        {
            Integer[][] integerIn = (Integer[][]) in;
            int[][] result = new int[integerIn.length][integerIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = integerIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Long[][])
        {
            Long[][] longIn = (Long[][]) in;
            long[][] result = new long[longIn.length][longIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = longIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Float[][])
        {
            Float[][] floatIn = (Float[][]) in;
            float[][] result = new float[floatIn.length][floatIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = floatIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Double[][])
        {
            Double[][] doubleIn = (Double[][]) in;
            double[][] result = new double[doubleIn.length][doubleIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = doubleIn[i][j];
                }
            }
            return result;
        }
        if (in instanceof Boolean[][])
        {
            Boolean[][] booleanIn = (Boolean[][]) in;
            boolean[][] result = new boolean[booleanIn.length][booleanIn[0].length];
            for (int i = 0; i < result.length; i++)
            {
                for (int j = 0; j < result[0].length; j++)
                {
                    result[i][j] = booleanIn[i][j];
                }
            }
            return result;
        }
        return in;
    }

    /**
     * Compare two arrays of any type (stolen from java.util.Arrays).
     * @param e1 Object (should be some kind of array)
     * @param e2 Object (should be some kind of array)
     * @return true of the arrays have the same type, size and all elements in the arrays are equal to their
     *         counterpart
     */
    protected boolean deepEquals0(final Object e1, final Object e2)
    {
        if (e1 instanceof Object[] && e2 instanceof Object[])
        {
            return Arrays.deepEquals((Object[]) e1, (Object[]) e2);
        }
        if (e1 instanceof byte[] && e2 instanceof byte[])
        {
            return Arrays.equals((byte[]) e1, (byte[]) e2);
        }
        if (e1 instanceof short[] && e2 instanceof short[])
        {
            return Arrays.equals((short[]) e1, (short[]) e2);
        }
        if (e1 instanceof int[] && e2 instanceof int[])
        {
            return Arrays.equals((int[]) e1, (int[]) e2);
        }
        if (e1 instanceof long[] && e2 instanceof long[])
        {
            return Arrays.equals((long[]) e1, (long[]) e2);
        }
        if (e1 instanceof char[] && e2 instanceof char[])
        {
            return Arrays.equals((char[]) e1, (char[]) e2);
        }
        if (e1 instanceof float[] && e2 instanceof float[])
        {
            return Arrays.equals((float[]) e1, (float[]) e2);
        }
        if (e1 instanceof double[] && e2 instanceof double[])
        {
            return Arrays.equals((double[]) e1, (double[]) e2);
        }
        if (e1 instanceof boolean[] && e2 instanceof boolean[])
        {
            return Arrays.equals((boolean[]) e1, (boolean[]) e2);
        }
        return e1.equals(e2);
    }

}
