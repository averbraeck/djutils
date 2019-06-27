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
        if (null == this.currentSerializer)
        {
            // We are expecting a field type byte
            this.currentFieldType = theByte;
            this.currentSerializer = TypedMessage.PRIMITIVE_DATA_DECODERS.get(this.currentFieldType);
            if (null == this.currentSerializer)
            {
                this.buffer.append(String.format("Bad field type %02x - resynchronizing", this.currentFieldType));
                return true;
                // May eventually re-synchronize, but that could take a lot of data.
            }
            this.positionInData = 0;
            if (this.currentSerializer instanceof ObjectSerializer)
            {
                // TODO handle the display unit si unit and money unit
            }
            else if (this.currentSerializer instanceof FixedSizeObjectSerializer)
            {
                try
                {
                    int size = this.currentSerializer.sizeWithPrefix(null);
                    this.totalDataSize = size;
                }
                catch (SerializationException e)
                {
                    e.printStackTrace(); // Cannot happen
                }
            }
            else if (this.currentSerializer instanceof ArrayOrMatrixSerializer)
            {
                prepareForDataElement(4 * ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer).getNumberOfDimensions());
                this.rowCount = 0;
                this.columnCount = 0;
            }
            else
            {
                throw new RuntimeException("unhandled serializer");
            }
            return false;
        }
        if (this.positionInData < this.dataElementBytes.length)
        {
            this.dataElementBytes[this.positionInData] = theByte;
        }
        this.positionInData++;
        if (this.positionInData == this.dataElementBytes.length)
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
            else if (this.currentSerializer instanceof ArrayOrMatrixSerializer)
            {
                if (this.rowCount == 0)
                {
                    // Got the height and width of a matrix, or length of an array
                    this.rowCount = this.endianUtil.decodeInt(this.dataElementBytes, 0);
                    this.currentRow = 0;
                    this.currentColumn = 0;
                    if (this.dataElementBytes.length == 8)
                    {
                        this.columnCount = this.endianUtil.decodeInt(this.dataElementBytes, 4);
                        this.buffer.append(String.format("matrix height %d, width %d", this.rowCount, this.columnCount));
                    }
                    else
                    {
                        this.columnCount = 1;
                        this.buffer.append(String.format("array length %d", this.rowCount));
                    }
                    this.totalDataSize += ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer).getElementSize() * this.rowCount
                            * this.columnCount;
                    prepareForDataElement(((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer).getElementSize());
                }
                else
                {
                    // Got one data element
                    Object value = ((ArrayOrMatrixSerializer<?, ?>) this.currentSerializer);
                }
                return true; // line break in the output
            }
        }
        if (this.positionInData == this.totalDataSize)
        {
            this.currentSerializer = null;
            this.positionInData = -1;
            this.totalDataSize = -1;
            return true;
        }
        return false;
    }

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
