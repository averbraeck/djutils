package org.djutils.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;

import org.djunits.Throw;
import org.djutils.immutablecollections.Immutable;
import org.djutils.immutablecollections.ImmutableArrayList;
import org.djutils.immutablecollections.ImmutableLinkedHashMap;
import org.djutils.immutablecollections.ImmutableList;
import org.djutils.immutablecollections.ImmutableMap;
import org.djutils.primitives.Primitive;

/**
 * List implementation of {@code Table}.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class ListDataTable extends AbstractDataTable
{

    /** Records. */
    private List<DataRecord> records = Collections.synchronizedList(new ArrayList<>());

    /** Column numbers. */
    private Map<DataColumn<?>, Integer> columnNumbers = new LinkedHashMap<>();

    /** Id numbers. */
    private Map<String, Integer> idNumbers = new LinkedHashMap<>();

    /**
     * Constructor with a regular collection.
     * @param id String; id
     * @param description String; description
     * @param columns Collection&lt;Column&lt;?&gt;&gt;; columns
     */
    public ListDataTable(final String id, final String description, final Collection<DataColumn<?>> columns)
    {
        this(id, description, new ImmutableArrayList<DataColumn<?>>(columns));
    }

    /**
     * Constructor with an immutable list.
     * @param id String; id
     * @param description String; description
     * @param columns ImmutableList&lt;Column&lt;?&gt;&gt;; columns
     */
    public ListDataTable(final String id, final String description, final ImmutableList<DataColumn<?>> columns)
    {
        super(id, description, columns);
        for (int index = 0; index < getColumns().size(); index++)
        {
            DataColumn<?> column = getColumns().get(index);
            this.columnNumbers.put(column, index);
            this.idNumbers.put(column.getId(), index);
        }
        Throw.when(getNumberOfColumns() != this.idNumbers.size(), IllegalArgumentException.class,
                "Duplicate column ids are not allowed.");
    }

    /**
     * {@inheritDoc} <br>
     * <br>
     * It is imperative that the user manually synchronize on the returned list when traversing it via {@link Iterator},
     * {@link Spliterator} or {@link Stream} when there is a risk of adding records while traversing the iterator:
     * 
     * <pre>
     *  List list = Collections.synchronizedList(new ArrayList());
     *      ...
     *  synchronized (list) 
     *  {
     *      Iterator i = list.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * 
     * Failure to follow this advice may result in non-deterministic behavior.<br>
     * <br>
     */
    @Override
    public Iterator<DataRecord> iterator()
    {
        return this.records.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.records.isEmpty();
    }

    /**
     * Adds a record to the table, based on a map with columns and values.
     * @param data Map&lt;String, Object&gt;; data with values given per column
     * @throws IllegalArgumentException when the size or data types in the data map do not comply to the columns
     * @throws NullPointerException when data is null
     */
    public void addRecordByColumns(final Map<DataColumn<?>, Object> data)
    {
        Throw.whenNull(data, "Data may not be null.");
        addRecordByColumns(new ImmutableLinkedHashMap<>(data, Immutable.WRAP));
    }

    /**
     * Adds a record to the table, based on an immutable map with columns and values.
     * @param data Map&lt;String, Object&gt;; data with values given per column
     * @throws IllegalArgumentException when the size or data types in the data map do not comply to the columns
     * @throws NullPointerException when data is null
     */
    public void addRecordByColumns(final ImmutableMap<DataColumn<?>, Object> data)
    {
        Throw.whenNull(data, "Data may not be null.");
        Throw.when(data.size() != getNumberOfColumns(), IllegalArgumentException.class,
                "Number of data columns doesn't match number of table columns.");
        Object[] dataObjects = new Object[getNumberOfColumns()];
        for (int index = 0; index < getColumns().size(); index++)
        {
            DataColumn<?> column = getColumns().get(index);
            Throw.when(!data.containsKey(column), IllegalArgumentException.class, "Missing data for column %s", column.getId());
            Object value = data.get(column);
            Throw.when(!Primitive.isPrimitiveAssignableFrom(column.getValueType(), value.getClass()),
                    IllegalArgumentException.class, "Data value for column %s is not of type %s, but of type %s.",
                    column.getId(), column.getValueType(), value.getClass());
            dataObjects[index] = value;
        }
        this.records.add(new ListRecord(dataObjects));
    }

    /**
     * Adds a record to the table, based on a map with column ids and values.
     * @param data Map&lt;String, Object&gt;; immutable data with values given per column id
     * @throws IllegalArgumentException when the size or data types in the data map do not comply to the columns
     * @throws NullPointerException when data is null
     */
    public void addRecordByColumnIds(final Map<String, Object> data)
    {
        Throw.whenNull(data, "Data may not be null.");
        addRecordByColumnIds(new ImmutableLinkedHashMap<>(data, Immutable.WRAP));
    }

    /**
     * Adds a record to the table, based on an immutable map with column ids and values.
     * @param data Map&lt;String, Object&gt;; data with values given per column id
     * @throws IllegalArgumentException when the size or data types in the data map do not comply to the columns
     * @throws NullPointerException when data is null
     */
    public void addRecordByColumnIds(final ImmutableMap<String, Object> data)
    {
        Throw.whenNull(data, "Data may not be null.");
        Throw.when(data.size() != getNumberOfColumns(), IllegalArgumentException.class,
                "Number of data columns doesn't match number of table columns.");
        Object[] dataObjects = new Object[getNumberOfColumns()];
        for (int index = 0; index < getColumns().size(); index++)
        {
            DataColumn<?> column = getColumns().get(index);
            Throw.when(!data.containsKey(column.getId()), IllegalArgumentException.class, "Missing data for column %s",
                    column.getId());
            Object value = data.get(column.getId());
            Class<?> dataClass = value.getClass();
            Throw.when(!Primitive.isPrimitiveAssignableFrom(column.getValueType(), dataClass), IllegalArgumentException.class,
                    "Data value for column %s is not of type %s, but of type %s.", column.getId(), column.getValueType(),
                    dataClass);
            dataObjects[index] = value;
        }
        this.records.add(new ListRecord(dataObjects));
    }

    /**
     * Adds a record to the table. The order in which the elements in the array are offered should be the same as the order of
     * the columns.
     * @param data Object[]; record data
     * @throws IllegalArgumentException when the size, order or data types in the {@code Object[]} do not comply to the columns
     * @throws NullPointerException when data is null
     */
    public void addRecord(final Object[] data)
    {
        Throw.whenNull(data, "Data may not be null.");
        Throw.when(data.length != getNumberOfColumns(), IllegalArgumentException.class,
                "Number of data columns doesn't match number of table columns.");
        Object[] dataObjects = new Object[getNumberOfColumns()];
        for (int index = 0; index < getColumns().size(); index++)
        {
            DataColumn<?> column = getColumns().get(index);
            Class<?> dataClass = data[index].getClass();
            Throw.when(!Primitive.isPrimitiveAssignableFrom(column.getValueType(), dataClass), IllegalArgumentException.class,
                    "Data value for column %s is not of type %s, but of type %s.", column.getId(), column.getValueType(),
                    dataClass);
            dataObjects[index] = data[index];
        }
        this.records.add(new ListRecord(dataObjects));
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("ListDataTable [getId()=");
        result.append(this.getId());
        result.append(", getDescription()=");
        result.append(this.getDescription());
        result.append("]\nColumns:\n");
        for (DataColumn<?> column : getColumns())
        {
            result.append("  ");
            result.append(column.toString());
            result.append("\n");
        }
        return result.toString();
    }

    /** Record in a {@code ListTable}. */
    public class ListRecord implements DataRecord
    {

        /** Values. */
        private final Object[] values;

        /**
         * Constructor.
         * @param values Object[]; values
         */
        public ListRecord(final Object[] values)
        {
            this.values = values;
        }

        /** {@inheritDoc} */
        @SuppressWarnings({"unchecked", "synthetic-access"})
        @Override
        public <T> T getValue(final DataColumn<T> column)
        {
            return (T) this.values[ListDataTable.this.columnNumbers.get(column)];
        }

        /** {@inheritDoc} */
        @SuppressWarnings("synthetic-access")
        @Override
        public Object getValue(final String id)
        {
            return this.values[ListDataTable.this.idNumbers.get(id)];
        }

        /** {@inheritDoc} */
        @Override
        public Object[] getValues()
        {
            return this.values;
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            StringBuilder result = new StringBuilder();
            result.append("ListDataTable.ListRecord\n");
            for (DataColumn<?> column : ListDataTable.this.getColumns())
            {
                result.append("  ");
                result.append(column.getId());
                result.append(" = ");
                result.append(getValue(column.getId()));
                result.append("\n");
            }
            return result.toString();
        }

    }

}
