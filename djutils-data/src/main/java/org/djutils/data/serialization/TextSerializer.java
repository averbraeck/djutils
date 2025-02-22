package org.djutils.data.serialization;

import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djutils.data.Column;
import org.djutils.exceptions.Throw;

/**
 * TextSerializer defines the serialize and deserialize methods.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <T> the value type
 */
public interface TextSerializer<T>
{
    /**
     * Serialize a value to text in such a way that it can be deserialized with the corresponding deserializer. Note that
     * {@code null} values for value <b>are allowed</b>. A {@code null} values stands for an empty column value in a CVS-file, a
     * missing tag in an XML-file, etc.
     * @param value the value to serialize, may be {@code null}
     * @param unit the unit used to convert the data to and store, so all valus in a column may have the same unit. The
     *            value may be {@code null} or blank
     * @return a string representation of the value that can later be deserialized, or {@code null}to denote a missing
     *         value
     */
    String serialize(T value, String unit);

    /**
     * Deserialize a value from text that has been created with the corresponding serializer. Note that {@code null} values for
     * text <b>are allowed</b>. A {@code null} values stands for an empty column value in a CVS-file, a missing tag in an
     * XML-file, etc. In this way, we can explicitly show values that were not specified in the file. Also, the type may be
     * {@code null}; this is, for instance, the case for any {@code TextSerializer} implementing {@code 
     * SpecificTextSerializer}, where no class needs to be provided (although it can).
     * @param type class of the value type, may be {@code null}
     * @param text the string to deserialize, may be {@code null} or blank
     * @param unit unit with the value, may be {@code null} or blank
     * @return an instance of the object created with the corresponding serializer, may be {@code null} when a value was not
     *         specified in the source from which the deserializer was called
     */
    T deserialize(Class<T> type, String text, String unit);

    /**
     * Resolve the correct (de)serializer for the given class, and return an instance of the (de)serializer.
     * @param valueClass the class to resolve the (de)serializer for
     * @return an instance of the correct (de)serializer
     * @throws TextSerializationException when there is no corresponding (de)serializer for the class
     */
    static TextSerializer<?> resolve(final Class<?> valueClass) throws TextSerializationException
    {
        Throw.whenNull(valueClass, "valueClass cannot be null");
        if (valueClass.isPrimitive())
        {
            if (valueClass.equals(int.class))
            {
                return new IntegerSerializer();
            }
            else if (valueClass.equals(double.class))
            {
                return new DoubleSerializer();
            }
            else if (valueClass.equals(float.class))
            {
                return new FloatSerializer();
            }
            else if (valueClass.equals(long.class))
            {
                return new LongSerializer();
            }
            else if (valueClass.equals(short.class))
            {
                return new ShortSerializer();
            }
            else if (valueClass.equals(byte.class))
            {
                return new ByteSerializer();
            }
            else if (valueClass.equals(boolean.class))
            {
                return new BooleanSerializer();
            }
            else if (valueClass.equals(char.class))
            {
                return new CharacterSerializer();
            }
        }

        else if (Number.class.isAssignableFrom(valueClass))
        {
            if (valueClass.equals(Integer.class))
            {
                return new IntegerSerializer();
            }
            else if (valueClass.equals(Double.class))
            {
                return new DoubleSerializer();
            }
            else if (valueClass.equals(Float.class))
            {
                return new FloatSerializer();
            }
            else if (valueClass.equals(Long.class))
            {
                return new LongSerializer();
            }
            else if (valueClass.equals(Short.class))
            {
                return new ShortSerializer();
            }
            else if (valueClass.equals(Byte.class))
            {
                return new ByteSerializer();
            }
            else if (DoubleScalar.class.isAssignableFrom(valueClass)) // DoubleScalar is a Number
            {
                return new DoubleScalarSerializer<>();
            }
            else if (FloatScalar.class.isAssignableFrom(valueClass)) // FloatScalar is a Number
            {
                return new FloatScalarSerializer<>();
            }
        }

        else if (valueClass.equals(Boolean.class))
        {
            return new BooleanSerializer();
        }

        else if (valueClass.equals(Character.class))
        {
            return new CharacterSerializer();
        }

        else if (valueClass.equals(String.class))
        {
            return new StringSerializer();
        }

        throw new TextSerializationException("Cannot resolve the Text(de)serializer for class " + valueClass.getName());
    }

    /**
     * Helper function to deal with casting when calling {@code TextSerializer.serialize()}. When the {@code resolve(class)}
     * method returns an 'unspecified' serializer, this {@code serialize} method allows you to use it. Note that {@code null}
     * values for value <b>are allowed</b>. A {@code null} values stands for an empty column value in a CVS-file, a missing tag
     * in an XML-file, etc.
     * @param <T> value type
     * @param serializer serializer
     * @param value value, may be {@code null}
     * @param unit the unit used to convert the data to and store, so all valus in a column may have the same unit. The
     *            value may be {@code null} or blank
     * @return serialized value, or {@code null}to denote a missing value
     */
    @SuppressWarnings("unchecked")
    static <T> String serialize(final TextSerializer<?> serializer, final Object value, final String unit)
    {
        return ((TextSerializer<T>) serializer).serialize((T) value, unit);
    }

    /**
     * Helper function to deal with casting when calling {@code TextSerializer.deserialize()}. When the {@code resolve(class)}
     * method returns an 'unspecified' serializer, this {@code serialize} method allows you to use it. Note that {@code null}
     * values for text <b>are allowed</b>. A {@code null} values stands for an empty column value in a CVS-file, a missing tag
     * in an XML-file, etc. In this way, we can explicitly show values that were not specified in the file for a certain column.
     * @param <T> value type
     * @param serializer serializer
     * @param text value, may be {@code null}
     * @param column columns
     * @return deserialized value, may be {@code null} when a value was not specified in the source for which the
     *         deserializer was called
     */
    @SuppressWarnings("unchecked")
    static <T> T deserialize(final TextSerializer<?> serializer, final String text, final Column<?> column)
    {
        return ((TextSerializer<T>) serializer).deserialize((Class<T>) column.getValueType(), text, column.getUnit());
    }

}
