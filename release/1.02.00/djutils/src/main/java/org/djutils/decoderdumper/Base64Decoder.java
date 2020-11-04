package org.djutils.decoderdumper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Decode base64 encoded data and show it as hex bytes. See https://en.wikipedia.org/wiki/Base64
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 7, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class Base64Decoder implements Decoder
{
    /** Assembling space for decoded data. */
    private int notYetDecodedData = 0;

    /** Number of accumulated bits in <code>notYetDecodedData</code>. */
    private int accumulatedBits = 0;

    /** Dumper used internally to assemble the decoded data into hex values and char values. */
    final private Dumper<Base64Decoder> internalDumper = new Dumper<>();

    /** Collector for the output of the internal dumper. */
    final private ByteArrayOutputStream baos;

    /** Count number of equals (=) symbols seen. */
    private int endOfInputCharsSeen = 0;

    /** Set when an error is detected in the input stream. */
    private boolean errorDetected = false;

    /**
     * Construct a new Base64Decoder.
     * @param decodedBytesPerLine int; maximum number of decoded input characters resulting in one output line
     * @param extraSpaceAfterEvery int; insert an extra space after every N output fields (a multiple of 3 makes sense for the
     *            base64 decoder because base64 encodes three bytes into 4 characters)
     */
    public Base64Decoder(int decodedBytesPerLine, int extraSpaceAfterEvery)
    {
        this.baos = new ByteArrayOutputStream();
        this.internalDumper.setOutputStream(this.baos);
        int maximumBytesPerOutputLine = (decodedBytesPerLine + 3) / 4 * 3;
        this.internalDumper.addDecoder(new HexDecoder(maximumBytesPerOutputLine, extraSpaceAfterEvery));
        this.internalDumper.addDecoder(new FixedString("  "));
        this.internalDumper.addDecoder(new CharDecoder(maximumBytesPerOutputLine, extraSpaceAfterEvery));
    }

    /** {@inheritDoc} */
    @Override
    public String getResult()
    {
        try
        {
            this.internalDumper.flush();
            String result = this.baos.toString();
            this.baos.reset();
            return result;
        }
        catch (IOException ioe)
        {
            // Cannot happen because writing to a ByteArrayOutputStream should never fail
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getMaximumWidth()
    {
        return this.internalDumper.getMaximumWidth();
    }

    /** {@inheritDoc} */
    @Override
    public boolean append(int address, byte theByte) throws IOException
    {
        if (theByte == 61)
        {
            this.endOfInputCharsSeen++; // equals
        }
        if (this.endOfInputCharsSeen > 0)
        {
            return false; // This decoder does not handle multiple base64 encoded objects in its input
        }
        int value;
        if (theByte >= 48 && theByte <= 57)
        {
            value = theByte - 48 + 52; // Digit
        }
        else if (theByte >= 65 && theByte <= 90)
        {
            value = theByte - 65 + 0; // Capital letter
        }
        else if (theByte >= 97 && theByte <= 122)
        {
            value = theByte - 97 + 26;
        }
        else if (theByte == 43 || theByte == 45 || theByte == 46)
        {
            value = 62; // Plus or minus or dot
        }
        else if (theByte == 47 || theByte == 95 || theByte == 44)
        {
            value = 63; // Slash or underscore or comma
        }
        else if (theByte <= 32)
        {
            return false; // White space can appear anywhere and should be ignored (but this test for white space is bad)
        }
        else
        {
            // Illegal byte in input
            if (!this.errorDetected) // First error
            {
                // At this point we might insert some indicator in the output to indicate the location of the first error
            }
            this.errorDetected = true;
            return false;
        }
        this.notYetDecodedData = (this.notYetDecodedData << 6) + value;
        this.accumulatedBits += 6;
        if (this.accumulatedBits >= 8)
        {
            int byteValue = this.notYetDecodedData >> (this.accumulatedBits - 8);
            this.accumulatedBits -= 8;
            this.notYetDecodedData -= byteValue << this.accumulatedBits;
            boolean result = this.internalDumper.append((byte) byteValue);
            return result;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean ignoreForIdenticalOutputCheck()
    {
        return false;
    }

}