package org.djutils.serialization.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.djutils.decoderdumper.Dumper;
import org.djutils.decoderdumper.FixedString;
import org.djutils.decoderdumper.HexAddressDecoder;
import org.djutils.decoderdumper.HexDecoder;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.SerialDataDecoder;

/**
 * Dumper for serialized data.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * version Jun 27, 2019 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class SerialDataDumper extends Dumper<SerialDataDumper>
{
    /**
     * Construct a new SerialDataDumper.
     * @param endianUtil used to decode multi-byte values
     * @param addressOffset address of the first byte that will be processed
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
     * @param endianUtil used to decode multi-byte values
     */
    public SerialDataDumper(final EndianUtil endianUtil)
    {
        this(endianUtil, 0);
    }

    /**
     * Create a SerialDataDumper object; use it to dump an array of bytes and return the dump as a String.
     * @param endianUtil used to decode multi-byte values
     * @param addressOffset address of the first byte
     * @param bytes the bytes to hex-dump
     * @return the hexadecimal and character dump of the <code>bytes</code>
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
     * @param endianUtil used to decode multi-byte values
     * @param bytes the bytes to hex-dump
     * @return the hexadecimal and character dump of the <code>bytes</code>
     */
    public static String serialDataDumper(final EndianUtil endianUtil, final byte[] bytes)
    {
        return serialDataDumper(endianUtil, 0, bytes);
    }

    @Override
    public String toString()
    {
        return "SerialDataDumper [super=" + super.toString() + "]";
    }

}
