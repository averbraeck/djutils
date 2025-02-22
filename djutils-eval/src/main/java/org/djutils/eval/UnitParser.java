package org.djutils.eval;

import org.djunits.value.vdouble.scalar.base.DoubleScalar;

/**
 * UnitParser.java.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface UnitParser
{
    /**
     * Parse a user-specific unit.
     * @param value the value (already parsed; expressed in the unit)
     * @param unit the unit specification
     * @return the resulting (strongly typed) value, or null if the user-specified parser cannot handle
     *         the provided unit
     */
    DoubleScalar<?, ?> parseUnit(final double value, final String unit);

}
