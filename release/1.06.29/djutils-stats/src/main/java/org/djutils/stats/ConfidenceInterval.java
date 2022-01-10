package org.djutils.stats;

/**
 * ConfidenceInterval lists the three type of confidence intervals: two one-sided and one two-sided interval.
 * <p>
 * Copyright (c) 2019-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public enum ConfidenceInterval
{
    /** LEFT_SIDE_CONFIDENCE refers to the left side confidence. */
    LEFT_SIDE_CONFIDENCE,

    /** BOTH_SIDE_CONFIDENCE refers to both sides of the confidence. */
    BOTH_SIDE_CONFIDENCE,

    /** RIGTH_SIDE_CONFIDENCE refers to the right side confidence. */
    RIGHT_SIDE_CONFIDENCE;
}
