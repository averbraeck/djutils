package org.djutils.event;

import java.io.Serializable;

/**
 * This functional interface provides a sourceId to an EventProducer.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/license.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public interface IdProvider extends Serializable
{
    /**
     * Return an id that identifies the EventProducer, e.g., its toString(), or a unique name by which the EventListener can
     * identify the sender of an event.
     * @return Serializable; an id that identifies the EventProducer
     */
    Serializable id();

}
