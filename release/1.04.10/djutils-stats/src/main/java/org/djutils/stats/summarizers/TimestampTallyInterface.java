package org.djutils.stats.summarizers;

import java.util.Calendar;

/**
 * The TimestampedTally interface defines the methods that a timestamped tally should implement in addition to the standard
 * weighted tally. Timestamps can be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public interface TimestampTallyInterface extends BasicTallyInterface
{
    /**
     * Process one observed value.
     * @param timestamp Calendar; the Calendar object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    double ingest(Calendar timestamp, double value);

    /**
     * Process one observed value.
     * @param timestampNumber Number; the object representing the timestamp
     * @param value double; the value to process
     * @return double; the value
     */
    double ingest(Number timestampNumber, double value);

    /**
     * Retrieve the current weighted sampleMean of all observations since the initialization.
     * @return double; the current weighted sampleMean
     */
    double getWeightedSampleMean();

    /**
     * Retrieve the current weighted standard deviation of the observations.
     * @return double; the current weighted standard deviation
     */
    double getWeightedSampleStdDev();

    /**
     * Retrieve the current weighted variance of the observations.
     * @return double; the current weighted variance of the observations
     */
    double getWeightedSampleVariance();

    /**
     * Retrieve the current weighted sum of the values of the observations.
     * @return double; the current weighted sum of the values of the observations
     */
    double getWeightedSum();

    /**
     * @return whether the time-persistent statistic is active (accepting observations) or not.
     */
    boolean isActive();

    /**
     * End the observations. After ending, no more observations will be accepted.
     * @param timestamp Number; the Number object representing the final timestamp
     */
    void endObservations(Number timestamp);

    /**
     * End the observations. After ending, no more observations will be accepted.
     * @param timestamp Calendar; the Calendar object representing the final timestamp
     */
    void endObservations(Calendar timestamp);

}
