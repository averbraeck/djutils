package org.djutils.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djutils.data.csv.CsvData;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.io.CompressedFileWriter;
import org.junit.jupiter.api.Test;

import de.siegmar.fastcsv.writer.LineDelimiter;

/**
 * TestCsvData tests writing and reading of a CSV file, and checks that all data is read back correctly into the Table.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TestCsvTable
{
    /**
     * test reading and writing of a CSV file.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testReadWriteCsv() throws IOException, TextSerializationException
    {
        File tempDataFile = File.createTempFile("testdata", ".csv");
        File tempMetaDataFile = File.createTempFile("testmetadata", ".csv");
        tempDataFile.deleteOnExit();
        tempMetaDataFile.deleteOnExit();

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class, "");
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class, "");
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class, "");
        List<Column<?>> columns = List.of(column1, column2, column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {1, 5.0, "normal"});
        table1.addRow(new Object[] {2, 10.0, "normal"});
        table1.addRow(new Object[] {3, 15.0, "normal"});
        table1.addRow(new Object[] {4, 20.0, "abnormal"});
        CsvData.writeData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath(), table1);

        Table table2 = CsvData.readData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath());
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

    /**
     * test reading and writing of a zipped CSV file.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testReadWriteZippedCsv() throws IOException, TextSerializationException
    {
        File tempZipFile = File.createTempFile(UUID.randomUUID().toString(), ".zip");
        CompressedFileWriter cfw = new CompressedFileWriter(tempZipFile.getAbsolutePath());
        tempZipFile.deleteOnExit();

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class, "");
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class, "");
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class, "");
        List<Column<?>> columns = List.of(column1, column2, column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {1, 5.0, "normal"});
        table1.addRow(new Object[] {2, 10.0, "normal"});
        table1.addRow(new Object[] {3, 15.0, "normal"});
        table1.addRow(new Object[] {4, 20.0, "abnormal"});
        CsvData.writeZippedData(cfw, "testdata.csv", "testmetadata.csv", table1);

        Table table2 = CsvData.readZippedData(tempZipFile.getAbsolutePath(), "testdata.csv", "testmetadata.csv");
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

    /**
     * test reading and writing of a zipped CSV file with different delimiters.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testrReadWriteZippedCsvDelimiters() throws IOException, TextSerializationException
    {
        File tempZipFile = File.createTempFile(UUID.randomUUID().toString(), ".zip");
        CompressedFileWriter cfw = new CompressedFileWriter(tempZipFile.getAbsolutePath());
        tempZipFile.deleteOnExit();

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class);
        Column<Double> column2 = new Column<>("value", "measured value [m]", double.class, null);
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class, "");
        List<Column<?>> columns = List.of(column1, column2, column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {1, 5.0, "normal"});
        table1.addRow(new Object[] {2, 10.0, "normal"});
        table1.addRow(new Object[] {3, 15.0, "normal"});
        table1.addRow(new Object[] {4, 20.0, "abnormal"});
        CsvData.writeZippedData(cfw, "testdata.csv", "testmetadata.csv", table1, ';', '\'', LineDelimiter.LF);

        Table table2 = CsvData.readZippedData(tempZipFile.getAbsolutePath(), "testdata.csv", "testmetadata.csv", ';', '\'');
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

    /**
     * test reading and writing of a CSV file with "missing values" coded as null values.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testReadWriteCsvNulls() throws IOException, TextSerializationException
    {
        File tempDataFile = File.createTempFile("testdata", ".csv");
        File tempMetaDataFile = File.createTempFile("testmetadata", ".csv");
        tempDataFile.deleteOnExit();
        tempMetaDataFile.deleteOnExit();

        Column<Integer> column1 = new Column<>("time", "time, rounded to second [s]", int.class);
        Column<Length> column2 = new Column<>("value", "measured value [m]", Length.class, "m");
        Column<String> column3 = new Column<>("remark", "remark about the measurement", String.class);
        List<Column<?>> columns = List.of(column1, column2, column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {1, Length.valueOf("1 mm"), "normal"});
        table1.addRow(new Object[] {null, Length.valueOf("10.0 km"), "normal"});
        table1.addRow(new Object[] {3, null, "abnormal"});
        table1.addRow(new Object[] {4, Length.ofSI(40.0), null});
        CsvData.writeData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath(), table1);

        Table table2 = CsvData.readData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath());
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

    /**
     * test reading and writing of a CSV file with columns that treat units in different ways.
     * @throws IOException on error
     * @throws TextSerializationException on unknown data type for (de)serialization
     */
    @Test
    public void testReadWriteCsvUnits() throws IOException, TextSerializationException
    {
        Locale.setDefault(Locale.US);

        File tempDataFile = File.createTempFile("testdata", ".csv");
        File tempMetaDataFile = File.createTempFile("testmetadata", ".csv");
        tempDataFile.deleteOnExit();
        tempMetaDataFile.deleteOnExit();

        Column<Duration> column1 = new Column<>("time", "time, rounded to second [s]", Duration.class, "ms");
        Column<Length> column2 = new Column<>("value", "measured value [m]", Length.class, "m");
        Column<Speed> column3 = new Column<>("remark", "remark about the measurement", Speed.class, "mi/h");
        List<Column<?>> columns = List.of(column1, column2, column3);
        ListTable table1 = new ListTable("tableId", "tableDescription", columns);
        table1.addRow(new Object[] {Duration.valueOf("1 s"), Length.valueOf("10.0 m"), Speed.valueOf("15.0 mi/h")});
        table1.addRow(new Object[] {Duration.valueOf("2 h"), Length.valueOf("20.0 km"), Speed.valueOf("25.0 km/h")});
        table1.addRow(new Object[] {Duration.valueOf("3 ms"), Length.valueOf("30.0 mm"), Speed.valueOf("35.0 m/s")});
        CsvData.writeData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath(), table1);

        Table table2 = CsvData.readData(tempDataFile.getAbsolutePath(), tempMetaDataFile.getAbsolutePath());
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
