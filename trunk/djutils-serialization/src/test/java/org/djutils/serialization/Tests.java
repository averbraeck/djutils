package org.djutils.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.djutils.decoderdumper.HexDumper;
import org.junit.Test;

/**
 * @author pknoppers
 */
public class Tests
{

    /**
     * Basic test encoding and decoding of the basic types.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void SimpleTests() throws SerializationException
    {
        int intValue = 123;
        Integer integerValue = -456;
        short shortValue = 234;
        Short shortValue2 = -345;
        long longValue = 98765l;
        Long longValue2 = -98765l;
        Byte byteValue = 12;
        byte byteValue2 = -23;
        float floatValue = 1.234f;
        Float floatValue2 = -3.456f;
        double doubleValue = 4.56789;
        Double doubleValue2 = -4.56789;
        boolean boolValue = true;
        Boolean boolValue2 = false;
        Character charValue = 'a';
        char charValue2 = 'b';
        String stringValue = "abcDEF123!@#ȦȧȨ\u0776\u0806\u080e";
        Object[] objects = new Object[] { intValue, integerValue, shortValue, shortValue2, longValue, longValue2, byteValue,
                byteValue2, floatValue, floatValue2, doubleValue, doubleValue2, boolValue, boolValue2, charValue, charValue2,
                stringValue };
        for (boolean encodeUTF8 : new boolean[] { false, true })
        {
            byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(objects) : TypedMessage.encodeUTF16(objects);
            System.out.print(HexDumper.hexDumper(serialized));
            Object[] decodedObjects = TypedMessage.decode(serialized);
            assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
            for (int i = 0; i < objects.length; i++)
            {
                assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                        objects[i], decodedObjects[i]);
            }
        }
    }

    /**
     * Test encoding and decoding of arrays.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void TestArrays() throws SerializationException
    {
        int[] integer = new int[] { 1, 2, 3 };
        Integer[] integerValues2 = new Integer[] { -1, -2, -3 };
        short[] shortValues = new short[] { 10, 20, 30 };
        Short[] shortValues2 = new Short[] { -10, -20, -30 };
        long[] longValues = new long[] { 1000, 2000, 3000 };
        Long[] longValues2 = new Long[] { -1000l, -2000l, -3000l };
        byte[] byteValues = new byte[] { 12, 13, 14 };
        Byte[] byteValues2 = new Byte[] { -12, -13, -14 };
        boolean[] boolValues = new boolean[] { false, true, true };
        Boolean[] boolValues2 = new Boolean[] { true, true, false };
        float[] floatValues = new float[] { 12.3f, 23.4f, 34.5f };
        Float[] floatValues2 = new Float[] { -12.3f, -23.4f, -34.5f };
        double[] doubleValues = new double[] { 23.45, 34.56, 45.67 };
        Double[] doubleValues2 = new Double[] { -23.45, -34.56, -45.67 };
        Object[] objects = new Object[] { integer, integerValues2, shortValues, shortValues2, longValues, longValues2,
                byteValues, byteValues2, floatValues, floatValues2, doubleValues, doubleValues2, boolValues, boolValues2 };
        for (boolean encodeUTF8 : new boolean[] { false, true })
        {
            byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(objects) : TypedMessage.encodeUTF16(objects);
            System.out.print(HexDumper.hexDumper(serialized));
            Object[] decodedObjects = TypedMessage.decode(serialized);
            assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
            for (int i = 0; i < objects.length; i++)
            {
                Object reference = makePrimitive(objects[i]);
                Object decoded = makePrimitive(decodedObjects[i]);
                if (!deepEquals0(reference, decoded))
                {
                    System.out.println("oops");
                }
                assertTrue("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                        deepEquals0(reference, decoded));
            }
        }
    }

    /**
     * Test encoding and decoding of arrays.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void TestMatricess() throws SerializationException
    {
        int[][] integer = new int[][] { { 1, 2, 3 }, { 4, 5, 6 } };
        Integer[][] integerValues2 = new Integer[][] { { -1, -2, -3 }, { -4, -5, -6 } };
        short[][] shortValues = new short[][] { { 10, 20, 30 }, { 40, 50, 60 } };
        Short[][] shortValues2 = new Short[][] { { -10, -20, -30 }, { -40, -50, -60 } };
        long[][] longValues = new long[][] { { 1000, 2000, 3000 }, { 3000, 4000, 5000 } };
        Long[][] longValues2 = new Long[][] { { -1000l, -2000l, -3000l }, { -3000l, -4000l, -5000l } };
        byte[][] byteValues = new byte[][] { { 12, 13, 14 }, { 15, 16, 17 } };
        Byte[][] byteValues2 = new Byte[][] { { -12, -13, -14 }, { -15, -16, -17 } };
        boolean[][] boolValues = new boolean[][] { { false, true, true }, { false, false, false } };
        Boolean[][] boolValues2 = new Boolean[][] { { true, true, false }, { true, true, true } };
        float[][] floatValues = new float[][] { { 12.3f, 23.4f, 34.5f }, { 44.4f, 55.5f, 66.6f } };
        Float[][] floatValues2 = new Float[][] { { -12.3f, -23.4f, -34.5f }, { -11.1f, -22.2f, -33.3f } };
        double[][] doubleValues = new double[][] { { 23.45, 34.56, 45.67 }, { 55.5, 66.6, 77.7 } };
        Double[][] doubleValues2 = new Double[][] { { -23.45, -34.56, -45.67 }, { -22.2, -33.3, -44.4 } };
        Object[] objects = new Object[] { integer, integerValues2, shortValues, shortValues2, longValues, longValues2,
                byteValues, byteValues2, floatValues, floatValues2, doubleValues, doubleValues2, boolValues, boolValues2 };
        for (boolean encodeUTF8 : new boolean[] { false, true })
        {
            byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(objects) : TypedMessage.encodeUTF16(objects);
            System.out.print(HexDumper.hexDumper(serialized));
            Object[] decodedObjects = TypedMessage.decode(serialized);
            assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
            for (int i = 0; i < objects.length; i++)
            {
                Object reference = makePrimitive(objects[i]);
                Object decoded = makePrimitive(decodedObjects[i]);
                if (!deepEquals0(reference, decoded))
                {
                    System.out.println("oops");
                }
                assertTrue("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                        deepEquals0(reference, decoded));
            }
        }
    }

    /**
     * Convert an array, or matrix of Byte, Short, Integer, etc. to an array/matrix of byte, short, int, etc.
     * @param in Object; the array to convert
     * @return Object; the converted input (if conversion was possible), or the unconverted input.
     */
    static Object makePrimitive(final Object in)
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
     * Compare two arrays of any type (stolen from java.util.Arrays)
     * @param e1 Object (should be some kind of array)
     * @param e2 Object (should be some kind of array)
     * @return boolean; true of the arrays have the same type, size and all elements in the arrays are equal to their
     *         counterpart
     */
    static boolean deepEquals0(Object e1, Object e2)
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
