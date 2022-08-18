# Using DataTables

A new in-memory `DataTable` is created by creating one or more `DataColumn` objects, organize them in a `List` and then calling the constructor of `ListDataTable` (which is - at this time - the only implementation of a `DataTable`):

```java
DataColumn<Integer> timeStamp = new SimpleDataColumn<>("timeStamp", "time rounded to nearest second", int.class);
DataColumn<Double> temperature = new SimpleDataColumn<>("temperature", "engine temperature in Celcius", double.class);
DataColumn<String> remark = new SimpleDataColumn<>("remark", "remark", String.class);
List<DataColumn<?>> columns = new ArrayList<>();
columns.add(timeStamp);
columns.add(temperature);
columns.add(remark);
ListDataTable table = new ListDataTable("engineTemperatureData", "engine temperature samples", columns);
```

One way to add a record is to provide an array with the values in the same order as used when constructing the table:

```java
Object[] record = new Object[] {600, 18.0, "starting engine"};
table.addRecord(record);
```

Another way uses a Map that maps the column names to values of the appropriate types:

```java
Map<String, Object> map = new HashMap<>();
map.put("remark", "leaving parking lot");
map.put("temperature", 28.5);
map.put("timeStamp", 660);
table.addRecordByColumnIds(map);
```

The contents in the `DataTable` can only be accessed sequentially:

```java
for (DataRecord dataRecord : table)
    for (int column = 0; column < table.getNumberOfColumns(); column++)
        System.out.println("column " + column + ": " + dataRecord.getValues()[column]);
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

The values within a `DataRecord` can also be accessed by `id`:

```java
for (DataRecord dataRecord : table)
    System.out.println("timeStamp=" + dataRecord.getValue("timeStamp") + ", temperature="
            + dataRecord.getValue("temperature") + ", " + dataRecord.getValue("remark"));
```

This outputs:

<pre>
timeStamp=600, temperature=18.0, starting engine
timeStamp=660, temperature=28.5, leaving parking lot
</pre>
<br/>

The in-memory table can be written to disk with code like:

```java
JSONData.writeData("C:/Temp/example.json", table);
```

This directory (C:/Temp) should exist and be writable for this to succeed (if not, an IOException will be thrown). When successful, a file is written that contains the following text:

```json
{
  "table": {
    "id": "engineTemperatureData",
    "description": "engine temperature samples",
    "class": "org.djutils.data.ListDataTable",
    "columns": [
      {
        "nr": 0,
        "id": "timeStamp",
        "description": "time rounded to nearest second",
        "type": "int"
      },
      {
        "nr": 1,
        "id": "temperature",
        "description": "engine temperature in Celcius",
        "type": "double"
      },
      {
        "nr": 2,
        "id": "remark",
        "description": "remark",
        "type": "java.lang.String"
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
DataTable readBack = JSONData.readData("C:/Temp/example.json");
```

Storing into and reading back from an XML file is very similar:

```java
XMLData.writeData("C:/Temp/example.xml", table);
        
DataTable readBack = XMLData.readData("C:/Temp/example.xml");
```

To store in CSV or TSV format, two file names must be provided because the meta-data can not be stored in the same file as the contents:

```java
CSVData.writeData("C:/Temp/example.csv", "C:/Temp/example.csvm", table);

DataTable readBack = CSVData.readData("C:/Temp/example.csv", "C:/Temp/example.csvm");
```
```java
TSVData.writeData("C:/Temp/example.tsv", "C:/Temp/example.tsvm", table);

DataTable readBack = TSVData.readData("C:/Temp/example.tsv", "C:/Temp/example.tsvm");
```

Many programs, including Microsoft Excel can open and read these CSV and TSV files, although many (including Excel) will not correctly parse `NaN`, `Infinity` and `-Infinity` values.


## Strongly typed quantities ###

The [DJUNITS project](https://djunits.org/manual/) implements strongly typed quantities that protect the programmer from mixing up times with speeds, etc. This project is fully compatible with those. The only disadvantage is that the stored data files may not be so easily imported by other software. To create a table with strongly typed quantities, use code like:

```java
DataColumn<Time> timeStamp = new SimpleDataColumn<>("timeStamp", 
        "time rounded to nearest second", Time.class);
DataColumn<AbsoluteTemperature> temperature = new SimpleDataColumn<>("temperature", 
        "engine temperature in Celcius", AbsoluteTemperature.class);
DataColumn<String> remark = new SimpleDataColumn<>("remark", "remark", String.class);
List<DataColumn<?>> columns = new ArrayList<>();
columns.add(timeStamp);
columns.add(temperature);
columns.add(remark);
ListDataTable table = new ListDataTable("engineTemperatureData", "engine temperature samples", columns);
```

Beware that the `Time` type in this code is `org.djunits.value.vdouble.scalar.Time`. To put in some data use code like:

```java
Object[] record = new Object[] { 
        new Time(600, TimeUnit.BASE_SECOND),
        new AbsoluteTemperature(18.0, AbsoluteTemperatureUnit.DEGREE_CELSIUS),
        "starting engine" };
table.addRecord(record);

Map<String, Object> map = new HashMap<>();
map.put("remark", "leaving parking lot");
map.put("temperature", new AbsoluteTemperature(28.5, AbsoluteTemperatureUnit.DEGREE_CELSIUS));
map.put("timeStamp", new Time(660, TimeUnit.BASE_SECOND));
table.addRecordByColumnIds(map);
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
    "class": "org.djutils.data.ListDataTable",
    "columns": [
      {
        "nr": 0,
        "id": "timeStamp",
        "description": "time rounded to nearest second",
        "type": "org.djunits.value.vdouble.scalar.Time"
      },
      {
        "nr": 1,
        "id": "temperature",
        "description": "engine temperature in Celcius",
        "type": "org.djunits.value.vdouble.scalar.AbsoluteTemperature"
      },
      {
        "nr": 2,
        "id": "remark",
        "description": "remark",
        "type": "java.lang.String"
      }
    ]
  },
  "data": [
    [{"0":"org.djunits.value.vdouble.scalar.Time#600.0 s"},{"1":"org.djunits.value.vdouble.scalar.AbsoluteTemperature#18.0 degC"},{"2":"starting engine"}],
    [{"0":"org.djunits.value.vdouble.scalar.Time#660.0 s"},{"1":"org.djunits.value.vdouble.scalar.AbsoluteTemperature#28.5 degC"},{"2":"leaving parking lot"}]
  ]
}
```

Similar differences occur when storing in XML, CSV, or TSV files. Of course, on read back, the reconstructed `DataTable` is exactly the same, regardless of the storage format used.
