package org.djutils.stats.summarizers;

import java.io.Serializable;

/**
 * The Tally interface defines the methods to be implemented by a tally object, which ingests a series of values and provides
 * mean, standard deviation, etc. of the ingested values. This basic interface definews the methods that all tallies share.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
interface BasicTallyInterface extends Serializable
{
    /**
     * initializes the Tally. This methods sets the max, min, n, sum and variance values to their initial values.
     */
    void initialize();

    /**
     * returns the description of this tally.
     * @return Sting description
     */
    String getDescription();

    /**
     * Returns the number of observations.
     * @return long n
     */
    long getN();

    /**
     * Returns the max.
     * @return double
     */
    double getMax();

    /**
     * Returns the min.
     * @return double
     */
    double getMin();

}
