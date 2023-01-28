package org.djutils.data.serialization;

import org.djunits.unit.Unit;
import org.djunits.value.vfloat.scalar.base.AbstractFloatScalar;

/**
 * FloatScalarSerializer (de)serializes DJUNITS float scalars. This class extends DoubleScalarSerializer. Since the target class
 * to deserialize is given to the deserialize method, the DoubleScalarSerializer.deserialize can also deserialize FloatScalar
 * values.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 */
public class FloatScalarSerializer<U extends Unit<U>, S extends AbstractFloatScalar<U, S>> extends DoubleScalarSerializer<U, S>
{

    /**
     * Serialize a FloatScalar value to text in such a way that it can be deserialized with the corresponding deserializer.
     * @param value Object; the scalar to serialize
     * @return String; a string representation of the value that can later be deserialized
     */
    @Override
    public String serialize(final S value)
    {
        return String.valueOf(value.floatValue());
    }

}
