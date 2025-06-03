package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djunits.quantity.Quantity;
import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AreaUnit;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.ElectricalCurrentUnit;
import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.unit.EnergyUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.SIUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.si.SIPrefixes;
import org.djunits.unit.unitsystem.UnitSystem;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.matrix.ElectricalCurrentMatrix;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.base.DoubleScalarRel;
import org.djunits.value.vdouble.vector.ElectricalCurrentVector;
import org.djunits.value.vdouble.vector.LengthVector;
import org.djunits.value.vdouble.vector.TimeVector;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vfloat.matrix.FloatElectricalResistanceMatrix;
import org.djunits.value.vfloat.scalar.FloatArea;
import org.djunits.value.vfloat.vector.FloatElectricalResistanceVector;
import org.djunits.value.vfloat.vector.FloatLengthVector;
import org.djunits.value.vfloat.vector.FloatTimeVector;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djutils.decoderdumper.HexDumper;
import org.djutils.exceptions.Try;
import org.djutils.serialization.util.SerialDataDumper;
import org.junit.jupiter.api.Test;

/**
 * UnitSerializationTest tests the encoding / decoding of values (Scalar, Vector, Matrix) with units.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class UnitSerializationTest extends AbstractSerializationTest
{

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
        QuantityType testAccelerationUnitType = new QuantityType(code, unitClass, name, description, siUnit);
        assertEquals(code, testAccelerationUnitType.getCode(), "code is returned");
        assertEquals(unitClass, testAccelerationUnitType.getDjunitsType(), "unit class is returned");
        assertEquals(name, testAccelerationUnitType.getName(), "name is returned");
        assertEquals(description, testAccelerationUnitType.getDescription(), "description is returned");
        assertEquals(siUnit, testAccelerationUnitType.getSiUnit(), "SI unit is returned");
        assertTrue(testAccelerationUnitType.toString().startsWith("UnitType"), "toString returns something descriptive");

        byte undefined = 126;
        assertEquals(testAccelerationUnitType, QuantityType.getUnitType(code), "new unit is in the byte type map");
        assertNull(QuantityType.getUnitType(undefined), "undefined byte returns null");
        assertEquals(unitClass, QuantityType.getUnitClass(code), "djunits type is returned");
        assertNull(QuantityType.getUnitClass(undefined), "undefined byte returns null");
        assertEquals(QuantityType.SPEED, QuantityType.getUnitType((byte) 22),
                "speed type can be found by byte code");
        assertEquals(QuantityType.SPEED, QuantityType.getUnitType(SpeedUnit.SI),
                "speed type can be found by unit type");
        assertEquals(QuantityType.SPEED, QuantityType.getUnitType(SpeedUnit.FOOT_PER_SECOND),
                "speed type can be found by non SI unit type");
        assertEquals(22, QuantityType.getUnitCode(SpeedUnit.SI), "speed unit code can be found by unit type");

        assertEquals(testAccelerationUnitType, new QuantityType(code, unitClass, name, description, siUnit));
        assertNotEquals(testAccelerationUnitType, QuantityType.ACCELERATION);
        assertNotEquals(QuantityType.ACCELERATION, null);
        assertNotEquals(testAccelerationUnitType, new Object());
        assertNotEquals(testAccelerationUnitType, new QuantityType(125, unitClass, name, description, siUnit));
        assertNotEquals(testAccelerationUnitType, new QuantityType(code, LengthUnit.class, name, description, siUnit));
        assertNotEquals(testAccelerationUnitType, new QuantityType(code, unitClass, "x", description, siUnit));
        assertNotEquals(testAccelerationUnitType, new QuantityType(code, unitClass, name, "x", siUnit));
        assertNotEquals(testAccelerationUnitType, new QuantityType(code, unitClass, name, description, "N/K"));

        Try.testFail(() -> QuantityType.getUnitCode(SIUnit.of("K/mol")));

        // restore the cache
        new QuantityType(QuantityType.ACCELERATION.getCode(), AccelerationUnit.class,
                QuantityType.ACCELERATION.getName(), QuantityType.ACCELERATION.getDescription(),
                QuantityType.ACCELERATION.getSiUnit());
        new QuantityType(QuantityType.LENGTH.getCode(), LengthUnit.class, QuantityType.LENGTH.getName(),
                QuantityType.LENGTH.getDescription(), QuantityType.LENGTH.getSiUnit());
    }

    /**
     * Test encoding and decoding of strongly typed quantities (DJUNITS).
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueRuntimeException when that happens uncaught, this test has failed
     */
    @Test
    public void testDJunits() throws SerializationException, ValueRuntimeException
    {
        Length length = new Length(123.4, LengthUnit.FOOT);
        Dimensionless value = new Dimensionless(345.6, DimensionlessUnit.SI);
        FloatArea area = new FloatArea(66.66f, AreaUnit.ACRE);
        ElectricalCurrentVector currents =
                new ElectricalCurrentVector(new double[] {1.2, 2.3, 3.4}, ElectricalCurrentUnit.MILLIAMPERE, StorageType.DENSE);
        FloatElectricalResistanceVector resistors = new FloatElectricalResistanceVector(new float[] {1.2f, 4.7f, 6.8f},
                ElectricalResistanceUnit.KILOOHM, StorageType.DENSE);
        ElectricalCurrentMatrix currentMatrix = new ElectricalCurrentMatrix(new double[][] {{1.2, 2.3, 3.4}, {5.5, 6.6, 7.7}},
                ElectricalCurrentUnit.MILLIAMPERE, StorageType.DENSE);
        FloatElectricalResistanceMatrix resistorMatrix = new FloatElectricalResistanceMatrix(
                new float[][] {{1.2f, 4.7f, 6.8f}, {2.2f, 3.3f, 4.4f}}, ElectricalResistanceUnit.KILOOHM, StorageType.DENSE);

        Object[] objects = new Object[] {length, value, area, currents, resistors, currentMatrix, resistorMatrix};
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            byte[] serialized = TypedMessage.encodeUTF16(endianness, objects);
            HexDumper.hexDumper(serialized);
            String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
            assertFalse(sdd.contains("Error"));
            assertTrue(sdd.contains("Djunits_DoubleScalar"));
            assertTrue(sdd.contains("Djunits_FloatScalar"));
            assertTrue(sdd.contains("Djunits_DoubleVector"));
            assertTrue(sdd.contains("Djunits_FloatVector"));
            assertTrue(sdd.contains("Djunits_DoubleMatrix"));
            assertTrue(sdd.contains("Djunits_FloatMatrix"));
            for (boolean primitive : new boolean[] {false, true})
            {
                Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(endianness, serialized)
                        : TypedMessage.decodeToObjectDataTypes(endianness, serialized);
                assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                for (int i = 0; i < objects.length; i++)
                {
                    assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
                            "decoded object at index " + i + "(" + objects[i] + ") equals corresponding object in input");
                }
            }
        }
    }

    /** Non existing unit. */
    private static final class NonsenseUnit2 extends Unit<NonsenseUnit2>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** one instance of the unit. */
        public static final Quantity<NonsenseUnit2> BASE = new Quantity<>("Nonsense", "kg.K/mol.s");

        /** The SI unit for acceleration is kg.K/mol.s. */
        public static final NonsenseUnit2 SI = new NonsenseUnit2().build(new Unit.Builder<NonsenseUnit2>().setQuantity(BASE)
                .setId("kg.K/mol.s").setName("x").setUnitSystem(UnitSystem.SI_DERIVED).setSiPrefixes(SIPrefixes.NONE, 1.0)
                .setScale(IdentityScale.SCALE));
    }

    /** Non-existing scalar. */
    private static final class NonsenseScalar extends DoubleScalarRel<NonsenseUnit2, NonsenseScalar>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param value v
         * @param unit u
         */
        NonsenseScalar(final double value, final NonsenseUnit2 unit)
        {
            super(value, unit);
        }

        /** {@inheritDoc} */
        @Override
        public NonsenseScalar instantiateRel(final double value, final NonsenseUnit2 unit)
        {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public DoubleScalarRel<?, ?> reciprocal()
        {
            return null;
        }
    }

    /**
     * Test encoding and decoding of strongly typed quantities (DJUNITS).
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueRuntimeException when that happens uncaught, this test has failed
     */
    @Test
    public void testDJunitsErrors() throws SerializationException, ValueRuntimeException
    {
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            Try.testFail(() -> TypedObject.encode(endianness, NonsenseUnit2.SI));
            NonsenseScalar ns = new NonsenseScalar(1.0, NonsenseUnit2.SI);
            Try.testFail(() -> TypedObject.encode(endianness, ns));
        }

    }

    /**
     * Test stored information about djunits SerializationUnits.
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueRuntimeException when that happens uncaught, this test has failed
     */
    @Test
    public void testSerializationUnits() throws SerializationException, ValueRuntimeException
    {
        QuantityType areaSerUnit = QuantityType.AREA;
        assertEquals("Area", areaSerUnit.getName());
        assertEquals("Area (m2)", areaSerUnit.getDescription());
        assertEquals(5, areaSerUnit.getCode());
        assertEquals(AreaUnit.class, areaSerUnit.getDjunitsType());
        assertEquals("[m^2]", areaSerUnit.getSiUnit());

        assertEquals(LengthUnit.class, QuantityType.getUnitClass((byte) 16));
        assertEquals(16, QuantityType.getUnitCode(LengthUnit.INCH));
        assertEquals(areaSerUnit, QuantityType.getUnitType((byte) 5));
        assertEquals(areaSerUnit, QuantityType.getUnitType(AreaUnit.ARE));

        assertNotEquals(QuantityType.RADIOACTIVITY, areaSerUnit);
        assertNotEquals(new Object(), areaSerUnit);
        assertNotEquals(QuantityType.RADIOACTIVITY.hashCode(), areaSerUnit.hashCode());
        assertNotEquals(new Object().hashCode(), areaSerUnit.hashCode());
    }

    /**
     * Test stored information about djunits display types.
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueRuntimeException when that happens uncaught, this test has failed
     */
    @Test
    public void testDJunitDisplayTypes() throws SerializationException, ValueRuntimeException
    {
        QuantityType areaSerUnit = QuantityType.AREA;
        UnitType aream2 = UnitType.AREA_SQUARE_METER;
        UnitType areaacre = UnitType.AREA_ACRE;
        UnitType masskg = UnitType.MASS_KILOGRAM;
        assertEquals("m2", aream2.getAbbreviation());
        assertEquals(0, aream2.getByteCode());
        assertEquals(18, areaacre.getByteCode());
        assertEquals(AreaUnit.SQUARE_METER, aream2.getDjunitsType());
        assertEquals(AreaUnit.ACRE, areaacre.getDjunitsType());
        assertEquals(0, aream2.getIntCode());
        assertEquals(18, areaacre.getIntCode());
        assertEquals("SQUARE_METER", aream2.getName());
        assertEquals("ACRE", areaacre.getName());
        assertEquals(areaSerUnit, aream2.getUnitType());
        assertEquals(areaacre.getUnitType(), aream2.getUnitType());

        assertEquals(8, UnitType.getByteCode(ElectricalResistanceUnit.STATOHM));
        assertEquals(areaacre, UnitType.getDisplayType(AreaUnit.ACRE));
        assertEquals(UnitType.ENERGY_CALORIE, UnitType.getDisplayType((byte) 11, 30));
        assertEquals(areaacre, UnitType.getDisplayType(areaSerUnit, 18));
        assertEquals(30, UnitType.getIntCode(EnergyUnit.CALORIE));
        assertEquals(EnergyUnit.CALORIE, UnitType.getUnit((byte) 11, 30));
        assertEquals(AreaUnit.ACRE, UnitType.getUnit(areaSerUnit, 18));

        assertNotEquals(aream2, areaacre);
        assertNotEquals(masskg, areaacre);
        assertNotEquals(new Object(), areaacre);
        assertNotEquals(aream2.hashCode(), areaacre.hashCode());
        assertNotEquals(masskg.hashCode(), areaacre.hashCode());
        assertNotEquals(new Object().hashCode(), areaacre.hashCode());
    }

    /**
     * Test a double column matrix, where each column can contain a different quantity and/or display unit.
     * @throws ValueRuntimeException if that happens uncaught; this test has failed
     * @throws SerializationException if that happens uncaught; this test has failed
     */
    @Test
    public void testDoubleUnitColumnMatrix() throws ValueRuntimeException, SerializationException
    {
        DoubleVector<?, ?, ?>[] array =
                new DoubleVector[] {new LengthVector(new double[] {0.1, 0.2, 0.3}, LengthUnit.INCH, StorageType.DENSE),
                        new TimeVector(new double[] {10.1, 20.2, 30.3}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
        Object[] objects = new Object[] {array};
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianness, objects)
                        : TypedMessage.encodeUTF16(endianness, objects);
                assertEquals(FieldTypes.DOUBLE_64_UNIT_COLUMN_MATRIX, serialized[0]);
                HexDumper.hexDumper(serialized);
                String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
                assertFalse(sdd.contains("Error"));
                assertTrue(sdd.contains("Djunits_double_vector_array"));
                assertTrue(sdd.contains("height"));
                assertTrue(sdd.contains("width"));
                assertTrue(sdd.contains("0.1in"));
                assertTrue(sdd.contains("10.1min"));
                assertTrue(sdd.contains("0.2in"));
                assertTrue(sdd.contains("20.2min"));
                assertTrue(sdd.contains("0.3in"));
                assertTrue(sdd.contains("30.3min"));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(endianness, serialized)
                            : TypedMessage.decodeToObjectDataTypes(endianness, serialized);
                    assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                    for (int i = 0; i < objects.length; i++)
                    {
                        if (objects[i] instanceof DoubleVector<?, ?, ?>[])
                        {
                            DoubleVector<?, ?, ?>[] arrayIn = (DoubleVector<?, ?, ?>[]) objects[i];
                            DoubleVector<?, ?, ?>[] arrayOut = (DoubleVector<?, ?, ?>[]) decodedObjects[i];
                            for (int j = 0; j < arrayOut.length; j++)
                            {
                                assertEquals(arrayIn[j], arrayOut[j], "Decoded Djutils array vector element matches");
                            }
                        }
                        else
                        {
                            assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
                                    "decoded object at index " + i + "(" + objects[i]
                                            + ") equals corresponding object in input");
                        }
                    }
                }
            }
        }
    }

    /**
     * Test a float column matrix, where each column can contain a different quantity and/or display unit.
     * @throws ValueRuntimeException if that happens uncaught; this test has failed
     * @throws SerializationException if that happens uncaught; this test has failed
     */
    @Test
    public void testFloatUnitColumnMatrix() throws ValueRuntimeException, SerializationException
    {
        FloatVector<?, ?, ?>[] array =
                new FloatVector[] {new FloatLengthVector(new float[] {0.1f, 0.2f, 0.3f}, LengthUnit.INCH, StorageType.DENSE),
                        new FloatTimeVector(new float[] {10.1f, 20.2f, 30.3f}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
        Object[] objects = new Object[] {array};
        for (Endianness endianness : new Endianness[] {Endianness.BIG_ENDIAN, Endianness.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianness, objects)
                        : TypedMessage.encodeUTF16(endianness, objects);
                assertEquals(FieldTypes.FLOAT_32_UNIT_COLUMN_MATRIX, serialized[0]);
                HexDumper.hexDumper(serialized);
                String sdd = SerialDataDumper.serialDataDumper(endianness, serialized);
                assertFalse(sdd.contains("Error"));
                assertTrue(sdd.contains("Djunits_float_vector_array"));
                assertTrue(sdd.contains("height"));
                assertTrue(sdd.contains("width"));
                assertTrue(sdd.contains("0.1in"));
                assertTrue(sdd.contains("10."));
                assertTrue(sdd.contains("min"));
                assertTrue(sdd.contains("0.2in"));
                assertTrue(sdd.contains("0.3in"));
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(endianness, serialized)
                            : TypedMessage.decodeToObjectDataTypes(endianness, serialized);
                    assertEquals(objects.length, decodedObjects.length, "Size of decoded matches");
                    for (int i = 0; i < objects.length; i++)
                    {
                        if (objects[i] instanceof FloatVector<?, ?, ?>[])
                        {
                            FloatVector<?, ?, ?>[] arrayIn = (FloatVector<?, ?, ?>[]) objects[i];
                            FloatVector<?, ?, ?>[] arrayOut = (FloatVector<?, ?, ?>[]) decodedObjects[i];
                            for (int j = 0; j < arrayOut.length; j++)
                            {
                                assertEquals(arrayIn[j], arrayOut[j], "Decoded Djutils array vector element matches");
                            }
                        }
                        else
                        {
                            assertTrue(deepEquals0(makePrimitive(objects[i]), makePrimitive(decodedObjects[i])),
                                    "decoded object at index " + i + "(" + objects[i]
                                            + ") equals corresponding object in input");
                        }
                    }
                }
            }
        }
    }

    /**
     * Test exceptions with instantiation of unit column matrices.
     */
    @Test
    public void testUnitColumnMatrixExceptions()
    {
        DoubleVector<?, ?, ?>[] dRagged =
                new DoubleVector[] {new LengthVector(new double[] {0.1, 0.2, 0.3}, LengthUnit.INCH, StorageType.DENSE),
                        new TimeVector(new double[] {10.1, 20.2}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
        FloatVector<?, ?, ?>[] fRagged =
                new FloatVector[] {new FloatLengthVector(new float[] {0.1f, 0.2f, 0.3f}, LengthUnit.INCH, StorageType.DENSE),
                        new FloatTimeVector(new float[] {10.1f, 20.2f}, TimeUnit.BASE_MINUTE, StorageType.DENSE)};
        Try.testFail(() -> TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, dRagged));
        Try.testFail(() -> TypedObject.encodeUTF8(Endianness.BIG_ENDIAN, fRagged));
    }

}
