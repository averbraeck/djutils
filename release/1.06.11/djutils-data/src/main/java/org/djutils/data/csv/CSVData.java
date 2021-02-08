package org.djutils.data.csv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.djutils.data.DataColumn;
import org.djutils.data.DataRecord;
import org.djutils.data.DataTable;
import org.djutils.data.ListDataTable;
import org.djutils.data.SimpleDataColumn;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.serialization.TextSerializer;
import org.djutils.exceptions.Throw;
import org.djutils.primitives.Primitive;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvAppender;
import de.siegmar.fastcsv.writer.CsvWriter;

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
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param escapechar char; the character to use for escaping quotechars or escapechars
     * @param lineEnd String; the line feed terminator to use
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final Writer writer, final Writer metaWriter, final DataTable dataTable, final char separator,
            final char quotechar, final char escapechar, final String lineEnd) throws IOException, TextSerializationException
    {
        // Write the metadata file
        CsvWriter csvMetaWriter = null;
        CsvWriter csvWriter = null;
        csvMetaWriter = new CsvWriter();
        csvMetaWriter.setFieldSeparator(separator);
        csvMetaWriter.setTextDelimiter(quotechar);
        csvMetaWriter.setLineDelimiter(lineEnd.toCharArray());
        try (CsvAppender csvMetaAppender = csvMetaWriter.append(metaWriter))
        {
            csvMetaAppender.appendLine("id", "description", "className");
            csvMetaAppender.appendLine(dataTable.getId(), dataTable.getDescription(), dataTable.getClass().getName());
            for (DataColumn<?> column : dataTable.getColumns())
            {
                csvMetaAppender.appendLine(column.getId(), column.getDescription(), column.getValueType().getName());
            }

            // Assemble the serializer array
            TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
            for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
            {
                DataColumn<?> column = dataTable.getColumns().get(i);
                serializers[i] = TextSerializer.resolve(column.getValueType());
            }

            // Write the data file
            csvWriter = new CsvWriter();
            csvWriter.setFieldSeparator(separator);
            csvWriter.setTextDelimiter(quotechar);
            csvWriter.setLineDelimiter(lineEnd.toCharArray());
            try (CsvAppender csvAppender = csvWriter.append(writer))
            {
                csvAppender.appendLine(dataTable.getColumnIds());
                String[] textFields = new String[dataTable.getNumberOfColumns()];
                for (DataRecord record : dataTable)
                {
                    Object[] values = record.getValues();
                    for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
                    {
                        textFields[i] = serializers[i].serialize(values[i]);
                    }
                    csvAppender.appendLine(textFields);
                }
            }
        }
    }

    /**
     * Write the data from the data table in CSV format. The writer writes the data, whereas the metaWriter writes the metadata.
     * The metadata consists of a CSV file with three columns: the id, the description, and the class. The first row after the
     * header contains the id, description, and class of the data table itself. The second and further rows contain information
     * about the columns of the data table.
     * @param writer Writer; the writer that writes the data, e.g. to a file
     * @param metaWriter Writer; the writer for the metadata
     * @param dataTable DataTable; the data table to write
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final Writer writer, final Writer metaWriter, final DataTable dataTable)
            throws IOException, TextSerializationException
    {
        writeData(writer, metaWriter, dataTable, ',', '"', '\\', "\n");
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
     * @param escapechar char; the character to use for escaping quotechars or escapechars
     * @param lineEnd String; the line feed terminator to use
     * @throws IOException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final Reader reader, final Reader metaReader, final char separator, final char quotechar,
            final char escapechar, final String lineEnd) throws IOException, TextSerializationException
    {
        // Read the metadata file and reconstruct the data table
        CsvReader csvMetaReader = new CsvReader();
        csvMetaReader.setFieldSeparator(separator);
        csvMetaReader.setTextDelimiter(quotechar);
        List<DataColumn<?>> columns = new ArrayList<>();
        try (CsvParser csvMetaParser = csvMetaReader.parse(metaReader))
        {
            CsvRow row = csvMetaParser.nextRow();
            Throw.when(row == null, IOException.class, "metafile does not contain header row");
            List<String> header = row.getFields();
            Throw.when(
                    header.size() != 3 || !"id".equals(header.get(0)) || !"description".equals(header.get(1))
                            || !"className".equals(header.get(2)),
                    IOException.class,
                    "header of the metafile does not contain 'id, description, className' as fields, but %s: ", header);

            // table metadata
            row = csvMetaParser.nextRow();
            Throw.when(row == null, IOException.class, "no table information in the metafile");
            List<String> tableLine = row.getFields();
            Throw.when(tableLine.size() != 3, IOException.class, "table data in the metafile does not contain 3 fields");
            Throw.when(!tableLine.get(2).endsWith("ListDataTable"), IOException.class,
                    "Currently, this method can only recreate a ListDataTable");

            // column metadata
            while ((row = csvMetaParser.nextRow()) != null)
            {
                List<String> line = row.getFields();
                Throw.when(line.size() != 3, IOException.class, "column data in the metafile does not contain 3 fields");
                String type = line.get(2);
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
                DataColumn<?> column = new SimpleDataColumn(line.get(0), line.get(1), valueClass);
                columns.add(column);
            }

            // create DataTable
            ListDataTable dataTable = new ListDataTable(tableLine.get(0), tableLine.get(1), columns);

            // Assemble the serializer array
            TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
            for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
            {
                DataColumn<?> column = dataTable.getColumns().get(i);
                serializers[i] = TextSerializer.resolve(column.getValueType());
            }

            // Read the data file header
            CsvReader csvReader = new CsvReader();
            csvReader.setFieldSeparator(separator);
            csvReader.setTextDelimiter(quotechar);
            try (CsvParser csvParser = csvReader.parse(reader))
            {
                row = csvParser.nextRow();
                Throw.when(row == null, IOException.class, "file does not contain header row");
                header = row.getFields();
                Throw.when(header.size() != columns.size(), IOException.class,
                        "Number of columns in the data file does not match column metadata size");
                for (int i = 0; i < header.size(); i++)
                {
                    Throw.when(!header.get(i).equals(columns.get(i).getId()), IOException.class,
                            "Header for column %d in the data file does not match column metadata info", i);
                }

                // Read the data file records
                List<String> data;
                while ((row = csvParser.nextRow()) != null)
                {
                    data = row.getFields();
                    Object[] values = new Object[columns.size()];
                    for (int i = 0; i < values.length; i++)
                    {
                        values[i] = serializers[i].deserialize(data.get(i));
                    }
                    dataTable.addRecord(values);
                }
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
        return readData(reader, metaReader, ',', '"', '\\', "\n");
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
