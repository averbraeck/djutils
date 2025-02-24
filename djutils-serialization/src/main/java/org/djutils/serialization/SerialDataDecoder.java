package org.djutils.serialization;

import java.io.IOException;

import org.djunits.unit.Unit;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djutils.decoderdumper.Decoder;
import org.djutils.serialization.serializers.ArrayOrMatrixWithUnitSerializer;
import org.djutils.serialization.serializers.BasicPrimitiveArrayOrMatrixSerializer;
import org.djutils.serialization.serializers.FixedSizeObjectSerializer;
import org.djutils.serialization.serializers.Pointer;
import org.djutils.serialization.serializers.Serializer;
import org.djutils.serialization.serializers.StringArraySerializer;
import org.djutils.serialization.serializers.StringMatrixSerializer;

/**
 * Decoder for inspection of serialized data. The SerialDataDecoder implements a state machine that processes one byte at a
 * time. Output is sent to the buffer (a StringBuilder).
 * <p>
 * Copyright (c) 2013-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class SerialDataDecoder implements Decoder
{
    /** The endian util to use to decode multi-byte values. */
    private final EndianUtil endianUtil;

    /** Type of the data that is currently being decoded. */
    private byte currentFieldType;

    /** The serializer for the <code>currentFieldType</code>. */
    private Serializer<?> currentSerializer = null;

    /** Position in the dataElementBytes where the next input byte shall be store. */
    private int nextDataElementByte = -1;

    /** Collects the bytes that constitute the current data element. */
    private byte[] dataElementBytes = new byte[0];

    /** Number of rows in an array or matrix. */
    private int rowCount;

    /** Number of columns in a matrix. */
    private int columnCount;

    /** Number of characters in a string. */
    private int charCount;

    /** Row of matrix or array that we are now reading. */
    private int currentRow;

    /** Column of matrix that we are now reading. */
    private int currentColumn;

    /** Character in the string that we are currently reading (either 8 or 16 bits). */
    private int currentChar;

    /** Djunits display unit. */
    private Unit<?> displayUnit;

    /** Array of units for array of column vectors. */
    private Unit<?>[] columnUnits = null;

    /** String builder for current output line. */
    private StringBuilder buffer = new StringBuilder();

    /**
     * Construct a new SerialDataDecoder.
     * @param endianUtil the endian util to use to decode multi-byte values
     */
    public SerialDataDecoder(final EndianUtil endianUtil)
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

    /**
     * Decode one (more) byte. This method must return true when a line becomes full due to this call, otherwise this method
     * must return false.
     * @param address the address that corresponds with the byte, for printing purposes.
     * @param theByte the byte to process
     * @return true if an output line has been completed by this call; false if at least one more byte can be appended to the
     *         local accumulator before the current output line is full
     * @throws IOException when the output device throws this exception
     */
    @Override
    public final boolean append(final int address, final byte theByte) throws IOException
    {
        boolean result = false;

        // check if first byte to indicate the field type
        if (this.currentSerializer == null)
        {
            result = processFieldTypeByte(theByte);
            return result;
        }

        // add byte to data element
        if (this.nextDataElementByte < this.dataElementBytes.length)
        {
            this.dataElementBytes[this.nextDataElementByte] = theByte;
        }
        this.nextDataElementByte++;
        // if data element complete, process it, and prepare for next data element (if any in current field type)
        if (this.nextDataElementByte == this.dataElementBytes.length)
        {
            result = processDataElement();
        }

        // are we done?
        if (this.currentSerializer == null)
        {
            return true;
        }
        return result;
    }

    /**
     * Process one byte that indicates the field type.
     * @param theByte the byte to process
     * @return whether line is full
     */
    private boolean processFieldTypeByte(final byte theByte)
    {
        this.currentFieldType = (byte) (theByte & 0x7F);
        this.currentSerializer = TypedObject.PRIMITIVE_DATA_DECODERS.get(this.currentFieldType);
        if (this.currentSerializer == null)
        {
            this.buffer.append(String.format("Error: Bad field type %02x - resynchronizing", this.currentFieldType));
            return true;
        }
        this.buffer.append(this.currentSerializer.dataClassName() + (this.currentSerializer.getNumberOfDimensions() > 0
                || this.currentSerializer.dataClassName().startsWith("Djunits") ? " " : ": "));

        this.columnCount = 0;
        this.rowCount = 0;
        this.displayUnit = null;
        this.columnUnits = null;

        // check the type and prepare for what is expected; primitive types
        if (this.currentSerializer instanceof FixedSizeObjectSerializer<?>)
        {
            var fsoe = (FixedSizeObjectSerializer<?>) this.currentSerializer;
            int size = fsoe.size(null);
            prepareForDataElement(size);
            return false;
        }

        // array or matrix type: next variable to expect is one or more ints (rows/cols)
        if (this.currentSerializer.getNumberOfDimensions() > 0)
        {
            int size = this.currentSerializer.getNumberOfDimensions() * 4;
            prepareForDataElement(size);
            return false;
        }

        // regular string type, next is an int for length
        if (this.currentFieldType == 9 || this.currentFieldType == 10)
        {
            prepareForDataElement(4);
            return false;
        }

        // djunits scalar type, next is quantity type and unit
        if (this.currentFieldType == 25 || this.currentFieldType == 26)
        {
            prepareForDataElement(2);
            return false;
        }

        this.buffer
                .append(String.format("Error: No field type handler for type %02x - resynchronizing", this.currentFieldType));
        return true;
    }

    /**
     * Process a completed data element. If the current data type has more elements, prepare for the next data element.
     * @return whether the line is full or not
     */
    private boolean processDataElement()
    {
        boolean result = false;

        // primitive types
        if (this.currentSerializer instanceof FixedSizeObjectSerializer<?>)
        {
            result = appendFixedSizeObject();
            done();
            return result;
        }

        // regular string type
        if (this.currentFieldType == 9 || this.currentFieldType == 10)
        {
            appendString();
            if (this.currentChar >= this.charCount)
            {
                done();
            }
            return false;
        }

        // processing of vector array type before array or matrix type
        if (this.currentFieldType == 31 || this.currentFieldType == 32)
        {
            if (this.rowCount == 0)
            {
                processRowsCols();
                prepareForDataElement(2 * this.columnCount); // unit type and display type for every column
                return false;
            }
            if (this.columnUnits == null)
            {
                return fillVectorArrayColumnUnits();
            }
            return appendVectorArrayElement();
        }

        // array or matrix type
        if (this.currentSerializer.getNumberOfDimensions() > 0)
        {
            if (this.rowCount == 0)
            {
                processRowsCols();
                if (this.currentSerializer.hasUnit())
                {
                    prepareForDataElement(2); // unit type and display type
                }
                else if (this.currentSerializer instanceof BasicPrimitiveArrayOrMatrixSerializer<?>)
                {
                    var bpams = (BasicPrimitiveArrayOrMatrixSerializer<?>) this.currentSerializer;
                    prepareForDataElement(bpams.getElementSize());
                }
                return false;
            }
            if (this.currentSerializer.hasUnit())
            {
                if (this.displayUnit == null)
                {
                    result = processUnit();
                    prepareForDataElement(((ArrayOrMatrixWithUnitSerializer<?, ?>) this.currentSerializer).getElementSize());
                    return result;
                }
                result = appendDjunitsElement();
                prepareForDataElement(this.dataElementBytes.length);
                incColumnCount();
                return result;
            }
            if (this.currentSerializer instanceof StringArraySerializer
                    || this.currentSerializer instanceof StringMatrixSerializer)
            {
                processStringElement();
                return false;
            }
            result = appendPrimitiveElement();
            prepareForDataElement(this.dataElementBytes.length);
            incColumnCount();
            return result;
        }

        // djunits scalar type
        if (this.currentFieldType == 25 || this.currentFieldType == 26)
        {
            if (this.displayUnit == null)
            {
                result = processUnit();
                prepareForDataElement(getSize() - 2); // subtract unit bytes
                return result;
            }
            result = appendDjunitsElement();
            done();
            return result;
        }

        // any leftovers?
        System.err.println("Did not process type " + this.currentFieldType);
        return true;
    }

    /**
     * Return the size of the encoding for a fixed size data element.
     * @return the encoding size of an element
     */
    private int getSize()
    {
        try
        {
            return this.currentSerializer.size(null);
        }
        catch (SerializationException e)
        {
            System.err.println("Could not determine size of element for field type " + this.currentFieldType);
            return 1;
        }
    }

    /**
     * Append a fixed size object (i.e., primitive type) to the buffer.
     * @return whether the line is full or not
     */
    private boolean appendFixedSizeObject()
    {
        try
        {
            Object value = this.currentSerializer.deSerialize(this.dataElementBytes, new Pointer(), this.endianUtil);
            this.buffer.append(value.toString());
        }
        catch (SerializationException e)
        {
            this.buffer.append("Error deserializing data");
            return true;
        }
        return false;
    }

    /**
     * Append a character of a string to the buffer.
     */
    private void appendString()
    {
        int elementSize = this.currentSerializer.dataClassName().contains("8") ? 1 : 2;
        if (this.charCount == 0)
        {
            this.charCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
            this.currentChar = 0;
            prepareForDataElement(elementSize);
        }
        else
        {
            if (elementSize == 1)
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
            this.currentChar++;
        }
        this.nextDataElementByte = 0;
    }

    /**
     * Process a string element in a string array or string matrix.
     */
    private void processStringElement()
    {
        appendString();
        if (this.currentChar >= this.charCount)
        {
            incColumnCount();
        }
    }

    /**
     * Process the height and width of a matrix, or length of an array.
     */
    private void processRowsCols()
    {
        this.currentRow = 0;
        this.currentColumn = 0;
        if (this.dataElementBytes.length == 8)
        {
            this.rowCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
            this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 4);
            this.buffer.append(String.format("height %d, width %d: ", this.rowCount, this.columnCount));
        }
        else
        {
            this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
            this.rowCount = 1;
            this.buffer.append(String.format("length %d: ", this.columnCount));
        }
    }

    /**
     * Fill the unit type for the columns of this special type.
     * @return whether line is full or not.
     */
    private boolean fillVectorArrayColumnUnits()
    {
        boolean result = false;
        this.columnUnits = new Unit<?>[this.columnCount];
        for (int i = 0; i < this.columnCount; i += 2)
        {
            byte unitTypeCode = this.dataElementBytes[i];
            byte displayUnitCode = this.dataElementBytes[i + 1];
            this.columnUnits[i] = DisplayType.getUnit(unitTypeCode, displayUnitCode);
            if (this.columnUnits[i] == null && !result)
            {
                this.buffer.append(
                        String.format("Error: Could not find unit type %d, display unit %d", unitTypeCode, displayUnitCode));
                result = true;
            }
        }
        prepareForDataElement(this.currentFieldType == 31 ? 4 : 8);
        return result;
    }

    /**
     * Append one (row) element in an array of (column) vectors.
     * @return whether the line is full or not
     */
    private boolean appendVectorArrayElement()
    {
        boolean result = false;
        try
        {
            if (this.currentFieldType == 31)
            {
                float f = this.endianUtil.decodeFloat(this.dataElementBytes, 0);
                this.buffer.append(String.valueOf(f));
            }
            else
            {
                double d = this.endianUtil.decodeDouble(this.dataElementBytes, 0);
                this.buffer.append(String.valueOf(d));
            }
        }
        catch (Exception e)
        {
            this.buffer.append("Error: Illegal element in vector array -- could not parse");
            result = true;
        }
        prepareForDataElement(this.dataElementBytes.length);
        incColumnCount();
        return result;
    }

    /**
     * Process a unit (2 bytes).
     * @return whether the line is full or not
     */
    private boolean processUnit()
    {
        byte unitTypeCode = this.dataElementBytes[0];
        byte displayUnitCode = this.dataElementBytes[1];
        this.displayUnit = DisplayType.getUnit(unitTypeCode, displayUnitCode);
        if (this.displayUnit == null)
        {
            this.buffer
                    .append(String.format("Error: Could not find unit ype %d, display unit %d", unitTypeCode, displayUnitCode));
            return true;
        }
        return false;
    }

    /**
     * Process one element of a djunits vector or array.
     * @return whether the line is full or not
     * @param <U> the unit type
     * @param <FS> the float scalar type
     * @param <DS> the double scalar type
     */
    @SuppressWarnings("unchecked")
    private <U extends Unit<U>, FS extends FloatScalar<U, FS>, DS extends DoubleScalar<U, DS>> boolean appendDjunitsElement()
    {
        boolean result = false;
        try
        {
            if (this.dataElementBytes.length == 4)
            {
                float f = this.endianUtil.decodeFloat(this.dataElementBytes, 0);
                FloatScalar<U, FS> afs = FloatScalar.instantiateAnonymous(f, this.displayUnit.getStandardUnit());
                afs.setDisplayUnit((U) this.displayUnit);
                this.buffer.append(afs.toDisplayString().replace(" ", "") + " ");
            }
            else
            {
                double d = this.endianUtil.decodeDouble(this.dataElementBytes, 0);
                DoubleScalar<U, DS> ads = DoubleScalar.instantiateAnonymous(d, this.displayUnit.getStandardUnit());
                ads.setDisplayUnit((U) this.displayUnit);
                this.buffer.append(ads.toDisplayString().replace(" ", "") + " ");
            }
        }
        catch (Exception e)
        {
            this.buffer.append("Error: Could not instantiate djunits element");
            result = true;
        }
        return result;
    }

    /**
     * Append a primitive element to the buffer.
     * @return whether the line is full or not.
     */
    private boolean appendPrimitiveElement()
    {
        boolean result = false;
        this.buffer.append(switch (this.currentSerializer.fieldType())
        {
            // @formatter:off
            case FieldTypes.BYTE_8_ARRAY, FieldTypes.BYTE_8_MATRIX -> 
                String.format("%02x ", this.dataElementBytes[0]);
            case FieldTypes.SHORT_16_ARRAY, FieldTypes.SHORT_16_MATRIX -> 
                String.format("%d ", this.endianUtil.decodeShort(this.dataElementBytes, 0));
            case FieldTypes.INT_32_ARRAY, FieldTypes.INT_32_MATRIX -> 
                String.format("%d ", this.endianUtil.decodeInt(this.dataElementBytes, 0));
            case FieldTypes.LONG_64_ARRAY, FieldTypes.LONG_64_MATRIX -> 
                String.format("%d ", this.endianUtil.decodeLong(this.dataElementBytes, 0));
            case FieldTypes.FLOAT_32_ARRAY, FieldTypes.FLOAT_32_MATRIX -> 
                String.format("%f ", this.endianUtil.decodeFloat(this.dataElementBytes, 0));
            case FieldTypes.DOUBLE_64_ARRAY, FieldTypes.DOUBLE_64_MATRIX -> 
                String.format("%f ", this.endianUtil.decodeDouble(this.dataElementBytes, 0));
            case FieldTypes.BOOLEAN_8_ARRAY, FieldTypes.BOOLEAN_8_MATRIX -> 
                this.dataElementBytes[0] == 0 ? "false " : "true ";
            // @formatter:on
            default -> "Error: Unhandled type of basicPrimitiveArraySerializer: " + this.currentSerializer.fieldType();
        });
        return result;
    }

    /**
     * Increase the column count and possibly row count. Reset when complete.
     */
    private void incColumnCount()
    {
        this.currentColumn++;
        if (this.currentColumn >= this.columnCount)
        {
            this.currentColumn = 0;
            this.currentRow++;
            if (this.currentRow >= this.rowCount)
            {
                done();
            }
        }
    }

    /**
     * Reset the state when done with the current variable.
     */
    private void done()
    {
        this.currentSerializer = null;
        this.rowCount = 0;
        this.columnCount = 0;
        this.charCount = 0;
    }

    /**
     * Allocate a buffer for the next data element (or two).
     * @param dataElementSize size of the buffer
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
