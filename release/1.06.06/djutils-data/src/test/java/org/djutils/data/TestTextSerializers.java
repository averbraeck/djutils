package org.djutils.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.djunits.unit.DirectionUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDirection;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.serialization.TextSerializer;
import org.junit.Test;

/**
 * TestTextSerializers tests the (de)serialization of the different classes from/into text. <br>
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestTextSerializers
{
    /**
     * Test the serializers of the primitive types.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testPrimitiveSerializers() throws TextSerializationException
    {
        int i = 10;
        TextSerializer<?> serializer = TextSerializer.resolve(int.class);
        assertEquals(i, serializer.deserialize(serializer.serialize(i)));

        double d = 124.5;
        serializer = TextSerializer.resolve(double.class);
        assertEquals(d, serializer.deserialize(serializer.serialize(d)));

        float f = 11.4f;
        serializer = TextSerializer.resolve(float.class);
        assertEquals(f, serializer.deserialize(serializer.serialize(f)));

        long l = 100_456_678L;
        serializer = TextSerializer.resolve(long.class);
        assertEquals(l, serializer.deserialize(serializer.serialize(l)));

        short s = (short) 12.34;
        serializer = TextSerializer.resolve(short.class);
        assertEquals(s, serializer.deserialize(serializer.serialize(s)));

        byte b = (byte) 67;
        serializer = TextSerializer.resolve(byte.class);
        assertEquals(b, serializer.deserialize(serializer.serialize(b)));

        char c = 'a';
        serializer = TextSerializer.resolve(char.class);
        assertEquals(c, serializer.deserialize(serializer.serialize(c)));

        boolean t = true;
        serializer = TextSerializer.resolve(boolean.class);
        assertEquals(t, serializer.deserialize(serializer.serialize(t)));
    }

    /**
     * Test the serializers of the Number types, Boolean and Character.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testNumberSerializers() throws TextSerializationException
    {
        Integer i = 10;
        TextSerializer<?> serializer = TextSerializer.resolve(i.getClass());
        assertEquals(i, serializer.deserialize(serializer.serialize(i)));

        Double d = 124.5;
        serializer = TextSerializer.resolve(d.getClass());
        assertEquals(d, serializer.deserialize(serializer.serialize(d)));

        Float f = 11.4f;
        serializer = TextSerializer.resolve(f.getClass());
        assertEquals(f, serializer.deserialize(serializer.serialize(f)));

        Long l = 100_456_678L;
        serializer = TextSerializer.resolve(l.getClass());
        assertEquals(l, serializer.deserialize(serializer.serialize(l)));

        Short s = (short) 12.34;
        serializer = TextSerializer.resolve(s.getClass());
        assertEquals(s, serializer.deserialize(serializer.serialize(s)));

        Byte b = (byte) 67;
        serializer = TextSerializer.resolve(b.getClass());
        assertEquals(b, serializer.deserialize(serializer.serialize(b)));

        Character c = 'a';
        serializer = TextSerializer.resolve(c.getClass());
        assertEquals(c, serializer.deserialize(serializer.serialize(c)));

        Boolean t = true;
        serializer = TextSerializer.resolve(t.getClass());
        assertEquals(t, serializer.deserialize(serializer.serialize(t)));
    }

    /**
     * Test the serializer of the String type.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testStringSerializer() throws TextSerializationException
    {
        String s = "abc, &%#";
        TextSerializer<?> serializer = TextSerializer.resolve(s.getClass());
        assertEquals(s, serializer.deserialize(serializer.serialize(s)));
    }

    /**
     * Test the errors for the serializers.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testSerializerErrors() throws TextSerializationException
    {
        try
        {
            TextSerializer.resolve(Object.class);
            fail("resolving an unknown serializer should have raised an exception");
        }
        catch (TextSerializationException tse)
        {
            // ok
        }

        try
        {
            TextSerializer.resolve(null);
            fail("null class should have raised an exception");
        }
        catch (NullPointerException npe)
        {
            // ok
        }

        Exception e = new TextSerializationException();
        assertNull(e.getMessage());
        assertNull(e.getCause());

        e = new TextSerializationException("bla");
        assertEquals("bla", e.getMessage());
        assertNull(e.getCause());

        e = new TextSerializationException(new IllegalArgumentException("abc"));
        assertNotNull(e.getMessage()); // something about the cause is added automatically
        assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        assertEquals("abc", e.getCause().getMessage());

        e = new TextSerializationException("bla", new IllegalArgumentException("abc"));
        assertEquals("bla", e.getMessage());
        assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        assertEquals("abc", e.getCause().getMessage());
    }

    /**
     * Test the serializers of the DoubleScalar and FloatScalar types.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testScalarSerializers() throws TextSerializationException
    {
        Length length = new Length(20.0, LengthUnit.KILOMETER);
        TextSerializer<?> serializer = TextSerializer.resolve(length.getClass());
        assertEquals(length, serializer.deserialize(serializer.serialize(length)));

        // repeat to test caching
        length = new Length(123.456, LengthUnit.MILE);
        serializer = TextSerializer.resolve(length.getClass());
        assertEquals(length, serializer.deserialize(serializer.serialize(length)));

        Time time = new Time(10.0, TimeUnit.BASE_DAY);
        serializer = TextSerializer.resolve(time.getClass());
        assertEquals(time, serializer.deserialize(serializer.serialize(time)));

        FloatDuration duration = new FloatDuration(12.5f, DurationUnit.WEEK);
        serializer = TextSerializer.resolve(duration.getClass());
        assertEquals(duration, serializer.deserialize(serializer.serialize(duration)));

        // repeat to test caching
        duration = new FloatDuration(876.32f, DurationUnit.MINUTE);
        serializer = TextSerializer.resolve(duration.getClass());
        assertEquals(duration, serializer.deserialize(serializer.serialize(duration)));

        FloatDirection direction = new FloatDirection(80.5, DirectionUnit.EAST_DEGREE);
        serializer = TextSerializer.resolve(direction.getClass());
        assertEquals(direction, serializer.deserialize(serializer.serialize(direction)));
    }
}
