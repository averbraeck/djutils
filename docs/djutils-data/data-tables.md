# Using Data Tables

A new in-memory `Table` is created by creating one or more `Column` objects, organize them in a `List` and then calling the constructor of `ListTable` (which is - at this time - the only implementation of a `DataTable`):

```java
Column<Integer> timeStamp = new Column<>("timeStamp", "time rounded to nearest second", int.class, "");
Column<Double> temperature = new Column<>("temperature", "engine temperature in Celcius", double.class, "");
Column<String> remark = new Column<>("remark", "remark", String.class, "");
List<Column<?>> columns = List.of(timeStamp, temperature, remark);
ListTable table = new ListTable("engineTemperatureData", "engine temperature samples", columns);
```

One way to add a record is to provide an array with the values in the same order as used when constructing the table:

```java
Object[] rowData = new Object[] { 600, 18.0, "starting engine" };
table.addRow(rowData);
```

Another way uses a Map that maps the column names to values of the appropriate types:

```java
Map<String, Object> map = new HashMap<>();
map.put("remark", "leaving parking lot");
map.put("temperature", 28.5);
map.put("timeStamp", 660);
table.addRowByColumnIds(map);
```

The contents in the `Table` can only be accessed sequentially:

```java
for (Row row : table)
    for (int column = 0; column < table.getNumberOfColumns(); column++)
       System.out.println("column " + column + ": " + row.getValues()[column]);
```

This outputs:

<pre>
column 0: 600
column 1: 18.0
column 2: starting engine
column 0: 660
column 1: 28.5
column 2: leaving parking lot
</pre>
<br/>

The values within a `Row` can also be accessed by `id`:

```java
for (Row row : table)
    System.out.println("timeStamp=" + row.getValue("timeStamp") + ", temperature="
        + row.getValue("temperature") + ", " + row.getValue("remark"));
```

This outputs:

<pre>
timeStamp=600, temperature=18.0, starting engine
timeStamp=660, temperature=28.5, leaving parking lot
</pre>
<br/>

The in-memory table can be written to disk with code like:

```java
JsonData.writeData("C:/Temp/example.json", table);
```

This directory (C:/Temp) should exist and be writable for this to succeed (if not, an IOException will be thrown). When successful, a file is written that contains the following text:

```json
{
  "table": {
    "id": "engineTemperatureData",
    "description": "engine temperature samples",
    "class": "org.djutils.data.ListTable",
    "columns": [
      {
        "nr": 0,
        "id": "timeStamp",
        "description": "time rounded to nearest second",
        "type": "java.lang.Integer",
        "unit": ""
      },
      {
        "nr": 1,
        "id": "temperature",
        "description": "engine temperature in Celcius",
        "type": "java.lang.Double",
        "unit": ""
      },
      {
        "nr": 2,
        "id": "remark",
        "description": "remark",
        "type": "java.lang.String",
        "unit": ""
      }
    ]
  },
  "data": [
    [{"0":"600"},{"1":"18.0"},{"2":"starting engine"}],
    [{"0":"660"},{"1":"28.5"},{"2":"leaving parking lot"}]
  ]
}
```

If you know a little bit about JSON format, you will recognize that this file describes the data format, followed by two records with the data values. Floating point values are stored as strings. This is done to ensure that even `NaN` (Not a Number), `Infinity` and `-Infinity` (negative infinity) can be stored and retrieved.

This data can be read back with code like:

```java
Table readBack = JsonData.readData("C:/Temp/example.json");
```

Storing into and reading back from an XML file is very similar:

```java
XmlData.writeData("C:/Temp/example.xml", table);
        
Table readBack = XmlData.readData("C:/Temp/example.xml");
```

To store in CSV or TSV format, two file names must be provided because the meta-data can not be stored in the same file as the contents:

```java
CsvData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

Table readBack = CsvData.readData("C:/Temp/example.csv", "C:/Temp/example.csvm");
```
```java
TsvData.writeData("C:/Temp/example.tsv", "C:/Temp/example.tsvm", table);

Table readBack = TsvData.readData("C:/Temp/example.tsv", "C:/Temp/example.tsvm");
```

Many programs, including Microsoft Excel can open and read these CSV and TSV files, although many (including Excel) will not correctly parse `NaN`, `Infinity` and `-Infinity` values.


## Strongly typed quantities ###

The [DJUNITS project](https://djunits.org/manual/) implements strongly typed quantities that protect the programmer from mixing up times with speeds, etc. This project is fully compatible with strongly typed quantities. The only disadvantage is that the stored data files may not be so easily imported by other software. To create a table with strongly typed quantities, use code like:

```java
Column<Time> timeStamp = new Column<>("timeStamp", 
    "time rounded to nearest second", Time.class, "s");
Column<AbsoluteTemperature> temperature =
    new Column<>("temperature", "engine temperature in Celcius", 
        AbsoluteTemperature.class, "K");
Column<String> remark = new Column<>("remark", "remark", String.class, "");
List<Column<?>> columns = new ArrayList<>();
columns.add(timeStamp);
columns.add(temperature);
columns.add(remark);
ListTable table = new ListTable("engineTemperatureData", 
    "engine temperature samples", columns);
```

Beware that the `Time` type in this code is `org.djunits.value.vdouble.scalar.Time`. To put in some data use code like:

```java
Object[] record = new Object[] { 
        new Time(600, TimeUnit.BASE_SECOND),
        new AbsoluteTemperature(18.0, AbsoluteTemperatureUnit.DEGREE_CELSIUS),
        "starting engine" };
table.addRow(record);
```

or

```java
Map<String, Object> map = new HashMap<>();
map.put("remark", "leaving parking lot");
map.put("temperature", new AbsoluteTemperature(28.5, AbsoluteTemperatureUnit.DEGREE_CELSIUS));
map.put("timeStamp", new Time(660, TimeUnit.BASE_SECOND));
table.addRowByColumnIds(map);
```

The code that prints the contents of the in-memory table is exactly the same. The output differs slightly:

<pre>
column 0: 600.000000 s
column 1: 18.0000000 °C
column 2: starting engine
column 0: 660.000000 s
column 1: 28.5000000 °C
column 2: leaving parking lot
</pre>
<br/>

Please  notice that the printed values are now printed with their units.

When stored in a JSON file, there are corresponding differences in the column definitions and the data values:

```json
{
  "table": {
    "id": "engineTemperatureData",
    "description": "engine temperature samples",
    "class": "org.djutils.data.ListTable",
    "columns": [
      {
        "nr": 0,
        "id": "timeStamp",
        "description": "time rounded to nearest second",
        "type": "org.djunits.value.vdouble.scalar.Time",
        "unit": "s"
      },
      {
        "nr": 1,
        "id": "temperature",
        "description": "engine temperature in Celcius",
        "type": "org.djunits.value.vdouble.scalar.AbsoluteTemperature",
        "unit": "K"
      },
      {
        "nr": 2,
        "id": "remark",
        "description": "remark",
        "type": "java.lang.String",
        "unit": ""
      }
    ]
  },
  "data": [
    [{"0":"600.0"},{"1":"291.15"},{"2":"starting engine"}],
    [{"0":"660.0"},{"1":"301.65"},{"2":"leaving parking lot"}]
  ]
}
```

Similar differences occur when storing in XML, CSV, or TSV files. Of course, on read back, the reconstructed `Table` is exactly the same, regardless of the storage format used.
