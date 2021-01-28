package org.djutils.stats.summarizers;

import java.util.Calendar;

/**
 * The TimestampedTally interface defines the methods that a timestamped tally should implement in addition to the standard
 * weighted tally. Timestamps can, e.g., be Number based or Calendar based.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface TimestampTallyInterface extends WeightedTallyInterface
{
    /**
     * Return whether the statistic is active (accepting observations) or not.
     * @return boolean; whether the statistic is active (accepting observations) or not
     */
    boolean isActive();

    /**
     * End the observations and closes the last interval of observations. After ending, no more observations will be accepted.
     * Calling this method will create an extra observation, and corresponding events for the EventBased implementations of this
     * interface will be called.
     * @param timestamp Number; the Number object representing the final timestamp
     */
    void endObservations(Number timestamp);

    /**
     * End the observations and closes the last interval of observations. After ending, no more observations will be accepted.
     * Calling this method will create an extra observation, and corresponding events for the EventBased implementations of this
     * interface will be called.
     * @param timestamp Calendar; the Calendar object representing the final timestamp
     */
    void endObservations(Calendar timestamp);

}
