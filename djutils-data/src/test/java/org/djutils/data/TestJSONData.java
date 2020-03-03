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

import org.djutils.data.json.JSONData;
import org.djutils.data.serialization.TextSerializationException;
import org.junit.Test;

/**
 * TestJSONData tests writing and reading of a JSON file, and checks that all data is read back correctly into the DataTable.
 * <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class TestJSONData
{
    /**
     * test reading and writing of a JSON file.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testreadWriteJSON() throws IOException, TextSerializationException
    {
        File tempDataFile = File.createTempFile("testdata", "json");

        DataColumn<Integer> column1 = new SimpleDataColumn<>("time", "time, rounded to second [s]", int.class);
        DataColumn<Double> column2 = new SimpleDataColumn<>("value", "measured value [m]", double.class);
        DataColumn<String> column3 = new SimpleDataColumn<>("remark", "remark about the measurement", String.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        ListDataTable table1 = new ListDataTable("tableId", "tableDescription", columns);
        table1.addRecord(new Object[] {1, 5.0, "normal"});
        table1.addRecord(new Object[] {2, 10.0, "normal"});
        table1.addRecord(new Object[] {3, 15.0, "normal"});
        table1.addRecord(new Object[] {4, 20.0, "abnormal"});
        JSONData.writeData(tempDataFile.getAbsolutePath(), table1);

        DataTable table2 = JSONData.readData(tempDataFile.getAbsolutePath());
        assertTrue(table2 instanceof ListDataTable);
        assertEquals(table1.getId(), table2.getId());
        assertEquals(table1.getDescription(), table2.getDescription());
        assertEquals(table1.getNumberOfColumns(), table2.getNumberOfColumns());
        assertArrayEquals(table1.getColumnIds(), table2.getColumnIds());
        assertArrayEquals(table1.getColumnDescriptions(), table2.getColumnDescriptions());
        assertArrayEquals(table1.getColumnDataTypes(), table2.getColumnDataTypes());
        assertArrayEquals(table1.getColumnDataTypeStrings(), table2.getColumnDataTypeStrings());

        Iterator<DataRecord> it1 = table1.iterator();
        Iterator<DataRecord> it2 = table2.iterator();
        while (it1.hasNext() && it2.hasNext())
        {
            DataRecord r1 = it1.next();
            DataRecord r2 = it2.next();
            assertArrayEquals(r1.getValues(), r2.getValues());
        }
        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
    }
}
