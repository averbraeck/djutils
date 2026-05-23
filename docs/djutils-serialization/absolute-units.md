# Absolute quantity, vector, matrix types with unit (#37 - #42)

## 37. Absolute float quantity with unit

The internal storage of the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type (see [Unit types](../unit-types)). After that, the String representation of the reference point is added, with a proper prefix for the String and a length. Finally, the actual number expressed in the SI unit follows as a 4-byte float value.

As an example: suppose the unit indicates that the type is a direction, whereas the display type indicates that the internally stored value 3.14159265 should be displayed as 180 degrees, relative to the EAST reference, this is coded as follows (assuming *big-endian* encoding). The unit type `05` encodes degrees.

<pre>
|37|04|05|9|0|0|0|4|E|A|S|T|0x40|0x49|0x0F|0xDB|
</pre>

In *little-endian* encoding, it looks as follows:

<pre>
|37|04|05|9|4|0|0|0|E|A|S|T|0xDB|0x0F|0x49|0x40|
</pre>



## 38. Absolute double quantity with unit

The internal storage of the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type (see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type (see [Unit types](../unit-types)). After that, the String representation of the reference point is added, with a proper prefix for the String and a length. Finally, the actual number expressed in the SI unit follows as an 8-byte double value.

As an example: suppose the unit indicates that the type is a direction, whereas the display type indicates that the internally stored value 3.14159265 should be displayed as 180 degrees, relative to the NORTH reference, this is coded as follows (assuming *big-endian* encoding). The unit type `05` encodes degrees.

<pre>
|38|04|05|9|0|0|0|5|N|O|R|T|H|0x40|0x09|0x21|0xFB|0x54|0x44|0x2D|0x18|
</pre>

In *little-endian* encoding, it looks as follows:

<pre>
|38|04|05|9|5|0|0|0|N|O|R|T|H|0x18|0x2D|0x44|0x54|0xFB|0x21|0x09|0x40|
</pre>


## 39. Array of absolute float quantities with unit

Number-preceded dense float array of absolute quantity values, stored internally in the SI unit, with a quantity type, a
unit type and a String reference. After the byte with value 39, the array types have a 32-bit int indicating the number
of values in the array that follows. This int is itself not preceded by a byte indicating it is an int. Then a one-byte
quantity type follows and a one-byte unit type. After that, the reference is stored as a UTF-8 or UTF-16 String, preceded
by code 9 or 10 to indicate the type of string. The internal storage of the values that are transmitted after that always
use the SI (or standard) unit. As an example: when we send an array of two absolute times, 2.0 minutes and 2.5 minutes,
relative to the UNIX epoch, this is coded as follows:

<pre>
|39|0|0|0|2|26|7|9|0|0|0|4|U|N|I|X|
|0x40|0x00|0x00|0x00|
|0x40|0x20|0x00|0x00|
</pre>


*little-endian* encoding

Dense float array, preceded by a little-endian 32-bit number indicating the number of floats, with quantity type, unit
type and reference string attached to the entire float array. Each float is stored in little-endian order.

     
## 40. Array of absolute double quantities with unit

Number-preceded dense double array of absolute quantity values, stored internally in the SI unit, with a quantity type, a
unit type and a String reference. After the byte with value 40, the array types have a 32-bit int indicating the number
of values in the array that follows. This int is itself not preceded by a byte indicating it is an int. Then a one-byte
quantity type follows and a one-byte unit type. After that, the reference is stored as a UTF-8 or UTF-16 String, preceded
by code 9 or 10 to indicate the type of string. The internal storage of the values that are transmitted after that always
use the SI (or standard) unit. As an example: when we send an array of two absolute times, 21.2 minutes and 21.5 minutes,
relative to the UNIX epoch, this is coded as follows:

<pre>
|40|0|0|0|2|26|7|9|0|0|0|4|U|N|I|X|
|0x40|0x35|0x33|0x33|0x33|0x33|0x33|0x33|
|0x40|0x35|0x80|0x00|0x00|0x00|0x00|0x00|
</pre>


*little-endian* encoding

Dense double array, preceded by a little-endian 32-bit number indicating the number of doubles, little-endian order, with
quantity type, unit type and reference string attached to the entire double array. Each double is stored in little-endian
order.


## 41. Matrix of absolute float quantities with unit

Rows/Cols-preceded dense float array of absolute quantity values, stored internally in the SI unit, with a quantity type,
a unit type and a String reference. After the byte with value 41, the matrix types have a 32-bit int indicating the
number of rows in the array that follows, followed by a 32-bit int indicating the number of columns. These integers are
not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type. After
that, the reference is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10 to indicate the type of string. The
internal storage of the values that are transmitted after that always use the SI (or standard) unit. Summarized, the
coding is as follows:

<pre>
|41|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|  |9|0|0|0|9|r|e|f|e|r|e|n|c|e|
|R|1|C|1|  |R|1|C|2| ... |R|1|C|n| 
|R|2|C|1|  |R|2|C|2| ... |R|2|C|n| 
... 
|R|m|C|1|  |R|m|C|2| ... |R|m|C|n|
</pre>

In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: `matrix[row][col]`.


*little-endian* encoding

Dense float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
quantity type, unit type and reference string attached to the entire float matrix. Each float is stored in little-endian
order.


## 42. Matrix of absolute double quantities with unit

Rows/Cols-preceded dense double array of absolute quantity values, stored internally in the SI unit, with a quantity
type, a unit type and a String reference. After the byte with value 42, the matrix types have a 32-bit int indicating the
number of rows in the array that follows, followed by a 32-bit int indicating the number of columns. These integers are
not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type. After
that, the reference is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10 to indicate the type of string. The
internal storage of the values that are transmitted after that always use the SI (or standard) unit. Summarized, the
coding is as follows:

<pre>
|42|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|  |9|0|0|0|9|r|e|f|e|r|e|n|c|e|
|R|1|C|1|.|.|.|.|  |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.| 
|R|2|C|1|.|.|.|.|  |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.| 
... 
|R|m|C|1|.|.|.|.|  |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
</pre>

In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
index: `matrix[row][col]`.


*little-endian* encoding

Dense double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
quantity type, unit type and reference string attached to the entire double matrix. Each double is stored in
little-endian order.
