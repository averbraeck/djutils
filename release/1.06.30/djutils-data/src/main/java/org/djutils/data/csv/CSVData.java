package org.djutils.data.csv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.djutils.data.DataColumn;
import org.djutils.data.DataRecord;
import org.djutils.data.DataTable;
import org.djutils.data.ListDataTable;
import org.djutils.data.SimpleDataColumn;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.serialization.TextSerializer;
import org.djutils.exceptions.Throw;
import org.djutils.primitives.Primitive;

import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;
import de.siegmar.fastcsv.writer.LineDelimiter;

/**
 * CSVData takes care of reading and writing of table data in CSV format. The class can be used, e.g., as follows:
 * 
 * <pre>
 * DataTable dataTable = new ListDataTable("data", "dataTable", columns);
 * Writer writer = new FileWriter("c:/data/data.csv");
 * Writer metaWriter = new FileWriter("c:/data/data.meta.csv");
 * CSVData.writeData(writer, metaWriter, dataTable);
 * </pre>
 * 
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class CSVData
{
    /**
     * Utility class, no public constructor.
     */
    private CSVData()
    {
        // utility class
    }

    /**
     * Write the data from the data table in CSV format. The writer writes the data, whereas the metaWriter writes the metadata.
     * The metadata consists of a CSV file with three columns: the id, the description, and the class. The first row after the
     * header contains the id, description, and class of the data table itself. The second and further rows contain information
     * about the columns of the data table.
     * @param writer Writer; the writer that writes the data, e.g. to a file
     * @param metaWriter Writer; the writer for the metadata
     * @param dataTable DataTable; the data table to write
     * @param separator char; the delimiter to use for separating entries
     * @param quotechar char; the character to use for quoted elements
     * @param lineDelimiter String; the line terminator to use, can be LineDelimiter.CR, LF, CRLF or PLATFORM
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final Writer writer, final Writer metaWriter, final DataTable dataTable, final char separator,
            final char quotechar, final LineDelimiter lineDelimiter) throws IOException, TextSerializationException
    {
        // Write the metadata file
        try (CsvWriter csvMetaWriter = CsvWriter.builder().fieldSeparator(separator).quoteCharacter(quotechar)
                .lineDelimiter(lineDelimiter).build(metaWriter))
        {
            csvMetaWriter.writeRow("id", "description", "className");
            csvMetaWriter.writeRow(dataTable.getId(), dataTable.getDescription(), dataTable.getClass().getName());
            for (DataColumn<?> column : dataTable.getColumns())
            {
                csvMetaWriter.writeRow(column.getId(), column.getDescription(), column.getValueType().getName());
            }

            // Assemble the serializer array
            TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
            for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
            {
                DataColumn<?> column = dataTable.getColumns().get(i);
                serializers[i] = TextSerializer.resolve(column.getValueType());
            }

            // Write the data file
            try (CsvWriter csvWriter = CsvWriter.builder().fieldSeparator(separator).quoteCharacter(quotechar)
                    .lineDelimiter(lineDelimiter).build(writer))
            {
                csvWriter.writeRow(dataTable.getColumnIds());
                String[] textFields = new String[dataTable.getNumberOfColumns()];
                for (DataRecord record : dataTable)
                {
                    Object[] values = record.getValues();
                    for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
                    {
                        textFields[i] = serializers[i].serialize(values[i]);
                    }
                    csvWriter.writeRow(textFields);
                }
            }
        }
    }

    /**
     * Write the data from the data table in CSV format. The writer writes the data, whereas the metaWriter writes the metadata.
     * The metadata consists of a CSV file with three columns: the id, the description, and the class. The first row after the
     * header contains the id, description, and class of the data table itself. The second and further rows contain information
     * about the columns of the data table. The line ending used will be CRLF which is RFC 4180 compliant.
     * @param writer Writer; the writer that writes the data, e.g. to a file
     * @param metaWriter Writer; the writer for the metadata
     * @param dataTable DataTable; the data table to write
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final Writer writer, final Writer metaWriter, final DataTable dataTable)
            throws IOException, TextSerializationException
    {
        writeData(writer, metaWriter, dataTable, ',', '"', LineDelimiter.CRLF);
    }

    /**
     * Write the data from the data table in CSV format.
     * @param filename String; the file name to write the data to
     * @param metaFilename String; the file name to write the metadata to
     * @param dataTable DataTable; the data table to write
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final String filename, final String metaFilename, final DataTable dataTable)
            throws IOException, TextSerializationException
    {
        FileWriter fw = null;
        FileWriter mfw = null;
        try
        {
            fw = new FileWriter(filename);
            mfw = new FileWriter(metaFilename);
            writeData(fw, mfw, dataTable);
        }
        finally
        {
            if (null != fw)
            {
                fw.close(); // May have already been closed when the CSV writer was closed, but multiple close is harmless
            }
            if (null != mfw)
            {
                mfw.close();
            }
        }
    }

    /**
     * Read the data from the CSV-file into the data table. Use the metadata to reconstruct the data table.
     * @param reader Reader; the reader that can read the data, e.g. from a file
     * @param metaReader Reader; the writer for the metadata
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @param separator char; the delimiter to use for separating entries
     * @param quotechar char; the character to use for quoted elements
     * @throws IOException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final Reader reader, final Reader metaReader, final char separator, final char quotechar)
            throws IOException, TextSerializationException
    {
        // Read the metadata file and reconstruct the data table
        List<DataColumn<?>> columns = new ArrayList<>();
        try (NamedCsvReader csvMetaReader =
                NamedCsvReader.builder().fieldSeparator(separator).quoteCharacter(quotechar).build(metaReader))
        {
            Set<String> metaHeader = csvMetaReader.getHeader();
            Throw.when(
                    metaHeader.size() != 3 || !metaHeader.contains("id") || !metaHeader.contains("description")
                            || !metaHeader.contains("className"),
                    IOException.class,
                    "header of the metafile does not contain 'id, description, className' as fields, but %s: ", metaHeader);

            // table metadata
            Map<String, String> tableRow = new LinkedHashMap<>();
            Iterator<NamedCsvRow> it = csvMetaReader.iterator();
            while (it.hasNext())
            {
                NamedCsvRow row = it.next();
                // table metadata
                if (tableRow.size() == 0)
                {
                    tableRow.putAll(row.getFields());
                    if (!tableRow.get("className").endsWith("ListDataTable"))
                    {
                        throw new IOException("Currently, this method can only recreate a ListDataTable");
                    }
                }
                else
                {
                    // column metadata
                    String type = row.getField("className");
                    Class<?> valueClass = Primitive.forName(type);
                    if (valueClass == null)
                    {
                        try
                        {
                            valueClass = Class.forName(type);
                        }
                        catch (ClassNotFoundException exception)
                        {
                            throw new IOException("Could not find class " + type, exception);
                        }
                    }
                    @SuppressWarnings({"rawtypes", "unchecked"})
                    DataColumn<?> column = new SimpleDataColumn(row.getField("id"), row.getField("description"), valueClass);
                    columns.add(column);
                }
            }

            Throw.when(tableRow == null, IOException.class, "no table information in the metafile");

            // create DataTable
            ListDataTable dataTable = new ListDataTable(tableRow.get("id"), tableRow.get("description"), columns);

            // Assemble the serializer array
            TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
            for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
            {
                DataColumn<?> column = dataTable.getColumns().get(i);
                serializers[i] = TextSerializer.resolve(column.getValueType());
            }

            // Read the data file
            try (NamedCsvReader csvReader =
                    NamedCsvReader.builder().fieldSeparator(separator).quoteCharacter(quotechar).build(reader))
            {
                Set<String> header = csvReader.getHeader();
                Throw.when(header.size() != columns.size(), IOException.class,
                        "Number of columns in the data file does not match column metadata size");
                for (int i = 0; i < columns.size(); i++)
                {
                    Throw.when(!header.contains(columns.get(i).getId()), IOException.class,
                            "Header with id %s not found in the data file", columns.get(i).getId());
                }

                // Read the data file records
                csvReader.forEach(row ->
                {
                    Object[] values = new Object[columns.size()];
                    for (int i = 0; i < columns.size(); i++)
                    {
                        values[i] = serializers[i].deserialize(row.getField(columns.get(i).getId()));
                    }
                    dataTable.addRecord(values);
                });
                return dataTable;
            }
        }
    }

    /**
     * Read the data from the CSV-file into the data table. Use the metadata to reconstruct the data table.
     * @param reader Reader; the reader that can read the data, e.g. from a file
     * @param metaReader Reader; the writer for the metadata
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final Reader reader, final Reader metaReader)
            throws IOException, TextSerializationException
    {
        return readData(reader, metaReader, ',', '"');
    }

    /**
     * Read the data from the CSV-file into the data table. Use the metadata to reconstruct the data table.
     * @param filename String; the file name to read the data from
     * @param metaFilename String; the file name to read the metadata from
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final String filename, final String metaFilename)
            throws IOException, TextSerializationException
    {
        FileReader fr = null;
        FileReader mfr = null;
        try
        {
            fr = new FileReader(filename);
            mfr = new FileReader(metaFilename);
            return readData(fr, mfr);
        }
        finally
        {
            if (null != fr)
            {
                fr.close(); // May have already been closed when the CSV reader was closed, but multiple close is harmless
            }
            if (null != mfr)
            {
                mfr.close();
            }
        }
    }

}
