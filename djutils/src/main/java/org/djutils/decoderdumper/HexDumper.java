package org.djutils.decoderdumper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Dump data in hexadecimal format and (insofar possible) as characters.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class HexDumper extends Dumper<HexDumper>
{

    /**
     * Construct a new HexDumper.
     * @param addressOffset int; address of the first byte that will be appended
     */
    public HexDumper(final int addressOffset)
    {
        super(addressOffset);
        addDecoder(new HexAddressDecoder(16));
        addDecoder(new FixedString(": "));
        addDecoder(new HexDecoder(16, 8));
        addDecoder(new FixedString("  "));
        addDecoder(new CharDecoder(16, 8));
        addDecoder(new FixedString("\n"));
    }

    /**
     * Construct a new HexDumper.
     */
    public HexDumper()
    {
        this(0);
    }

    /**
     * Create a HexDumper object; use it to dump an array of bytes and return the dump as a String.
     * @param addressOffset int; address of the first byte
     * @param bytes byte[]; the bytes to hex-dump
     * @return String; the hexadecimal and character dump of the <code>bytes</code>
     */
    public static String hexDumper(final int addressOffset, final byte[] bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            new HexDumper(addressOffset).setOutputStream(baos).append(bytes).flush();
        }
        catch (IOException exception)
        {
            // Cannot happen because ByteOutputStream.write(byte[]) cannot fail
        }
        return baos.toString();
    }

    /**
     * Create a HexDumper object with addressOffset 0; use it to dump an array of bytes and return the dump as a String.
     * @param bytes byte[]; the bytes to hex-dump
     * @return String; the hexadecimal and character dump of the <code>bytes</code>
     */
    public static String hexDumper(final byte[] bytes)
    {
        return hexDumper(0, bytes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "HexDumper [super=" + super.toString() + "]";
    }

}
