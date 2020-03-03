package org.djutils.stats.summarizers;

/**
 * The WeightedTally interface defines the methods that a time-weighted tally should implement.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public interface WeightedTallyInterface extends BasicTallyInterface
{
    /**
     * Retrieve the current weighted sample mean of all observations since the initialization.
     * @return double; the current weighted sample mean
     */
    double getWeightedSampleMean();

    /**
     * Retrieve the current weighted mean of all observations since the initialization.
     * @return double; the current weighted mean
     */
    default double getWeightedMean()
    {
        return getWeightedSampleMean();
    }

    /**
     * Retrieve the current weighted sample standard deviation of the observations.
     * @return double; the current weighted sample standard deviation
     */
    double getWeightedSampleStDev();

    /**
     * Retrieve the current weighted standard deviation of the observations.
     * @return double; the current weighted standard deviation
     */
    double getWeightedStDev();

    /**
     * Retrieve the current weighted sample variance of the observations.
     * @return double; the current weighted sample variance of the observations
     */
    double getWeightedSampleVariance();

    /**
     * Retrieve the current weighted variance of the observations.
     * @return double; the current weighted variance of the observations
     */
    double getWeightedVariance();

    /**
     * Retrieve the current weighted sum of the values of the observations.
     * @return double; the current weighted sum of the values of the observations
     */
    double getWeightedSum();

    /**
     * Process one observed weighted value.
     * @param weight double; the weight of the value to process
     * @param value double; the value to process
     * @return double; the value
     */
    double ingest(double weight, double value);

}
