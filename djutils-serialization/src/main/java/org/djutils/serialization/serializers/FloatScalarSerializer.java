package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.value.vfloat.scalar.base.FloatScalar;
import org.djutils.serialization.Endianness;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS FloatScalar.
 * <p>
 * Copyright (c) 2019-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 */
public class FloatScalarSerializer<U extends Unit<U>, S extends FloatScalar<U, S>>
        extends ObjectWithUnitSerializer<U, S>
{
    /** */
    public FloatScalarSerializer()
    {
        super(FieldTypes.FLOAT_32_UNIT, "Djunits_FloatScalar");
    }

    @Override
    public int size(final S afs) throws SerializationException
    {
        return 2 + 4;
    }

    @Override
    public void serialize(final S afs, final byte[] buffer, final Pointer pointer, final Endianness endianUtil)
            throws SerializationException
    {
        encodeUnit(afs.getDisplayUnit(), buffer, pointer, endianUtil);
        float v = afs.getSI();
        endianUtil.encodeFloat(v, buffer, pointer.getAndIncrement(4));
    }

    @Override
    public S deSerialize(final byte[] buffer, final Pointer pointer, final Endianness endianUtil) throws SerializationException
    {
        U unit = getUnit(buffer, pointer, endianUtil);
        S afs = FloatScalar.instantiateAnonymous(endianUtil.decodeFloat(buffer, pointer.getAndIncrement(4)),
                unit.getStandardUnit());
        afs.setDisplayUnit(unit);
        return afs;
    }

}
