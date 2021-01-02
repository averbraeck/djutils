package org.djutils.event;

import java.io.Serializable;

/**
 * The TimedEventInterface defines the getTimeStamp method on top of the methods that a regular event has to implement. <br>
 * <br>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <T> the Comparable type that represents time
 */
public interface TimedEventInterface<T extends Comparable<T> & Serializable> extends EventInterface
{
    /**
     * Returns the timeStamp of this event.
     * @return T; the time stamp
     */
    T getTimeStamp();

}
