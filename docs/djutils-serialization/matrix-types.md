# Matrix types (#18 - #24)

The matrix types are preceded by a 32-bit int indicating the number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by row, without a separator between the rows. A matrix with 2 rows and 3 columns of integers 1-2-4 6-7-8 is therefore coded as follows:

<pre>
|20|0|0|0|2|0|0|0|3|
|0|0|0|1|0|0|0|2|0|0|0|4|
|0|0|0|6|0|0|0|7|0|0|0|8|
</pre>

In this illustration, the 33 bytes have been divided over three lines for readability. The serialized data may not contain such line breaks.


## Big-endian matrix types

| code | name | description |
| ------ | ------- | -------------- |
| 18 | BYTE_8_MATRIX | Byte matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 19 | SHORT_16_MATRIX | Short matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 20 | INT_32_MATRIX | Integer matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 21 | LONG_64_MATRIX | Long matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 22 | FLOAT_32_MATRIX | Float matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 23 | DOUBLE_64_MATRIX | Double matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |
| 24 | BOOLEAN_8_MATRIX | Boolean matrix, preceded by a 32-bit big-endian number for row count and a 32-bit big-endian number for column count |



## Little-endian matrix types

| code | name | description |
| ------ | ------- | -------------- |
| 18) | BYTE_8_MATRIX | Byte matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count |
| 19 | SHORT_16_MATRIX | Short matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count. Each short is little-endian coded |
| 20 | INT_32_MATRIX | Integer matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count. Each integer is little-endian coded |
| 21 | LONG_64_MATRIX | Long matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count. Each long is little-endian coded |
| 22 | FLOAT_32_MATRIX | Float matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count. Each float is little-endian coded |
| 23 | DOUBLE_64_MATRIX | Double matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count. Each double is little-endian coded |
| 24 | BOOLEAN_8_MATRIX | Boolean matrix, preceded by a 32-bit little-endian number for row count and a 32-bit little-endian number for column count |