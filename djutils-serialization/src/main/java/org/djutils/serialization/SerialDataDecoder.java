package org.djutils.serialization;

import java.io.IOException;

import org.djutils.decoderdumper.Decoder;

/**
 * Decoder for inspection of serialized data.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class SerialDataDecoder implements Decoder
{
    /** The endian util to use to decode multi-byte values. */
    private final EndianUtil endianUtil;

    /** Type of the data that is currently being decoded. */
    private byte currentFieldType;

    /** The serializer for the <code>currentFieldType</code>. */
    private Serializer<?> currentSerializer = null;

    /** Position in the data of the <code>currentFieldType</code>. */
    private int positionInData = -1;

    /** Position in the dataElementBytes where the next input byte shall be store. */
    private int nextDataElementByte = -1;

    /** Collects the bytes that constitute the current data element. */
    private byte[] dataElementBytes = new byte[0];

    /** Size of the data that is currently being decode. */
    private int totalDataSize = -1;

    /** Number of rows in an array or matrix. */
    private int rowCount;

    /** Number of columns in a matrix. */
    private int columnCount;

    /** Row of matrix or array that we are now reading. */
    private int currentRow;

    /** Column of matrix that we are now reading. */
    private int currentColumn;

    /** String builder for current output line. */
    private StringBuilder buffer = new StringBuilder();

    /**
     * Construct a new SerialDataDecoder.
     * @param endianUtil EndianUtil; the endian util to use to decode multi-byte values
     */
    SerialDataDecoder(final EndianUtil endianUtil)
    {
        this.endianUtil = endianUtil;
    }

    @Override
    public final String getResult()
    {
        String result = this.buffer.toString();
        this.buffer.setLength(0);
        return result;
    }

    @Override
    public final int getMaximumWidth()
    {
        return 40;
    }

    @Override
    public final boolean append(final int address, final byte theByte) throws IOException
    {
        boolean result = false;
        if (null == this.currentSerializer)
        {
            // We are expecting a field type byte
            this.currentFieldType = theByte;
            this.currentSerializer = TypedMessage.PRIMITIVE_DATA_DECODERS.get(this.currentFieldType);
            if (null == this.currentSerializer)
            {
                this.buffer.append(String.format("Bad field type %02x - resynchronizing", this.currentFieldType));
                result = true;
                // May eventually re-synchronize, but that could take a lot of data.
            }
            else
            {
                this.positionInData = 1;
                this.totalDataSize = 1; // to be adjusted
                if (this.currentSerializer instanceof ObjectSerializer)
                {
                    // TODO handle the display unit si unit and money unit
                }
                else
                {
                    try
                    {
                        if (this.currentSerializer.getNumberOfDimensions() == 0)
                        {
                            int size = this.currentSerializer.size(null);
                            this.totalDataSize += size;
                            prepareForDataElement(size);
                        }
                        else
                        {
                            int size = this.currentSerializer.getNumberOfDimensions() * 4;
                            prepareForDataElement(size);
                            this.totalDataSize += size;
                        }
                    }
                    catch (SerializationException e)
                    {
                        e.printStackTrace(); // Cannot happen
                    }
                }
            }
            return result;
        }
        if (this.nextDataElementByte < this.dataElementBytes.length)
        {
            this.dataElementBytes[this.nextDataElementByte] = theByte;
        }
        this.nextDataElementByte++;
        this.positionInData++;
        if (this.nextDataElementByte == this.dataElementBytes.length)
        {
            if (this.currentSerializer instanceof FixedSizeObjectSerializer)
            {
                try
                {
                    Object value = this.currentSerializer.deSerialize(this.dataElementBytes, new Pointer(), this.endianUtil);
                    buffer.append(value.toString());
                }
                catch (SerializationException e)
                {
                    buffer.append("Error deserializing data");
                }
            }
            else if (this.currentSerializer.getNumberOfDimensions() > 0)
            {
                if (this.rowCount == 0)
                {
                    // Got the height and width of a matrix, or length of an array
                    this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
                    this.currentRow = 0;
                    this.currentColumn = 0;
                    if (this.dataElementBytes.length == 8)
                    {
                        this.rowCount = this.columnCount;
                        this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 4);
                        this.buffer.append(String.format("%s height %d, width %d", this.currentSerializer.dataClassName(),
                                this.rowCount, this.columnCount));
                    }
                    else
                    {
                        this.rowCount = 1;
                        this.buffer
                                .append(String.format("%s length %d", this.currentSerializer.dataClassName(), this.columnCount));
                    }
                    int elementSize = -1;
                    if (this.currentSerializer instanceof ArrayOrMatrixSerializer<?, ?>)
                    {
                        elementSize = ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer).getElementSize();
                    }
                    else if (this.currentSerializer instanceof BasicPrimitiveArraySerializer)
                    {
                        elementSize = ((BasicPrimitiveArraySerializer<?>) this.currentSerializer).getElementSize();
                    }
                    else
                    {
                        throw new RuntimeException("Unhandled type of array or matrix serializer");
                    }
                    this.totalDataSize += elementSize * this.rowCount * this.columnCount;
                    prepareForDataElement(elementSize);
                    // System.out.println("Selecting element size " + elementSize + " for serializer "
                    // + this.currentSerializer.dataClassName());
                }
                else
                {
                    // Got one data element
                    if (this.currentSerializer instanceof ArrayOrMatrixSerializer<?, ?>)
                    {
                        Object value = ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer)
                                .deSerializeElement(dataElementBytes, 0, this.endianUtil);
                        this.buffer.append(value.toString());
                    }
                    else if (this.currentSerializer instanceof BasicPrimitiveArraySerializer)
                    {
                        // It looks like we'll have to do this ourselves.
                        BasicPrimitiveArraySerializer<?> basicPrimitiveArraySerializer =
                                (BasicPrimitiveArraySerializer<?>) this.currentSerializer;
                        switch (basicPrimitiveArraySerializer.fieldType())
                        {
                            case FieldTypes.BYTE_8_ARRAY:
                            case FieldTypes.BYTE_8_MATRIX:
                                this.buffer.append(String.format(" %02x", dataElementBytes[0]));
                                break;

                            case FieldTypes.SHORT_16_ARRAY:
                            case FieldTypes.SHORT_16_MATRIX:
                                this.buffer.append(String.format(" %d", endianUtil.decodeShort(dataElementBytes, 0)));
                                break;

                            case FieldTypes.INT_32_ARRAY:
                            case FieldTypes.INT_32_MATRIX:
                                this.buffer.append(String.format(" %d", endianUtil.decodeInt(dataElementBytes, 0)));
                                break;

                            case FieldTypes.LONG_64_ARRAY:
                            case FieldTypes.LONG_64_MATRIX:
                                this.buffer.append(String.format(" %d", endianUtil.decodeLong(dataElementBytes, 0)));
                                break;

                            case FieldTypes.FLOAT_32_ARRAY:
                            case FieldTypes.FLOAT_32_MATRIX:
                                this.buffer.append(String.format(" %f", endianUtil.decodeFloat(dataElementBytes, 0)));
                                break;

                            case FieldTypes.DOUBLE_64_ARRAY:
                            case FieldTypes.DOUBLE_64_MATRIX:
                                this.buffer.append(String.format(" %f", endianUtil.decodeDouble(dataElementBytes, 0)));
                                break;

                            case FieldTypes.BOOLEAN_8_ARRAY:
                            case FieldTypes.BOOLEAN_8_MATRIX:
                                this.buffer.append(0 == dataElementBytes[0] ? " false" : " true");
                                break;

                            default:
                                throw new RuntimeException(
                                        "Unhandled type of basicPrimitiveArraySerializer: " + basicPrimitiveArraySerializer);
                        }
                    }
                    this.nextDataElementByte = 0;
                    this.currentColumn++;
                    if (this.currentColumn == this.columnCount)
                    {
                        this.currentColumn = 0;
                        this.currentRow++;
                    }
                }
                // System.out.println(
                // "Parsed 1 element; next element is for column " + this.currentColumn + ", row " + this.currentRow);
                result = true;
            }
        }
        if (this.positionInData == this.totalDataSize)
        {
            this.currentSerializer = null;
            this.positionInData = -1;
            this.totalDataSize = -1;
            this.rowCount = 0;
            this.columnCount = 0;
            return true;
        }
        return result;
    }

    /**
     * Allocate a buffer for the next data element (or two).
     * @param dataElementSize int; size of the buffer
     */
    private void prepareForDataElement(final int dataElementSize)
    {
        this.dataElementBytes = new byte[dataElementSize];
        this.nextDataElementByte = 0;
    }

    @Override
    public final boolean ignoreForIdenticalOutputCheck()
    {
        return false;
    }

}
