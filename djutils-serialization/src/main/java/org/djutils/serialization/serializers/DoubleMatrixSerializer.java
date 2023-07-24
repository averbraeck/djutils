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
import org.djunits.value.vdouble.matrix.SIMatrix;
import org.djunits.value.vdouble.matrix.base.DoubleMatrix;
import org.djunits.value.vdouble.matrix.data.DoubleMatrixData;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS DoubleMatrix.
 * <p>
 * Copyright (c) 2019-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 * @param <V> the vector type
 * @param <M> the matrix type
 */
public class DoubleMatrixSerializer<U extends Unit<U>, S extends DoubleScalar<U, S>, V extends DoubleVector<U, S, V>,
        M extends DoubleMatrix<U, S, V, M>> extends ArrayOrMatrixWithUnitSerializer<U, M>
{
    /** The cache to make the lookup of the constructor for a Vevtor belonging to a unit faster. */
    private static final Map<Unit<?>, Constructor<? extends DoubleMatrix<?, ?, ?, ?>>> CACHE = new HashMap<>();

    /** */
    public DoubleMatrixSerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT_MATRIX, "Djunits_DoubleMatrix", 2);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final M adm) throws SerializationException
    {
        try
        {
            return 4 + 4 + 2 + 8 * adm.rows() * adm.cols();
        }
        catch (ValueRuntimeException e)
        {
            throw new SerializationException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final M adm, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        try
        {
            endianUtil.encodeInt(adm.rows(), buffer, pointer.getAndIncrement(4));
            endianUtil.encodeInt(adm.cols(), buffer, pointer.getAndIncrement(4));
            encodeUnit(adm.getDisplayUnit(), buffer, pointer, endianUtil);
            for (int i = 0; i < adm.rows(); i++)
            {
                for (int j = 0; j < adm.cols(); j++)
                {
                    endianUtil.encodeDouble(adm.get(i, j).getSI(), buffer, pointer.getAndIncrement(8));
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
        try
        {
            int height = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
            int width = endianUtil.decodeInt(buffer, pointer.getAndIncrement(4));
            Unit<? extends Unit<?>> unit = getUnit(buffer, pointer, endianUtil);
            double[][] array = new double[height][width];
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    array[i][j] = endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8));
                }
            }
            DoubleMatrixData fvd = DoubleMatrixData.instantiate(array, IdentityScale.SCALE, StorageType.DENSE);
            return instantiateAnonymous(fvd, unit);
        }
        catch (ValueRuntimeException exception)
        {
            throw new SerializationException(exception);
        }
    }

    /**
     * Instantiate the DoubleMatrix based on its unit. Loose check for types on the compiler. This allows the unit to be
     * specified as a Unit&lt;?&gt; type.<br>
     * <b>Note</b> that it is possible to make mistakes with anonymous units.
     * @param data DoubleMatrixData; the values
     * @param unit Unit&lt;?&gt;; the unit in which the value is expressed
     * @return M; an instantiated DoubleMatrix with the provided displayUunit
     * @param <U> the unit type
     * @param <S> the scalar type
     * @param <V> the vector type
     * @param <M> the matrix type
     */
    @SuppressWarnings("unchecked")
    public static <U extends Unit<U>, S extends DoubleScalar<U, S>, V extends DoubleVector<U, S, V>,
            M extends DoubleMatrix<U, S, V, M>> M instantiateAnonymous(final DoubleMatrixData data, final Unit<?> unit)
    {
        try
        {
            Constructor<? extends DoubleMatrix<?, ?, ?, ?>> matrixConstructor = CACHE.get(unit);
            if (matrixConstructor == null)
            {
                if (!unit.getClass().getSimpleName().endsWith("Unit"))
                {
                    throw new ClassNotFoundException("Unit " + unit.getClass().getSimpleName()
                            + " name does noet end with 'Unit'. Cannot find corresponding scalar");
                }
                Class<? extends DoubleMatrix<?, ?, ?, ?>> matrixClass;
                if (unit instanceof SIUnit)
                {
                    matrixClass = SIMatrix.class;
                }
                else
                {
                    matrixClass = (Class<DoubleMatrix<?, ?, ?, ?>>) Class.forName("org.djunits.value.vdouble.matrix."
                            + unit.getClass().getSimpleName().replace("Unit", "") + "Matrix");
                }
                matrixConstructor = matrixClass.getDeclaredConstructor(DoubleMatrixData.class, unit.getClass());
                CACHE.put(unit, matrixConstructor);
            }
            return (M) matrixConstructor.newInstance(data, unit);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
        {
            throw new UnitRuntimeException(
                    "Cannot instantiate DoubleMatrix of unit " + unit.toString() + ". Reason: " + exception.getMessage());
        }
    }

}
