package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.vecmat.def.Vector;
import org.djunits.vecmat.dn.VectorN;
import org.djunits.vecmat.storage.DenseDoubleDataSi;
import org.djunits.vecmat.storage.DenseFloatDataSi;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes an array of quantity Vectors, each with their own quantity.
 * <p>
 * Copyright (c) 2019-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public abstract class VectorArrayCodec extends BasicCodec<Vector<?, ?, ?, ?, ?>[]>
{
    /**
     * Construct the VectorArrayCodec.
     * @param type the field type as defined by the {@link FieldTypes} class
     */
    public VectorArrayCodec(final byte type)
    {
        super(type);
    }

    @Override
    public int getNumberOfDimensions()
    {
        return 1;
    }

    @Override
    public boolean hasUnit()
    {
        return true;
    }

    /** Converter for Quantity Column Vector Array with float values. */
    public static final BasicCodec<Vector<?, ?, ?, ?, ?>[]> COL_VECTOR_ARRAY_FLOAT =
            new VectorArrayCodec(FieldTypes.FLOAT_32_UNIT_COL_VECTOR_ARRAY)
            {
                @Override
                public int size(final Vector<?, ?, ?, ?, ?>[] vector)
                {
                    return 4 + 4 + 2 * vector.length + 4 * vector.length * vector[0].size();
                }

                @Override
                public void serialize(final Vector<?, ?, ?, ?, ?>[] vector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(vector[0].size(), buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(vector.length, buffer, pointer.getAndIncrement(4));
                    for (int v = 0; v < vector.length; v++)
                    {
                        UnitCodec.encodeQuantityUnit(vector[v].get(0), buffer, pointer);
                    }
                    for (int i = 0; i < vector[0].size(); i++)
                    {
                        for (int v = 0; v < vector.length; v++)
                        {
                            endianness.encodeFloat((float) vector[v].get(i).si(), buffer, pointer.getAndIncrement(4));
                        }
                    }
                }

                @Override
                public Vector<?, ?, ?, ?, ?>[] deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int vecs = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Vector<?, ?, ?, ?, ?>[] vectorArray = new Vector[vecs];
                    Unit<?, ?>[] unitArray = new Unit[vecs];
                    for (int v = 0; v < vecs; v++)
                    {
                        unitArray[v] = UnitCodec.getUnit(buffer, pointer);
                    }
                    float[][] dataSi = new float[vecs][rows];
                    for (int i = 0; i < rows; i++)
                    {
                        for (int v = 0; v < vecs; v++)
                        {
                            dataSi[v][i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                        }
                    }
                    for (int v = 0; v < vecs; v++)
                    {
                        Unit<?, ?> unit = unitArray[v];
                        VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseFloatDataSi(dataSi[v], rows, 1), unit);
                        UnitCodec.setDisplayUnit(vector, unit);
                        vectorArray[v] = vector;
                    }
                    return vectorArray;
                }
            };

    /** Converter for Quantity Column Vector Array with double values. */
    public static final BasicCodec<Vector<?, ?, ?, ?, ?>[]> COL_VECTOR_ARRAY_DOUBLE =
            new VectorArrayCodec(FieldTypes.DOUBLE_64_UNIT_COL_VECTOR_ARRAY)
            {
                @Override
                public int size(final Vector<?, ?, ?, ?, ?>[] vector)
                {
                    return 4 + 4 + 2 * vector.length + 8 * vector.length * vector[0].size();
                }

                @Override
                public void serialize(final Vector<?, ?, ?, ?, ?>[] vector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(vector[0].size(), buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(vector.length, buffer, pointer.getAndIncrement(4));
                    for (int v = 0; v < vector.length; v++)
                    {
                        UnitCodec.encodeQuantityUnit(vector[v].get(0), buffer, pointer);
                    }
                    for (int i = 0; i < vector[0].size(); i++)
                    {
                        for (int v = 0; v < vector.length; v++)
                        {
                            endianness.encodeDouble(vector[v].get(i).si(), buffer, pointer.getAndIncrement(8));
                        }
                    }
                }

                @Override
                public Vector<?, ?, ?, ?, ?>[] deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int rows = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int vecs = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Vector<?, ?, ?, ?, ?>[] vectorArray = new Vector[vecs];
                    Unit<?, ?>[] unitArray = new Unit[vecs];
                    for (int v = 0; v < vecs; v++)
                    {
                        unitArray[v] = UnitCodec.getUnit(buffer, pointer);
                    }
                    double[][] dataSi = new double[vecs][rows];
                    for (int i = 0; i < rows; i++)
                    {
                        for (int v = 0; v < vecs; v++)
                        {
                            dataSi[v][i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                        }
                    }
                    for (int v = 0; v < vecs; v++)
                    {
                        Unit<?, ?> unit = unitArray[v];
                        VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseDoubleDataSi(dataSi[v], rows, 1), unit);
                        UnitCodec.setDisplayUnit(vector, unit);
                        vectorArray[v] = vector;
                    }
                    return vectorArray;
                }
            };

    /** Converter for Quantity Row Vector Array with float values. */
    public static final BasicCodec<Vector<?, ?, ?, ?, ?>[]> ROW_VECTOR_ARRAY_FLOAT =
            new VectorArrayCodec(FieldTypes.FLOAT_32_UNIT_ROW_VECTOR_ARRAY)
            {
                @Override
                public int size(final Vector<?, ?, ?, ?, ?>[] vector)
                {
                    return 4 + 4 + 2 * vector.length + 4 * vector.length * vector[0].size();
                }

                @Override
                public void serialize(final Vector<?, ?, ?, ?, ?>[] vector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(vector.length, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(vector[0].size(), buffer, pointer.getAndIncrement(4));
                    for (int v = 0; v < vector.length; v++)
                    {
                        UnitCodec.encodeQuantityUnit(vector[v].get(0), buffer, pointer);
                        for (int i = 0; i < vector[v].size(); i++)
                        {
                            endianness.encodeFloat((float) vector[v].get(i).si(), buffer, pointer.getAndIncrement(4));
                        }
                    }
                }

                @Override
                public Vector<?, ?, ?, ?, ?>[] deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int vecs = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Vector<?, ?, ?, ?, ?>[] vectorArray = new Vector[vecs];
                    for (int v = 0; v < vecs; v++)
                    {
                        Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                        float[] dataSi = new float[cols];
                        for (int i = 0; i < cols; i++)
                        {
                            dataSi[i] = endianness.decodeFloat(buffer, pointer.getAndIncrement(4));
                        }
                        VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseFloatDataSi(dataSi, 1, cols), unit);
                        UnitCodec.setDisplayUnit(vector, unit);
                        vectorArray[v] = vector;
                    }
                    return vectorArray;
                }
            };

    /** Converter for Quantity Row Vector Array with double values. */
    public static final BasicCodec<Vector<?, ?, ?, ?, ?>[]> ROW_VECTOR_ARRAY_DOUBLE =
            new VectorArrayCodec(FieldTypes.DOUBLE_64_UNIT_ROW_VECTOR_ARRAY)
            {
                @Override
                public int size(final Vector<?, ?, ?, ?, ?>[] vector)
                {
                    return 4 + 4 + 2 * vector.length + 8 * vector.length * vector[0].size();
                }

                @Override
                public void serialize(final Vector<?, ?, ?, ?, ?>[] vector, final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    endianness.encodeInt(vector.length, buffer, pointer.getAndIncrement(4));
                    endianness.encodeInt(vector[0].size(), buffer, pointer.getAndIncrement(4));
                    for (int v = 0; v < vector.length; v++)
                    {
                        UnitCodec.encodeQuantityUnit(vector[v].get(0), buffer, pointer);
                        for (int i = 0; i < vector[v].size(); i++)
                        {
                            endianness.encodeDouble(vector[v].get(i).si(), buffer, pointer.getAndIncrement(8));
                        }
                    }
                }

                @Override
                public Vector<?, ?, ?, ?, ?>[] deserialize(final byte[] buffer, final Pointer pointer,
                        final Endianness endianness) throws SerializationException
                {
                    int vecs = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    int cols = endianness.decodeInt(buffer, pointer.getAndIncrement(4));
                    Vector<?, ?, ?, ?, ?>[] vectorArray = new Vector[vecs];
                    for (int v = 0; v < vecs; v++)
                    {
                        Unit<?, ?> unit = UnitCodec.getUnit(buffer, pointer);
                        double[] dataSi = new double[cols];
                        for (int i = 0; i < cols; i++)
                        {
                            dataSi[i] = endianness.decodeDouble(buffer, pointer.getAndIncrement(8));
                        }
                        VectorN<?, ?, ?, ?, ?> vector = VectorN.Col.ofSi(new DenseDoubleDataSi(dataSi, 1, cols), unit);
                        UnitCodec.setDisplayUnit(vector, unit);
                        vectorArray[v] = vector;
                    }
                    return vectorArray;
                }
            };

}
