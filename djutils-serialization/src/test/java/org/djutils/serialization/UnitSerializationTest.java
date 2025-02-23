package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AreaUnit;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.ElectricalCurrentUnit;
import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.unit.EnergyUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.matrix.ElectricalCurrentMatrix;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.vector.ElectricalCurrentVector;
import org.djunits.value.vdouble.vector.LengthVector;
import org.djunits.value.vdouble.vector.TimeVector;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djunits.value.vfloat.matrix.FloatElectricalResistanceMatrix;
import org.djunits.value.vfloat.scalar.FloatArea;
import org.djunits.value.vfloat.vector.FloatElectricalResistanceVector;
import org.djutils.decoderdumper.HexDumper;
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
        SerializationUnits testAccelerationUnitType = new SerializationUnits(code, unitClass, name, description, siUnit);
        assertEquals(code, testAccelerationUnitType.getCode(), "code is returned");
        assertEquals(unitClass, testAccelerationUnitType.getDjunitsType(), "unit class is returned");
        assertEquals(name, testAccelerationUnitType.getName(), "name is returned");
        assertEquals(description, testAccelerationUnitType.getDescription(), "description is returned");
        assertEquals(siUnit, testAccelerationUnitType.getSiUnit(), "SI unit is returned");
        assertTrue(testAccelerationUnitType.toString().startsWith("UnitType"), "toString returns something descriptive");

        byte undefined = 126;
        assertEquals(testAccelerationUnitType, SerializationUnits.getUnitType(code), "new unit is in the byte type map");
        assertNull(SerializationUnits.getUnitType(undefined), "undefined byte returns null");
        assertEquals(unitClass, SerializationUnits.getUnitClass(code), "djunits type is returned");
        assertNull(SerializationUnits.getUnitClass(undefined), "undefined byte returns null");
        assertEquals(SerializationUnits.SPEED, SerializationUnits.getUnitType((byte) 22),
                "speed type can be found by byte code");
        assertEquals(SerializationUnits.SPEED, SerializationUnits.getUnitType(SpeedUnit.SI),
                "speed type can be found by unit type");
        assertEquals(SerializationUnits.SPEED, SerializationUnits.getUnitType(SpeedUnit.FOOT_PER_SECOND),
                "speed type can be found by non SI unit type");
        assertEquals(22, SerializationUnits.getUnitCode(SpeedUnit.SI), "speed unit code can be found by unit type");
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
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            byte[] serialized = TypedMessage.encodeUTF16(endianUtil, objects);
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

    /**
     * Test stored information about djunits SerializationUnits.
     * @throws SerializationException when that happens uncaught, this test has failed
     * @throws ValueRuntimeException when that happens uncaught, this test has failed
     */
    @Test
    public void testSerializationUnits() throws SerializationException, ValueRuntimeException
    {
        SerializationUnits areaSerUnit = SerializationUnits.AREA;
        assertEquals("Area", areaSerUnit.getName());
        assertEquals("Area (m2)", areaSerUnit.getDescription());
        assertEquals(5, areaSerUnit.getCode());
        assertEquals(AreaUnit.class, areaSerUnit.getDjunitsType());
        assertEquals("[m^2]", areaSerUnit.getSiUnit());

        assertEquals(LengthUnit.class, SerializationUnits.getUnitClass((byte) 16));
        assertEquals(16, SerializationUnits.getUnitCode(LengthUnit.INCH));
        assertEquals(areaSerUnit, SerializationUnits.getUnitType((byte) 5));
        assertEquals(areaSerUnit, SerializationUnits.getUnitType(AreaUnit.ARE));

        assertNotEquals(SerializationUnits.RADIOACTIVITY, areaSerUnit);
        assertNotEquals(new Object(), areaSerUnit);
        assertNotEquals(SerializationUnits.RADIOACTIVITY.hashCode(), areaSerUnit.hashCode());
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
        SerializationUnits areaSerUnit = SerializationUnits.AREA;
        DisplayType aream2 = DisplayType.AREA_SQUARE_METER;
        DisplayType areaacre = DisplayType.AREA_ACRE;
        DisplayType masskg = DisplayType.MASS_KILOGRAM;
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

        assertEquals(8, DisplayType.getByteCode(ElectricalResistanceUnit.STATOHM));
        assertEquals(areaacre, DisplayType.getDisplayType(AreaUnit.ACRE));
        assertEquals(DisplayType.ENERGY_CALORIE, DisplayType.getDisplayType((byte) 11, 30));
        assertEquals(areaacre, DisplayType.getDisplayType(areaSerUnit, 18));
        assertEquals(30, DisplayType.getIntCode(EnergyUnit.CALORIE));
        assertEquals(EnergyUnit.CALORIE, DisplayType.getUnit((byte) 11, 30));
        assertEquals(AreaUnit.ACRE, DisplayType.getUnit(areaSerUnit, 18));

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
        for (EndianUtil endianUtil : new EndianUtil[] {EndianUtil.BIG_ENDIAN, EndianUtil.LITTLE_ENDIAN})
        {
            for (boolean encodeUTF8 : new boolean[] {false, true})
            {
                byte[] serialized = encodeUTF8 ? TypedMessage.encodeUTF8(endianUtil, objects)
                        : TypedMessage.encodeUTF16(endianUtil, objects);
                assertEquals(endianUtil.isBigEndian() ? FieldTypes.DOUBLE_64_UNIT_COLUMN_MATRIX
                        : FieldTypes.DOUBLE_64_UNIT_COLUMN_MATRIX_LE, serialized[0]);
                HexDumper.hexDumper(serialized);
                SerialDataDumper.serialDataDumper(endianUtil, serialized);
                for (boolean primitive : new boolean[] {false, true})
                {
                    Object[] decodedObjects = primitive ? TypedMessage.decodeToPrimitiveDataTypes(serialized)
                            : TypedMessage.decodeToObjectDataTypes(serialized);
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

}
