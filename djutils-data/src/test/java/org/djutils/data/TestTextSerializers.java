package org.djutils.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import org.djunits.unit.DirectionUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDirection;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatLength;
import org.djutils.data.serialization.BooleanSerializer;
import org.djutils.data.serialization.ByteSerializer;
import org.djutils.data.serialization.CharacterSerializer;
import org.djutils.data.serialization.DoubleScalarSerializer;
import org.djutils.data.serialization.DoubleSerializer;
import org.djutils.data.serialization.FloatScalarSerializer;
import org.djutils.data.serialization.FloatSerializer;
import org.djutils.data.serialization.IntegerSerializer;
import org.djutils.data.serialization.LongSerializer;
import org.djutils.data.serialization.ShortSerializer;
import org.djutils.data.serialization.StringSerializer;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.serialization.TextSerializer;
import org.djutils.exceptions.Try;
import org.junit.jupiter.api.Test;

/**
 * TestTextSerializers tests the (de)serialization of the different classes from/into text. <br>
 * <br>
 * Copyright (c) 2020-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * Test the serializers of the primitive types, using the generic (de)serializer from the textSerializer interface.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testPrimitiveSerializersGeneric() throws TextSerializationException
    {
        int i = 10;
        TextSerializer<?> serializer = TextSerializer.resolve(int.class);
        Column<?> column = new Column<>("c", "d", int.class, "");
        assertEquals(Integer.valueOf(i),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, i, null), column));

        double d = 124.5;
        serializer = TextSerializer.resolve(double.class);
        column = new Column<>("c", "d", double.class, "");
        assertEquals(Double.valueOf(d),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, d, null), column));

        float f = 11.4f;
        serializer = TextSerializer.resolve(float.class);
        column = new Column<>("c", "d", float.class, "");
        assertEquals(Float.valueOf(f),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, f, null), column));

        long l = 100_456_678L;
        serializer = TextSerializer.resolve(long.class);
        column = new Column<>("c", "d", long.class, "");
        assertEquals(Long.valueOf(l),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, l, null), column));

        short s = (short) 12.34;
        serializer = TextSerializer.resolve(short.class);
        column = new Column<>("c", "d", short.class, "");
        assertEquals(Short.valueOf(s),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, s, null), column));

        byte b = (byte) 67;
        serializer = TextSerializer.resolve(byte.class);
        column = new Column<>("c", "d", byte.class, "");
        assertEquals(Byte.valueOf(b),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, b, null), column));

        char c = 'a';
        serializer = TextSerializer.resolve(char.class);
        column = new Column<>("c", "d", char.class, "");
        assertEquals(Character.valueOf(c),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, c, null), column));

        boolean t = true;
        serializer = TextSerializer.resolve(boolean.class);
        column = new Column<>("c", "d", boolean.class, "");
        assertEquals(Boolean.valueOf(t),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, t, null), column));
    }

    /**
     * Test the serializers of the primitive types, using the generic (de)serializer from the textSerializer interface.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testPrimitiveClassSerializersGeneric() throws TextSerializationException
    {
        int i = 10;
        TextSerializer<?> serializer = TextSerializer.resolve(Integer.class);
        Column<?> column = new Column<>("c", "d", Integer.class, "");
        assertEquals(Integer.valueOf(i),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, i, null), column));

        double d = 124.5;
        serializer = TextSerializer.resolve(Double.class);
        column = new Column<>("c", "d", Double.class, "");
        assertEquals(Double.valueOf(d),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, d, null), column));

        float f = 11.4f;
        serializer = TextSerializer.resolve(Float.class);
        column = new Column<>("c", "d", Float.class, "");
        assertEquals(Float.valueOf(f),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, f, null), column));

        long l = 100_456_678L;
        serializer = TextSerializer.resolve(Long.class);
        column = new Column<>("c", "d", Long.class, "");
        assertEquals(Long.valueOf(l),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, l, null), column));

        short s = (short) 12.34;
        serializer = TextSerializer.resolve(Short.class);
        column = new Column<>("c", "d", Short.class, "");
        assertEquals(Short.valueOf(s),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, s, null), column));

        byte b = (byte) 67;
        serializer = TextSerializer.resolve(Byte.class);
        column = new Column<>("c", "d", Byte.class, "");
        assertEquals(Byte.valueOf(b),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, b, null), column));

        char c = 'a';
        serializer = TextSerializer.resolve(Character.class);
        column = new Column<>("c", "d", Character.class, "");
        assertEquals(Character.valueOf(c),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, c, null), column));

        boolean t = true;
        serializer = TextSerializer.resolve(Boolean.class);
        column = new Column<>("c", "d", Boolean.class, "");
        assertEquals(Boolean.valueOf(t),
                TextSerializer.deserialize(serializer, TextSerializer.serialize(serializer, t, null), column));
    }

    /**
     * Test the serializers of the Number types, Boolean and Character.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testNumberSerializers() throws TextSerializationException
    {
        Integer i = 10;
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertEquals(i, integerSerializer.deserialize(integerSerializer.serialize(i)));

        Double d = 124.5;
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals(d, doubleSerializer.deserialize(doubleSerializer.serialize(d)));

        Float f = 11.4f;
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(f, floatSerializer.deserialize(floatSerializer.serialize(f)));

        Long l = 100_456_678L;
        LongSerializer longSerializer = new LongSerializer();
        assertEquals(l, longSerializer.deserialize(longSerializer.serialize(l)));

        Short s = (short) 12.34;
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(s, shortSerializer.deserialize(shortSerializer.serialize(s)));

        Byte b = (byte) 67;
        ByteSerializer byteSerializer = new ByteSerializer();
        assertEquals(b, byteSerializer.deserialize(byteSerializer.serialize(b)));

        Character c = 'a';
        CharacterSerializer characterSerializer = new CharacterSerializer();
        assertEquals(c, characterSerializer.deserialize(characterSerializer.serialize(c)));

        Boolean t = true;
        BooleanSerializer booleanSerializer = new BooleanSerializer();
        assertEquals(t, booleanSerializer.deserialize(booleanSerializer.serialize(t)));

        TextSerializer<?> lengthSerializer = TextSerializer.resolve(Length.class);
        Column<?> lengthColumn = new Column<>("c", "d", Length.class, "m");
        Length length = Length.of(10.0, "m");
        assertEquals(length, TextSerializer.deserialize(lengthSerializer,
                TextSerializer.serialize(lengthSerializer, length, lengthColumn.getUnit()), lengthColumn));

        TextSerializer<?> floatLengthSerializer = TextSerializer.resolve(FloatLength.class);
        Column<?> floatLengthColumn = new Column<>("c", "d", FloatLength.class, "m");
        FloatLength floatLength = FloatLength.of(10.0f, "m");
        assertEquals(floatLength, TextSerializer.deserialize(floatLengthSerializer,
                TextSerializer.serialize(floatLengthSerializer, floatLength, floatLengthColumn.getUnit()), floatLengthColumn));

        Try.testFail(() -> TextSerializer.deserialize(lengthSerializer, "1.0 xx", lengthColumn),
                "Deserializing an unknown unit for length should have thrown a RuntimeException", RuntimeException.class);

        Try.testFail(() -> TextSerializer.resolve(NumberExtension.class),
                "Getting serializer for unknown Number class should have thrown a TextSerializationException",
                TextSerializationException.class);
    }

    /** test class that extends Number. */
    private class NumberExtension extends Number
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        @Override
        public int intValue()
        {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public long longValue()
        {
            return 0L;
        }

        /** {@inheritDoc} */
        @Override
        public float floatValue()
        {
            return 0.0f;
        }

        /** {@inheritDoc} */
        @Override
        public double doubleValue()
        {
            return 0.0;
        }
    }

    /**
     * Test the serializers for correctly handling null values.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testNullValueSerializers() throws TextSerializationException
    {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertNull(integerSerializer.deserialize(integerSerializer.serialize(null)));
        assertNull(integerSerializer.deserialize(""));
        assertNull(integerSerializer.deserialize(null));

        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertNull(doubleSerializer.deserialize(doubleSerializer.serialize(null)));
        assertNull(doubleSerializer.deserialize(""));
        assertNull(doubleSerializer.deserialize(null));

        FloatSerializer floatSerializer = new FloatSerializer();
        assertNull(floatSerializer.deserialize(floatSerializer.serialize(null)));
        assertNull(floatSerializer.deserialize(""));
        assertNull(floatSerializer.deserialize(null));

        LongSerializer longSerializer = new LongSerializer();
        assertNull(longSerializer.deserialize(longSerializer.serialize(null)));
        assertNull(longSerializer.deserialize(""));
        assertNull(longSerializer.deserialize(null));

        ShortSerializer shortSerializer = new ShortSerializer();
        assertNull(shortSerializer.deserialize(shortSerializer.serialize(null)));
        assertNull(shortSerializer.deserialize(""));
        assertNull(shortSerializer.deserialize(null));

        ByteSerializer byteSerializer = new ByteSerializer();
        assertNull(byteSerializer.deserialize(byteSerializer.serialize(null)));
        assertNull(byteSerializer.deserialize(""));
        assertNull(byteSerializer.deserialize(null));

        CharacterSerializer characterSerializer = new CharacterSerializer();
        assertNull(characterSerializer.deserialize(characterSerializer.serialize(null)));
        assertNull(characterSerializer.deserialize(""));
        assertNull(characterSerializer.deserialize(null));

        BooleanSerializer booleanSerializer = new BooleanSerializer();
        assertNull(booleanSerializer.deserialize(booleanSerializer.serialize(null)));
        assertNull(booleanSerializer.deserialize(""));
        assertNull(booleanSerializer.deserialize(null));

        TextSerializer<?> lengthSerializer = TextSerializer.resolve(Length.class);
        Column<?> lengthColumn = new Column<>("c", "d", Length.class, "m");
        assertNull(TextSerializer.deserialize(lengthSerializer,
                TextSerializer.serialize(lengthSerializer, null, lengthColumn.getUnit()), lengthColumn));
        assertNull(TextSerializer.deserialize(lengthSerializer, "", lengthColumn));
        assertNull(TextSerializer.deserialize(lengthSerializer, null, lengthColumn));

        TextSerializer<?> floatLengthSerializer = TextSerializer.resolve(FloatLength.class);
        Column<?> floatLengthColumn = new Column<>("c", "d", FloatLength.class, "m");
        assertNull(TextSerializer.deserialize(floatLengthSerializer,
                TextSerializer.serialize(floatLengthSerializer, null, floatLengthColumn.getUnit()), floatLengthColumn));
        assertNull(TextSerializer.deserialize(floatLengthSerializer, "", lengthColumn));
        assertNull(TextSerializer.deserialize(floatLengthSerializer, null, lengthColumn));

        StringSerializer stringSerializer = new StringSerializer();
        assertNull(stringSerializer.deserialize(stringSerializer.serialize(null)));
        assertNull(stringSerializer.deserialize(""));
        assertNull(stringSerializer.deserialize(null));
    }

    /**
     * Test the serializer of the String type.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testStringSerializer() throws TextSerializationException
    {
        String s = "abc, &%#";
        StringSerializer serializer = new StringSerializer();
        assertEquals(s, serializer.deserialize(serializer.serialize(s)));
    }

    /**
     * Test the errors for the serializers.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testSerializerErrors() throws TextSerializationException
    {
        Try.testFail(() -> TextSerializer.resolve(Object.class),
                "resolving an unknown serializer should have raised an exception", TextSerializationException.class);
        Try.testFail(() -> TextSerializer.resolve(null), "null class should have raised an exception",
                NullPointerException.class);

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
        Locale.setDefault(Locale.US);

        Length length = new Length(20.0, LengthUnit.KILOMETER);
        DoubleScalarSerializer<LengthUnit, Length> lengthSerializer = new DoubleScalarSerializer<>();
        assertEquals(length, lengthSerializer.deserialize(Length.class, lengthSerializer.serialize(length, "m"), "m"));

        // repeat to test caching
        length = new Length(123.456, LengthUnit.MILE);
        assertEquals(length, lengthSerializer.deserialize(Length.class, lengthSerializer.serialize(length, "m"), "m"));

        Time time = new Time(10.0, TimeUnit.BASE_DAY);
        DoubleScalarSerializer<TimeUnit, Time> timeSerializer = new DoubleScalarSerializer<>();
        assertEquals(time, timeSerializer.deserialize(Time.class, timeSerializer.serialize(time, "s"), "s"));

        FloatDuration duration = new FloatDuration(12.5f, DurationUnit.WEEK);
        FloatScalarSerializer<DurationUnit, FloatDuration> floatDurationSerializer = new FloatScalarSerializer<>();
        assertEquals(duration, floatDurationSerializer.deserialize(FloatDuration.class,
                floatDurationSerializer.serialize(duration, "s"), "s"));

        // repeat to test caching
        duration = new FloatDuration(876.32f, DurationUnit.MINUTE);
        assertEquals(duration, floatDurationSerializer.deserialize(FloatDuration.class,
                floatDurationSerializer.serialize(duration, "s"), "s"));

        FloatDirection direction = new FloatDirection(80.5, DirectionUnit.EAST_DEGREE);
        FloatScalarSerializer<DirectionUnit, FloatDirection> floatDirectionSerializer = new FloatScalarSerializer<>();
        assertEquals(direction, floatDirectionSerializer.deserialize(FloatDirection.class,
                floatDirectionSerializer.serialize(direction, "rad"), "rad"));
    }

    /**
     * Test the serializers of the DoubleScalar and FloatScalar types with a different unit in the Column.
     * @throws TextSerializationException when serializer could not be found
     */
    @Test
    public void testScalarSerializersColumnUnits() throws TextSerializationException
    {
        Locale.setDefault(Locale.US);

        TextSerializer<?> lengthSerializer = TextSerializer.resolve(Length.class);
        Column<Length> lengthColumn = new Column<>("c", "d", Length.class, "km");
        Length length = new Length(30.0, LengthUnit.METER);
        assertEquals(length, TextSerializer.deserialize(lengthSerializer,
                TextSerializer.serialize(lengthSerializer, length, lengthColumn.getUnit()), lengthColumn));
        length = new Length(20.0, LengthUnit.KILOMETER);
        assertEquals(length, TextSerializer.deserialize(lengthSerializer,
                TextSerializer.serialize(lengthSerializer, length, lengthColumn.getUnit()), lengthColumn));
        length = new Length(Math.PI, LengthUnit.MILE);
        assertEquals(length, TextSerializer.deserialize(lengthSerializer,
                TextSerializer.serialize(lengthSerializer, length, lengthColumn.getUnit()), lengthColumn));

        TextSerializer<?> floatLengthSerializer = TextSerializer.resolve(FloatLength.class);
        Column<?> floatLengthColumn = new Column<>("c", "d", FloatLength.class, "mm");
        FloatLength floatLength = new FloatLength(30.0f, LengthUnit.METER);
        assertEquals(floatLength, TextSerializer.deserialize(floatLengthSerializer,
                TextSerializer.serialize(floatLengthSerializer, floatLength, floatLengthColumn.getUnit()), floatLengthColumn));
        floatLength = new FloatLength(20.0f, LengthUnit.KILOMETER);
        assertEquals(floatLength, TextSerializer.deserialize(floatLengthSerializer,
                TextSerializer.serialize(floatLengthSerializer, floatLength, floatLengthColumn.getUnit()), floatLengthColumn));
        floatLength = new FloatLength(Math.PI, LengthUnit.MILE);
        assertEquals(floatLength, TextSerializer.deserialize(floatLengthSerializer,
                TextSerializer.serialize(floatLengthSerializer, floatLength, floatLengthColumn.getUnit()), floatLengthColumn));

    }
}
