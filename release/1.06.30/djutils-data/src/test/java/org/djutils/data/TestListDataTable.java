package org.djutils.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.djutils.immutablecollections.ImmutableList;
import org.djutils.immutablecollections.ImmutableMap;
import org.djutils.primitives.Primitive;
import org.junit.Test;

/**
 * TestListTable tests the functions of the ListTable, the Column and the Record. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestListDataTable
{
    /** Test the ListTable, the Column and the Record. */
    @Test
    public void testListTable()
    {
        DataColumn<Integer> column1 = new SimpleDataColumn<>("time", "time rounded to second [s]", int.class);
        DataColumn<Double> column2 = new SimpleDataColumn<>("value", "measured value [m]", double.class);
        DataColumn<String> column3 = new SimpleDataColumn<>("remark", "remark about the measurement", String.class);

        assertEquals("time", column1.getId());
        assertEquals("time rounded to second [s]", column1.getDescription());
        assertEquals(int.class, column1.getValueType());

        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListDataTable table = new ListDataTable("tableId", "tableDescription", columns);

        assertEquals("tableId", table.getId());
        assertEquals("tableDescription", table.getDescription());
        assertEquals(3, table.getColumns().size());

        assertArrayEquals(new String[] {"time", "value", "remark"}, table.getColumnIds());
        assertArrayEquals(new String[] {"time rounded to second [s]", "measured value [m]", "remark about the measurement"},
                table.getColumnDescriptions());
        assertArrayEquals(new Class<?>[] {int.class, double.class, String.class}, table.getColumnDataTypes());
        assertArrayEquals(new String[] {"int", "double", "java.lang.String"}, table.getColumnDataTypeStrings());

        // add some data
        assertTrue(table.isEmpty());
        table.addRecord(new Object[] {2, 5.0, "normal"});
        assertFalse(table.isEmpty());
        Map<DataColumn<?>, Object> cdata = new LinkedHashMap<>();
        cdata.put(column3, "second");
        cdata.put(column2, 7.7);
        cdata.put(column1, 4);
        table.addRecordByColumns(cdata);
        Map<String, Object> idata = new TreeMap<>();
        idata.put("time", 6);
        idata.put("value", 9.12);
        idata.put("remark", "third");
        table.addRecordByColumnIds(idata);
        
        DataRecord record = table.iterator().next();
        assertArrayEquals(new Object[] {2, 5.0, "normal"}, record.getValues());

        DataColumn<Double> c1 = new SimpleDataColumn<>("x", "x", double.class);
        DataColumn<double[][]> c2 = new SimpleDataColumn<>("y", "y", double[][].class);
        ListDataTable txy = new ListDataTable("xy[][]", "x, y[][]", ImmutableList.of(c1, c2));
        assertArrayEquals(new String[] {"x", "y"}, txy.getColumnIds());
        assertArrayEquals(new String[] {"x", "y"}, txy.getColumnDescriptions());
        assertArrayEquals(new Class<?>[] {double.class, double[][].class}, txy.getColumnDataTypes());
        assertArrayEquals(new String[] {"double", "[[D"}, txy.getColumnDataTypeStrings());
        
        String tableString = table.toString();
        assertTrue(tableString.startsWith("ListDataTable"));
        assertTrue(tableString.contains("tableId"));
        assertTrue(tableString.contains("tableDescription"));
        assertTrue(tableString.contains("SimpleDataColumn"));
        
        String recordString = table.iterator().next().toString();
        assertTrue(recordString.startsWith("ListDataTable.ListRecord"));
        assertTrue(recordString.contains("time = 2"));
        assertTrue(recordString.contains("value = 5.0"));
    }

    /** Test the ListTable, the Column and the Record. */
    @Test
    public void testSubclassListTable()
    {
        // table with double column
        DataColumn<Double> column = new SimpleDataColumn<>("value", "measured value", double.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(column);
        ListDataTable table = new ListDataTable("tableId", "tableDescription", columns);

        // try double arguments in double column
        table.addRecord(new Object[] {5.0});
        Map<DataColumn<?>, Object> cdata = new LinkedHashMap<>();
        cdata.put(column, 7.7);
        table.addRecordByColumns(cdata);
        Map<String, Object> idata = new TreeMap<>();
        idata.put("value", 9.12);
        table.addRecordByColumnIds(idata);

        // try Double arguments in double column
        ListDataTable table2 = new ListDataTable("tableId", "tableDescription", columns);
        table2.addRecord(new Object[] {Double.valueOf(5.0)});
        cdata = new LinkedHashMap<>();
        cdata.put(column, Double.valueOf(7.7));
        table2.addRecordByColumns(cdata);
        idata = new TreeMap<>();
        idata.put("value", Double.valueOf(9.12));
        table2.addRecordByColumnIds(idata);

        // compare both tables
        tableCompare(table, table2);

        // table with Number column
        DataColumn<Number> nColumn = new SimpleDataColumn<>("value", "measured value", Number.class);
        List<DataColumn<?>> nColumns = new ArrayList<>();
        nColumns.add(nColumn);
        ListDataTable nTable = new ListDataTable("tableId", "tableDescription", nColumns);

        // try double arguments in Number column
        nTable.addRecord(new Object[] {5.0});
        Map<DataColumn<?>, Object> cndata = new LinkedHashMap<>();
        cndata.put(nColumn, 7.7);
        nTable.addRecordByColumns(cndata);
        Map<String, Object> indata = new TreeMap<>();
        indata.put("value", 9.12);
        nTable.addRecordByColumnIds(indata);

        // try Double arguments in Number column
        ListDataTable nTable2 = new ListDataTable("tableId", "tableDescription", nColumns);
        nTable2.addRecord(new Object[] {Double.valueOf(5.0)});
        cndata = new LinkedHashMap<>();
        cndata.put(nColumn, Double.valueOf(7.7));
        nTable2.addRecordByColumns(cndata);
        indata = new TreeMap<>();
        indata.put("value", Double.valueOf(9.12));
        nTable2.addRecordByColumnIds(indata);

        // compare both tables
        tableCompare(nTable, nTable2);
    }

    /**
     * Compare the contents of two tables, where primitive content and wrapped primitive content (e.g., a double and a Double)
     * are considered the same if the stored value is the same.
     * @param table1 the first table
     * @param table2 the second table
     */
    public static void tableCompare(final ListDataTable table1, final ListDataTable table2)
    {
        assertEquals(table1.getColumns().size(), table2.getColumns().size());
        assertEquals(table1.getId(), table2.getId());
        assertEquals(table1.getDescription(), table2.getDescription());
        assertEquals(table1.getNumberOfColumns(), table2.getNumberOfColumns());
        assertEquals(table1.getColumns().size(), table2.getColumns().size());
        for (int i = 0; i < table1.getColumns().size(); i++)
        {
            DataColumn<?> c1 = table1.getColumns().get(i);
            DataColumn<?> c2 = table2.getColumns().get(i);
            assertEquals(c1.getId(), c2.getId());
            assertEquals(c1.getDescription(), c2.getDescription());
            assertTrue(Primitive.isPrimitiveAssignableFrom(c1.getValueType(), c2.getValueType()));
            assertTrue(Primitive.isPrimitiveAssignableFrom(c2.getValueType(), c1.getValueType()));
        }

        Iterator<DataRecord> it2 = table2.iterator();
        for (DataRecord r1 : table1)
        {
            assertTrue(it2.hasNext());
            DataRecord r2 = it2.next();
            for (int i = 0; i < table1.getColumns().size(); i++)
            {
                DataColumn<?> c1 = table1.getColumns().get(i);
                String c2id = table2.getColumns().get(i).getId();
                Object v1 = r1.getValue(c1);
                Object v2 = r2.getValue(c2id);
                assertEquals(v1.toString(), v2.toString());
            }
        }
        assertFalse(it2.hasNext());
    }

    /** Test column and table construction with wrong arguments. */
    @Test
    public void testIllegalColumnTable()
    {
        DataColumn<Integer> column1 = new SimpleDataColumn<>("time", "time, rounded to second [s]", int.class);
        DataColumn<Double> column2 = new SimpleDataColumn<>("value", "measured value [m]", double.class);
        DataColumn<String> column3 = new SimpleDataColumn<>("remark", "remark about the measurement", String.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);

        //
        // test illegal columns
        //

        try
        {
            new SimpleDataColumn<>(null, "measured value [m]", double.class);
            fail("null id should have thrown NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // ok
        }
        try
        {
            new SimpleDataColumn<>("value", null, double.class);
            fail("null description should have thrown NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // ok
        }
        try
        {
            new SimpleDataColumn<>("value", "measured value [m]", null);
            fail("null valueType should have thrown NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // ok
        }
        try
        {
            new SimpleDataColumn<>("", "measured value [m]", double.class);
            fail("empty id should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        //
        // test illegal tables
        //

        List<DataColumn<?>> cx = new ArrayList<>();
        cx.add(column1);
        cx.add(column1); // duplicate
        cx.add(column3);
        try
        {
            new ListDataTable("tableId", "tableDescription", cx);
            fail("duplicate column should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        cx = new ArrayList<>();
        cx.add(column1);
        cx.add(new SimpleDataColumn<>("time", "another timestamp", double.class));
        cx.add(column3);
        try
        {
            new ListDataTable("tableId", "tableDescription", cx);
            fail("duplicate column id should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable(null, "tableDescription", columns);
            fail("null id should have thrown NullPointerException");
        }
        catch (NullPointerException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable("", "tableDescription", columns);
            fail("empty id should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable("tableId", null, columns);
            fail("null id should have thrown NullPointerException");
        }
        catch (NullPointerException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable("tableId", "tableDescription", (ImmutableList<DataColumn<?>>) null);
            fail("null columns should have thrown NullPointerException");
        }
        catch (NullPointerException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable("tableId", "tableDescription", (Collection<DataColumn<?>>) null);
            fail("null columns should have thrown NullPointerException");
        }
        catch (NullPointerException iae)
        {
            // ok
        }

        try
        {
            new ListDataTable("tableId", "tableDescription", new ArrayList<DataColumn<?>>());
            fail("zero columns should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }
    }

    /** Test column and table construction with wrong value arguments. */
    @Test
    public void testIllegalRecordTable()
    {
        DataColumn<Integer> column1 = new SimpleDataColumn<>("time", "time, rounded to second [s]", int.class);
        DataColumn<Double> column2 = new SimpleDataColumn<>("value", "measured value [m]", double.class);
        DataColumn<String> column3 = new SimpleDataColumn<>("remark", "remark about the measurement", String.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListDataTable table = new ListDataTable("tableId", "tableDescription", columns);

        //
        // test null data
        //

        try
        {
            table.addRecord((Object[]) null);
            fail("null data record should have raised exception");
        }
        catch (NullPointerException npe)
        {
            // ok
        }

        try
        {
            table.addRecordByColumnIds((Map<String, Object>) null);
            fail("null data record should have raised exception");
        }
        catch (NullPointerException npe)
        {
            // ok
        }

        try
        {
            table.addRecordByColumns((Map<DataColumn<?>, Object>) null);
            fail("null data record should have raised exception");
        }
        catch (NullPointerException npe)
        {
            // ok
        }

        //
        // test too few columns data
        //

        try
        {
            table.addRecord(new Object[] {});
            fail("empty data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            table.addRecordByColumnIds(new HashMap<String, Object>());
            fail("empty data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            table.addRecordByColumns(new HashMap<DataColumn<?>, Object>());
            fail("empty data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        //
        // test too many columns data
        //

        try
        {
            table.addRecord(new Object[] {1, 2, 3, 4});
            fail("too long data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            table.addRecordByColumnIds(ImmutableMap.of("time", 3, "value", 3.5, "remark", "none", "extra", "xx"));
            fail("too long data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            DataColumn<String> column4 = new SimpleDataColumn<>("c4", "column 4", String.class);
            table.addRecordByColumns(ImmutableMap.of(column1, 3, column2, 3.5, column3, "remark", column4, "xx"));
            fail("too long data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        //
        // test wrong type data
        //

        try
        {
            table.addRecord(new Object[] {1, 2, 3});
            fail("wrong type data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            table.addRecordByColumnIds(ImmutableMap.of("time", 3, "value", 2L, "remark", "none"));
            fail("wrong type data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            table.addRecordByColumns(ImmutableMap.of(column1, 3, column2, 3.5, column3, 2L));
            fail("wrong type data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        //
        // test missing column data, where the number of columns is okay
        //

        try
        {
            table.addRecordByColumnIds(ImmutableMap.of("time", 3, "remark", "none", "wrong", 3));
            fail("wrong type data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

        try
        {
            DataColumn<String> column4 = new SimpleDataColumn<>("c4", "column 4", String.class);
            table.addRecordByColumns(ImmutableMap.of(column1, 3, column2, 4.5, column4, "xx"));
            fail("wrong type data record should have raised exception");
        }
        catch (IllegalArgumentException iae)
        {
            // ok
        }

    }

}
