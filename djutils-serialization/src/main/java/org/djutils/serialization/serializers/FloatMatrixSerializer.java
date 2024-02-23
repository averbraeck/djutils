package org.djutils.serialization.serializers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.SIUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.scale.IdentityScale;
import org.djunits.unit.util.UnitRuntimeException;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vfloat.matrix.FloatSIMatrix;
import org.djunits.value.vfloat.matrix.base.FloatMatrix;
import org.djunits.value.vfloat.matrix.data.FloatMatrixData;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djunits.value.vfloat.vector.base.FloatVector;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatMatrix.
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 * @param <M> the matrix type
 */
public class FloatMatrixSerializer<U extends Unit<U>, S extends FloatScalar<U, S>, V extends FloatVector<U, S, V>,
        M extends FloatMatrix<U, S, V, M>> extends ArrayOrMatrixWithUnitSerializer<U, M>
{
    /** The cache to make the lookup of the constructor for a Vevtor belonging to a unit faster. */
    private static final Map<Unit<?>, Constructor<? extends FloatMatrix<?, ?, ?, ?>>> CACHE = new HashMap<>();

    /** */
    public FloatMatrixSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT_MATRIX, "Djunits_FloatMatrix", 2);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final M afm) throws SerializationException
    {
        try
        {
            return 4 + 4 + 2 + 4 * afm.rows() * afm.cols();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final M afm, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(afm.rows(), buffer, pointer.getAndIncrement(4));
            endianUtil.encodeInt(afm.cols(), buffer, pointer.getAndIncrement(4));
            encodeUnit(afm.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < afm.rows(); i++)
            {
                for (int j = 0; j < afm.cols(); j++)
                {
                    endianUtil.encodeFloat(afm.get(i, j).getSI(), buffer, pointer.getAndIncrement(4));
                }
            }
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public M deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
    {
        int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
        Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
        float[][] array = new float[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                array[i][j] = endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4));
            }
        }
        try
        {
            FloatMatrixData fvd = FloatMatrixData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

    /**
     * Instantiate the FloatMatrix based on its unit. Loose check for types on the compiler. This allows the unit to be
     * specified as a Unit&lt;?&gt; type.<br>
     * <b>Note</b> that it is possible to make mistakes with anonymous units.
     * @param data FloatMatrixData; the values
     * @param unit Unit&lt;?&gt;; the unit in which the value is expressed
     * @return M; an instantiated FloatMatrix with the provided displayUunit
     * @param <U> the unit type
     * @param <S> the scalar type
     * @param <V> the vector type
     * @param <M> the matrix type
     */
    @SuppressWarnings("unchecked")
    public static <U extends Unit<U>, S extends FloatScalar<U, S>, V extends FloatVector<U, S, V>,
            M extends FloatMatrix<U, S, V, M>> M instantiateAnonymous(final FloatMatrixData data, final Unit<?> unit)
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
