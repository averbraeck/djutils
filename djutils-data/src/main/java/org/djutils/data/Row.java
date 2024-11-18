package org.djutils.data;

import java.util.Arrays;

/**
 * Row in a table.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class Row
{

    /** Table. */
    private final Table table;
    
    /** Values. */
    private final Object[] values;

    /**
     * Constructor.
     * @param table table.
     * @param values values.
     */
    public Row(final Table table, final Object[] values)
    {
        this.table = table;
        this.values = values;
    }

    /**
     * Returns the column value in this row. For performance, use {@code getValue(int columnNumber)}.
     * @param column column.
     * @param <T> value type.
     * @return the column value in this row.
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(final Column<T> column)
    {
        return (T) this.values[this.table.getColumnNumber(column)];
    }

    /**
     * Returns the column value in this row. For performance, use {@code getValue(int columnNumber)}.
     * @param id column id.
     * @return the column value in this row.
     */
    public Object getValue(final String id)
    {
        return this.values[this.table.getColumnNumber(id)];
    }
    
    /**
     * Returns the column value in this row.
     * @param columnNumber column number.
     * @return the column value in this row.
     */
    public Object getValue(final int columnNumber)
    {
        return this.values[columnNumber];
    }
    
    /**
     * Returns the column values of this record in the natural order of the columns.
     * @return the column value in this record
     */
    public Object[] getValues()
    {
        return this.values;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(this.values);
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Row other = (Row) obj;
        return Arrays.deepEquals(this.values, other.values);
    }

    @Override
    public String toString()
    {
        return "Row " + Arrays.toString(this.values);
    }

}
