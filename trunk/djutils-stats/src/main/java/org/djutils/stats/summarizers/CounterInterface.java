package org.djutils.stats.summarizers;

import java.io.Serializable;

/**
 * The Counter interface defines the methods to implement for a statistics event counter.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public interface CounterInterface extends Serializable
{
    /**
     * Initializes the counter.
     */
    void initialize();

    /**
     * Process one observed value.
     * @param value long; the value to process
     * @return long; the value
     */
    long ingest(long value);

    /**
     * Returns the description of the counter.
     * @return String; the description
     */
    String getDescription();

    /**
     * Returns the current counter value.
     * @return long; the counter value
     */
    long getCount();

    /**
     * Returns the current number of observations.
     * @return long; the number of observations
     */
    long getN();

}
