package org.djutils.serialization.demo;

import java.util.Arrays;

import org.djunits.formatter.VectorFormat;
import org.djunits.quantity.Area;
import org.djunits.quantity.Length;
import org.djunits.vecmat.dn.VectorN;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.SerializationException;
import org.djutils.serialization.codecs.Codec;
import org.djutils.serialization.codecs.MessageCodec;

/**
 * SerializationDemo shows a few examples of coding and decoding of objects and object arrays.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 * @author Peter Knoppers
 * @author Wouter Schakel
 */
public final class SerializationDemo
{
    /** */
    private SerializationDemo()
    {
        // utility class.
    }

    /**
     * Show coding and decoding of an object array.
     * @throws SerializationException on error
     */
    private static void showMessage() throws SerializationException
    {
        int[] array = new int[] {1, 2, 4, 8, 16, 32};
        VectorN.Col<Area> vector = VectorN.Col.of(new double[] {12.0, 24.0}, Area.Unit.km2);
        String string = "Hello world";
        byte[] buffer = MessageCodec.encode(Endianness.BIG_ENDIAN, array, vector, string);
        Object[] decoded = MessageCodec.decodeToPrimitiveDataTypes(Endianness.BIG_ENDIAN, buffer);
        System.out.println("\nMessageCodec:");
        System.out.println(String.format("%s -> %s, equals = %b", Arrays.toString(array), Arrays.toString((int[]) decoded[0]),
                Arrays.equals(array, (int[]) decoded[0])));
        System.out.println(String.format("%s -> %s, equals = %b", vector.format(VectorFormat.Row.instance()),
                ((VectorN.Col<?>) decoded[1]).format(VectorFormat.Row.instance()), vector.equals(decoded[1])));
        System.out.println(String.format("%s -> %s, equals = %b", string, (String) decoded[2], string.equals(decoded[2])));
    }

    /**
     * Show coding and decoding of a single object.
     * @throws SerializationException on error
     */
    private static void showObject() throws SerializationException
    {
        Length length = Length.of(20.0, "km");
        byte[] buffer = Codec.encode(length, Endianness.BIG_ENDIAN);
        Length decoded = (Length) Codec.decodeToObjectDataType(Endianness.BIG_ENDIAN, buffer);
        System.out.println("\nCodec:");
        System.out
            .println(String.format("%s -> %s, equals = %b", length.toString(), decoded.toString(), length.equals(decoded)));
    }

    /**
     * Show a few examples of coding and decoding.
     * @param args not used
     * @throws SerializationException on error
     */
    public static void main(final String[] args) throws SerializationException
    {
        showObject();
        showMessage();
    }

}
