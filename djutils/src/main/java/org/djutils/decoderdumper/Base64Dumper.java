package org.djutils.decoderdumper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Decode base64 encoded data and dump it in hexadecimal format and (insofar possible) as characters.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 7, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class Base64Dumper extends Dumper<Base64Dumper>
{
    /**
     * Construct a new Base64Dumper.
     * @param addressOffset int; address of the first byte that will be appended
     */
    public Base64Dumper(final int addressOffset)
    {
        super(addressOffset);
        addDecoder(new HexAddressDecoder(16));
        addDecoder(new FixedString(" "));
        addDecoder(new CharDecoder(16, 4));
        addDecoder(new FixedString(": "));
        addDecoder(new Base64Decoder(16, 3));
        addDecoder(new FixedString("\n"));
    }

    /**
     * Construct a new Base64Dumper with initial address offset 0.
     */
    public Base64Dumper()
    {
        this(0);
    }

    /**
     * Create a HexDumper object; use it to dump an array of bytes and return the dump as a String.
     * @param addressOffset int; address of the first byte
     * @param bytes byte[]; the bytes to hex-dump
     * @return String; the hexadecimal and character dump of the base64 decoded <code>bytes</code>
     */
    public static String base64Dumper(final int addressOffset, final byte[] bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            new Base64Dumper(addressOffset).setOutputStream(baos).append(bytes).flush();
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
     * @return String; the hexadecimal and character dump of the base64 decoded <code>bytes</code>
     */
    public static String base64Dumper(final byte[] bytes)
    {
        return base64Dumper(0, bytes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Base64Dumper [super=" + super.toString() + "]";
    }

}
