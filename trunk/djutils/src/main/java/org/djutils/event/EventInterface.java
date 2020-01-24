package org.djutils.event;

import java.io.Serializable;

/**
 * The EventInterface defines the a strongly typed event (using the EventType). The sender of the event can be identified,
 * allowing for fine-grained filtering of events. Because events are often sent over the network, the interface demands that the
 * event and its source id and content are serializable. It is the repsonsibility of the programmer, though, that the
 * <b>content</b> of the object is serializable as well.<br>
 * <br>
 * In contrast with earlier implementations of the Event package, a <b>sourceId</b> is sent over the network rather than a
 * pointer to the source itself. This has several advantages:
 * <ol>
 * <li>The object extending the EventProducer does not have to be Serializable itself</li>
 * <li>There is no risk that the entire EventProducer object gets serialized (including subclasses) and is sent over the network
 * <li>There is no risk that the receiver of an event gets a pointer to the sending object, while still being able to identify
 * the sending object
 * </ol>
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * Return the id of the source of the event. The source is, or identifies the sender of the event
     * @return Serializable; the id of the source of the event
     */
    Serializable getSourceId();

    /**
     * Return the content (payload) of this event.
     * @return Serializable; the content (payload) of this event
     */
    Serializable getContent();

    /**
     * Return the type of the event.
     * @return EventType; the type of the event
     */
    EventType getType();
    
}
