package org.djutils.stats.summarizers;

/**
 * TallyStatistic makes the common methods of any type of Tally available (unweighted, weighted, timestamped, or event based).
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface TallyStatistic extends Statistic
{
    /**
     * Returns the maximum value of any given observation, or NaN when no observations were registered.
     * @return the maximum value of any given observation
     */
    double getMax();

    /**
     * Returns the minimum value of any given observation, or NaN when no observations were registered.
     * @return the minimum value of any given observation
     */
    double getMin();

}
