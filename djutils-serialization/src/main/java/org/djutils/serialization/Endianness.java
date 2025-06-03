package org.djutils.serialization;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

/**
 * Method to help with Little Endian / Big Endian conversions for the Sim0MQ messages. All Sim0MQ messages are encoded Big
 * Endian over the wire.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class Endianness
{
    /** Does this EndianUtil encode and decode messages in bigEndian? */
    private final boolean bigEndian;

    /** Is this platform bigEndian? */
    private static final boolean PLATFORM_BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

    /**
     * Report whether this platform is bigEndian, or littleEndian.
     * @return true if this platform is bigEndian; false if this platform is littleEndian
     */
    public static boolean isPlatformBigEndian()
    {
        return PLATFORM_BIG_ENDIAN;
    }

    /** Directly usable bigEndian EndianUtil. */
    public static final Endianness BIG_ENDIAN = new Endianness(true);

    /** Directly usable littleEndian EndianUtil. */
    public static final Endianness LITTLE_ENDIAN = new Endianness(false);

    /**
     * Construct an EndianUtil object with user specified endianness.
     * @param bigEndian if true encoding and decoding use big endian style; if false; encoding and decoding use little
     *            endian style
     */
    private Endianness(final boolean bigEndian)
    {
        this.bigEndian = bigEndian;
    }

    /**
     * Construct an EndianUtil object that uses bigEndian encoding.
     * @return EndianUtil that uses bigEndian encoding
     */
    public static Endianness bigEndian()
    {
        return BIG_ENDIAN;
    }

    /**
     * Construct an EndianUtil object that uses littleEndian encoding.
     * @return EndianUtil that uses littleEndian encoding
     */
    public static Endianness littleEndian()
    {
        return LITTLE_ENDIAN;
    }

    /**
     * Report if this EndianUtil is bigEndian.
     * @return true if this EndianUtil is bigEndian; false if this EndianUtil is littleEndian
     */
    public boolean isBigEndian()
    {
        return this.bigEndian;
    }

    /**
     * Decode a short.
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the short value
     */
    public short decodeShort(final byte[] message, final int pointer)
    {
        if (this.bigEndian)
        {
            return (short) (((message[pointer] & 0xff) << 8) | ((message[pointer + 1] & 0xff)));
        }
        else
        {
            return (short) (((message[pointer + 1] & 0xff) << 8) | ((message[pointer] & 0xff)));
        }
    }

    /**
     * Decode a int.
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the integer value
     */
    public int decodeInt(final byte[] message, final int pointer)
    {
        if (this.bigEndian)
        {
            return (((message[pointer] & 0xff) << 24) | ((message[pointer + 1] & 0xff) << 16)
                    | ((message[pointer + 2] & 0xff) << 8) | ((message[pointer + 3] & 0xff)));
        }
        else
        {
            return (((message[pointer + 3] & 0xff) << 24) | ((message[pointer + 2] & 0xff) << 16)
                    | ((message[pointer + 1] & 0xff) << 8) | ((message[pointer] & 0xff)));
        }
    }

    /**
     * Decode a long.
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the long value
     */
    public long decodeLong(final byte[] message, final int pointer)
    {
        if (this.bigEndian)
        {
            return ((((long) message[pointer]) << 56) | (((long) message[pointer + 1] & 0xff) << 48)
                    | (((long) message[pointer + 2] & 0xff) << 40) | (((long) message[pointer + 3] & 0xff) << 32)
                    | (((long) message[pointer + 4] & 0xff) << 24) | (((long) message[pointer + 5] & 0xff) << 16)
                    | (((long) message[pointer + 6] & 0xff) << 8) | (((long) message[pointer + 7] & 0xff)));
        }
        else
        {
            return ((((long) message[pointer + 7]) << 56) | (((long) message[pointer + 6] & 0xff) << 48)
                    | (((long) message[pointer + 5] & 0xff) << 40) | (((long) message[pointer + 4] & 0xff) << 32)
                    | (((long) message[pointer + 3] & 0xff) << 24) | (((long) message[pointer + 2] & 0xff) << 16)
                    | (((long) message[pointer + 1] & 0xff) << 8) | (((long) message[pointer] & 0xff)));
        }
    }

    /**
     * Decode a float.
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the float value
     */
    public float decodeFloat(final byte[] message, final int pointer)
    {
        int bits = decodeInt(message, pointer);
        return Float.intBitsToFloat(bits);
    }

    /**
     * Decode a double.
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the double value
     */
    public double decodeDouble(final byte[] message, final int pointer)
    {
        long bits = decodeLong(message, pointer);
        return Double.longBitsToDouble(bits);
    }

    /**
     * Decode a char (16 bits).
     * @param message the ZeroMQ byte array to decode
     * @param pointer the first byte to consider
     * @return the short value
     */
    public char decodeChar(final byte[] message, final int pointer)
    {
        return (char) decodeShort(message, pointer);
    }

    /**
     * Decode a String including the length int from the message byte array.
     * @param message the message byte array
     * @param pointer the start position in the array
     * @return the Java String at position pointer
     * @throws SerializationException when the bytes cannot be parsed as UTF8
     */
    public String decodeUTF8String(final byte[] message, final int pointer) throws SerializationException
    {
        int len = decodeInt(message, pointer);
        byte[] c = new byte[len];
        for (int i = 0; i < len; i++)
        {
            c[i] = message[pointer + i + 4];
        }
        try
        {
            return new String(c, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new SerializationException(e);
        }
    }

    /**
     * Decode a String including the length int from the message byte array.
     * @param message the message byte array
     * @param pointer the start position in the array
     * @return the Java String at position pointer
     */
    public String decodeUTF16String(final byte[] message, final int pointer)
    {
        int len = decodeInt(message, pointer);
        char[] c = new char[len];
        for (int i = 0; i < len; i++)
        {
            c[i] = decodeChar(message, pointer + 2 * i + 4);
        }
        return String.copyValueOf(c);
    }

    /**
     * Encode a short into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     * @return the new pointer after writing
     */
    public int encodeShort(final short v, final byte[] message, final int pointer)
    {
        int p = pointer;
        if (this.bigEndian)
        {
            message[p++] = (byte) (v >> 8);
            message[p++] = (byte) (v);
        }
        else
        {
            message[p++] = (byte) (v);
            message[p++] = (byte) (v >> 8);
        }
        return p;
    }

    /**
     * Encode a char (16 bits) into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     * @return the new pointer after writing
     */
    public int encodeChar(final char v, final byte[] message, final int pointer)
    {
        return encodeShort((short) v, message, pointer);
    }

    /**
     * Encode a int into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     */
    public void encodeInt(final int v, final byte[] message, final int pointer)
    {
        int p = pointer;
        if (this.bigEndian)
        {
            message[p++] = (byte) ((v >> 24) & 0xFF);
            message[p++] = (byte) ((v >> 16) & 0xFF);
            message[p++] = (byte) ((v >> 8) & 0xFF);
            message[p++] = (byte) (v & 0xFF);
        }
        else
        {
            message[p++] = (byte) (v & 0xFF);
            message[p++] = (byte) ((v >> 8) & 0xFF);
            message[p++] = (byte) ((v >> 16) & 0xFF);
            message[p++] = (byte) ((v >> 24) & 0xFF);
        }
    }

    /**
     * Encode a long into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     * @return the new pointer after writing
     */
    public int encodeLong(final long v, final byte[] message, final int pointer)
    {
        int p = pointer;
        if (this.bigEndian)
        {
            message[p++] = (byte) ((v >> 56) & 0xFF);
            message[p++] = (byte) ((v >> 48) & 0xFF);
            message[p++] = (byte) ((v >> 40) & 0xFF);
            message[p++] = (byte) ((v >> 32) & 0xFF);
            message[p++] = (byte) ((v >> 24) & 0xFF);
            message[p++] = (byte) ((v >> 16) & 0xFF);
            message[p++] = (byte) ((v >> 8) & 0xFF);
            message[p++] = (byte) (v & 0xFF);
        }
        else
        {
            message[p++] = (byte) (v & 0xFF);
            message[p++] = (byte) ((v >> 8) & 0xFF);
            message[p++] = (byte) ((v >> 16) & 0xFF);
            message[p++] = (byte) ((v >> 24) & 0xFF);
            message[p++] = (byte) ((v >> 32) & 0xFF);
            message[p++] = (byte) ((v >> 40) & 0xFF);
            message[p++] = (byte) ((v >> 48) & 0xFF);
            message[p++] = (byte) ((v >> 56) & 0xFF);
        }
        return p;
    }

    /**
     * Encode a float into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     */
    public void encodeFloat(final float v, final byte[] message, final int pointer)
    {
        int vint = Float.floatToIntBits(v);
        encodeInt(vint, message, pointer);
    }

    /**
     * Encode a double into a message buffer.
     * @param v the variable to encode
     * @param message the message buffer to encode the variable into
     * @param pointer the pointer to start writing
     * @return the new pointer after writing
     */
    public int encodeDouble(final double v, final byte[] message, final int pointer)
    {
        long vlong = Double.doubleToLongBits(v);
        return encodeLong(vlong, message, pointer);
    }

    @Override
    public String toString()
    {
        return "EndianUtil [bigEndian=" + this.bigEndian + "]";
    }

}
