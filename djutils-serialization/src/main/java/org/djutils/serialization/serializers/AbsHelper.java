package org.djutils.serialization.serializers;

import org.djunits.quantity.Angle;
import org.djunits.quantity.Direction;
import org.djunits.quantity.Duration;
import org.djunits.quantity.Length;
import org.djunits.quantity.Position;
import org.djunits.quantity.Temperature;
import org.djunits.quantity.TemperatureDifference;
import org.djunits.quantity.Time;
import org.djunits.quantity.def.AbsBasic;
import org.djunits.quantity.def.Quantity;
import org.djutils.exceptions.Throw;
import org.djutils.serialization.SerializationException;

/**
 * AbsHelper contains a number of static methods to help instantiate absolute quantity classes.
 * <p>
 * Copyright (c) 2026-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public final class AbsHelper
{
    /** Utility class, do not instantiate. */
    private AbsHelper()
    {
        // utility class
    }

    /**
     * Instantiate an absolute quantity based on a relative quantity and a reference string.
     * @param quantity the relative quantity
     * @param refStr the reference string
     * @return the absolute quantity
     * @throws SerializationException when the reference or absolute class could not be found
     */
    static AbsBasic<?, ?, ?> instantiateAbsQuantity(final Quantity<?> quantity, final String refStr)
            throws SerializationException
    {
        AbsBasic<?, ?, ?> absQuantity;
        if (quantity instanceof Angle angle)
        {
            Direction.Reference ref = Direction.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Direction could no be found", refStr);
            absQuantity = ref.instantiate(angle);
        }
        else if (quantity instanceof Length length)
        {
            Position.Reference ref = Position.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Position could no be found", refStr);
            absQuantity = ref.instantiate(length);
        }
        else if (quantity instanceof TemperatureDifference temperature)
        {
            Temperature.Reference ref = Temperature.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Temperature could no be found", refStr);
            absQuantity = ref.instantiate(temperature);
        }
        else if (quantity instanceof Duration duration)
        {
            Time.Reference ref = Time.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Time could no be found", refStr);
            absQuantity = ref.instantiate(duration);
        }
        else
        {
            throw new SerializationException(
                    "Absolute quantity for quantity " + quantity.getClass().getSimpleName() + " could not be deserialized");
        }
        return absQuantity;
    }
}
