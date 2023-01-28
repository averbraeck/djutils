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
import org.djutils.data.csv.CsvData;
import org.djutils.data.csv.TsvData;
import org.djutils.data.json.JsonData;
import org.djutils.data.serialization.TextSerializationException;
import org.djutils.data.xml.XmlData;

/**
 * DataDemo.java demonstration code used in the documentation.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
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
        Column<Integer> timeStamp = new Column<>("timeStamp", "time rounded to nearest second", int.class, "");
        Column<Double> temperature = new Column<>("temperature", "engine temperature in Celcius", double.class, "");
        Column<String> remark = new Column<>("remark", "remark", String.class, "");
        List<Column<?>> columns = new ArrayList<>();
        columns.add(timeStamp);
        columns.add(temperature);
        columns.add(remark);
        ListTable table = new ListTable("engineTemperatureData", "engine temperature samples", columns);

        System.out.println(table);

        Object[] row = new Object[] { 600, 18.0, "starting engine" };
        table.addRow(row);

        Map<String, Object> map = new HashMap<>();
        map.put("remark", "leaving parking lot");
        map.put("temperature", 28.5);
        map.put("timeStamp", 660);
        table.addRowByColumnIds(map);

        for (Row dataRecord : table)
        {
            for (int column = 0; column < table.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        for (Row dataRecord : table)
        {
            System.out.println("timeStamp=" + dataRecord.getValue("timeStamp") + ", temperature="
                    + dataRecord.getValue("temperature") + ", " + dataRecord.getValue("remark"));
        }

        System.out.println("JSON");
        JsonData.writeData("C:/Temp/example.json", table);

        Table readBack = JsonData.readData("C:/Temp/example.json");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("XML");
        XmlData.writeData("C:/Temp/example.xml", table);

        readBack = XmlData.readData("C:/Temp/example.xml");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("CSV");
        CsvData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

        readBack = CsvData.readData("C:/Temp/example.csv", "C:/Temp/example.csvm");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        System.out.println("TSV");
        TsvData.writeData("C:/Temp/example.tsv", "C:/Temp/example.tsvm", table);

        readBack = TsvData.readData("C:/Temp/example.tsv", "C:/Temp/example.tsvm");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

        table.addRow(new Object[] { 720, Double.NaN, "can we store a NaN value?" });
        table.addRow(new Object[] { 780, Double.POSITIVE_INFINITY, "can we store positive infinity?" });
        table.addRow(new Object[] { 840, Double.NEGATIVE_INFINITY, "can we store negative infinity?" });
        System.out.println("JSON");
        JsonData.writeData("C:/Temp/example.json", table);

        readBack = JsonData.readData("C:/Temp/example.json");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        CsvData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

    }

    /**
     * Example using DJUNITS strongly types values.
     * @throws TextSerializationException ...
     * @throws IOException ...
     */
    public static void example2() throws IOException, TextSerializationException
    {
        Column<Time> timeStamp = new Column<>("timeStamp", "time rounded to nearest second", Time.class, "s");
        Column<AbsoluteTemperature> temperature =
                new Column<>("temperature", "engine temperature in Celcius", AbsoluteTemperature.class, "K");
        Column<String> remark = new Column<>("remark", "remark", String.class, "");
        List<Column<?>> columns = new ArrayList<>();
        columns.add(timeStamp);
        columns.add(temperature);
        columns.add(remark);
        ListTable table = new ListTable("engineTemperatureData", "engine temperature samples", columns);

        System.out.println(table);

        Object[] record = new Object[] { new Time(600, TimeUnit.BASE_SECOND),
                new AbsoluteTemperature(18.0, AbsoluteTemperatureUnit.DEGREE_CELSIUS), "starting engine" };
        table.addRow(record);

        Map<String, Object> map = new HashMap<>();
        map.put("remark", "leaving parking lot");
        map.put("temperature", new AbsoluteTemperature(28.5, AbsoluteTemperatureUnit.DEGREE_CELSIUS));
        map.put("timeStamp", new Time(660, TimeUnit.BASE_SECOND));
        table.addRowByColumnIds(map);

        for (Row dataRecord : table)
        {
            for (int column = 0; column < table.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }
        for (Row dataRecord : table)
        {
            System.out.println("timeStamp=" + dataRecord.getValue("timeStamp") + ", temperature="
                    + dataRecord.getValue("temperature") + ", " + dataRecord.getValue("remark"));
        }

        System.out.println("JSON");
        JsonData.writeData("C:/Temp/example.json", table);

        Table readBack = JsonData.readData("C:/Temp/example.json");
        for (Row dataRecord : readBack)
        {
            for (int column = 0; column < readBack.getNumberOfColumns(); column++)
            {
                System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
            }
        }

    }

}
