package org.djutils.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.djunits.unit.AbsoluteTemperatureUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.AbsoluteTemperature;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.data.csv.CSVData;
import org.djutils.data.csv.TSVData;
import org.djutils.data.json.JSONData;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.xml.XMLData;

/**
 * DataDemo.java demonstration code used in the GRAV documentation. <br>
 * <br>
 * Copyright (c) 2020-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public final class DataDemo
{
    /**
     * Do not instantiate.
     */
    private DataDemo()
    {
        // Do not instantiate
    }

    /**
     * Demonstration code.
     * @param args String[]; the command line arguments (not used)
     * @throws TextSerializationException ...
     * @throws IOException ...
     * @throws XMLStreamException ...
     */
    public static void main(final String[] args)
            throws IOException, TextSerializationException, XMLStreamException
    {
        System.out.println("Example using java basic types");
        example1();
        System.out.println("Example using DJUNITS");
        example2();
    }

    /**
     * Straight forward example.
     * @throws TextSerializationException ...
     * @throws IOException ...
     * @throws XMLStreamException ...
     */
    public static void example1() throws IOException, TextSerializationException, XMLStreamException
    {
        DataColumn<Integer> timeStamp = new SimpleDataColumn<>("timeStamp", "time rounded to nearest second", int.class);
        DataColumn<Double> temperature = new SimpleDataColumn<>("temperature", "engine temperature in Celcius", double.class);
        DataColumn<String> remark = new SimpleDataColumn<>("remark", "remark", String.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(timeStamp);
        columns.add(temperature);
        columns.add(remark);
        ListDataTable table = new ListDataTable("engineTemperatureData", "engine temperature samples", columns);

        System.out.println(table);

        Object[] record = new Object[] { 600, 18.0, "starting engine" };
        table.addRecord(record);

        Map<String, Object> map = new HashMap<>();
        map.put("remark", "leaving parking lot");
        map.put("temperature", 28.5);
        map.put("timeStamp", 660);
        table.addRecordByColumnIds(map);

        for (DataRecord dataRecord : table)
        {
            for (int column = 0; column < table.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        for (DataRecord dataRecord : table)
        {
            System.out.println("timeStamp=" + dataRecord.getValue("timeStamp") + ", temperature="
                    + dataRecord.getValue("temperature") + ", " + dataRecord.getValue("remark"));
        }

        System.out.println("JSON");
        JSONData.writeData("C:/Temp/example.json", table);

        DataTable readBack = JSONData.readData("C:/Temp/example.json");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("XML");
        XMLData.writeData("C:/Temp/example.xml", table);

        readBack = XMLData.readData("C:/Temp/example.xml");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("CSV");
        CSVData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

        readBack = CSVData.readData("C:/Temp/example.csv", "C:/Temp/example.csvm");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("TSV");
        TSVData.writeData("C:/Temp/example.tsv", "C:/Temp/example.tsvm", table);

        readBack = TSVData.readData("C:/Temp/example.tsv", "C:/Temp/example.tsvm");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        table.addRecord(new Object[] { 720, Double.NaN, "can we store a NaN value?" });
        table.addRecord(new Object[] { 780, Double.POSITIVE_INFINITY, "can we store positive infinity?" });
        table.addRecord(new Object[] { 840, Double.NEGATIVE_INFINITY, "can we store negative infinity?" });
        System.out.println("JSON");
        JSONData.writeData("C:/Temp/example.json", table);

        readBack = JSONData.readData("C:/Temp/example.json");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        CSVData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

    }

    /**
     * Example using DJUNITS strongly types values.
     * @throws TextSerializationException ...
     * @throws IOException ...
     */
    public static void example2() throws IOException, TextSerializationException
    {
        DataColumn<Time> timeStamp = new SimpleDataColumn<>("timeStamp", "time rounded to nearest second", Time.class);
        DataColumn<AbsoluteTemperature> temperature =
                new SimpleDataColumn<>("temperature", "engine temperature in Celcius", AbsoluteTemperature.class);
        DataColumn<String> remark = new SimpleDataColumn<>("remark", "remark", String.class);
        List<DataColumn<?>> columns = new ArrayList<>();
        columns.add(timeStamp);
        columns.add(temperature);
        columns.add(remark);
        ListDataTable table = new ListDataTable("engineTemperatureData", "engine temperature samples", columns);

        System.out.println(table);

        Object[] record = new Object[] { new Time(600, TimeUnit.BASE_SECOND),
                new AbsoluteTemperature(18.0, AbsoluteTemperatureUnit.DEGREE_CELSIUS), "starting engine" };
        table.addRecord(record);

        Map<String, Object> map = new HashMap<>();
        map.put("remark", "leaving parking lot");
        map.put("temperature", new AbsoluteTemperature(28.5, AbsoluteTemperatureUnit.DEGREE_CELSIUS));
        map.put("timeStamp", new Time(660, TimeUnit.BASE_SECOND));
        table.addRecordByColumnIds(map);

        for (DataRecord dataRecord : table)
        {
            for (int column = 0; column < table.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        for (DataRecord dataRecord : table)
        {
            System.out.println("timeStamp=" + dataRecord.getValue("timeStamp") + ", temperature="
                    + dataRecord.getValue("temperature") + ", " + dataRecord.getValue("remark"));
        }

        System.out.println("JSON");
        JSONData.writeData("C:/Temp/example.json", table);

        DataTable readBack = JSONData.readData("C:/Temp/example.json");
        for (DataRecord dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

    }

}
