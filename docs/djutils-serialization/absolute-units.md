# Absolute quantity, vector, matrix types with unit (#37 - #42)

## 37. Absolute float quantity with unit

The internal storage of the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte unit type (see the table in [Coding of units](../coding-units) ) and a one-byte display type (see  [Display types for units](../display-types)). At the end of the object, the String representation of the reference point is added, with a proper prefix for the String and a length.

As an example: suppose the unit indicates that the type is a direction, whereas the display type indicates that the internally stored value 3.14159265 should be displayed as 180 degrees, relative to the EAST reference, this is coded as follows (assuming *big-endian* encoding). The display type `03` is used to keep the direction compatible with previous versions of djutils-serialization, but `01` could also be used.

<pre>
|37|04|03|0x40|0x49|0x0F|0xDB| 9 | 0 | 0 | 0 | 4 | E | A | S | T |
</pre>

In *little-endian* encoding, it looks as follows:

<pre>
|37|04|03|0xDB|0x0F|0x49|0x40| 9 | 0 | 0 | 0 | 4 | E | A | S | T |
</pre>



## 38. Absolute double quantity with unit

The internal storage of the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte unit type (see the table in [Coding of units](../coding-units) ) and a one-byte display type (see  [Display types for units](../display-types)). At the end of the object, the String representation of the reference point is added, with a proper prefix for the String and a length.

As an example: suppose the unit indicates that the type is a direction, whereas the display type indicates that the internally stored value 3.14159265 should be displayed as 180 degrees, relative to the NORTH reference, this is coded as follows (assuming *big-endian* encoding). The display type `01` is used to keep the direction compatible with previous versions of djutils-serialization.

<pre>
|38|04|01|0x40|0x09|0x21|0xFB|0x54|0x44|0x2D|0x18| 9 | 0 | 0 | 0 | 5 | N | O | R | T | H |
</pre>

In *little-endian* encoding, it looks as follows:

<pre>
|38|04|01|0x18|0x2D|0x44|0x54|0xFB|0x21|0x09|0x40| 9 | 0 | 0 | 0 | 5 | N | O | R | T | H |
</pre>
