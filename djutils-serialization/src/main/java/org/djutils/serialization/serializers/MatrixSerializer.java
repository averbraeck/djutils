package org.djutils.serialization.serializers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.Unit;
import org.djunits.unit.UnitRuntimeException;
import org.djunits.unit.si.SIUnit;
import org.djunits.vecmat.def.Matrix;
import org.djunits.vecmat.dnxm.MatrixNxM;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatMatrix.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <M> the matrix type
 */
public class MatrixSerializer<U extends Unit<U, ?>, M extends MatrixNxM<?>> extends ArrayOrMatrixWithUnitSerializer<U, M>
{
    /** The cache to make the lookup of the constructor for a Matrix belonging to a unit faster. */
    private static final Map<Unit<?, ?>, Constructor<? extends Matrix<?, ?, ?, ?, ?>>> CACHE = new HashMap<>();

    /** */
    public MatrixSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_MATRIX, "Djunits_FloatMatrix", 2);
    }

    @Override
    public int size(final M matrix)
    {
        return 4 + 4 + 2 + 8 * matrix.rows() * matrix.cols();
    }

    @Override
    public int getElementSize()
    {
        return 4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final M matrix, final byte[] buffer, final Pointer pointer, final Endianness endianness)
            throws SerializationException
    {
        endianness.encodeInt(matrix.rows(), buffer, pointer.getAndIncrement(4));
        endianness.encodeInt(matrix.cols(), buffer, pointer.getAndIncrement(4));
        encodeUnit((U) matrix.getDisplayUnit(), buffer, pointer, endianness);
        for (int i = 0; i < matrix.rows(); i++)
        {
            for (int j = 0; j < matrix.cols(); j++)
            {
                endianness.encodeDouble(matrix.get(i, j).si(), buffer, pointer.getAndIncrement(4));
            }
        }
    }

    @Override
    public M deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianness) throws SerializationException
    {
        int height = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
        Unit<?, ?> unit = getUnit(buffer, pointer, endianness);
        double[][] array = new double[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                array[i][j] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
            }
        }
        return MatrixNxM.of(array, unit);
    }

    /**
     * Instantiate the FloatMatrix based on its unit. Loose check for types on the compiler. This allows the unit to be
     * specified as a Unit&lt;?&gt; type.<br>
     * <b>Note</b> that it is possible to make mistakes with anonymous units.
     * @param data the values
     * @param unit the unit in which the value is expressed
     * @return an instantiated FloatMatrix with the provided displayUunit
     * @param <U> the unit type
     * @param <S> the scalar type
     * @param <V> the vector type
     * @param <M> the matrix type
     */
    @SuppressWarnings("unchecked")
    public static <U extends Unit<U>, S extends FloatScalar<U, S>, V extends FloatVector<U, S, V>,
            M extends FloatMatrix<U, S, V, M>> M instantiateAnonymous(final FloatMatrixData data, final Unit<?, ?> unit)
    {
        try
        {
            Constructor<? extends FloatMatrix<?, ?, ?, ?>> matrixConstructor = CACHE.get(unit);
            if (matrixConstructor == null)
            {
                if (!unit.getClass().getSimpleName().endsWith("Unit"))
                {
                    throw new ClassNotFoundException("Unit " + unit.getClass().getSimpleName()
                            + " name does noet end with 'Unit'. Cannot find corresponding scalar");
                }
                Class<? extends FloatMatrix<?, ?, ?, ?>> matrixClass;
                if (unit instanceof SIUnit)
                {
                    matrixClass = FloatSIMatrix.class;
                }
                else
                {
                    matrixClass = (Class<FloatMatrix<?, ?, ?, ?>>) Class.forName("org.djunits.value.vfloat.matrix.Float"
                            + unit.getClass().getSimpleName().replace("Unit", "") + "Matrix");
                }
                matrixConstructor = matrixClass.getDeclaredConstructor(FloatMatrixData.class, unit.getClass());
                CACHE.put(unit, matrixConstructor);
            }
            return (M) matrixConstructor.newInstance(data, unit);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
        {
            throw new UnitRuntimeException(
                    "Cannot instantiate FloatMatrix of unit " + unit.toString() + ". Reason: " + exception.getMessage());
        }
    }

}
