package org.djutils.serialization;

import java.io.IOException;

import org.djunits.unit.Unit;
import org.djutils.decoderdumper.Decoder;

/**
 * Decoder for inspection of serialized data.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class SerialDataDecoder implements Decoder
{
    /** The endian util to use to decode multi-byte values. */
    private final EndianUtil endianUtil;

    /** Type of the data that is currently being decoded. */
    private byte currentFieldType;

    /** The serializer for the <code>currentFieldType</code>. */
    private Serializer<?> currentSerializer = null;

    /** Position in the data of the <code>dataElementBytes</code>. */
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

    /** Djunits display unit. */
    private Unit<?> displayUnit;

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
        return 80;
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
                this.buffer.append(this.currentSerializer.dataClassName() + (this.currentSerializer.getNumberOfDimensions() > 0
                        || this.currentSerializer.dataClassName().startsWith("Djunits") ? " " : ": "));
                this.positionInData = 1;
                this.totalDataSize = 1; // to be adjusted
                this.columnCount = 0;
                this.rowCount = 0;
                this.displayUnit = null;
                if (this.currentSerializer.dataClassName().startsWith("String_"))
                {
                    prepareForDataElement(4);
                    this.totalDataSize += 4;
                }
                else if (this.currentSerializer.dataClassName().contentEquals("Djunits_vector_array"))
                {
                    prepareForDataElement(8);
                    this.totalDataSize += 8;
                }
                else if (this.currentSerializer.getNumberOfDimensions() > 0)
                {
                    int size = this.currentSerializer.getNumberOfDimensions() * 4;
                    prepareForDataElement(size);
                    this.totalDataSize += size;
                }
                else if (this.currentSerializer instanceof ObjectSerializer)
                {
                    try
                    {
                        int size;
                        if (this.currentSerializer.dataClassName().startsWith("Djunits"))
                        {
                            // We won't get away calling the size method with null here
                            // Prepare to get the display unit; requires at least two more bytes
                            size = 2;
                            this.displayUnit = null;
                        }
                        else
                        {
                            size = this.currentSerializer.size(null);
                        }
                        prepareForDataElement(size);
                        this.totalDataSize += size;
                    }
                    catch (SerializationException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        int size = this.currentSerializer.size(null);
                        this.totalDataSize += size;
                        prepareForDataElement(size);
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
            if (this.currentSerializer.dataClassName().startsWith("String_"))
            {
                int elementSize = this.currentSerializer.dataClassName().endsWith("8") ? 1 : 2;
                if (this.columnCount == 0) // re-using columnCount to store number of characters
                {
                    this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
                    prepareForDataElement(elementSize);
                    this.totalDataSize += this.columnCount * elementSize;
                }
                else
                {
                    if (1 == elementSize)
                    {
                        if (this.dataElementBytes[0] > 32 && this.dataElementBytes[0] < 127)
                        {
                            this.buffer.append((char) this.dataElementBytes[0]); // safe to print
                        }
                        else
                        {
                            this.buffer.append("."); // not safe to print
                        }
                    }
                    else
                    {
                        char character = this.endianUtil.decodeChar(this.dataElementBytes, 0);
                        if (Character.isAlphabetic(character))
                        {
                            this.buffer.append(character); // safe to print
                        }
                        else
                        {
                            this.buffer.append("."); // not safe to print
                        }
                    }
                }
                this.currentColumn = 0;
                this.nextDataElementByte = 0;
            }
            else if (this.currentSerializer.dataClassName().contentEquals("Djunits_vector_array"))
            {
                if (this.rowCount == 0)
                {
                    this.rowCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
                    this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 4);
                    this.currentRow = -1; // indicates we are parsing the units
                    this.currentColumn = 0;
                    prepareForDataElement(2);
                    this.totalDataSize += 2;
                }
                else if (this.currentRow < 0)
                {
                    // parse one unit
                    TypedMessage.getUnit(this.dataElementBytes, new Pointer(), this.endianUtil);
                    this.displayUnit = TypedMessage.getUnit(this.dataElementBytes, new Pointer(), this.endianUtil);
                    this.buffer.append("unit for column " + this.currentColumn + ": ");
                    this.buffer.append(this.displayUnit);
                    this.currentColumn++;
                    if (this.currentColumn < this.columnCount)
                    {
                        prepareForDataElement(2);
                        this.totalDataSize += 2;
                        this.buffer.append(", ");
                    }
                    else
                    {
                        // Done with the units; prepare to parse the values
                        this.currentRow = 0;
                        this.currentColumn = 0;
                        prepareForDataElement(8);
                        this.totalDataSize += 8 * this.columnCount * this.rowCount;
                    }
                }
                else
                {
                    // process one double value
                    this.buffer.append(String.format("value at row %d column %d: ", this.currentRow, this.currentColumn));
                    this.buffer.append(this.endianUtil.decodeDouble(this.dataElementBytes, 0));
                    this.positionInData = 0;
                    this.currentColumn++;
                    if (this.currentColumn >= this.columnCount)
                    {
                        this.currentColumn = 0;
                        this.currentRow++;
                    }
                    this.buffer.append(" ");
                    this.nextDataElementByte = 0;
                }
            }
            else if (this.currentSerializer.dataClassName().startsWith("Djunits"))
            {
                if (this.currentSerializer.getNumberOfDimensions() > 0 && 0 == this.rowCount)
                {
                    this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
                    this.currentRow = 0;
                    this.currentColumn = 0;
                    if (this.dataElementBytes.length == 8)
                    {
                        this.rowCount = this.columnCount;
                        this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 4);
                        this.buffer.append(String.format("height %d, width %d", this.rowCount, this.columnCount));
                    }
                    else
                    {
                        this.rowCount = 1;
                        this.buffer.append(String.format("length %d", this.columnCount));
                    }
                    // Prepare for the unit.
                    prepareForDataElement(2);
                    this.totalDataSize += 2;
                    this.buffer.append(", ");
                    return false;
                }
                else if (null == this.displayUnit)
                {
                    this.displayUnit = TypedMessage.getUnit(this.dataElementBytes, new Pointer(), this.endianUtil);
                    this.buffer.append("unit " + this.displayUnit);
                    int numberOfDimensions = this.currentSerializer.getNumberOfDimensions();
                    int elementSize = this.currentSerializer.dataClassName().contains("Float") ? 4 : 8;
                    this.totalDataSize += elementSize * (0 == numberOfDimensions ? 1 : this.rowCount * this.columnCount);
                    prepareForDataElement(elementSize);
                    if (0 == numberOfDimensions)
                    {
                        this.buffer.append(": ");
                    }
                    else
                    {
                        result = true;
                    }
                }
                else
                {
                    // get one value
                    int dimensions = this.currentSerializer.getNumberOfDimensions();
                    if (dimensions == 1)
                    {
                        this.buffer.append(String.format("value at index %d: ", this.currentColumn));
                    }
                    else if (dimensions == 2)
                    {
                        this.buffer.append(String.format("value at row %d column %d: ", this.currentRow, this.currentColumn));
                    }
                    // else dimension == 0
                    if (dimensions > 0)
                    {
                        this.currentColumn++;
                        if (this.currentColumn >= this.columnCount)
                        {
                            this.currentColumn = 0;
                            this.currentRow++;
                        }
                    }
                    this.buffer.append(this.dataElementBytes.length == 4 ? this.endianUtil.decodeFloat(this.dataElementBytes, 0)
                            : this.endianUtil.decodeDouble(this.dataElementBytes, 0));
                    this.nextDataElementByte = 0;
                    result = true;
                }
            }
            else if (this.currentSerializer instanceof FixedSizeObjectSerializer)
            {
                try
                {
                    Object value = this.currentSerializer.deSerialize(this.dataElementBytes, new Pointer(), this.endianUtil);
                    this.buffer.append(value.toString());
                }
                catch (SerializationException e)
                {
                    this.buffer.append("Error deserializing data");
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
                        this.buffer.append(String.format("height %d, width %d", this.rowCount, this.columnCount));
                    }
                    else
                    {
                        this.rowCount = 1;
                        this.buffer.append(String.format("length %d", this.columnCount));
                    }
                    int elementSize = -1;
                    if (this.currentSerializer instanceof ArrayOrMatrixSerializer<?, ?>)
                    {
                        elementSize = ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer).getElementSize();
                    }
                    else if (this.currentSerializer instanceof BasicPrimitiveArrayOrMatrixSerializer)
                    {
                        elementSize = ((BasicPrimitiveArrayOrMatrixSerializer<?>) this.currentSerializer).getElementSize();
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
                    if (this.currentSerializer.getNumberOfDimensions() == 1)
                    {
                        this.buffer.append(String.format("value at index %d: ", this.currentColumn));
                    }
                    else // 2 dimensions
                    {
                        this.buffer.append(String.format("value at row %d column %d: ", this.currentRow, this.currentColumn));
                    }
                    if (this.currentSerializer instanceof ArrayOrMatrixSerializer<?, ?>)
                    {
                        Object value = ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer)
                                .deSerializeElement(this.dataElementBytes, 0, this.endianUtil);
                        this.buffer.append(value.toString());
                    }
                    else if (this.currentSerializer instanceof BasicPrimitiveArrayOrMatrixSerializer)
                    {
                        // It looks like we'll have to do this ourselves.
                        BasicPrimitiveArrayOrMatrixSerializer<?> basicPrimitiveArraySerializer =
                                (BasicPrimitiveArrayOrMatrixSerializer<?>) this.currentSerializer;
                        switch (basicPrimitiveArraySerializer.fieldType())
                        {
                            case FieldTypes.BYTE_8_ARRAY:
                            case FieldTypes.BYTE_8_MATRIX:
                                this.buffer.append(String.format("%02x", this.dataElementBytes[0]));
                                break;

                            case FieldTypes.SHORT_16_ARRAY:
                            case FieldTypes.SHORT_16_MATRIX:
                                this.buffer.append(String.format("%d", this.endianUtil.decodeShort(this.dataElementBytes, 0)));
                                break;

                            case FieldTypes.INT_32_ARRAY:
                            case FieldTypes.INT_32_MATRIX:
                                this.buffer.append(String.format("%d", this.endianUtil.decodeInt(this.dataElementBytes, 0)));
                                break;

                            case FieldTypes.LONG_64_ARRAY:
                            case FieldTypes.LONG_64_MATRIX:
                                this.buffer.append(String.format("%d", this.endianUtil.decodeLong(this.dataElementBytes, 0)));
                                break;

                            case FieldTypes.FLOAT_32_ARRAY:
                            case FieldTypes.FLOAT_32_MATRIX:
                                this.buffer.append(String.format("%f", this.endianUtil.decodeFloat(this.dataElementBytes, 0)));
                                break;

                            case FieldTypes.DOUBLE_64_ARRAY:
                            case FieldTypes.DOUBLE_64_MATRIX:
                                this.buffer.append(String.format("%f", this.endianUtil.decodeDouble(this.dataElementBytes, 0)));
                                break;

                            case FieldTypes.BOOLEAN_8_ARRAY:
                            case FieldTypes.BOOLEAN_8_MATRIX:
                                this.buffer.append(0 == this.dataElementBytes[0] ? "false" : "true");
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
