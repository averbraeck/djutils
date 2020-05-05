package org.djutils.event;

import java.io.Serializable;

import org.djutils.metadata.MetaData;

/**
 * The EventType is the description of a topic used for the subscription to asynchronous events. Event types are used by
 * EventProducers to show which events they potentially fire. EventTypes are typically defined as static final fields. In order
 * to prevent name clashes for the EventType, the full name of the class from which the EventType was defined (usually in the
 * &lt;clinit&gt;) is added to the equals() and hashCode() methods of the EventType. In that way, EventTypes that are the same
 * will be unique, but EventTypes with just the same name but defined in different classes will be different. This is the
 * abstract class that can be tailored to any event type. <br>
 * <br>
 * Note: the reason why this is important is because <b>remote events</b> that use EventTypes can have <i>multiple versions</i>
 * of the same public static final EventType: one the is defined in the client, and one that is defined via the network. These
 * will have <i>different addresses in memory</i> but they share the same class and name info, so equals() will yield true.
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
public interface EventTypeInterface extends Serializable
{
    /**
     * Return the event type name.
     * @return String; the event type name
     */
    String getName();

    /**
     * Retrieve the MetaData that describes the payload of events of this EventType.
     * @return MetaData; describes the payload of events of this EventType
     */
    MetaData getMetaData();

    /**
     * Retrieve the event type that defines valid events of this EventType, e.g., to indicate a TimedEvent is expected.
     * @return Class&lt;EventTypeInterface&gt;;; the class of valid events of this event type
     */
    Class<? extends EventInterface> getValidEventType();
}
