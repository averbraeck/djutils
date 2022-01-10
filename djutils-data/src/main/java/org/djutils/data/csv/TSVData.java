package org.djutils.data.csv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.djutils.data.DataTable;
import org.djutils.data.serialization.TextSerializationException;

import de.siegmar.fastcsv.writer.LineDelimiter;

/**
 * TSVData takes care of reading and writing of table data in Tab-Separated-Value format. The class can be used, e.g., as
 * follows:
 * 
 * <pre>
 * DataTable dataTable = new ListDataTable("data", "dataTable", columns);
 * Writer writer = new FileWriter("c:/data/data.tsv");
 * Writer metaWriter = new FileWriter("c:/data/data.meta.tsv");
 * TSVData.writeData(writer, metaWriter, dataTable);
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
public final class TSVData
{
    /**
     * Utility class, no public constructor.
     */
    private TSVData()
    {
        // utility class
    }

    /**
     * Write the data from the data table in TSV format. The writer writes the data, whereas the metaWriter writes the metadata.
     * The metadata consists of a TSV file with three columns: the id, the description, and the class. The first row after the
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
        CSVData.writeData(writer, metaWriter, dataTable, '\t', '\u0000', LineDelimiter.CRLF);
    }

    /**
     * Write the data from the data table in TSV format.
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
                fw.close();
            }
            if (null != mfw)
            {
                mfw.close();
            }
        }
    }

    /**
     * Read the data from the TSV-file into the data table. Use the metadata to reconstruct the data table.
     * @param reader Reader; the reader that can read the data, e.g. from a file
     * @param metaReader Reader; the writer for the metadata
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException on I/O error when reading the data
     * @throws TextSerializationException on unknown data type for serialization
     */
    public static DataTable readData(final Reader reader, final Reader metaReader)
            throws IOException, TextSerializationException
    {
        return CSVData.readData(reader, metaReader, '\t', '\u0000');
    }

    /**
     * Read the data from the TSV-file into the data table. Use the metadata to reconstruct the data table.
     * @param filename String; the file name to read the data from
     * @param metaFilename String; the file name to read the metadata from
     * @return dataTable the data table reconstructed from the meta data and filled with the data
     * @throws IOException on I/O error when reading the data
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
                fr.close();
            }
            if (null != mfr)
            {
                mfr.close();
            }
        }
    }

}
