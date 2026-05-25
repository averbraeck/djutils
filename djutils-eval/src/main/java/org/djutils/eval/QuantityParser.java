package org.djutils.eval;

import org.djunits.quantity.def.Quantity;

/**
 * Parse a quantity based on value and unit.
 * <p>
 * Copyright (c) 2023-2026 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Peter Knoppers
 */
public interface QuantityParser
{
    /**
     * Parse a user-specific quantity.
     * @param value the value (already parsed; expressed in the unit)
     * @param unit the unit specification
     * @return the resulting (strongly typed) quantity, or null if the user-specified parser cannot handle the provided unit
     */
    Quantity<?> parseQuantity(final double value, final String unit);

}
