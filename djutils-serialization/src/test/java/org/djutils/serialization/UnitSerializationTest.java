package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djunits.quantity.Acceleration;
import org.djunits.quantity.Area;
import org.djunits.quantity.Dimensionless;
import org.djunits.quantity.ElectricCurrent;
import org.djunits.quantity.ElectricalResistance;
import org.djunits.quantity.Energy;
import org.djunits.quantity.Length;
import org.djunits.quantity.Speed;
import org.djunits.quantity.def.Quantity;
import org.djunits.unit.Unitless;
import org.djunits.unit.si.SIUnit;
import org.djunits.vecmat.def.Matrix;
import org.djunits.vecmat.def.Vector;
import org.djunits.vecmat.dn.VectorN;
import org.djunits.vecmat.dnxm.MatrixNxM;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.decoderdumper.HexDumper;
import org.djutils.serialization.codecs.MessageCodec;
import org.djutils.serialization.util.SerialDataDumper;
import org.djutils.test.UnitTest;
import org.junit.jupiter.api.Test;

/**
 * UnitSerializationTest tests the encoding / decoding of values (Quantity, Vector, Matrix) with units.
 * <p>
 * Copyright (c) 2023-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public class UnitSerializationTest extends AbstractSerializationTest
{

    /**
     * Test the QuantityType class.
     */
    @Test
    public void testQuantityType()
    {
        byte code = 127;
        Class<Acceleration> quantityClass = Acceleration.class;
        String name = "AccelerationName";
        String description = "AccelerationDescription";
        String siUnit = "[m/s^2]";
        QuantityType testAccelerationQuantityType = new QuantityType(code, quantityClass, name, description, siUnit);
        assertEquals(code, testAccelerationQuantityType.getCode(), "code is returned");
        assertEquals(quantityClass, testAccelerationQuantityType.getQuantityClass(), "unit class is returned");
        assertEquals(name, testAccelerationQuantityType.getName(), "name is returned");
        assertEquals(description, testAccelerationQuantityType.getDescription(), "description is returned");
        assertEquals(siUnit, testAccelerationQuantityType.getSiUnit(), "SI unit is returned");
        assertTrue(testAccelerationQuantityType.toString().startsWith("QuantityType"),
                "toString returns something descriptive");

        byte undefined = 126;
        assertEquals(testAccelerationQuantityType, QuantityType.getQuantityType(code), "new unit is in the byte type map");
        assertNull(QuantityType.getQuantityType(undefined), "undefined byte returns null");
        assertEquals(quantityClass, QuantityType.getQuantityClass(code), "djunits type is returned");
        assertNull(QuantityType.getQuantityClass(undefined), "undefined byte returns null");
        assertEquals(QuantityType.SPEED, QuantityType.getQuantityType((byte) 22), "speed type can be found by byte code");
        assertEquals(QuantityType.SPEED, QuantityType.getQuantityType(Speed.Unit.SI), "speed type can be found by unit type");
        assertEquals(QuantityType.SPEED, QuantityType.getQuantityType(Speed.Unit.ft_s),
                "speed type can be found by non SI unit type");
        assertEquals(22, QuantityType.getQuantityType(Speed.Unit.SI).getCode(), "speed unit code can be found by unit type");

        assertEquals(testAccelerationQuantityType, new QuantityType(code, quantityClass, name, description, siUnit));
        assertNotEquals(testAccelerationQuantityType, QuantityType.ACCELERATION);
        assertNotEquals(QuantityType.ACCELERATION, null);
        assertNotEquals(testAccelerationQuantityType, new Object());
        assertNotEquals(testAccelerationQuantityType, new QuantityType(125, quantityClass, name, description, siUnit));
        assertNotEquals(testAccelerationQuantityType, new QuantityType(code, Length.class, name, description, siUnit));
        assertNotEquals(testAccelerationQuantityType, new QuantityType(code, quantityClass, "x", description, siUnit));
        assertNotEquals(testAccelerationQuantityType, new QuantityType(code, quantityClass, name, "x", siUnit));
        assertNotEquals(testAccelerationQuantityType, new QuantityType(code, quantityClass, name, description, "N/K"));

        UnitTest.testFail(() -> UnitType.getByteCode(SIUnit.of("K/mol")));

        // restore the cache
        new QuantityType(QuantityType.ACCELERATION.getCode(), Acceleration.class, QuantityType.ACCELERATION.getName(),
                QuantityType.ACCELERATION.getDescription(), QuantityType.ACCELERATION.getSiUnit());
        new QuantityType(QuantityType.LENGTH.getCode(), Length.class, QuantityType.LENGTH.getName(),
                QuantityType.LENGTH.getDescription(), QuantityType.LENGTH.getSiUnit());
    }

    /**
     * Test encoding and decoding of strongly typed quantities (DJUNITS).
     * @throws SerializationException when that happens uncaught, this test has failed
     */
    @Test
    public void testQuantities() throws SerializationException
    {
        Length length = new Length(123.4, Length.Unit.ft);
        Dimensionless value = new Dimensionless(345.6, Unitless.BASE);
        Area area = new Area(66.66, Area.Unit.ac);
        VectorN.Col<ElectricCurrent> currents = VectorN.Col.of(new double[] {1.2, 2.3, 3.4}, ElectricCurrent.Unit.mA);
        VectorN.Row<ElectricalResistance> resistors =
                new VectorN.Row<>(DenseFloatDataSi.of(new float[] {1.2f, 4.7f, 6.8f}, 1, 3, ElectricalResistance.Unit.kohm),
                        ElectricalResistance.Unit.kohm);
        MatrixNxM<ElectricCurrent> currentMatrix =
                MatrixNxM.of(new double[][] {{1.2, 2.3, 3.4}, {5.5, 6.6, 7.7}}, ElectricCurrent.Unit.mA);
        MatrixNxM<ElectricalResistance> resistorMatrix = new MatrixNxM<ElectricalResistance>(
                DenseFloatDataSi.of(new float[][] {{1.2f, 4.7f, 6.8f}, {2.2f, 3.3f, 4.4f}}, ElectricalResistance.Unit.kohm),
                ElectricalResistance.Unit.kohm);

        Object[] doubleObjects = new Object[] {length, value, area, currents, currentMatrix};
        Object[] floatObjects = new Object[] {length, value, area, resistors, resistorMatrix};

        // test double
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            byte[] serialized = MessageCodec.encode(true, false, endianness, doubleObjects);
            HexDumper.hexDumper(serialized);
            String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
            assertFalse(sdd.contains("Error"));
            assertFalse(sdd.contains("quantity_32_unit"));
            assertFalse(sdd.contains("vector_32_unit"));
            assertFalse(sdd.contains("matrix_32_unit"));
            assertTrue(sdd.contains("quantity_64_unit"));
            assertTrue(sdd.contains("vector_64_unit"));
            assertTrue(sdd.contains("matrix_64_unit"));
            for (boolean primitive : new boolean[] {false, true})
            {
                Object[] decodedObjects = primitive ? MessageCodec.decodeToPrimitiveDataTypes(endianness, serialized)
                        : MessageCodec.decodeToObjectDataTypes(endianness, serialized);
                assertEquals(doubleObjects.length, decodedObjects.length, "Size of decoded matches");
                for (int i = 0; i < doubleObjects.length; i++)
                {
                    assertEquals(doubleObjects[i], decodedObjects[i], "decoded object at index " + i + " (" + doubleObjects[i]
                            + ") equals corresponding object in input");
                }
            }
        }

        // test float
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            byte[] serialized = MessageCodec.encode(true, true, endianness, floatObjects);
            HexDumper.hexDumper(serialized);
            String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
            assertFalse(sdd.contains("Error"));
            assertTrue(sdd.contains("quantity_32_unit"));
            assertTrue(sdd.contains("vector_32_unit"));
            assertTrue(sdd.contains("matrix_32_unit"));
            assertFalse(sdd.contains("quantity_64_unit"));
            assertFalse(sdd.contains("vector_64_unit"));
            assertFalse(sdd.contains("matrix_64_unit"));
            for (boolean primitive : new boolean[] {false, true})
            {
                Object[] decodedObjects = primitive ? MessageCodec.decodeToPrimitiveDataTypes(endianness, serialized)
                        : MessageCodec.decodeToObjectDataTypes(endianness, serialized);
                assertEquals(floatObjects.length, decodedObjects.length, "Size of decoded matches");
                for (int i = 0; i < floatObjects.length; i++)
                {
                    if (floatObjects[i] instanceof Quantity eq)
                    {
                        var dq = (Quantity<?>) decodedObjects[i];
                        assertEquals(eq.si, dq.si, 10.0 * Math.ulp((float) eq.si));
                        assertEquals(eq.getDisplayUnit(), dq.getDisplayUnit());
                    }
                    else if (floatObjects[i] instanceof Vector ev)
                    {
                        var dv = (Vector<?, ?, ?, ?, ?>) decodedObjects[i];
                        assertArrayEquals(ev.getSiArray(), dv.getSiArray(), 100.0 * Math.ulp(ev.si(0)));
                        assertEquals(ev.getDisplayUnit(), dv.getDisplayUnit());
                    }
                    else if (floatObjects[i] instanceof Matrix em)
                    {
                        var dm = (Matrix<?, ?, ?, ?, ?>) decodedObjects[i];
                        assertArrayEquals(em.getSiArray(), dm.getSiArray(), 100.0 * Math.ulp(em.si(0, 0)));
                        assertEquals(em.getDisplayUnit(), dm.getDisplayUnit());
                    }
                }
            }
        }
    }

    // /** Non existing unit. */
    // private static final class NonsenseUnit2 extends Unit<NonsenseUnit2>
    // {
    // /** */
    // private static final long serialVersionUID = 1L;
    //
    // /** one instance of the unit. */
    // public static final Quantity<NonsenseUnit2> BASE = new Quantity<>("Nonsense", "kg.K/mol.s");
    //
    // /** The SI unit for acceleration is kg.K/mol.s. */
    // public static final NonsenseUnit2 SI = new NonsenseUnit2().build(new Unit.Builder<NonsenseUnit2>().setQuantity(BASE)
    // .setId("kg.K/mol.s")
    // .setName("x")
    // .setUnitSystem(UnitSystem.SI_DERIVED)
    // .setSiPrefixes(SIPrefixes.NONE, 1.0)
    // .setScale(IdentityScale.SCALE));
    // }
    //
    // /** Non-existing scalar. */
    // private static final class NonsenseScalar extends DoubleScalarRel<NonsenseUnit2, NonsenseScalar>
    // {
    // /** */
    // private static final long serialVersionUID = 1L;
    //
    // /**
    // * @param value v
    // * @param unit u
    // */
    // NonsenseScalar(final double value, final NonsenseUnit2 unit)
    // {
    // super(value, unit);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public NonsenseScalar instantiateRel(final double value, final NonsenseUnit2 unit)
    // {
    // return null;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public DoubleScalarRel<?, ?> reciprocal()
    // {
    // return null;
    // }
    // }
    //
    // /**
    // * Test encoding and decoding of strongly typed quantities (DJUNITS).
    // * @throws SerializationException when that happens uncaught, this test has failed
    // * @throws ValueRuntimeException when that happens uncaught, this test has failed
    // */
    // @Test
    // public void testDJunitsErrors() throws SerializationException, ValueRuntimeException
    // {
    // for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
    // {
    // UnitTest.testFail(() -> TypedObject.encode(NonsenseUnit2.SI, endianness));
    // NonsenseScalar ns = new NonsenseScalar(1.0, NonsenseUnit2.SI);
    // UnitTest.testFail(() -> TypedObject.encode(ns, endianness));
    // }
    //
    // }

    /**
     * Test stored information about djunits Quantities.
     * @throws SerializationException when that happens uncaught, this test has failed
     */
    @Test
    public void testSerializationQuantities() throws SerializationException
    {
        QuantityType areaSerUnit = QuantityType.AREA;
        assertEquals("Area", areaSerUnit.getName());
        assertEquals("Area (m2)", areaSerUnit.getDescription());
        assertEquals(5, areaSerUnit.getCode());
        assertEquals(Area.class, areaSerUnit.getQuantityClass());
        assertEquals("[m^2]", areaSerUnit.getSiUnit());

        assertEquals(Length.class, QuantityType.getQuantityClass((byte) 16));
        assertEquals(16, QuantityType.getQuantityType(Length.Unit.in).getCode());
        assertEquals(areaSerUnit, QuantityType.getQuantityType((byte) 5));
        assertEquals(areaSerUnit, QuantityType.getQuantityType(Area.Unit.a));

        assertNotEquals(QuantityType.RADIOACTIVITY, areaSerUnit);
        assertNotEquals(new Object(), areaSerUnit);
        assertNotEquals(QuantityType.RADIOACTIVITY.hashCode(), areaSerUnit.hashCode());
        assertNotEquals(new Object().hashCode(), areaSerUnit.hashCode());
    }

    /**
     * Test stored information about djunits display types.
     * @throws SerializationException when that happens uncaught, this test has failed
     */
    @Test
    public void testUnitTypes() throws SerializationException
    {
        QuantityType areaSerUnit = QuantityType.AREA;
        UnitType aream2 = UnitType.AREA_SQUARE_METER;
        UnitType areaacre = UnitType.AREA_ACRE;
        UnitType masskg = UnitType.MASS_KILOGRAM;
        assertEquals("m2", aream2.getAbbreviation());
        assertEquals(0, aream2.getByteCode());
        assertEquals(18, areaacre.getByteCode());
        assertEquals(Area.Unit.m2, aream2.getUnit());
        assertEquals(Area.Unit.ac, areaacre.getUnit());
        assertEquals(0, aream2.getIntCode());
        assertEquals(18, areaacre.getIntCode());
        assertEquals("SQUARE_METER", aream2.getName());
        assertEquals("ACRE", areaacre.getName());
        assertEquals(areaSerUnit, aream2.getQuantityType());
        assertEquals(areaacre.getQuantityType(), aream2.getQuantityType());

        assertEquals(8, UnitType.getByteCode(ElectricalResistance.Unit.statohm));
        assertEquals(areaacre, UnitType.getUnitType(Area.Unit.ac));
        assertEquals(UnitType.ENERGY_CALORIE, UnitType.getUnitType((byte) 11, 30));
        assertEquals(areaacre, UnitType.getUnitType(areaSerUnit, 18));
        assertEquals(30, UnitType.getIntCode(Energy.Unit.cal));
        assertEquals(Energy.Unit.cal, UnitType.getUnit((byte) 11, 30));
        assertEquals(Area.Unit.ac, UnitType.getUnit(areaSerUnit, 18));

        assertNotEquals(aream2, areaacre);
        assertNotEquals(masskg, areaacre);
        assertNotEquals(new Object(), areaacre);
        assertNotEquals(aream2.hashCode(), areaacre.hashCode());
        assertNotEquals(masskg.hashCode(), areaacre.hashCode());
        assertNotEquals(new Object().hashCode(), areaacre.hashCode());
    }

    // /**
    // * Test a double column matrix, where each column can contain a different quantity and/or display unit.
    // * @throws SerializationException if that happens uncaught; this test has failed
    // */
    // @Test
    // public void testDoubleUnitColumnMatrix() throws SerializationException
    // {
    // DoubleVector<?, ?, ?>[] array =
    // new DoubleVector[] {new LengthVector(new double[] {0.1, 0.2, 0.3}, LengthUnit.INCH, StorageType.DENSE),
    // new TimeVector(new double[] {10.1, 20.2, 30.3}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
    // Object[] objects = new Object[] {array};
    // for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
    // {
    // for (boolean encodeUTF8 : new boolean[] {false, true})
    // {
    // byte[] serialized = encodeUTF8 ? MessageCodec.encodeUTF8(endianness, objects)
    // : MessageCodec.encodeUTF16(endianness, objects);
    // assertEquals(FieldTypes.DOUBLE_64_UNIT_COL_VECTOR_ARRAY, serialized[0]);
    // HexDumper.hexDumper(serialized);
    // String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
    // assertFalse(sdd.contains("Error"));
    // assertTrue(sdd.contains("Djunits_double_vector_array"));
    // assertTrue(sdd.contains("height"));
    // assertTrue(sdd.contains("width"));
    // assertTrue(sdd.contains("0.1in"));
    // assertTrue(sdd.contains("10.1min"));
    // assertTrue(sdd.contains("0.2in"));
    // assertTrue(sdd.contains("20.2min"));
    // assertTrue(sdd.contains("0.3in"));
    // assertTrue(sdd.contains("30.3min"));
    // for (boolean primitive : new boolean[] {false, true})
    // {
    // Object[] decodedObjects = primitive ? MessageCodec.decodeToPrimitiveDataTypes(endianness, serialized)
    // : MessageCodec.decodeToObjectDataTypes(endianness, serialized);
    // assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
    // for (int i = 0; i < objects.length; i++)
    // {
    // if (objects[i] instanceof DoubleVector<?, ?, ?>[])
    // {
    // DoubleVector<?, ?, ?>[] arrayIn = (DoubleVector<?, ?, ?>[]) objects[i];
    // DoubleVector<?, ?, ?>[] arrayOut = (DoubleVector<?, ?, ?>[]) decodedObjects[i];
    // for (int j = 0; j < arrayOut.length; j++)
    // {
    // assertEquals(arrayIn[j], arrayOut[j], "Decoded Djutils array vector element matches");
    // }
    // }
    // else
    // {
    // assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
    // "decoded object at index " + i + "(" + objects[i]
    // + ") equals corresponding object in input");
    // }
    // }
    // }
    // }
    // }
    // }
    //
    // /**
    // * Test a float column matrix, where each column can contain a different quantity and/or display unit.
    // * @throws ValueRuntimeException if that happens uncaught; this test has failed
    // * @throws SerializationException if that happens uncaught; this test has failed
    // */
    // @Test
    // public void testFloatUnitColumnMatrix() throws ValueRuntimeException, SerializationException
    // {
    // FloatVector<?, ?, ?>[] array =
    // new FloatVector[] {new FloatLengthVector(new float[] {0.1f, 0.2f, 0.3f}, LengthUnit.INCH, StorageType.DENSE),
    // new FloatTimeVector(new float[] {10.1f, 20.2f, 30.3f}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
    // Object[] objects = new Object[] {array};
    // for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
    // {
    // for (boolean encodeUTF8 : new boolean[] {false, true})
    // {
    // byte[] serialized = encodeUTF8 ? MessageCodec.encodeUTF8(endianness, objects)
    // : MessageCodec.encodeUTF16(endianness, objects);
    // assertEquals(FieldTypes.FLOAT_32_UNIT_COL_VECTOR_ARRAY, serialized[0]);
    // HexDumper.hexDumper(serialized);
    // String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
    // assertFalse(sdd.contains("Error"));
    // assertTrue(sdd.contains("Djunits_float_vector_array"));
    // assertTrue(sdd.contains("height"));
    // assertTrue(sdd.contains("width"));
    // assertTrue(sdd.contains("0.1in"));
    // assertTrue(sdd.contains("10."));
    // assertTrue(sdd.contains("min"));
    // assertTrue(sdd.contains("0.2in"));
    // assertTrue(sdd.contains("0.3in"));
    // for (boolean primitive : new boolean[] {false, true})
    // {
    // Object[] decodedObjects = primitive ? MessageCodec.decodeToPrimitiveDataTypes(endianness, serialized)
    // : MessageCodec.decodeToObjectDataTypes(endianness, serialized);
    // assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
    // for (int i = 0; i < objects.length; i++)
    // {
    // if (objects[i] instanceof FloatVector<?, ?, ?>[])
    // {
    // FloatVector<?, ?, ?>[] arrayIn = (FloatVector<?, ?, ?>[]) objects[i];
    // FloatVector<?, ?, ?>[] arrayOut = (FloatVector<?, ?, ?>[]) decodedObjects[i];
    // for (int j = 0; j < arrayOut.length; j++)
    // {
    // assertEquals(arrayIn[j], arrayOut[j], "Decoded Djutils array vector element matches");
    // }
    // }
    // else
    // {
    // assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
    // "decoded object at index " + i + "(" + objects[i]
    // + ") equals corresponding object in input");
    // }
    // }
    // }
    // }
    // }
    // }
    //
    // /**
    // * Test exceptions with instantiation of unit column matrices.
    // */
    // @Test
    // public void testUnitColumnMatrixExceptions()
    // {
    // DoubleVector<?, ?, ?>[] dRagged =
    // new DoubleVector[] {new LengthVector(new double[] {0.1, 0.2, 0.3}, LengthUnit.INCH, StorageType.DENSE),
    // new TimeVector(new double[] {10.1, 20.2}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
    // FloatVector<?, ?, ?>[] fRagged =
    // new FloatVector[] {new FloatLengthVector(new float[] {0.1f, 0.2f, 0.3f}, LengthUnit.INCH, StorageType.DENSE),
    // new FloatTimeVector(new float[] {10.1f, 20.2f}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
    // UnitTest.testFail(() -> TypedObject.encodeUTF8(dRagged, Endianness.BIG_ENDIAN));
    // UnitTest.testFail(() -> TypedObject.encodeUTF8(fRagged, Endianness.BIG_ENDIAN));
    // }

}
