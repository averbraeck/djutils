package org.djutils.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.djutils.decoderdumper.Dumper;
import org.djutils.decoderdumper.FixedString;
import org.djutils.decoderdumper.HexAddressDecoder;
import org.djutils.decoderdumper.HexDecoder;

/**
 * Dumper for serialized data.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2019-06-07 01:33:02 +0200 (Mon, 7 Jun 2019) $, @version $Revision: 1401 $, by $Author: pknoppers $, initial
 * version Jun 27, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class SerialDataDumper extends Dumper<SerialDataDumper>
{
    /**
     * Construct a new SerialDataDumper.
     * @param endianUtil EndianUtil; used to decode multi-byte values
     * @param addressOffset int; address of the first byte that will be processed
     */
    public SerialDataDumper(final EndianUtil endianUtil, final int addressOffset)
    {
        super(addressOffset);
        addDecoder(new HexAddressDecoder(16));
        addDecoder(new FixedString(": "));
        addDecoder(new HexDecoder(16, 8));
        addDecoder(new FixedString("  "));
        addDecoder(new SerialDataDecoder(endianUtil));
        addDecoder(new FixedString("\n"));
    }

    /**
     * Construct a new SerialDataDumper.
     * @param endianUtil EndianUtil; used to decode multi-byte values
     */
    public SerialDataDumper(final EndianUtil endianUtil)
    {
        this(endianUtil, 0);
    }

    /**
     * Create a SerialDataDumper object; use it to dump an array of bytes and return the dump as a String.
     * @param endianUtil EndianUtil; used to decode multi-byte values
     * @param addressOffset int; address of the first byte
     * @param bytes byte[]; the bytes to hex-dump
     * @return String; the hexadecimal and character dump of the <code>bytes</code>
     */
    public static String serialDataDumper(final EndianUtil endianUtil, final int addressOffset, final byte[] bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            new SerialDataDumper(endianUtil, addressOffset).setOutputStream(baos).append(bytes).flush();
        }
        catch (IOException exception)
        {
            // Cannot happen because ByteOutputStream.write(byte[]) cannot fail
        }
        return baos.toString();
    }

    /**
     * Create a SerialDataDumper object with addressOffset 0; use it to dump an array of bytes and return the dump as a String.
     * @param endianUtil EndianUtil; used to decode multi-byte values
     * @param bytes byte[]; the bytes to hex-dump
     * @return String; the hexadecimal and character dump of the <code>bytes</code>
     */
    public static String serialDataDumper(final EndianUtil endianUtil, final byte[] bytes)
    {
        return serialDataDumper(endianUtil, 0, bytes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SerialDataDumper [super=" + super.toString() + "]";
    }

}
