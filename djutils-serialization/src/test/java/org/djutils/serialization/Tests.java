package org.djutils.serialization;

import static org.junit.Assert.assertEquals;

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
        byte[] serialized = TypedMessage.encodeUTF16(objects);
        System.out.println(HexDumper.hexDumper(serialized));
        Object[] decodedObjects = TypedMessage.decode(serialized);
        assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
        for (int i = 0; i < objects.length; i++)
        {
            assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                    objects[i], decodedObjects[i]);
        }
        serialized = TypedMessage.encodeUTF8(objects);
        System.out.println(HexDumper.hexDumper(serialized));
        decodedObjects = TypedMessage.decode(serialized);
        assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
        for (int i = 0; i < objects.length; i++)
        {
            assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                    objects[i], decodedObjects[i]);
        }
    }

    /**
     * Test encoding and decoding of arrays.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void TestArrays() throws SerializationException
    {
        int[] intValues = new int[] { 1, 2, 3 };
        Integer[] integerValues2 = new Integer[] { -1, -2, -3 };
        Object[] objects = new Object[] { integerValues2, intValues };
        byte[] serialized = TypedMessage.encodeUTF16(objects);
        Object[] decodedObjects = TypedMessage.decode(serialized);
        assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
        for (int i = 0; i < objects.length; i++)
        {
            assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                    objects[i], decodedObjects[i]);
        }
        serialized = TypedMessage.encodeUTF8(objects);
        System.out.println(HexDumper.hexDumper(serialized));
        decodedObjects = TypedMessage.decode(serialized);
        assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
        for (int i = 0; i < objects.length; i++)
        {
            assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                    objects[i], decodedObjects[i]);
        }
    }
}
