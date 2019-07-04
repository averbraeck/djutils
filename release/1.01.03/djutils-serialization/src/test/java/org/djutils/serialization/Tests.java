package org.djutils.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AreaUnit;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.ElectricalCurrentUnit;
import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.MoneyPerAreaUnit;
import org.djunits.unit.MoneyPerDurationUnit;
import org.djunits.unit.MoneyPerEnergyUnit;
import org.djunits.unit.MoneyPerLengthUnit;
import org.djunits.unit.MoneyPerMassUnit;
import org.djunits.unit.MoneyPerVolumeUnit;
import org.djunits.unit.MoneyUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.value.StorageType;
import org.djunits.value.ValueException;
import org.djunits.value.vdouble.matrix.ElectricalCurrentMatrix;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Money;
import org.djunits.value.vdouble.scalar.MoneyPerArea;
import org.djunits.value.vdouble.scalar.MoneyPerDuration;
import org.djunits.value.vdouble.scalar.MoneyPerEnergy;
import org.djunits.value.vdouble.scalar.MoneyPerLength;
import org.djunits.value.vdouble.scalar.MoneyPerMass;
import org.djunits.value.vdouble.scalar.MoneyPerVolume;
import org.djunits.value.vdouble.vector.AbstractDoubleVector;
import org.djunits.value.vdouble.vector.ElectricalCurrentVector;
import org.djunits.value.vdouble.vector.LengthVector;
import org.djunits.value.vdouble.vector.MoneyVector;
import org.djunits.value.vfloat.matrix.FloatElectricalResistanceMatrix;
import org.djunits.value.vfloat.scalar.FloatArea;
import org.djunits.value.vfloat.scalar.FloatMoney;
import org.djunits.value.vfloat.scalar.FloatMoneyPerVolume;
import org.djunits.value.vfloat.vector.FloatElectricalResistanceVector;
import org.junit.Test;

/**
 * Test message conversions.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Jun 10, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Tests
{

    /**
     * Basic test encoding and decoding of the basic types.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void simpleTests() throws SerializationException
    {
        int intValue = 123;
        Integer integerValue = -456;
        short shortValue = 234;
        Short shortValue2 = -345;
        long longValue = 98765L;
        Long longValue2 = -98765L;
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
        Object[] objects = new Object[] {intValue, integerValue, shortValue, shortValue2, longValue, longValue2, byteValue,
                byteValue2, floatValue, floatValue2, doubleValue, doubleValue2, boolValue, boolValue2, charValue, charValue2,
                stringValue};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                // System.out.println("" + endianUtil + ", UTF8=" + encodeUTF8);
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                // System.out.print(HexDumper.hexDumper(serialized));
                // System.out.print(SerialDataDumper.serialDataDumper(endianUtil, serialized));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                            : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                    assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                    for (int i = 0; i < objects.length; i++)
                    {
                        assertEquals(
                                "decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                                objects[i], decodedObjects[i]);
                    }
                }
            }
        }
    }

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

        Object[] objects = new Object[] {copyright, xi, permille, smiley, abc, complex};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                Object[] decodedObjects = TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                for (int i = 0; i < objects.length; i++)
                {
                    assertEquals("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                            objects[i], decodedObjects[i]);
                }
            }
        }

    }

    /**
     * Compare two byte arrays.
     * @param actual the calculated byte array
     * @param expected the expected byte array
     */
    private void compare(final byte[] actual, final byte[] expected)
    {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals("byte " + i + " expected: " + expected[i] + ", actual: " + actual[i], expected[i], actual[i]);
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

        byte[] b8 = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, s);
        byte[] b16BE = TypedMessage.encodeUTF16(EndianUtil.BIG_ENDIAN, s);
        byte[] b16LE = TypedMessage.encodeUTF16(EndianUtil.LITTLE_ENDIAN, s);

        assertEquals(expected8, b8.length - 5);
        assertEquals(expected16, b16BE.length - 5);
        assertEquals(expected16, b16LE.length - 5);

        // get the number from the byte arrays
        assertEquals(9, b8[0]);
        assertEquals(expected8, EndianUtil.BIG_ENDIAN.decodeInt(b8, 1));
        assertEquals(10, b16BE[0]);
        assertEquals(expected16 / 2, EndianUtil.BIG_ENDIAN.decodeInt(b16BE, 1));
        // TODO: assertEquals(10, b16LE[0]); the code for UTF-16LE will be different from 10 in a next version of djutils
        assertEquals(expected16 / 2, EndianUtil.LITTLE_ENDIAN.decodeInt(b16LE, 1));
    }

    /**
     * Test encoding and decoding of arrays.
     * @throws SerializationException when that happens uncaught this test has failed
     */
    @Test
    public void testArrays() throws SerializationException
    {
        int[] integer = new int[] {1, 2, 3};
        Integer[] integerValues2 = new Integer[] {-1, -2, -3};
        short[] shortValues = new short[] {10, 20, 30};
        Short[] shortValues2 = new Short[] {-10, -20, -30};
        long[] longValues = new long[] {1000, 2000, 3000};
        Long[] longValues2 = new Long[] {-1000L, -2000L, -3000L};
        byte[] byteValues = new byte[] {12, 13, 14};
        Byte[] byteValues2 = new Byte[] {-12, -13, -14};
        boolean[] boolValues = new boolean[] {false, true, true};
        Boolean[] boolValues2 = new Boolean[] {true, true, false};
        float[] floatValues = new float[] {12.3f, 23.4f, 34.5f};
        Float[] floatValues2 = new Float[] {-12.3f, -23.4f, -34.5f};
        double[] doubleValues = new double[] {23.45, 34.56, 45.67};
        Double[] doubleValues2 = new Double[] {-23.45, -34.56, -45.67};
        Object[] objects = new Object[] {integer, integerValues2, shortValues, shortValues2, longValues, longValues2,
                byteValues, byteValues2, floatValues, floatValues2, doubleValues, doubleValues2, boolValues, boolValues2};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                // System.out.print(HexDumper.hexDumper(serialized));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                            : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                    assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                    for (int i = 0; i < objects.length; i++)
                    {
                        assertTrue("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                                deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])));
                    }
                }
            }
        }
    }

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
                // System.out.print(HexDumper.hexDumper(serialized));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                            : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                    assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                    for (int i = 0; i < objects.length; i++)
                    {
                        assertTrue("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                                deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])));
                    }
                }
            }
        }
    }

    /**
     * Test encoding and decoding of strongly typed quantities (DJUNITS).
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueException when that happens uncaught, this test has failed
     */
    @Test
    public void testDJunits() throws SerializationException, ValueException
    {
        Length length = new Length(123.4, LengthUnit.FOOT);
        Dimensionless value = new Dimensionless(345.6, DimensionlessUnit.SI);
        Money money = new Money(456, MoneyUnit.EUR);
        FloatMoney floatMoney = new FloatMoney(123.45f, MoneyUnit.AED);
        MoneyPerArea mpa = new MoneyPerArea(0.33, MoneyPerAreaUnit.USD_PER_ACRE);
        MoneyPerLength mpl = new MoneyPerLength(0.22, MoneyPerLengthUnit.USD_PER_MILE);
        MoneyPerEnergy mpe = new MoneyPerEnergy(0.33, MoneyPerEnergyUnit.EUR_PER_KILOWATTHOUR);
        MoneyPerMass mpm = new MoneyPerMass(0.33, MoneyPerMassUnit.USD_PER_POUND);
        MoneyPerDuration mpt = new MoneyPerDuration(0.33, MoneyPerDurationUnit.EUR_PER_DAY);
        MoneyPerVolume mpv = new MoneyPerVolume(0.33, MoneyPerVolumeUnit.USD_PER_OUNCE_US_FLUID);
        FloatMoneyPerVolume mpvw = new FloatMoneyPerVolume(0.99, MoneyPerVolumeUnit.USD_PER_OUNCE_US_FLUID);
        FloatArea area = new FloatArea(12345.678f, AreaUnit.ACRE);
        ElectricalCurrentVector currents =
                new ElectricalCurrentVector(new double[] {1.2, 2.3, 3.4}, ElectricalCurrentUnit.MILLIAMPERE, StorageType.DENSE);
        FloatElectricalResistanceVector resistors = new FloatElectricalResistanceVector(new float[] {1.2f, 4.7f, 6.8f},
                ElectricalResistanceUnit.KILOOHM, StorageType.DENSE);
        ElectricalCurrentMatrix currentMatrix = new ElectricalCurrentMatrix(new double[][] {{1.2, 2.3, 3.4}, {5.5, 6.6, 7.7}},
                ElectricalCurrentUnit.MILLIAMPERE, StorageType.DENSE);
        FloatElectricalResistanceMatrix resistorMatrix = new FloatElectricalResistanceMatrix(
                new float[][] {{1.2f, 4.7f, 6.8f}, {2.2f, 3.3f, 4.4f}}, ElectricalResistanceUnit.KILOOHM, StorageType.DENSE);

        Object[] objects = new Object[] {length, value, money, floatMoney, mpa, mpl, mpe, mpm, mpt, mpv, mpvw, area, currents,
                resistors, currentMatrix, resistorMatrix};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            byte[] serialized = TypedMessage.encodeUTF16(endianUtil, objects);
            // System.out.print(HexDumper.hexDumper(serialized));
            for (boolean primitive : new boolean[] {false, true})
            {
                Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                        : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                for (int i = 0; i < objects.length; i++)
                {
                    assertTrue("decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input",
                            deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])));
                }
            }
        }
    }

    /** Class used to test serialization of classes that implement SerializableObject. */
    static class Compound implements SerializableObject<Compound>
    {
        /** Field 1. */
        public Integer intValue;

        /** Field 2. */
        public Double doubleValue;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.doubleValue == null) ? 0 : this.doubleValue.hashCode());
            result = prime * result + ((this.intValue == null) ? 0 : this.intValue.hashCode());
            return result;
        }

        @SuppressWarnings("checkstyle:needbraces")
        @Override
        public boolean equals(final Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Compound other = (Compound) obj;
            if (this.doubleValue == null)
            {
                if (other.doubleValue != null)
                    return false;
            }
            else if (!this.doubleValue.equals(other.doubleValue))
                return false;
            if (this.intValue == null)
            {
                if (other.intValue != null)
                    return false;
            }
            else if (!this.intValue.equals(other.intValue))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Compound [intValue=" + this.intValue + ", doubleValue=" + this.doubleValue + "]";
        }

        /**
         * Construct a new Compound object.
         * @param intValue int; the value to assign to intValue
         * @param doubleValue double; the value to assign to doubleValue
         */
        Compound(final int intValue, final double doubleValue)
        {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
        }

        @Override
        public List<Object> exportAsList()
        {
            List<Object> result = new ArrayList<>();
            result.add(this.intValue);
            result.add(this.doubleValue);
            return result;
        }

    }

    /**
     * Test the compound array encoder and decoder.
     * @throws SerializationException when that happens uncaught, this test has failed
     */
    @Test
    public void testCompoundArrays() throws SerializationException
    {
        Compound[] testArray = new Compound[] {new Compound(1, 0.1), new Compound(2, 0.2), new Compound(3, 0.3)};
        Object[] objects = new Object[] {testArray};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                // System.out.println("Encoding " + (encodeUTF8 ? "UTF8" : "UTF16") + ", " + endianUtil);
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                // System.out.print(HexDumper.hexDumper(serialized));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                            : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                    assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                    // Replace all List objects in the result by corresponding new Compound objects
                    for (int i = 0; i < objects.length; i++)
                    {
                        Object o = decodedObjects[i];
                        if (o instanceof TypedMessage.MinimalSerializableObject[])
                        {
                            TypedMessage.MinimalSerializableObject[] in = ((TypedMessage.MinimalSerializableObject[]) o);
                            Compound[] out = new Compound[in.length];
                            for (int j = 0; j < in.length; j++)
                            {
                                List<Object> fields = in[j].exportAsList();
                                Integer intValue = (Integer) fields.get(0);
                                Double doubleValue = (Double) fields.get(1);
                                out[j] = new Compound(intValue, doubleValue);
                            }
                            decodedObjects[i] = out;
                        }
                    }
                    for (int i = 0; i < objects.length; i++)
                    {
                        if (objects[i] instanceof Compound[])
                        {
                            Compound[] in = (Compound[]) objects[i];
                            assertTrue("decoded object is now also a Compound[]", decodedObjects[i] instanceof Compound[]);
                            Compound[] out = (Compound[]) objects[i];
                            assertEquals("Compound arrays have same length", in.length, out.length);
                            for (int j = 0; j < in.length; j++)
                            {
                                assertEquals("reconstructed compound object matches input", in[j], out[j]);
                            }
                        }
                        else
                        {
                            assertTrue(
                                    "decoded object at index " + i + "(" + objects[i]
                                            + ") equals corresponding object in input",
                                    deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])));
                        }
                    }
                }
            }
        }
    }

    /**
     * Test serialization and deserialization of arrays of Djutils vectors.
     * @throws ValueException if that happens uncaught; this test has failed
     * @throws SerializationException if that happens uncaught; this test has failed
     */
    @Test
    public void testArrayOfDjutilsVectors() throws ValueException, SerializationException
    {
        AbstractDoubleVector<?, ?>[] array =
                new AbstractDoubleVector[] {new LengthVector(new double[] {0.1, 0.2, 0.3}, LengthUnit.INCH, StorageType.DENSE),
                        new MoneyVector(new double[] {10.1, 20.2, 30.3}, MoneyUnit.EUR, StorageType.DENSE)};
        Object[] objects = new Object[] {array};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                // System.out.println("Encoding " + (encodeUTF8 ? "UTF8" : "UTF16") + ", " + endianUtil);
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                // System.out.print(HexDumper.hexDumper(serialized));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized, endianUtil)
                            : TypedMessage.decodeToObjectDataTypes(serialized, endianUtil);
                    assertEquals("Size of decoded matches", objects.length, decodedObjects.length);
                    for (int i = 0; i < objects.length; i++)
                    {
                        if (objects[i] instanceof AbstractDoubleVector<?, ?>[])
                        {
                            AbstractDoubleVector<?, ?>[] arrayIn = (AbstractDoubleVector<?, ?>[]) objects[i];
                            AbstractDoubleVector<?, ?>[] arrayOut = (AbstractDoubleVector<?, ?>[]) decodedObjects[i];
                            for (int j = 0; j < arrayOut.length; j++)
                            {
                                assertEquals("Decoded Djutils array vector element matches", arrayIn[j], arrayOut[j]);
                            }
                        }
                        else
                        {
                            assertTrue(
                                    "decoded object at index " + i + "(" + objects[i]
                                            + ") equals corresponding object in input",
                                    deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])));
                        }
                    }
                }
            }
        }
    }

    /**
     * Test that jagged matrices are detected and cause a SerializationException.
     */
    @Test
    public void testJaggedMatrices()
    {
        int[][] integer = new int[][] {{1, 2, 3}, {5, 6}};
        Integer[][] integerValues2 = new Integer[][] {{-1, -2}, {-4, -5, -6}};
        short[][] shortValues = new short[][] {{10, 20}, {40, 50, 60}};
        Short[][] shortValues2 = new Short[][] {{-10, -20, -30}, {-40, -50}};
        long[][] longValues = new long[][] {{1000, 2000, 3000}, {3000, 4000}};
        Long[][] longValues2 = new Long[][] {{-1000L, -2000L}, {-3000L, -4000L, -5000L}};
        byte[][] byteValues = new byte[][] {{12, 13}, {15, 16, 17}};
        Byte[][] byteValues2 = new Byte[][] {{-12, -13, -14}, {-15, -16}};
        boolean[][] boolValues = new boolean[][] {{false, true, true}, {false, false}};
        Boolean[][] boolValues2 = new Boolean[][] {{true, true}, {true, true, true}};
        float[][] floatValues = new float[][] {{12.3f, 23.4f}, {44.4f, 55.5f, 66.6f}};
        Float[][] floatValues2 = new Float[][] {{-12.3f, -23.4f, -34.5f}, {-11.1f, -22.2f}};
        double[][] doubleValues = new double[][] {{23.45, 34.56, 45.67}, {55.5, 66.6}};
        Double[][] doubleValues2 = new Double[][] {{-23.45, -34.56}, {-22.2, -33.3, -44.4}};
        Object[] objects = new Object[] {integer, integerValues2, shortValues, shortValues2, longValues, longValues2,
                byteValues, byteValues2, floatValues, floatValues2, doubleValues, doubleValues2, boolValues, boolValues2};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (Object object : objects)
            {
                Object[] singleObjectArray = new Object[] {object};
                try
                {
                    TypedMessage.encodeUTF16(endianUtil, singleObjectArray);
                    fail("Jagged array should have thrown a SerializationException");
                }
                catch (SerializationException se)
                {
                    // Ignore expected exception
                }
                try
                {
                    TypedMessage.encodeUTF8(endianUtil, singleObjectArray);
                    fail("Jagged array should have thrown a SerializationException");
                }
                catch (SerializationException se)
                {
                    // Ignore expected exception
                }
            }
        }
    }

    /**
     * Test that the encoder throws a SerializationException when given something that it does not know how to serialize.
     */
    @Test
    public void testUnhandledObject()
    {
        File file = new File("whatever");
        Object[] objects = new Object[] {file};
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            try
            {
                TypedMessage.encodeUTF16(endianUtil, objects);
                fail("Non serializable object should have thrown a SerializationException");
            }
            catch (SerializationException se)
            {
                // Ignore expected exception
            }

            Integer[][] badMatrix = new Integer[0][0];
            objects = new Object[] {badMatrix};
            try
            {
                TypedMessage.encodeUTF16(endianUtil, objects);
                fail("Zero sized matrix should have thrown a SerializationException");
            }
            catch (SerializationException se)
            {
                // Ignore expected exception
            }
        }
    }

    /**
     * Test the Pointer class.
     */
    @Test
    public void pointerTest()
    {
        Pointer pointer = new Pointer();
        assertEquals("initial offset is 0", 0, pointer.get());
        assertEquals("initial offset is 0", 0, pointer.getAndIncrement(10));
        assertEquals("offset is now 10", 10, pointer.get());
        pointer.inc(20);
        assertEquals("offset is now 30", 30, pointer.get());
        assertTrue("ToString method returns something descriptive", pointer.toString().startsWith("Pointer"));
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
     * Compare two arrays of any type (stolen from java.util.Arrays).
     * @param e1 Object (should be some kind of array)
     * @param e2 Object (should be some kind of array)
     * @return boolean; true of the arrays have the same type, size and all elements in the arrays are equal to their
     *         counterpart
     */
    static boolean deepEquals0(final Object e1, final Object e2)
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

    /**
     * Test the UnitType class.
     */
    @Test
    public void testUnitType()
    {
        byte code = 127;
        Class<AccelerationUnit> unitClass = AccelerationUnit.class;
        String name = "AccelerationName";
        String description = "AccelerationDescription";
        String siUnit = "[m/s^2]";
        SerializationUnits testAccelerationUnitType = new SerializationUnits(code, unitClass, name, description, siUnit);
        assertEquals("code is returned", code, testAccelerationUnitType.getCode());
        assertEquals("unit class is returned", unitClass, testAccelerationUnitType.getDjunitsType());
        assertEquals("name is returned", name, testAccelerationUnitType.getName());
        assertEquals("description is returned", description, testAccelerationUnitType.getDescription());
        assertEquals("SI unit is returned", siUnit, testAccelerationUnitType.getSiUnit());
        assertTrue("toString returns something descriptive", testAccelerationUnitType.toString().startsWith("UnitType"));

        byte undefined = 126;
        assertEquals("new unit is in the byte type map", testAccelerationUnitType, SerializationUnits.getUnitType(code));
        assertNull("undefined byte returns null", SerializationUnits.getUnitType(undefined));
        assertEquals("djunits type is returned", unitClass, SerializationUnits.getUnitClass(code));
        assertNull("undefined byte returns null", SerializationUnits.getUnitClass(undefined));
        assertEquals("speed type can be found by byte code", SerializationUnits.SPEED,
                SerializationUnits.getUnitType((byte) 22));
        assertEquals("speed type can be found by unit type", SerializationUnits.SPEED,
                SerializationUnits.getUnitType(SpeedUnit.SI));
        assertEquals("speed type can be found by non SI unit type", SerializationUnits.SPEED,
                SerializationUnits.getUnitType(SpeedUnit.FOOT_PER_SECOND));
        assertEquals("speed unit code can be found by unit type", 22, SerializationUnits.getUnitCode(SpeedUnit.SI));
    }

    /**
     * Test all constructors for SerializationException.
     */
    @Test
    public final void serializationExceptionTest()
    {
        String message = "MessageString";
        Exception e = new SerializationException(message);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should be null", null, e.getCause());
        e = new SerializationException();
        assertEquals("cause should be null", null, e.getCause());
        String causeString = "CauseString";
        Throwable cause = new Throwable(causeString);
        e = new SerializationException(cause);
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
        e = new SerializationException(message, cause);
        assertEquals("message should be our message", message, e.getMessage());
        assertEquals("cause should not be our cause", cause, e.getCause());
        assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
        for (boolean enableSuppression : new boolean[] {true, false})
        {
            for (boolean writableStackTrace : new boolean[] {true, false})
            {
                e = new SerializationException(message, cause, enableSuppression, writableStackTrace);
                assertTrue("Exception should not be null", null != e);
                assertEquals("message should be our message", message, e.getMessage());
                assertEquals("cause should not be our cause", cause, e.getCause());
                assertEquals("cause description should be our cause string", causeString, e.getCause().getMessage());
                // Don't know how to check if suppression is enabled/disabled
                StackTraceElement[] stackTrace = new StackTraceElement[1];
                stackTrace[0] = new StackTraceElement("a", "b", "c", 1234);
                try
                {
                    e.setStackTrace(stackTrace);
                }
                catch (Exception e1)
                {
                    assertTrue("Stack trace should be writable", writableStackTrace);
                    continue;
                }
                // You wouldn't believe it, but a call to setStackTrace if non-writable is silently ignored
                StackTraceElement[] retrievedStackTrace = e.getStackTrace();
                if (retrievedStackTrace.length > 0)
                {
                    assertTrue("stack trace should be writable", writableStackTrace);
                }
            }
        }
    }

    /**
     * Test the remainder of the EndianUtil class.
     */
    @Test
    public void testEndianUtil()
    {
        assertTrue("EndianUtil.BIG_ENDIAN is big endian", EndianUtil.BIG_ENDIAN.isBigEndian());
        assertFalse("EndianUtil.LITTLE_ENDIAN is not big endian", EndianUtil.LITTLE_ENDIAN.isBigEndian());
        assertEquals("Platform endianness matches what EndianUtil says", ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN),
                EndianUtil.isPlatformBigEndian());
        assertTrue("EndianUtil.BIG_ENDIAN is big endian", EndianUtil.bigEndian().isBigEndian());
        assertFalse("EndianUtil.LITTLE_ENDIAN is not big endian", EndianUtil.littleEndian().isBigEndian());
        assertTrue("EndianUtil has descriptive toString method", EndianUtil.BIG_ENDIAN.toString().startsWith("EndianUtil"));
    }

    /**
     * Test the toString and dataClassName methods of the BasicSerializer.
     */
    @Test
    public void testBasicSerializer()
    {
        byte code = 123;
        String dataClassName = "dataClass";
        BasicSerializer<Byte> testSerializer = new BasicSerializer<Byte>(code, dataClassName)
        {

            @Override
            public int size(final Byte object) throws SerializationException
            {
                // Auto-generated method stub; never called
                return 0;
            }

            @Override
            public int sizeWithPrefix(final Byte object) throws SerializationException
            {
                // Auto-generated method stub; never called
                return 0;
            }

            @Override
            public void serialize(final Byte object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                    throws SerializationException
            {
                // Auto-generated method stub; never called
            }

            @Override
            public void serializeWithPrefix(final Byte object, final byte[] buffer, final Pointer pointer,
                    final EndianUtil endianUtil) throws SerializationException
            {
                // Auto-generated method stub; never called
            }

            @Override
            public Byte deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
                    throws SerializationException
            {
                // Auto-generated method stub; never called
                return null;
            }

            @Override
            public int getNumberOfDimensions()
            {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        // We only want to test two methods; so we don't have to provide real implementation for other methods
        assertEquals("data class name is returned", dataClassName, testSerializer.dataClassName());
        assertTrue("toString returns something descriptive", testSerializer.toString().startsWith("BasicSerializer"));
    }

}
