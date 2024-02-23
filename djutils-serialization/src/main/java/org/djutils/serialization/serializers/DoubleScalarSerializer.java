package org.djutils.serialization.serializers;

import org.djunits.unit.Unit;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djutils.serialization.EndianUtil;
import org.djutils.serialization.FieldTypes;
import org.djutils.serialization.SerializationException;

/**
 * (De)serializes a DJUNITS DoubleScalar.
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type
 * @param <S> the scalar type
 */
public class DoubleScalarSerializer<U extends Unit<U>, S extends DoubleScalar<U, S>>
        extends ObjectWithUnitSerializer<U, S>
{
    /** */
    public DoubleScalarSerializer()
    {
        super(FieldTypes.DOUBLE_64_UNIT, "Djunits_DoubleScalar");
    }

    /** {@inheritDoc} */
    @Override
    public int size(final S ads) throws SerializationException
    {
        return 2 + 8;
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final S ads, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        encodeUnit(ads.getDisplayUnit(), buffer, pointer, endianUtil);
        double v = ads.getSI();
        endianUtil.encodeDouble(v, buffer, pointer.getAndIncrement(8));
    }

    /** {@inheritDoc} */
    @Override
    public S deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil) throws SerializationException
    {
        U unit = getUnit(buffer, pointer, endianUtil);
        S afd = DoubleScalar.instantiateAnonymous(endianUtil.decodeDouble(buffer, pointer.getAndIncrement(8)),
                unit.getStandardUnit());
        afd.setDisplayUnit(unit);
        return afd;
    }
}
