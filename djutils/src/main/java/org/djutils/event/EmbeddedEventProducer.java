package org.djutils.event;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.djutils.event.ref.Reference;
import org.djutils.event.ref.ReferenceType;

/**
 * EmbeddedEventProducer is an EventProducer that embeds the exact event producer (local, RMI, message bus, ...) that is used.
 * Classes can extend this type to embed an EventProducer, or copy this
 * <p>
 * Copyright (c) 2022-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EmbeddedEventProducer implements EventProducer
{
    /** */
    private static final long serialVersionUID = 20221230L;

    /** the embedded event producer. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final EventProducer eventProducer;

    /**
     * constructs a new EventProducingCollection with a local EventProducer.
     * @param sourceId Serializable; the id by which the EventProducer can be identified by the EventListener
     */
    public EmbeddedEventProducer(final Serializable sourceId)
    {
        this.eventProducer = new LocalEventProducer(sourceId);
    }

    /**
     * constructs a new EventProducingCollection with a specific EventProducer.
     * @param eventProducer EventProducer; the EventProducer to send events to the subscribers
     */
    public EmbeddedEventProducer(final EventProducer eventProducer)
    {
        this.eventProducer = eventProducer;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.eventProducer.getSourceId();
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(final EventListener listener, final EventType eventType, final int position,
            final ReferenceType referenceType)
    {
        return this.eventProducer.addListener(listener, eventType, position, referenceType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeListener(final EventListener listener, final EventType eventType)
    {
        return this.eventProducer.removeListener(listener, eventType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners()
    {
        return this.eventProducer.hasListeners();
    }

    /** {@inheritDoc} */
    @Override
    public int numberOfListeners(final EventType eventType)
    {
        return this.eventProducer.numberOfListeners(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListener>> getListenerReferences(final EventType eventType)
    {
        return this.eventProducer.getListenerReferences(eventType);
    }

    /** {@inheritDoc} */
    @Override
    public Set<EventType> getEventTypesWithListeners()
    {
        return this.eventProducer.getEventTypesWithListeners();
    }

    /** {@inheritDoc} */
    @Override
    public void fireEvent(final Event event)
    {
        this.eventProducer.fireEvent(event);
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners()
    {
        return this.eventProducer.removeAllListeners();
    }

    /** {@inheritDoc} */
    @Override
    public int removeAllListeners(final Class<?> ofClass)
    {
        return this.eventProducer.removeAllListeners(ofClass);
    }

}
