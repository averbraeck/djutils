package org.djutils.data;

import org.djutils.base.Identifiable;
import org.djutils.immutablecollections.ImmutableList;

/**
 * Table with data stored in structured records.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface DataTable extends Iterable<DataRecord>, Identifiable
{

    /**
     * Returns the description.
     * @return description
     */
    String getDescription();

    /**
     * Returns the list of columns.
     * @return list of columns
     */
    ImmutableList<DataColumn<?>> getColumns();

    /**
     * Returns the number of columns.
     * @return number of columns
     */
    default int getNumberOfColumns()
    {
        return getColumns().size();
    }

    /**
     * Returns whether the table is empty.
     * @return whether the table is empty
     */
    boolean isEmpty();

    /**
     * Return the column ids as a String[].
     * @return String[]; the column ids
     */
    default String[] getColumnIds()
    {
        String[] headers = new String[getNumberOfColumns()];
        int index = 0;
        for (DataColumn<?> column : getColumns())
        {
            headers[index++] = column.getId();
        }
        return headers;
    }

    /**
     * Return the column descriptions as a String[].
     * @return String[] the column headers
     */
    default String[] getColumnDescriptions()
    {
        String[] descriptions = new String[getNumberOfColumns()];
        int index = 0;
        for (DataColumn<?> column : getColumns())
        {
            descriptions[index++] = column.getDescription();
        }
        return descriptions;
    }

    /**
     * Return the column data types as a Class&lt;?&gt;[].
     * @return Class&lt;?&gt;[] the column data types
     */
    default Class<?>[] getColumnDataTypes()
    {
        Class<?>[] dataTypes = new Class[getNumberOfColumns()];
        int index = 0;
        for (DataColumn<?> column : getColumns())
        {
            dataTypes[index++] = column.getValueType();
        }
        return dataTypes;
    }

    /**
     * Return the column data types as a String[]. Each data type is presented as the full class name or the primitive name. In
     * case of an array, the result is preceded by an "[" for each dimension. After one or more "[" symbols, the class name is
     * preceded by an "L" for a non-primitive class or interface, and by "I" for integer, "Z" for boolean, "B" for byte, "C" for
     * char, "D" for double, "F" for float, "J" for long and "S" for short. So for a column with a double, "double" is returned.
     * For a column with a "Double", "java.lang.Double" is returned, for an int[][], "[[I" is returned, and for a Long[],
     * "[Ljava.lang.Long" is returned.
     * @return String[] the column data types as an array of Strings
     */
    default String[] getColumnDataTypeStrings()
    {
        String[] dataTypes = new String[getNumberOfColumns()];
        int index = 0;
        for (DataColumn<?> column : getColumns())
        {
            dataTypes[index++] = column.getValueType().getName();
        }
        return dataTypes;
    }

}
