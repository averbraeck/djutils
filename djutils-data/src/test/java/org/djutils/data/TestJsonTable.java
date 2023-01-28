package org.djutils.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.djutils.data.json.JsonData;
import org.djutils.data.serialization.TextSerializationException;
import org.junit.Test;

/**
 * TestJsonData tests writing and reading of a JSON file, and checks that all data is read back correctly into the Table.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TestJsonTable
{
    /**
     * test reading and writing of a JSON file.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testreadWriteJSON() throws IOException, TextSerializationException
    {
        File tempDataFile = File.createTempFile("testdata", ".json");
        tempDataFile.deleteOnExit();

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class, "");
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class, "");
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class, "");
        List<Column<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {1, 5.0, "normal"});
        table1.addRow(new Object[] {2, 10.0, "normal"});
        table1.addRow(new Object[] {3, 15.0, "normal"});
        table1.addRow(new Object[] {4, 20.0, "abnormal"});
        JsonData.writeData(tempDataFile.getAbsolutePath(), table1);

        Table table2 = JsonData.readData(tempDataFile.getAbsolutePath());
        assertTrue(table2 instanceof ListTable);
        assertEquals(table1.getId(), table2.getId());
        assertEquals(table1.getDescription(), table2.getDescription());
        assertEquals(table1.getNumberOfColumns(), table2.getNumberOfColumns());
        assertArrayEquals(table1.getColumnIds(), table2.getColumnIds());
        assertArrayEquals(table1.getColumnDescriptions(), table2.getColumnDescriptions());
        assertArrayEquals(table1.getColumnDataTypes(), table2.getColumnDataTypes());
        assertArrayEquals(table1.getColumnDataTypeStrings(), table2.getColumnDataTypeStrings());

        Iterator<Row> it1 = table1.iterator();
        Iterator<Row> it2 = table2.iterator();
        while (it1.hasNext() && it2.hasNext())
        {
            Row r1 = it1.next();
            Row r2 = it2.next();
            assertArrayEquals(r1.getValues(), r2.getValues());
        }
        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
    }
}
