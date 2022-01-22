package org.djutils.data;

import java.io.Serializable;

import org.djunits.Throw;

/**
 * SimpleColumn implements the Column interface with a single value. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> the value type
 */
public class SimpleDataColumn<T> implements DataColumn<T>, Serializable
{
    /** */
    private static final long serialVersionUID = 20200229L;

    /** the column id, preferably without spaces or symbols. */
    private final String id;

    /** the column description. */
    private final String description;

    /** the value type. */
    private final Class<T> valueType;

    /**
     * Construct a simple, single valued column.
     * @param id String; the column id, preferably without spaces or symbols
     * @param description String; the column description
     * @param valueType Class&lt;T&gt;; the value type
     */
    public SimpleDataColumn(final String id, final String description, final Class<T> valueType)
    {
        Throw.whenNull(id, "id cannot be null");
        Throw.when(id.length() == 0, IllegalArgumentException.class, "id cannot be empty");
        Throw.whenNull(description, "description cannot be null");
        Throw.whenNull(valueType, "valueType cannot be null");
        this.id = id;
        this.description = description;
        this.valueType = valueType;
    }

    /** {@inheritDoc} */
    @Override
    public final String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public Class<T> getValueType()
    {
        return this.valueType;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SimpleDataColumn [id=" + this.id + ", description=" + this.description + ", valueType=" + this.valueType + "]";
    }

}
