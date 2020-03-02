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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

/**
 * CSVData takes care of reading and writing of table data in CSV format. The class can be used, e.g., as follows:
 * 
 * <pre>
 * DataTable dataTable = new ListDataTable("data", "dataTable", columns);
 * Writer writer = new FileWriter("c:/data/data.csv");
 * CSVData.writeData(writer, dataTable);
 * </pre>
 * 
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * about the columns of the data table. The writers are closed after finishing the writing of the data.
     * @param writer Writer; the writer that writes the data, e.g. to a file
     * @param metaWriter Writer; the writer for the metadata
     * @param dataTable the data table to write
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final Writer writer, final Writer metaWriter, final DataTable dataTable)
            throws IOException, TextSerializationException
    {
        // write the metadata file
        CSVWriter csvMetaWriter = new CSVWriter(metaWriter);
        csvMetaWriter.writeNext(new String[] {"id", "description", "className"});
        csvMetaWriter.writeNext(new String[] {dataTable.getId(), dataTable.getDescription(), dataTable.getClass().getName()});
        for (DataColumn<?> column : dataTable.getColumns())
        {
            csvMetaWriter.writeNext(new String[] {column.getId(), column.getDescription(), column.getValueType().getName()});
        }
        csvMetaWriter.close();
        metaWriter.close();

        // initialize the serializers
        TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
        for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
        {
            DataColumn<?> column = dataTable.getColumns().get(i);
            serializers[i] = TextSerializer.resolve(column.getValueType());
        }

        // write the data file
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(dataTable.getColumnIds());
        String[] textFields = new String[dataTable.getNumberOfColumns()];
        for (DataRecord record : dataTable)
        {
            Object[] values = record.getValues();
            for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
            {
                textFields[i] = serializers[i].serialize(values[i]);
            }
            csvWriter.writeNext(textFields);
        }
        csvWriter.close();
        writer.close();
    }

    /**
     * Write the data from the data table in CSV format.
     * @param filename String; the file name to write the data to
     * @param metaFilename String; the file name to write the metadata to
     * @param dataTable the data table to write
     * @throws IOException on I/O error when writing the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static void writeData(final String filename, final String metaFilename, final DataTable dataTable)
            throws IOException, TextSerializationException
    {
        writeData(new FileWriter(filename), new FileWriter(metaFilename), dataTable);
    }

    /**
     * Read the data from the csv-file into the data table. Use the metadata to reconstruct the data table.
     * @param reader Reader; the reader that can read the data, e.g. from a file
     * @param metaReader Reader; the writer for the metadata
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException on I/O error when reading the data
     * @throws CsvValidationException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final Reader reader, final Reader metaReader)
            throws IOException, CsvValidationException, TextSerializationException
    {
        // read the metadata file and reconstruct the data table
        CSVReader csvMetaReader = new CSVReader(metaReader);
        List<DataColumn<?>> columns = new ArrayList<>();
        String[] header = csvMetaReader.readNext();
        Throw.when(
                header.length != 3 || !"id".equals(header[0]) || !"description".equals(header[1])
                        || !"className".equals(header[2]),
                IOException.class, "header of the metafile does not contain 'id, description, className' as fields");

        // table metadata
        String[] tableLine = csvMetaReader.readNext();
        Throw.when(tableLine == null, IOException.class, "no table information in the metafile");
        Throw.when(tableLine.length != 3, IOException.class, "table data in the metafile does not contain 3 fields");
        Throw.when(!tableLine[2].endsWith("ListDataTable"), IOException.class,
                "Currently, this method can only recreate a ListDataTable");

        // column metadata
        String[] line = csvMetaReader.readNext();
        while (line != null)
        {
            Throw.when(line.length != 3, IOException.class, "column data in the metafile does not contain 3 fields");
            String type = line[2];
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
                finally
                {
                    csvMetaReader.close();
                    metaReader.close();
                }
            }
            @SuppressWarnings({"rawtypes", "unchecked"})
            DataColumn<?> column = new SimpleDataColumn(line[0], line[1], valueClass);
            columns.add(column);
            line = csvMetaReader.readNext();
        }

        // create DataTable and close files
        ListDataTable dataTable = new ListDataTable(tableLine[0], tableLine[1], columns);
        csvMetaReader.close();
        metaReader.close();

        // obtain the serializers
        TextSerializer<?>[] serializers = new TextSerializer[dataTable.getNumberOfColumns()];
        for (int i = 0; i < dataTable.getNumberOfColumns(); i++)
        {
            DataColumn<?> column = dataTable.getColumns().get(i);
            serializers[i] = TextSerializer.resolve(column.getValueType());
        }

        // read the data file header
        CSVReader csvReader = new CSVReader(reader);
        header = csvReader.readNext();
        Throw.when(header.length != columns.size(), IOException.class,
                "Number of columns in the data file does not match column metadata size");
        for (int i = 0; i < header.length; i++)
        {
            Throw.when(!header[i].equals(columns.get(i).getId()), IOException.class,
                    "Header for column %d in the data file does not match column metadata info", i);
        }

        // read the data file records
        String[] data = csvReader.readNext();
        while (data != null)
        {
            Object[] values = new Object[columns.size()];
            for (int i = 0; i < values.length; i++)
            {
                values[i] = serializers[i].deserialize(data[i]);
            }
            dataTable.addRecord(values);
            data = csvReader.readNext();
        }
        csvReader.close();
        reader.close();

        return dataTable;
    }

    /**
     * Read the data from the csv-file into the data table. Use the metadata to reconstruct the data table.
     * @param filename String; the file name to read the data from
     * @param metaFilename String; the file name to read the metadata from
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException on I/O error when reading the data
     * @throws CsvValidationException when the CSV data was not formatted right
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final String filename, final String metaFilename)
            throws IOException, CsvValidationException, TextSerializationException
    {
        return readData(new FileReader(filename), new FileReader(metaFilename));
    }

}
