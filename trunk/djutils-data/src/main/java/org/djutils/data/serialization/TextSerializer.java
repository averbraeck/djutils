package org.djutils.data.serialization;

import org.djunits.value.vdouble.scalar.base.DoubleScalarInterface;
import org.djunits.value.vfloat.scalar.base.FloatScalarInterface;
import org.djutils.exceptions.Throw;

/**
 * TextSerializer defines the serialize and deserialize methods. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> the value type
 */
public interface TextSerializer<T>
{
    /**
     * Serialize a value to text in such a way that it can be deserialized with the corresponding deserializer.
     * @param value T; the value to serialize
     * @return String; a string representation of the value that can later be deserialized
     */
    String serialize(Object value);

    /**
     * Deserialize a value from text that has been created with the corresponding serializer.
     * @param text String; the string to deserialize
     * @return T; an instance of the object created with the corresponding serializer
     */
    Object deserialize(String text);

    /**
     * Resolve the correct (de)serializer for the given class, and return an instance of the (de)serializer.
     * @param valueClass Class&lt;?&gt;; the class to resolve the (de)serializer for
     * @return an instance of the correct (de)serializer
     * @throws TextSerializationException when there is no corresponding (de)serializer for the class
     */
    @SuppressWarnings("rawtypes")
    static TextSerializer<?> resolve(final Class<?> valueClass) throws TextSerializationException
    {
        Throw.whenNull(valueClass, "valueClass cannot be null");
        if (valueClass.isPrimitive())
        {
            if (valueClass.equals(int.class))
            {
                return new PrimitiveSerializer.Int();
            }
            else if (valueClass.equals(double.class))
            {
                return new PrimitiveSerializer.Double();
            }
            else if (valueClass.equals(float.class))
            {
                return new PrimitiveSerializer.Float();
            }
            else if (valueClass.equals(long.class))
            {
                return new PrimitiveSerializer.Long();
            }
            else if (valueClass.equals(short.class))
            {
                return new PrimitiveSerializer.Short();
            }
            else if (valueClass.equals(byte.class))
            {
                return new PrimitiveSerializer.Byte();
            }
            else if (valueClass.equals(boolean.class))
            {
                return new PrimitiveSerializer.Boolean();
            }
            else if (valueClass.equals(char.class))
            {
                return new PrimitiveSerializer.Char();
            }
        }

        else if (valueClass.isAssignableFrom(Number.class))
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
            else if (valueClass.equals(Boolean.class))
            {
                return new BooleanSerializer();
            }
            else if (valueClass.equals(Character.class))
            {
                return new CharacterSerializer();
            }
        }

        else if (valueClass.equals(String.class))
        {
            return new StringSerializer();
        }

        else if (valueClass.isAssignableFrom(DoubleScalarInterface.class))
        {
            return new DoubleScalarSerializer();
        }

        else if (valueClass.isAssignableFrom(FloatScalarInterface.class))
        {
            return new FloatScalarSerializer();
        }

        throw new TextSerializationException("Cannot resolve the Text(se)serializer for class " + valueClass.getName());
    }
}
