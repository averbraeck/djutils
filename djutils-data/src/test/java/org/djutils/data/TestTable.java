package org.djutils.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.djunits.value.vfloat.scalar.FloatSpeed;
import org.djutils.test.UnitTest;
import org.junit.jupiter.api.Test;

/**
 * Table test.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TestTable
{

    /**
     * Test table creation, reading and writing.
     * @throws IOException when temp file fails
     */
    @Test
    public void testTable() throws IOException
    {
        // column constructor nulls
        UnitTest.testFail(() -> new Column<>(null, "description", String.class, null),
                "Column constructor should not accept a null id.", NullPointerException.class);
        UnitTest.testFail(() -> new Column<>("id", null, String.class, null),
                "Column constructor should not accept a null description.", NullPointerException.class);
        UnitTest.testFail(() -> new Column<>("id", "description", null, null),
                "Column constructor should not accept a null value type.", NullPointerException.class);

        // column contents after construction
        Column<String> column1 = new Column<>("id1", "description1", String.class, null);
        assertEquals(column1.getId(), "id1");
        assertEquals(column1.getDescription(), "description1");
        assertEquals(column1.getValueType(), String.class);
        assertEquals(column1.getUnit(), null);
        assertNotEquals(column1, "not a column");

        Column<Double> column2 = new Column<>("id2", "description2", Double.class, "m/s");
        assertEquals(column2.getUnit(), "m/s");

        Column<FloatSpeed> column3 = new Column<>("id3", "description3", FloatSpeed.class, "m/s");

        Column<Double> column4 = new Column<>("id1", "test for no equal column ids", Double.class, "m/s");
        column4.toString();
        column4.equals(null);
        assertNotEquals(column4, column2);

        // table: duplicate column id's
        List<Column<?>> columnList = List.of(column1, column2, column4);
        UnitTest.testFail(() -> new ListTable("id", "description", columnList), "Columns with the same id should not be accepted.",
                IllegalArgumentException.class);

        // table
        Collection<Column<?>> columns = new LinkedHashSet<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListTable table = new ListTable("id", "description", columns);
        table.toString();
        assertTrue(table.isEmpty());

        // add data, correctly
        String[] data1 = new String[] {"string1", "string2", "string3"};
        Double[] data2 = new Double[] {1.1, 2.2, 3.3};
        FloatSpeed[] data3 = new FloatSpeed[] {FloatSpeed.instantiateSI(0.1f), FloatSpeed.instantiateSI(0.2f),
                FloatSpeed.instantiateSI(0.3f)};
        table.addRow(new Object[] {data1[0], data2[0], data3[0]});
        table.addRow(Map.of(column1, data1[1], column2, data2[1], column3, data3[1]));
        table.addRowByColumnIds(Map.of("id1", data1[2], "id2", data2[2], "id3", data3[2]));

        // add data, incorrectly
        UnitTest.testFail(() -> table.addRow(new Object[] {data2[0], data1[0], data3[0]}),
                "Adding data types in wrong order should fail.");
        UnitTest.testFail(() -> table.addRow(new Object[] {data1[0], data2[0]}), "Adding too few data types should fail.");
        UnitTest.testFail(() -> table.addRow(Map.of(column2, data1[1], column1, data2[1], column3, data3[1])),
                "Adding data types in wrong order should fail.");
        UnitTest.testFail(() -> table.addRow(Map.of(column1, data1[1], column2, data2[1])),
                "Adding too few data types should fail.");
        UnitTest.testFail(() -> table.addRowByColumnIds(Map.of("id2", data1[2], "id1", data2[2], "id3", data3[2])),
                "Adding data types in wrong order should fail.");
        UnitTest.testFail(() -> table.addRowByColumnIds(Map.of("id1", data1[2], "id2", data2[2])),
                "Adding too few data types should fail.");

        // test contents
        testTableInstance(table, column1, column2, column3, data1, data2, data3);

    }

    /**
     * Test contents of a table.
     * @param table table
     * @param column1 column 1
     * @param column2 column 2
     * @param column3 column 3
     * @param data1 data in column 1
     * @param data2 data in column 2
     * @param data3 data in column 3
     */
    @SuppressWarnings("unlikely-arg-type")
    private void testTableInstance(final Table table, final Column<String> column1, final Column<Double> column2,
            final Column<FloatSpeed> column3, final String[] data1, final Double[] data2, final FloatSpeed[] data3)
    {
        assertEquals(table.getId(), "id");
        assertEquals(table.getDescription(), "description");

        assertEquals(table.getNumberOfColumns(), 3);
        assertEquals(table.getColumn(0), column1);
        assertEquals(table.getColumn(1), column2);
        assertEquals(table.getColumn(2), column3);
        assertEquals(table.getColumnNumber(column1), 0);
        assertEquals(table.getColumnNumber(column2), 1);
        assertEquals(table.getColumnNumber(column3), 2);
        assertEquals(table.getColumnNumber("id1"), 0);
        assertEquals(table.getColumnNumber("id2"), 1);
        assertEquals(table.getColumnNumber("id3"), 2);

        UnitTest.testFail(() -> table.getColumn(-1), IndexOutOfBoundsException.class);
        UnitTest.testFail(() -> table.getColumn(3), IndexOutOfBoundsException.class);
        UnitTest.testFail(() -> table.getColumnNumber(new Column<>("id4", "no such column", String.class, null)),
                IllegalArgumentException.class);
        UnitTest.testFail(() -> table.getColumnNumber("id4"), IllegalArgumentException.class);

        int rowNum = 0;
        for (Row row : table)
        {
            row.toString();
            row.hashCode();
            row.equals(row);
            row.equals(null);
            row.equals("nope");
            row.equals(new Row(table, new Object[2]));
            assertEquals(data1[rowNum], row.getValue(0));
            assertEquals(data2[rowNum], row.getValue(1));
            assertEquals(data3[rowNum], row.getValue(2));
            assertEquals(data1[rowNum], row.getValue(column1));
            assertEquals(data2[rowNum], row.getValue(column2));
            assertEquals(data3[rowNum], row.getValue(column3));
            assertEquals(data1[rowNum], row.getValue("id1"));
            assertEquals(data2[rowNum], row.getValue("id2"));
            assertEquals(data3[rowNum], row.getValue("id3"));
            rowNum++;
        }
        assertTrue(rowNum == data1.length, "Table has wrong number of rows.");
    }

}
