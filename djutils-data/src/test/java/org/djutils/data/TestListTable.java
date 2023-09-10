package org.djutils.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.djunits.value.vdouble.scalar.Length;
import org.djutils.exceptions.Try;
import org.djutils.primitives.Primitive;
import org.junit.jupiter.api.Test;

/**
 * TestListTable tests the functions of the ListTable, the Column and the Record.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TestListTable
{
    /** Test the ListTable, the Column and the Record. */
    @Test
    public void testListTable()
    {
        Column<Integer> column1 = new Column<>("time", "time rounded to second [s]", int.class, "");
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class, "");
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class, "");

        assertEquals("time", column1.getId());
        assertEquals("time rounded to second [s]", column1.getDescription());
        assertEquals(Integer.class, column1.getValueType());

        List<Column<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListTable table = new ListTable("tableId", "tableDescription", columns);

        assertEquals("tableId", table.getId());
        assertEquals("tableDescription", table.getDescription());
        assertEquals(3, table.getColumns().size());

        assertArrayEquals(new String[] {"time", "value", "remark"}, table.getColumnIds());
        assertArrayEquals(new String[] {"time rounded to second [s]", "measured value [m]", "remark about the measurement"},
                table.getColumnDescriptions());
        assertArrayEquals(new Class<?>[] {Integer.class, Double.class, String.class}, table.getColumnDataTypes());
        assertArrayEquals(new String[] {"java.lang.Integer", "java.lang.Double", "java.lang.String"},
                table.getColumnDataTypeStrings());

        // add some data
        assertTrue(table.isEmpty());
        table.addRow(new Object[] {2, 5.0, "normal"});
        assertFalse(table.isEmpty());
        Map<String, Object> idata = new TreeMap<>();
        idata.put("time", 6);
        idata.put("value", 9.12);
        idata.put("remark", "third");
        table.addRowByColumnIds(idata);

        Row record = table.iterator().next();
        assertArrayEquals(new Object[] {2, 5.0, "normal"}, record.getValues());

        Column<Double> c1 = new Column<>("x", "x", double.class, "");
        Column<double[][]> c2 = new Column<>("y", "y", double[][].class, "");
        ListTable txy = new ListTable("xy[][]", "x, y[][]", List.of(c1, c2));
        assertArrayEquals(new String[] {"x", "y"}, txy.getColumnIds());
        assertArrayEquals(new String[] {"x", "y"}, txy.getColumnDescriptions());
        assertArrayEquals(new Class<?>[] {Double.class, double[][].class}, txy.getColumnDataTypes());
        assertArrayEquals(new String[] {"java.lang.Double", "[[D"}, txy.getColumnDataTypeStrings());

        String tableString = table.toString();
        assertTrue(tableString.startsWith("Table"));
        assertTrue(tableString.contains("id"));
        assertTrue(tableString.contains("description"));
        assertTrue(tableString.contains("column"));

        String recordString = table.iterator().next().toString();
        assertTrue(recordString.startsWith("Row"));
        assertTrue(recordString.contains("2,"));
        assertTrue(recordString.contains("5.0,"));
    }

    /** Test the ListTable, the Column and the Record. */
    @Test
    public void testSubclassListTable()
    {
        // table with double column
        Column<Double> column = new Column<>("value", "measured value", double.class, "");
        List<Column<?>> columns = new ArrayList<>();
        columns.add(column);
        ListTable table = new ListTable("tableId", "tableDescription", columns);

        // try double arguments in double column
        table.addRow(new Object[] {5.0});
        Map<String, Object> idata = new TreeMap<>();
        idata.put("value", 9.12);
        table.addRowByColumnIds(idata);

        // try Double arguments in double column
        ListTable table2 = new ListTable("tableId", "tableDescription", columns);
        table2.addRow(new Object[] {Double.valueOf(5.0)});
        idata = new TreeMap<>();
        idata.put("value", Double.valueOf(9.12));
        table2.addRowByColumnIds(idata);

        // compare both tables
        tableCompare(table, table2);

        // table with Number column
        Column<Number> nColumn = new Column<>("value", "measured value", Number.class, "");
        List<Column<?>> nColumns = new ArrayList<>();
        nColumns.add(nColumn);
        ListTable nTable = new ListTable("tableId", "tableDescription", nColumns);

        // try double arguments in Number column
        nTable.addRow(new Object[] {5.0});
        Map<String, Object> indata = new TreeMap<>();
        indata.put("value", 9.12);
        nTable.addRowByColumnIds(indata);

        // try Double arguments in Number column
        ListTable nTable2 = new ListTable("tableId", "tableDescription", nColumns);
        nTable2.addRow(new Object[] {Double.valueOf(5.0)});
        indata = new TreeMap<>();
        indata.put("value", Double.valueOf(9.12));
        nTable2.addRowByColumnIds(indata);

        // compare both tables
        tableCompare(nTable, nTable2);
    }

    /**
     * Compare the contents of two tables, where primitive content and wrapped primitive content (e.g., a double and a Double)
     * are considered the same if the stored value is the same.
     * @param table1 the first table
     * @param table2 the second table
     */
    public static void tableCompare(final ListTable table1, final ListTable table2)
    {
        assertEquals(table1.getColumns().size(), table2.getColumns().size());
        assertEquals(table1.getId(), table2.getId());
        assertEquals(table1.getDescription(), table2.getDescription());
        assertEquals(table1.getNumberOfColumns(), table2.getNumberOfColumns());
        assertEquals(table1.getColumns().size(), table2.getColumns().size());
        for (int i = 0; i < table1.getColumns().size(); i++)
        {
            Column<?> c1 = table1.getColumns().get(i);
            Column<?> c2 = table2.getColumns().get(i);
            assertEquals(c1.getId(), c2.getId());
            assertEquals(c1.getDescription(), c2.getDescription());
            assertTrue(Primitive.isPrimitiveAssignableFrom(c1.getValueType(), c2.getValueType()));
            assertTrue(Primitive.isPrimitiveAssignableFrom(c2.getValueType(), c1.getValueType()));
        }

        Iterator<Row> it2 = table2.iterator();
        for (Row r1 : table1)
        {
            assertTrue(it2.hasNext());
            Row r2 = it2.next();
            for (int i = 0; i < table1.getColumns().size(); i++)
            {
                Column<?> c1 = table1.getColumns().get(i);
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
        Locale.setDefault(Locale.US);

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class);
        Column<Double> column2 = new Column<>("value", "measured value [m]", Double.class);
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class);
        List<Column<?>> columns = List.of(column1, column2, column3);

        // test illegal columns
        Try.testFail(() -> new Column<>(null, "measured value [m]", double.class, ""),
                "null id should have thrown NullPointerException", NullPointerException.class);

        Try.testFail(() -> new Column<>("value", null, double.class, ""),
                "null description should have thrown NullPointerException", NullPointerException.class);
        Try.testFail(() -> new Column<>("value", "measured value [m]", null, ""),
                "null valueType should have thrown NullPointerException", NullPointerException.class);
        Try.testFail(() -> new Column<>("", "measured value [m]", double.class, ""),
                "empty id should have thrown IllegalArgumentException", IllegalArgumentException.class);
        Try.testFail(() -> new Column<>("length", "length [m]", Length.class, null),
                "null unit for djunits value should have thrown IllegalArgumentException", IllegalArgumentException.class);
        Try.testFail(() -> new Column<>("length", "length [m]", Length.class, ""),
                "empty unit for djunits value should have thrown IllegalArgumentException", IllegalArgumentException.class);
        Try.testFail(() -> new Column<>("length", "length [m]", Length.class, "xx"),
                "wrong unit for djunits value should have thrown IllegalArgumentException", IllegalArgumentException.class);
        Try.testFail(() -> new Column<>("length", "length [m]", Length.class, "s"),
                "wrong unit for djunits value should have thrown IllegalArgumentException", IllegalArgumentException.class);

        // test illegal tables
        List<Column<?>> cx = List.of(column1, column1, column3); // duplicate
        Try.testFail(() -> new ListTable("tableId", "tableDescription", cx),
                "duplicate column should have thrown IllegalArgumentException", IllegalArgumentException.class);

        List<Column<?>> cy = List.of(column1, new Column<>("time", "another timestamp", double.class, ""), column3);
        Try.testFail(() -> new ListTable("tableId", "tableDescription", cy),
                "duplicate column id should have thrown IllegalArgumentException", IllegalArgumentException.class);

        Try.testFail(() -> new ListTable(null, "tableDescription", columns), "null id should have thrown NullPointerException",
                NullPointerException.class);
        Try.testFail(() -> new ListTable("", "tableDescription", columns),
                "empty id should have thrown IllegalArgumentException", IllegalArgumentException.class);
        Try.testFail(() -> new ListTable("tableId", null, columns), "empty description should have thrown NullPointerException",
                NullPointerException.class);
        Try.testFail(() -> new ListTable("tableId", "tableDescription", (List<Column<?>>) null),
                "null columns should have thrown NullPointerException", NullPointerException.class);
        Try.testFail(() -> new ListTable("tableId", "tableDescription", (Collection<Column<?>>) null),
                "null columns should have thrown NullPointerException", NullPointerException.class);
        Try.testFail(() -> new ListTable("tableId", "tableDescription", new ArrayList<Column<?>>()),
                "zero columns should have thrown IllegalArgumentException", IllegalArgumentException.class);
    }

    /** Test column and table construction with wrong value arguments. */
    @Test
    public void testIllegalRecordTable()
    {
        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", Integer.class);
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class);
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class);
        List<Column<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListTable table = new ListTable("tableId", "tableDescription", columns);

        // test add row with wrong content
        Column<String> column3a = new Column<>("bla", "bla", String.class);
        Map<Column<?>, Object> data = new HashMap<>();
        data.put(column1, 2);
        data.put(column2, 4.0);
        data.put(column3a, "bla");
        Try.testFail(() -> table.addRow(data),
                "Adding a column that was not specified for the table should throw IllegalArgumentException",
                IllegalArgumentException.class);

        // test null data
        Try.testFail(() -> table.addRow((Object[]) null), "null data record should have raised exception",
                NullPointerException.class);
        Try.testFail(() -> table.addRowByColumnIds((Map<String, Object>) null), "null data record should have raised exception",
                NullPointerException.class);

        // test too few columns data
        Try.testFail(() -> table.addRow(new Object[] {}), "empty data record should have raised exception",
                IllegalArgumentException.class);

        Try.testFail(() -> table.addRowByColumnIds(new HashMap<String, Object>()),
                "empty data record should have raised exception", IllegalArgumentException.class);

        // test too many columns data
        Try.testFail(() -> table.addRow(new Object[] {1, 2, 3, 4}), "too long data record should have raised exception",
                IllegalArgumentException.class);
        Try.testFail(() -> table.addRowByColumnIds(Map.of("time", 3, "value", 3.5, "remark", "none", "extra", "xx")),
                "too long data record should have raised exception", IllegalArgumentException.class);

        // test wrong type data
        Try.testFail(() -> table.addRow(new Object[] {1, 2, 3}), "wrong type data record should have raised exception",
                IllegalArgumentException.class);
        Try.testFail(() -> table.addRowByColumnIds(Map.of("time", 3, "value", 2L, "remark", "none")),
                "wrong type data record should have raised exception", IllegalArgumentException.class);

        // test missing column data, where the number of columns is okay
        Try.testFail(() -> table.addRowByColumnIds(Map.of("time", 3, "remark", "none", "wrong", 3)),
                "wrong type data record should have raised exception", IllegalArgumentException.class);
    }

}
