package org.djutils.serialization;

/**
 * Serializer for arrays or matrices.
 * @param <T> type; with [] or [][]
 * @param <E> type without [] or [][]
 */
public abstract class ArrayOrMatrixSerializer<T extends Object, E extends Object> extends BasicSerializer<T>
{
    /** Size of one element of the encoded data. */
    private final int elementSize;

    /** Number of dimension; 1 for array, 2 for matrix. */
    private final int numberOfDimensions;

    /**
     * Construct a new ArrayOrMatrixSerializere.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param elementSize int; the number of bytes needed to encode one additional array, or matrix element
     * @param dataClassName String; returned by the dataClassName method
     * @param numberOfDimensions int; should be 1 for array serializer and 2 for matrix serializer
     */
    ArrayOrMatrixSerializer(final byte type, final int elementSize, final String dataClassName, final int numberOfDimensions)
    {
        super(type, dataClassName);
        this.elementSize = elementSize;
        this.numberOfDimensions = numberOfDimensions;
    }

    /**
     * Return the number of bytes needed to encode one additional element.
     * @return int; the number of bytes needed to encode one additional element
     */
    public final int getElementSize()
    {
        return this.elementSize;
    }

    /**
     * Return the number of dimensions of the stored data.
     * @return int; 1 for array, 2 for matrix
     */
    @Override
    public final int getNumberOfDimensions()
    {
        return this.numberOfDimensions;
    }

    /**
     * Serializer for one array or matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param object E; the object to serialize
     * @param buffer byte[]; the byte buffer for the serialized object
     * @param offset int; index in byte buffer where first serialized byte must be stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     */
    abstract void serializeElement(E object, byte[] buffer, int offset, EndianUtil endianUtil);

    /**
     * Deserializer for one array or matrix element (without type prefix) must be implemented in implementing sub classes.
     * @param buffer byte[]; the byte buffer from which the object is to be deserialized
     * @param offset int; index in byte buffer where first byte of the object is stored
     * @param endianUtil EndianUtil; selects bigEndian or littleEndian encoding
     * @return E; the deserialized object
     */
    abstract E deSerializeElement(byte[] buffer, int offset, EndianUtil endianUtil);

}
