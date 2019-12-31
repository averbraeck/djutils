package org.djutils.event;

import java.io.Serializable;

/**
 * The EventInterface defines the a strongly typed event (using the EventType). The sender of the event can be identified,
 * allowing for fine-grained filtering of events.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. This class was
 * originally part of the DSOL project, see <a href="https://simulation.tudelft.nl/dsol/manual" target="_blank">
 * https://simulation.tudelft.nl/dsol/manual</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */

public interface EventInterface extends Serializable
{

    /**
     * returns the source of the event. The source is the sender of the event
     * @return the source of the event
     */
    Object getSource();

    /**
     * returns the content of this event.
     * @return the content of this event
     */
    Object getContent();

    /**
     * returns the type of the event.
     * @return the eventType of the event
     */
    EventType getType();
}
