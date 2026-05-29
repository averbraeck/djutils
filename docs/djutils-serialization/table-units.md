# Table types with unit (#31 - #32 and #43 - #44)

## 31a. Float matrix with unique units per column (big endian)

Dense float array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the byte
with value 31, the matrix types have a 32-bit int indicating the number of rows in the array that follows (i.e., the
number of elements per column vector), followed by a 32-bit int indicating the number of vectors (i.e, the number of
columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1
follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see  [Unit types](../unit-types)). Then the quantity type and unit type for column 2, etc. The internal storage of 
the values that are transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows:
     
<pre>
|31|  |R|O|W|S|  |V|E|C|S|
|UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
|R|1|V|1|  |R|1|V|2| ... |R|m|V|n| 
|R|2|V|1|  |R|2|V|2| ... |R|m|V|n| 
... 
|R|n|V|1|  |R|n|V|2| ... |R|m|V|n|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: matrix\[row]\[col]. This data type is ideal for, for instance, sending a time series of values, where column 1
indicates the time, and column 2 the value. 

Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and dimensionless values 
v = {20.0, 40.0, 50.0, 60.0}, then the coding is as follows: (assuming *big-endian* encoding):

<pre>
|31| |0|0|0|4| |0|0|0|2|
|26|8| |0|0|
|0x3F|0x80|0x00|0x00| |0x41|0xA0|0x00|0x00|
|0x40|0x00|0x00|0x00| |0x42|0x20|0x00|0x00|
|0x40|0x00|0x40|0x00| |0x42|0x48|0x00|0x00|
|0x40|0x80|0x00|0x00| |0x42|0x70|0x00|0x00|
</pre>

<br>


## 31b. Float matrix with unique units per column (little endian)

Dense float array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the byte
with value 31, the matrix types have a 32-bit little endian int indicating the number of rows in the array that follows (i.e., the
number of elements per column vector), followed by a 32-bit little endian int indicating the number of vectors (i.e, the number of
columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1
follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see  [Unit types](../unit-types)). Then the quantity type and unit type for column 2, etc. The internal storage of 
the little endian float values that are transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows:
     
<pre>
|31|  |R|O|W|S|  |V|E|C|S|
|UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
|R|1|V|1|  |R|1|V|2| ... |R|m|V|n| 
|R|2|V|1|  |R|2|V|2| ... |R|m|V|n| 
... 
|R|n|V|1|  |R|n|V|2| ... |R|m|V|n|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: matrix\[row]\[col]. This data type is ideal for, for instance, sending a time series of values, where column 1
indicates the time, and column 2 the value. 

Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and dimensionless values 
v = {20.0, 40.0, 50.0, 60.0}, then the coding is as follows: (assuming *little-endian* encoding):

<pre>
|31| |4|0|0|0| |2|0|0|0|
|26|8| |0|0|
|0x00|0x00|0x80|0x3F| |0x00|0x00|0xA0|0x41|
|0x00|0x00|0x00|0x40| |0x00|0x00|0x20|0x42|
|0x00|0x40|0x00|0x40| |0x00|0x00|0x48|0x42|
|0x00|0x00|0x80|0x40| |0x00|0x00|0x70|0x42|
</pre>

<br>


## 32a. Double matrix with unique units per column (big endian)

Dense double array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the
byte with value 32, the matrix types have a 32-bit int indicating the number of rows in the array that follows (i.e., the
number of elements per column vector), followed by a 32-bit int indicating the number of vectors (i.e, the number of
columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1
follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see  [Unit types](../unit-types)). Then the quantity type and unit type for column 2, etc. The internal storage of 
the double values that are transmitted next, always use the SI (or standard) unit. Summarized, the coding is as follows:

<pre>
|32|  |R|O|W|S|  |V|E|C|S|
|UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
|R|1|V|1|.|.|.|.|  |R|1|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
|R|2|V|1|.|.|.|.|  |R|2|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
... 
|R|n|V|1|.|.|.|.|  |R|n|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: matrix\[row]\[col]. This data type is ideal for, for instance, sending a time series of values, where column 1
indicates the time, and column 2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010,
2011, 2012, 2013} and areas in acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows (assuming *big-endian* encoding):

<pre>
|32|  |0|0|0|4|  |0|0|0|2|
|0|0|  |5|18|
|0x40|0x9F|0x68|0x00|0x00|0x00|0x00|0x00|
|0x40|0x79|0xFB|0x33|0x33|0x33|0x33|0x33|
|0x40|0x9F|0x6C|0x00|0x00|0x00|0x00|0x00|
|0x40|0x7A|0x76|0x66|0x66|0x66|0x66|0x66|
|0x40|0x9F|0x70|0x00|0x00|0x00|0x00|0x00|
|0x40|0x7A|0xC0|0x00|0x00|0x00|0x00|0x00|
|0x40|0x9F|0x74|0x00|0x00|0x00|0x00|0x00|
|0x40|0x7A|0x91|0x99|0x99|0x99|0x99|0x9A|
</pre>

<br>


## 32b. Double matrix with unique units per column (little endian)

Dense double array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the
byte with value 32, the matrix types have a 32-bit little-endian int indicating the number of rows in the array that follows 
(i.e., the number of elements per column vector), followed by a 32-bit little-endian int indicating the number of vectors 
(i.e, the number of columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity 
type for column 1 follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see  [Unit types](../unit-types)). Then the quantity type and unit type for column 2, etc. The internal storage of 
the double-precision little-endian values that are transmitted next, always use the SI (or standard) unit. 
Summarized, the coding is as follows:

<pre>
|32|  |R|O|W|S|  |V|E|C|S|
|UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
|R|1|V|1|.|.|.|.|  |R|1|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
|R|2|V|1|.|.|.|.|  |R|2|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
... 
|R|n|V|1|.|.|.|.|  |R|n|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: matrix\[row]\[col]. This data type is ideal for, for instance, sending a time series of values, where column 1
indicates the time, and column 2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010,
2011, 2012, 2013} and areas in acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows 
(assuming *little-endian* encoding):

<pre>
|32| |4|0|0|0| |2|0|0|0|
|0|0|  |5|18|
|0x00|0x00|0x00|0x00|0x00|0x68|0x9F|0x40|
|0x33|0x33|0x33|0x33|0x33|0xFB|0x79|0x40|
|0x00|0x00|0x00|0x00|0x00|0x6C|0x9F|0x40|
|0x66|0x66|0x66|0x66|0x66|0x76|0x7A|0x40|
|0x00|0x00|0x00|0x00|0x00|0x70|0x9F|0x40|
|0x00|0x00|0x00|0x00|0x00|0xC0|0x7A|0x40|
|0x00|0x00|0x00|0x00|0x00|0x74|0x9F|0x40|
|0x9A|0x99|0x99|0x99|0x99|0x91|0x7A|0x40|
</pre>

<br>

## 43a. Float matrix with unique units per row (big endian)

Dense float matrix, stored internally in the SI unit, with a unique quantity type and unit type per row. This is the
transposed version of type 31. After the byte with value 43, the matrix types have a 32-bit int indicating the number of
vectors in the array that follows, followed by a 32-bit int indicating the number of elements per vector. These integers
are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1 follows (see the table 
in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 (see [Unit types](../unit-types)), 
followed by the data for the vector as 4-byte float values. Then the quantity type, unit type and data for vector 2, etc. 
The internal storage of the values that are transmitted after that always use the SI (or standard) unit. 
Summarized, the coding is as follows:

<pre>
|43|  |V|E|C|S|  |C|O|L|S|
|UT1|DT1| |V|1|C|1|  |V|1|C|2| ... |V|1|C|n| 
|UT2|DT2| |V|2|C|1|  |V|2|C|2| ... |V|2|C|n| 
... 
|UTn|DTn| |V|m|C|1|  |V|m|C|2| ... |V|m|C|n|
</pre>

This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
2 the value. Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and dimensionless values v = {20.0,
40.0, 50.0, 60.0}, then the coding is as follows:

<pre>
|31|    |0|0|0|2|  |0|0|0|4|
|26|8|  |0x3F|0x80|0x00|0x00|  |0x40|0x00|0x00|0x00|  |0x40|0x00|0x40|0x00|  |0x40|0x80|0x00|0x00| 
|0|0|   |0x41|0xA0|0x00|0x00|  |0x42|0x20|0x00|0x00|  |0x42|0x48|0x00|0x00|  |0x42|0x70|0x00|0x00|
</pre>

Note that this type is the transposed version of type 31, and can easily transfer a <code>Vector[]</code> array where
each vector has its own quantity and unit.

This type has been added in djutils version 2.5

<br>

## 43b. Float matrix with unique units per row (little endian)

Dense float matrix, stored internally in the SI unit, with a unique quantity type and unit type per row. This is the
transposed version of type 31. After the byte with value 43, the matrix types have a 32-bit little-endian int indicating 
the number of vectors in the array that follows, followed by a 32-bit little-endian int indicating the number of elements 
per vector. These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for 
column 1 follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see [Unit types](../unit-types)), followed by the data for the vector as 4-byte little-endian float values. Then the 
quantity type, unit type and data for vector 2, etc. The internal storage of the values that are transmitted after 
that always use the SI (or standard) unit. Summarized, the coding is as follows:

<pre>
|43|  |V|E|C|S|  |C|O|L|S|
|UT1|DT1| |V|1|C|1|  |V|1|C|2| ... |V|1|C|n| 
|UT2|DT2| |V|2|C|1|  |V|2|C|2| ... |V|2|C|n| 
... 
|UTn|DTn| |V|m|C|1|  |V|m|C|2| ... |V|m|C|n|
</pre>

This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
2 the value. Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and dimensionless values v = {20.0,
40.0, 50.0, 60.0}, then the coding is as follows:

<pre>
|31|    |2|0|0|0|  |4|0|0|0|
|26|8|  |0x00|0x00|0x80|0x3F|  |0x00|0x00|0x00|0x40|  |0x00|0x40|0x00|0x40|  |0x00|0x00|0x80|0x40| 
|0|0|   |0x41|0xA0|0x00|0x00|  |0x42|0x20|0x00|0x00|  |0x42|0x48|0x00|0x00|  |0x42|0x70|0x00|0x00|
|0|0|   |0x41|0xA0|0x00|0x00|  |0x42|0x20|0x00|0x00|  |0x42|0x48|0x00|0x00|  |0x42|0x70|0x00|0x00|
</pre>

Note that this type is the transposed version of type 31, and can easily transfer a <code>Vector[]</code> array where
each vector has its own quantity and unit.

This type has been added in djutils version 2.5

<br>

## 44a. Double matrix with unique units per row (big endian)

Dense double matrix, stored internally in the SI unit, with a unique quantity type and unit type per row. This is the
transposed version of type 32. After the byte with value 44, the matrix types have a 32-bit int indicating the number of
vectors in the array that follows, followed by a 32-bit int indicating the number of elements per vector. These integers
are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1 follows (see the table 
in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 (see [Unit types](../unit-types)), 
followed by the data for the vector as 8-byte double values. Then the quantity type, unit type and data for vector 2, etc. 
The internal storage of the values that are transmitted after that always use the SI (or standard) unit. 
Summarized, the coding is as follows:

<pre>
|44|  |V|E|C|S|  |C|O|L|S|
|UT1|DT1|  |V|1|C|1|.|.|.|.|  |V|1|C|2|.|.|.|.| ... |V|1|C|n|.|.|.|.| 
|UT1|DT1|  |V|2|C|1|.|.|.|.|  |V|2|C|2|.|.|.|.| ... |V|2|C|n|.|.|.|.| 
... 
|UTn|DTn|  |V|m|C|1|.|.|.|.|  |V|m|C|2|.|.|.|.| ... |V|m|C|n|.|.|.|.|
</pre>

This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010, 2011, 2012, 2013} and areas in
acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows:

<pre>
|32|    |0|0|0|4|  |0|0|0|2|
|0|0|   |0x40|0x9F|0x68|0x00|0x00|0x00|0x00|0x00| |0x40|0x9F|0x6C|0x00|0x00|0x00|0x00|0x00|
        |0x40|0x9F|0x70|0x00|0x00|0x00|0x00|0x00| |0x40|0x9F|0x74|0x00|0x00|0x00|0x00|0x00|
|5|18|  |0x40|0x79|0xFB|0x33|0x33|0x33|0x33|0x33| |0x40|0x7A|0x76|0x66|0x66|0x66|0x66|0x66|
        |0x40|0x7A|0xC0|0x00|0x00|0x00|0x00|0x00| |0x40|0x7A|0x91|0x99|0x99|0x99|0x99|0x9A|
</pre>

Note that this type is the transposed version of type 32, and can easily transfer a <code>Vector[]</code> array where
each vector has its own quantity and unit.

This type has been added in djutils version 2.5

<br>

## 43b. Double matrix with unique units per row (little endian)

Dense double matrix, stored internally in the SI unit, with a unique quantity type and unit type per row. This is the
transposed version of type 32. After the byte with value 44, the matrix types have a 32-bit little-endian int indicating 
the number of vectors in the array that follows, followed by a 32-bit little-endian int indicating the number of elements 
per vector. These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for 
column 1 follows (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte unit type for column 1 
(see [Unit types](../unit-types)), followed by the data for the vector as 8-byte little-endian double values. Then the 
quantity type, unit type and data for vector 2, etc. The internal storage of the values that are transmitted after 
that always use the SI (or standard) unit. Summarized, the coding is as follows:

<pre>
|44|  |V|E|C|S|  |C|O|L|S|
|UT1|DT1|  |V|1|C|1|.|.|.|.|  |V|1|C|2|.|.|.|.| ... |V|1|C|n|.|.|.|.| 
|UT1|DT1|  |V|2|C|1|.|.|.|.|  |V|2|C|2|.|.|.|.| ... |V|2|C|n|.|.|.|.| 
... 
|UTn|DTn|  |V|m|C|1|.|.|.|.|  |V|m|C|2|.|.|.|.| ... |V|m|C|n|.|.|.|.|
</pre>

This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010, 2011, 2012, 2013} and areas in
acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows:

<pre>
|32|    |4|0|0|0|  |2|0|0|0|
|0|0|   |0x00|0x00|0x00|0x00|0x00|0x68|0x9F|0x40| |0x00|0x00|0x00|0x00|0x00|0x6C|0x9F|0x40|
        |0x00|0x00|0x00|0x00|0x00|0x70|0x9F|0x40| |0x00|0x00|0x00|0x00|0x00|0x74|0x9F|0x40|
|5|18|  |0x33|0x33|0x33|0x33|0x33|0xFB|0x79|0x40| |0x66|0x66|0x66|0x66|0x66|0x76|0x7A|0x40|
        |0x00|0x00|0x00|0x00|0x00|0xC0|0x7A|0x40| |0x9A|0x99|0x99|0x99|0x99|0x91|0x7A|0x40|
</pre>

Note that this type is the transposed version of type 32, and can easily transfer a <code>Vector[]</code> array where
each vector has its own quantity and unit.

This type has been added in djutils version 2.5

