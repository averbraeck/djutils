package org.djutils.serialization.codecs;

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
import org.djunits.quantity.def.Reference;
import org.djunits.unit.Unit;
import org.djunits.vecmat.dn.AbsVectorN;
import org.djunits.vecmat.dn.VectorN;
import org.djunits.vecmat.dnxm.AbsMatrixNxM;
import org.djunits.vecmat.dnxm.MatrixNxM;
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
     * Resolve the reference from a String and a unit.
     * @param refStr the reference string
     * @param unit the unit for the relative quantity
     * @return the reference
     * @throws SerializationException when the reference or absolute class could not be found
     */
    static Reference<?, ?, ?> instantiateReference(final String refStr, final Unit<?, ?> unit) throws SerializationException
    {
        Quantity<?> quantity = unit.ofSi(0.0);
        if (quantity instanceof Angle)
        {
            Direction.Reference ref = Direction.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Direction could no be found", refStr);
            return ref;
        }
        if (quantity instanceof Length)
        {
            Position.Reference ref = Position.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Position could no be found", refStr);
            return ref;
        }
        if (quantity instanceof TemperatureDifference)
        {
            Temperature.Reference ref = Temperature.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Temperature could no be found", refStr);
            return ref;
        }
        else if (quantity instanceof Duration)
        {
            Time.Reference ref = Time.Reference.get(refStr);
            Throw.when(ref == null, SerializationException.class, "reference %s for Time could no be found", refStr);
            return ref;
        }
        throw new SerializationException(
                "Absolute object for quantity " + quantity.getClass().getSimpleName() + " could not be deserialized");
    }

    /**
     * Instantiate an absolute quantity based on a relative quantity and a reference string.
     * @param quantity the relative quantity
     * @param refStr the reference string
     * @return the absolute quantity
     * @throws SerializationException when the reference or absolute class could not be found
     * @param <Q> the quantity type
     */
    @SuppressWarnings("unchecked")
    static <Q extends Quantity<Q>> AbsBasic<?, Q, ?> instantiateAbsQuantity(final Quantity<?> quantity, final String refStr)
            throws SerializationException
    {
        Reference<?, ?, Q> ref = (Reference<?, ?, Q>) instantiateReference(refStr, quantity.getDisplayUnit());
        return ref.instantiate((Q) quantity);
    }

    /**
     * Instantiate an absolute vector based on a relative vector and a reference string.
     * @param vector the vector with relative quantities
     * @param refStr the reference string
     * @return the vector with absolute quantities
     * @throws SerializationException when the reference or absolute class could not be found
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static AbsVectorN.Col<?, ?> instantiateAbsVector(final VectorN.Col<?> vector, final String refStr)
            throws SerializationException
    {
        Reference<?, ?, ?> ref = instantiateReference(refStr, vector.getDisplayUnit());
        return new AbsVectorN.Col(vector, ref);
    }

    /**
     * Instantiate an absolute matrix based on a relative matrix and a reference string.
     * @param matrix the matrix with relative quantities
     * @param refStr the reference string
     * @return the matrix with absolute quantities
     * @throws SerializationException when the reference or absolute class could not be found
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static AbsMatrixNxM<?, ?> instantiateAbsMatrix(final MatrixNxM<?> matrix, final String refStr) throws SerializationException
    {
        Reference<?, ?, ?> ref = instantiateReference(refStr, matrix.getDisplayUnit());
        return new AbsMatrixNxM(matrix, ref);
    }

}
