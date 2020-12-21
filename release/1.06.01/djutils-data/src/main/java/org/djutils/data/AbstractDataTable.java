package org.djutils.data;

import org.djunits.Throw;
import org.djutils.immutablecollections.ImmutableList;

/**
 * Abstract {@code Table} implementation taking care of the columns.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class AbstractDataTable implements DataTable
{

    /** Id. */
    private final String id;

    /** Description. */
    private final String description;

    /** Columns. */
    private final ImmutableList<DataColumn<?>> columns;

    /**
     * Constructor for the data table using an ImmutableCollection for the columns.
     * @param id String; id
     * @param description String; description
     * @param columns ImmutableList&lt;DataColumn&lt;?&gt;&gt;; columns
     * @throws NullPointerException when id, description or columns is null
     * @throws IllegalArgumentException when id is empty or there are zero columns
     */
    public AbstractDataTable(final String id, final String description, final ImmutableList<DataColumn<?>> columns)
    {
        Throw.whenNull(id, "Id may not be null.");
        Throw.whenNull(description, "Description may not be null.");
        Throw.whenNull(columns, "Columns may not be null.");
        Throw.when(id.length() == 0, IllegalArgumentException.class, "id cannot be empty");
        Throw.when(columns.size() == 0, IllegalArgumentException.class, "there should be at least one column");
        this.id = id;
        this.description = description;
        this.columns = columns;
    }

    /** {@inheritDoc} */
    @Override
    public ImmutableList<DataColumn<?>> getColumns()
    {
        return this.columns;
    }

    /** {@inheritDoc} */
    @Override
    public String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return this.description;
    }

}
