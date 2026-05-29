package org.djutils.serialization;

/**
 * Type numbers to encode different data types within djutils-serialization.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public final class FieldTypes
{

    /**
     * <b>Big endian and Little endian encoding</b> <br>
     * Byte, 8 bit signed two's complement integer.
     */
    public static final byte BYTE_8 = 0;

    /**
     * <b>Big endian encoding</b> <br>
     * Short, 16 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Short, 16 bit signed two's complement integer, little endian order.
     */
    public static final byte SHORT_16 = 1;

    /**
     * <b>Big endian encoding</b> <br>
     * Integer, 32 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Integer, 32 bit signed two's complement integer, little endian order.
     */
    public static final byte INT_32 = 2;

    /**
     * <b>Big endian encoding</b> <br>
     * Long, 64 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Long, 64 bit signed two's complement integer, little endian order.
     */
    public static final byte LONG_64 = 3;

    /**
     * <b>Big endian encoding</b> <br>
     * Float, single-precision 32-bit IEEE 754 floating point.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Float, single-precision 32-bit IEEE 754 floating point, little endian order.
     */
    public static final byte FLOAT_32 = 4;

    /**
     * <b>Big endian encoding</b> <br>
     * Float, double-precision 64-bit IEEE 754 floating point.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Float, double-precision 64-bit IEEE 754 floating point, little endian order.
     */
    public static final byte DOUBLE_64 = 5;

    /**
     * <b>Big endian and Little endian encoding</b> <br>
     * Boolean, sent / received as a byte; 0 = false, 1 = true.
     */
    public static final byte BOOLEAN_8 = 6;

    /**
     * <b>Big endian and Little endian encoding</b> <br>
     * Char, 8-bit ASCII character. Note that not all characters can be represented in 8 bits.
     */
    public static final byte CHAR_8 = 7;

    /**
     * <b>Big endian encoding</b> <br>
     * Char, 16-bit Unicode character, big endian order. Note that not all characters can be represented in two bytes using
     * UTF-16.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Char, 16-bit Unicode character, little-endian order for the 2 bytes.
     */
    public static final byte CHAR_16 = 8;

    /**
     * <b>Big endian encoding</b> <br>
     * String, number-preceded byte array of 8-bits characters. The string types are preceded by a 32-bit int indicating the
     * number of bytes in the array that follows. This int is itself not preceded by a byte indicating it is an int. An ASCII
     * string "Hello" is therefore coded as follows: |9|0|0|0|5|H|e|l|l|o|. Note that the int indicates the number of bytes, not
     * the number of characters. The string itself is coded with the first character at the start of the array and the last
     * character at the end.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * String, 32-bit little-endian number indicating the number of bytes, followed by a byte array of UTF-8 encoded characters.
     * Note that the length indicates the number of bytes in the encoding, not the number of characters in the string. The
     * string itself is coded with the first character at the start of the array and the last character at the end.
     */
    public static final byte STRING_UTF8 = 9;

    /**
     * <b>Big endian encoding</b> <br>
     * String, number-preceded char array of 16-bits characters, big-endian order. The string types are preceded by a 32-bit int
     * indicating the number of shorts in the array that follows. This int is itself not preceded by a byte indicating it is an
     * int. Note that the int indicates the number of shorts, not the number of characters; the number of bytes is therefore
     * equal to 2 * length. The string itself is coded with the first character at the start of the array and the last character
     * at the end.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * String, 32-bit little-endian number indicating the number of shorts in the encoding, followed by a byte array of UTF-16
     * encoded characters. Each 2-byte character is represented in little-endian order. Note that the length indicates the
     * number of shorts (2 bytes) in the encoding, not the number of characters in the string. The number of bytes to represent
     * the string in the encoding is therefore equal to 2 * length. The string itself is coded with the first character at the
     * start of the array and the last character at the end.
     */
    public static final byte STRING_UTF16 = 10;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded byte array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 8 bytes with numbers 1
     * through 8 is therefore coded as follows: |11|0|0|0|8|1|2|3|4|5|6|7|8|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Byte array, preceded by a 32-bit little-endian number indicating the number of bytes.
     */
    public static final byte BYTE_8_ARRAY = 11;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded short array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 8 shorts with numbers 100
     * through 107 is therefore coded as follows: |12|0|0|0|8|0|100|0|101|0|102|0|103|0|104|0|105|0|106|0|107|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Short array, preceded by a 32-bit little-endian number indicating the number of shorts, little-endian coded shorts.
     */
    public static final byte SHORT_16_ARRAY = 12;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded int array. The array types are preceded by a 32-bit int indicating the number of values in the array that
     * follows. This int is itself not preceded by a byte indicating it is an int. An array of 4 ints with numbers 100 through
     * 103 is therefore coded as follows: |13|0|0|0|4|0|0|0|100|0|0|0|101|0|0|0|102|0|0|0|103|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Integer array, preceded by a 32-bit little-endian number indicating the number of integers, little-endian coded ints.
     */
    public static final byte INT_32_ARRAY = 13;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded long array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 3 longs with numbers 100
     * through 102 is therefore coded as follows: |14|0|0|0|3|0|0|0|0|0|0|0|100|0|0|0|0|0|0|0|101|0|0|0|0|0|0|0|102|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Long array, preceded by a 32-bit little-endian number indicating the number of longs, little-endian coded longs.
     */
    public static final byte LONG_64_ARRAY = 14;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded float array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Float array, preceded by a 32-bit little-endian number indicating the number of floats, little-endian coded floats.
     */
    public static final byte FLOAT_32_ARRAY = 15;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded double array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Double array, preceded by a 32-bit little-endian number indicating the number of doubles, little-endian coded doubles.
     */
    public static final byte DOUBLE_64_ARRAY = 16;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded boolean array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Boolean array, preceded by a 32-bit little-endian number indicating the number of booleans.
     */
    public static final byte BOOLEAN_8_ARRAY = 17;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded byte matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of bytes 1-2-4 6-7-8 is therefore coded as
     * follows: |18|0|0|0|2|0|0|0|3|0|1|0|2|0|4|0|6|0|7|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Byte matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count.
     */
    public static final byte BYTE_8_MATRIX = 18;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded short matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of shorts 1-2-4 6-7-8 is therefore coded as
     * follows: |19|0|0|0|2|0|0|0|3|1|2|4|6|7|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Short matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded shorts.
     */
    public static final byte SHORT_16_MATRIX = 19;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded int matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of integers 1-2-4 6-7-8 is therefore coded
     * as follows: |20|0|0|0|2|0|0|0|3|0|0|0|1|0|0|0|2|0|0|0|4|0|0|0|6|0|0|0|7|0|0|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * <p>
     * Integer matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded ints.
     */
    public static final byte INT_32_MATRIX = 20;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded long matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of long vales 1-2-4 6-7-8 is therefore
     * coded as follows:
     * |21|0|0|0|2|0|0|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|4|0|0|0|0|0|0|0|6|0|0|0|0|0|0|0|7|0|0|0|0|0|0|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Long matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded longs.
     */
    public static final byte LONG_64_MATRIX = 21;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded float matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Float matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded floats.
     */
    public static final byte FLOAT_32_MATRIX = 22;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded double matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Double matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian doubles.
     */
    public static final byte DOUBLE_64_MATRIX = 23;

    /**
     * <b>Big endian encoding</b> <br>
     * Number of rows and number of columns preceded boolean matrix. The matrix types are preceded by a 32-bit int indicating
     * the number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Boolean matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count.
     */
    public static final byte BOOLEAN_8_MATRIX = 24;

    /**
     * <b>Big endian encoding</b> <br>
     * Float, stored internally in the SI unit, with a quantity type and unit type attached. The internal storage of the value
     * that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type, and a
     * one-byte unit type. As an example: suppose the quantity indicates that the type is a length, whereas the display type
     * indicates that the internally stored value 60000.0 should be displayed as 60.0 km, this is coded as follows:
     * |25|16|11|0x47|0x6A|0x60|0x00|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Float stored internally as a little-endian float in the corresponding SI unit, with quantity type and unit type attached.
     * The total size of the object is 7 bytes.
     */
    public static final byte FLOAT_32_UNIT = 25;

    /**
     * <b>Big endian encoding</b> <br>
     * Double, stored internally in the SI unit, with a quantity type and unit type attached. The internal storage of the value
     * that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type and a
     * one-byte display type. As an example: suppose the quantity indicates that the type is a length, whereas the display type
     * indicates that the internally stored value 60000.0 should be displayed as 60.0 km, this is coded as follows:
     * |26|16|11|0x47|0x6A|0x60|0x00|0x00|0x00|0x00|0x00|
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Double stored internally as a little-endian double in the corresponding SI unit, with quantity type and unit type
     * attached. The total size of the object is 11 bytes.
     */
    public static final byte DOUBLE_64_UNIT = 26;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded dense float array, stored internally in the SI unit, with a quantity type and unit type. After the byte
     * with value 27, the array types have a 32-bit int indicating the number of values in the array that follows. This int is
     * itself not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type.
     * The internal storage of the values that are transmitted after that always use the SI (or standard) unit. As an example:
     * when we send an array of two durations, 2.0 minutes and 2.5 minutes, this is coded as follows:
     * 
     * <pre>
     * |27|0|0|0|2|25|7|
     * |0x40|0x00|0x00|0x00|
     * |0x40|0x20|0x00|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Dense float array, preceded by a little-endian 32-bit number indicating the number of floats, with quantity type and unit
     * type attached to the entire float array. Each float is stored in little-endian order.
     */
    public static final byte FLOAT_32_UNIT_ARRAY = 27;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded dense double array, stored internally in the SI unit, with a quantity type and unit type. After the byte
     * with value 28, the array types have a 32-bit int indicating the number of values in the array that follows. This int is
     * itself not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type.
     * The internal storage of the values that are transmitted after that always use the SI (or standard) unit. As an example:
     * when we send an array of two durations, 21.2 minutes and 21.5 minutes, this is coded as follows:
     * 
     * <pre>
     * |28|0|0|0|2|25|7|
     * |0x40|0x35|0x33|0x33|0x33|0x33|0x33|0x33|
     * |0x40|0x35|0x80|0x00|0x00|0x00|0x00|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Dense double array, preceded by a little-endian 32-bit number indicating the number of doubles, little-endian order, with
     * quantity type and unit type attached to the entire double array. Each double is stored in little-endian order.
     */
    public static final byte DOUBLE_64_UNIT_ARRAY = 28;

    /**
     * <b>Big endian encoding</b> <br>
     * Rows/Cols-preceded dense float array, stored internally in the SI unit, with a quantity type and unit type. After the
     * byte with value 29, the matrix types have a 32-bit int indicating the number of rows in the array that follows, followed
     * by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an int. Then
     * a one-byte quantity type follows and a one-byte unit type The internal storage of the values that are transmitted after
     * that always use the SI (or standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |29|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|
     * |R|1|C|1|  |R|1|C|2| ... |R|1|C|n| 
     * |R|2|C|1|  |R|2|C|2| ... |R|2|C|n| 
     * ... 
     * |R|m|C|1|  |R|m|C|2| ... |R|m|C|n|
     * </pre>
     * 
     * In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * quantity type and unit type attached to the entire float matrix. Each float is stored in little-endian order.
     */
    public static final byte FLOAT_32_UNIT_MATRIX = 29;

    /**
     * <b>Big endian encoding</b> <br>
     * Rows/Cols-preceded dense double array, stored internally in the SI unit, with a quantity type and unit type. After the
     * byte with value 30, the matrix types have a 32-bit int indicating the number of rows in the array that follows, followed
     * by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an int. Then
     * a one-byte quantity type follows and a one-byte unit type The internal storage of the values that are transmitted after
     * that always use the SI (or standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |30|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|
     * |R|1|C|1|.|.|.|.|  |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.| 
     * |R|2|C|1|.|.|.|.|  |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.| 
     * ... 
     * |R|m|C|1|.|.|.|.|  |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
     * </pre>
     * 
     * In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * quantity type and unit type attached to the entire double matrix. Each double is stored in little-endian order.
     */
    public static final byte DOUBLE_64_UNIT_MATRIX = 30;

    /**
     * <b>Big endian encoding</b> <br>
     * Dense float array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the byte
     * with value 31, the matrix types have a 32-bit int indicating the number of rows in the array that follows (i.e., the
     * number of elements per column vector), followed by a 32-bit int indicating the number of vectors (i.e, the number of
     * columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1
     * follows (see {@link QuantityType}) and a one-byte unit type for column 1 (see {@link UnitType}). Then the quantity type
     * and unit type for column 2, etc. The internal storage of the values that are transmitted after that always use the SI (or
     * standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |31|  |R|O|W|S|  |V|E|C|S|
     * |UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
     * |R|1|V|1|  |R|1|V|2| ... |R|m|V|n| 
     * |R|2|V|1|  |R|2|V|2| ... |R|m|V|n| 
     * ... 
     * |R|n|V|1|  |R|n|V|2| ... |R|m|V|n|
     * </pre>
     * 
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col]. This data type is ideal for, for instance, sending a time series of values, where column 1
     * indicates the time, and column 2 the value. Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and
     * dimensionless values v = {20.0, 40.0, 50.0, 60.0}, then the coding is as follows:
     * 
     * <pre>
     * |31|  |0|0|0|4|  |0|0|0|2|
     * |26|8|  |0|0|
     * |0x3F|0x80|0x00|0x00|  |0x41|0xA0|0x00|0x00|
     * |0x40|0x00|0x00|0x00|  |0x42|0x20|0x00|0x00|
     * |0x40|0x00|0x40|0x00|  |0x42|0x48|0x00|0x00|
     * |0x40|0x80|0x00|0x00|  |0x42|0x70|0x00|0x00|
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense little-endian float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column
     * count int, with a unique quantity type and unit type per column of the float matrix.
     */
    public static final byte FLOAT_32_UNIT_COLUMN_MATRIX = 31;

    /**
     * <b>Big endian encoding</b> <br>
     * Dense double array, stored internally in the SI unit, with a unique quantity type and unit type per column. After the
     * byte with value 32, the matrix types have a 32-bit int indicating the number of rows in the array that follows (i.e., the
     * number of elements per column vector), followed by a 32-bit int indicating the number of vectors (i.e, the number of
     * columns). These integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for column 1
     * follows (see {@link QuantityType}) and a one-byte unit type for column 1 (see {@link UnitType}). Then the quantity type
     * and unit type for column 2, etc. The internal storage of the values that are transmitted after that always use the SI (or
     * standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |32|  |R|O|W|S|  |V|E|C|S|
     * |UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
     * |R|1|V|1|.|.|.|.|  |R|1|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
     * |R|2|V|1|.|.|.|.|  |R|2|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.| 
     * ... 
     * |R|n|V|1|.|.|.|.|  |R|n|V|2|.|.|.|.| ... |R|m|V|n|.|.|.|.|
     * </pre>
     * 
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col]. This data type is ideal for, for instance, sending a time series of values, where column 1
     * indicates the time, and column 2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010,
     * 2011, 2012, 2013} and areas in acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows:
     * 
     * <pre>
     * |32|  |0|0|0|4|  |0|0|0|2|
     * |0|0|  |5|18|
     * |0x40|0x9F|0x68|0x00|0x00|0x00|0x00|0x00|
     * |0x40|0x79|0xFB|0x33|0x33|0x33|0x33|0x33|
     * |0x40|0x9F|0x6C|0x00|0x00|0x00|0x00|0x00|
     * |0x40|0x7A|0x76|0x66|0x66|0x66|0x66|0x66|
     * |0x40|0x9F|0x70|0x00|0x00|0x00|0x00|0x00|
     * |0x40|0x7A|0xC0|0x00|0x00|0x00|0x00|0x00|
     * |0x40|0x9F|0x74|0x00|0x00|0x00|0x00|0x00|
     * |0x40|0x7A|0x91|0x99|0x99|0x99|0x99|0x9A|
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense little-endian double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column
     * count int, with a unique quantity type and unit type per column of the double matrix.
     */
    public static final byte DOUBLE_64_UNIT_COLUMN_MATRIX = 32;

    /**
     * <b>Big endian encoding</b> <br>
     * Array of UTF-8 Strings. The number of strings is provided in a 32-bit big-endian integer. Each string is preceded by a
     * 32-bit int indicating the number of bytes in the array that follows. This int is itself not preceded by a byte indicating
     * it is an int. Note that the int to code the length for each string indicates the number of bytes, not the number of
     * characters. As an example, coding two series for a graph is done as follows:
     * 
     * <pre>
     * | 33 | 0 | 0 | 0 | 2 | 
     * | 0 | 0 | 0 | 7 | S | e | r | i | e | s | 1 | 
     * | 0 | 0 | 0 | 7 | S | e | r | i | e | s | 2 |
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Little-endian array of UTF-8 Strings. The number of strings is provided in a 32-bit little-endian integer. Each string is
     * preceded by a 32-bit int indicating the number of bytes in the array that follows. This int is itself not preceded by a
     * byte indicating it is an int. Note that the int to code the length for each string indicates the number of bytes, not the
     * number of characters. As an example, coding two series for a graph is done as follows:
     * 
     * <pre>
     * | 33 | 2 | 0 | 0 | 0 | 
     * | 7 | 0 | 0 | 0 | S | e | r | i | e | s | 1 | 
     * | 7 | 0 | 0 | 0 | S | e | r | i | e | s | 2 |
     * </pre>
     */
    public static final byte STRING_UTF8_ARRAY = 33;

    /**
     * <b>Big endian encoding</b> <br>
     * Array of UTF-16 Strings. The number of strings is provided in a 32-bit big-endian integer. Each string is preceded by a
     * 32-bit int indicating the number of shorts (2-byte UTF-16 encoding) in the array that follows. This int is itself not
     * preceded by a byte indicating it is an int. Note that the int to code the length for each string indicates the number of
     * shorts, not the number of characters in the original string, nor the number of bytes. As an example, coding two series
     * for a graph is done as follows:
     * 
     * <pre>
     * |0x22|0x00|0x00|0x00|0x02|
     * |0x00|0x00|0x00|0x07|0x00|0x53|0x00|0x65|0x00|0x72|0x00|0x69|0x00|0x65|0x00|0x73|0x00|0x31|
     * |0x00|0x00|0x00|0x07|0x00|0x53|0x00|0x65|0x00|0x72|0x00|0x69|0x00|0x65|0x00|0x73|0x00|0x32|
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Little-endian array of UTF-16 Strings. The number of strings is provided in a 32-bit little-endian integer. Each string
     * is preceded by a 32-bit int indicating the number of shorts (2-byte UTF-16 encoding) in the array that follows. This int
     * is itself not preceded by a byte indicating it is an int. Note that the int to code the length for each string indicates
     * the number of shorts, not the number of characters in the original string, nor the number of bytes. As an example, coding
     * two series for a graph is done as follows:
     * 
     * <pre>
     * |0x22|0x02|0x00|0x00|0x00|
     * |0x07|0x00|0x00|0x00|0x53|0x00|0x65|0x00|0x72|0x00|0x69|0x00|0x65|0x00|0x73|0x00|0x31|0x00|
     * |0x07|0x00|0x00|0x00|0x53|0x00|0x65|0x00|0x72|0x00|0x69|0x00|0x65|0x00|0x73|0x00|0x32|0x00|
     * </pre>
     */
    public static final byte STRING_UTF16_ARRAY = 34;

    /**
     * <b>Big endian encoding</b> <br>
     * Matrix of UTF-8 Strings. First, the number of rows is provided in a 32-bit big-endian integer, followed by the number of
     * columns encoded in a 32-bit big-endian integer. Each string is preceded by a 32-bit int indicating the number of bytes in
     * the array that follows. This int is itself not preceded by a byte indicating it is an int. The strings are provided
     * row-by-row. Note that the int to code the length for each string indicates the number of bytes, not the number of
     * characters. In general, the coding is as follows:
     * 
     * <pre>
     * | 35 | R | O | W | S | C | O | L | S | 
     * |  0 | 0 | 0 | 4 | R | 1 | C | 1 | 
     * |  0 | 0 | 0 | 4 | R | 1 | C | 2 |
     * ...
     * |  0 | 0 | 0 | 4 | R | 1 | C | n | 
     * |  0 | 0 | 0 | 4 | R | 2 | C | 1 |
     * |  0 | 0 | 0 | 4 | R | 2 | C | 2 |
     * ...
     * |  0 | 0 | 0 | 4 | R | m | C | n |
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Little-endian matrix of UTF-8 Strings. First, the number of rows is provided in a 32-bit little-endian integer, followed
     * by the number of columns encoded in a 32-bit little-endian integer. Each string is preceded by a 32-bit int indicating
     * the number of bytes in the array that follows. This int is itself not preceded by a byte indicating it is an int. The
     * strings are provided row-by-row. Note that the int to code the length for each string indicates the number of bytes, not
     * the number of characters. In general, the coding is as follows:
     * 
     * <pre>
     * | 35 | R | O | W | S | C | O | L | S |
     * |  4 | 0 | 0 | 0 | R | 1 | C | 1 | 
     * |  4 | 0 | 0 | 0 | R | 1 | C | 2 |
     * ...
     * |  4 | 0 | 0 | 0 | R | 1 | C | n | 
     * |  4 | 0 | 0 | 0 | R | 2 | C | 1 |
     * |  4 | 0 | 0 | 0 | R | 2 | C | 2 |
     * ...
     * |  4 | 0 | 0 | 0 | R | m | C | n |
     * </pre>
     */
    public static final byte STRING_UTF8_MATRIX = 35;

    /**
     * <b>Big endian encoding</b> <br>
     * Matrix of UTF-16 Strings. First, the number of rows is provided in a 32-bit big-endian integer, followed by the number of
     * columns encoded in a 32-bit big-endian integer. Each string is preceded by a 32-bit int indicating the number of bytes in
     * the array that follows. This int is itself not preceded by a byte indicating it is an int. The strings are provided
     * row-by-row. Note that the int to code the length for each string indicates the number of shorts (2-bytes), not the number
     * of characters in the original string, nor the number of bytes in the encoding. In general, the coding is as follows:
     * 
     * <pre>
     * | 36 | R | O | W | S | C | O | L | S | 
     * |  0 | 0 | 0 | 4 | . | R | . | 1 | . | C | . | 1 | 
     * |  0 | 0 | 0 | 4 | . | R | . | 1 | . | C | . | 2 |
     * ...
     * |  0 | 0 | 0 | 4 | . | R | . | 1 | . | C | . | n | 
     * |  0 | 0 | 0 | 4 | . | R | . | 2 | . | C | . | 1 | 
     * |  0 | 0 | 0 | 4 | . | R | . | 2 | . | C | . | 2 | 
     * ...
     * |  0 | 0 | 0 | 4 | . | R | . | m | . | C | . | n |
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Little-endian matrix of UTF-16 Strings. First, the number of rows is provided in a 32-bit little-endian integer, followed
     * by the number of columns encoded in a 32-bit little-endian integer. Each string is preceded by a 32-bit int indicating
     * the number of bytes in the array that follows. This int is itself not preceded by a byte indicating it is an int. The
     * strings are provided row-by-row. Note that the int to code the length for each string indicates the number of shorts
     * (2-bytes), not the number of characters in the original string, nor the number of bytes in the encoding. In general, the
     * coding is as follows:
     * 
     * <pre>
     * | 36 | R | O | W | S | C | O | L | S | 
     * |  4 | 0 | 0 | 0 | R | . | 1 | . | C | . | 1 | . | 
     * |  4 | 0 | 0 | 0 | R | . | 1 | . | C | . | 2 | . |
     * ...
     * |  4 | 0 | 0 | 0 | R | . | 1 | . | C | . | n | . |
     * |  4 | 0 | 0 | 0 | R | . | 2 | . | C | . | 1 | . |
     * |  4 | 0 | 0 | 0 | R | . | 2 | . | C | . | 2 | . |
     * ...
     * |  4 | 0 | 0 | 0 | R | . | m | . | C | . | n | . |
     * </pre>
     */
    public static final byte STRING_UTF16_MATRIX = 36;

    /**
     * <b>Big endian encoding</b> <br>
     * Float, stored internally in the SI unit, with a quantity type, unit type and reference attached. The internal storage of
     * the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type,
     * and a one-byte unit type, as well as the reference that is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10
     * to indicate the type of string. As an example: suppose the quantity indicates that the type is a Position, where the
     * internally stored value 60000.0 should be displayed as 60.0 km, relative to the reference called "origin" that is coded
     * as UTF-8. this is endcoded as follows:
     * 
     * <pre>
     * |37|17|11|9|0|0|0|6|o|r|i|g|i|n|0x47|0x6A|0x60|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Float stored internally as a little-endian float in the corresponding SI unit, with quantity type, unit type and
     * reference attached. The total size of the object can vary due to the length of the reference string.
     */
    public static final byte FLOAT_32_UNIT_ABS = 37;

    /**
     * <b>Big endian encoding</b> <br>
     * Double, stored internally in the SI unit, with a quantity type, unit type and reference attached. The internal storage of
     * the value that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte quantity type,
     * and a one-byte unit type, as well as the reference that is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10
     * to indicate the type of string. As an example: suppose the quantity indicates that the type is a Position, where the
     * internally stored value 60000.0 should be displayed as 60.0 km, relative to the reference called "origin" that is coded
     * as UTF-8. this is endcoded as follows:
     * 
     * <pre>
     * |38|17|11|9|0|0|0|6|o|r|i|g|i|n|0x47|0x6A|0x60|0x00|0x00|0x00|0x00|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Double stored internally as a little-endian double in the corresponding SI unit, with quantity type, unit type and
     * reference attached. The total size of the object can vary due to the length of the reference string.
     */
    public static final byte DOUBLE_64_UNIT_ABS = 38;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded dense float array of absolute quantity values, stored internally in the SI unit, with a quantity type, a
     * unit type and a String reference. After the byte with value 39, the array types have a 32-bit int indicating the number
     * of values in the array that follows. This int is itself not preceded by a byte indicating it is an int. Then a one-byte
     * quantity type follows and a one-byte unit type. After that, the reference is stored as a UTF-8 or UTF-16 String, preceded
     * by code 9 or 10 to indicate the type of string. The internal storage of the values that are transmitted after that always
     * use the SI (or standard) unit. As an example: when we send an array of two absolute times, 2.0 minutes and 2.5 minutes,
     * relative to the UNIX epoch, this is coded as follows:
     * 
     * <pre>
     * |39|0|0|0|2|26|7|9|0|0|0|4|U|N|I|X|
     * |0x40|0x00|0x00|0x00|
     * |0x40|0x20|0x00|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Dense float array, preceded by a little-endian 32-bit number indicating the number of floats, with quantity type, unit
     * type and reference string attached to the entire float array. Each float is stored in little-endian order.
     */
    public static final byte FLOAT_32_UNIT_ABS_ARRAY = 39;

    /**
     * <b>Big endian encoding</b> <br>
     * Number-preceded dense double array of absolute quantity values, stored internally in the SI unit, with a quantity type, a
     * unit type and a String reference. After the byte with value 40, the array types have a 32-bit int indicating the number
     * of values in the array that follows. This int is itself not preceded by a byte indicating it is an int. Then a one-byte
     * quantity type follows and a one-byte unit type. After that, the reference is stored as a UTF-8 or UTF-16 String, preceded
     * by code 9 or 10 to indicate the type of string. The internal storage of the values that are transmitted after that always
     * use the SI (or standard) unit. As an example: when we send an array of two absolute times, 21.2 minutes and 21.5 minutes,
     * relative to the UNIX epoch, this is coded as follows:
     * 
     * <pre>
     * |40|0|0|0|2|26|7|9|0|0|0|4|U|N|I|X|
     * |0x40|0x35|0x33|0x33|0x33|0x33|0x33|0x33|
     * |0x40|0x35|0x80|0x00|0x00|0x00|0x00|0x00|
     * </pre>
     * 
     * <b>Little-endian encoding</b> <br>
     * Dense double array, preceded by a little-endian 32-bit number indicating the number of doubles, little-endian order, with
     * quantity type, unit type and reference string attached to the entire double array. Each double is stored in little-endian
     * order.
     */
    public static final byte DOUBLE_64_UNIT_ABS_ARRAY = 40;

    /**
     * <b>Big endian encoding</b> <br>
     * Rows/Cols-preceded dense float array of absolute quantity values, stored internally in the SI unit, with a quantity type,
     * a unit type and a String reference. After the byte with value 41, the matrix types have a 32-bit int indicating the
     * number of rows in the array that follows, followed by a 32-bit int indicating the number of columns. These integers are
     * not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type. After
     * that, the reference is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10 to indicate the type of string. The
     * internal storage of the values that are transmitted after that always use the SI (or standard) unit. Summarized, the
     * coding is as follows:
     * 
     * <pre>
     * |41|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|  |9|0|0|0|9|r|e|f|e|r|e|n|c|e|
     * |R|1|C|1|  |R|1|C|2| ... |R|1|C|n| 
     * |R|2|C|1|  |R|2|C|2| ... |R|2|C|n| 
     * ... 
     * |R|m|C|1|  |R|m|C|2| ... |R|m|C|n|
     * </pre>
     * 
     * In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * quantity type, unit type and reference string attached to the entire float matrix. Each float is stored in little-endian
     * order.
     */
    public static final byte FLOAT_32_UNIT_ABS_MATRIX = 41;

    /**
     * <b>Big endian encoding</b> <br>
     * Rows/Cols-preceded dense double array of absolute quantity values, stored internally in the SI unit, with a quantity
     * type, a unit type and a String reference. After the byte with value 42, the matrix types have a 32-bit int indicating the
     * number of rows in the array that follows, followed by a 32-bit int indicating the number of columns. These integers are
     * not preceded by a byte indicating it is an int. Then a one-byte quantity type follows and a one-byte unit type. After
     * that, the reference is stored as a UTF-8 or UTF-16 String, preceded by code 9 or 10 to indicate the type of string. The
     * internal storage of the values that are transmitted after that always use the SI (or standard) unit. Summarized, the
     * coding is as follows:
     * 
     * <pre>
     * |42|  |R|O|W|S|  |C|O|L|S|  |UT|  |DT|  |9|0|0|0|9|r|e|f|e|r|e|n|c|e|
     * |R|1|C|1|.|.|.|.|  |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.| 
     * |R|2|C|1|.|.|.|.|  |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.| 
     * ... 
     * |R|m|C|1|.|.|.|.|  |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
     * </pre>
     * 
     * In the language sending ore receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * quantity type, unit type and reference string attached to the entire double matrix. Each double is stored in
     * little-endian order.
     */
    public static final byte DOUBLE_64_UNIT_ABS_MATRIX = 42;

    /**
     * <b>Big endian encoding</b> <br>
     * Dense float matrix, stored internally in the SI unit, with a unique quantity type and unit type per row. This is the
     * transposed version of type 31. After the byte with value 43, the matrix types have a 32-bit int indicating the number of
     * vectors in the array that follows, followed by a 32-bit int indicating the number of elements per vector. These integers
     * are not preceded by a byte indicating it is an int. Then a one-byte quantity type for vector (row) 1 follows (see
     * {@link QuantityType}) and a one-byte unit type for vector (row) 1 (see {@link UnitType}), followed by the data for the
     * vector as 4-byte float values. Then the quantity type, unit type and data for vector 2, etc. The internal storage of the
     * values that are transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |43|  |V|E|C|S|  |C|O|L|S|
     * |UT1|DT1| |V|1|C|1|  |V|1|C|2| ... |V|1|C|n| 
     * |UT2|DT2| |V|2|C|1|  |V|2|C|2| ... |V|2|C|n| 
     * ... 
     * |UTn|DTn| |V|m|C|1|  |V|m|C|2| ... |V|m|C|n|
     * </pre>
     * 
     * This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
     * 2 the value. Suppose that we have a time series of 4 values at t = {1, 2, 3, 4} hours and dimensionless values v = {20.0,
     * 40.0, 50.0, 60.0}, then the coding is as follows:
     * 
     * <pre>
     * |31|    |0|0|0|2|  |0|0|0|4|
     * |26|8|  |0x3F|0x80|0x00|0x00|  |0x40|0x00|0x00|0x00|  |0x40|0x00|0x40|0x00|  |0x40|0x80|0x00|0x00| 
     * |0|0|   |0x41|0xA0|0x00|0x00|  |0x42|0x20|0x00|0x00|  |0x42|0x48|0x00|0x00|  |0x42|0x70|0x00|0x00|
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense little-endian float matrix, preceded by a 32-bit little-endian vector count int and a 32-bit little-endian column
     * count int, with a unique quantity type and unit type per vector row/column.
     * <p>
     * Note that this type is the transposed version of type 31, and can easily transfer a <code>Vector[]</code> array where
     * each vector has its own quantity and unit.
     * @since version 2.5
     */
    public static final byte FLOAT_32_UNIT_VECTOR_ARRAY = 43;

    /**
     * <b>Big endian encoding</b> <br>
     * Dense double matrix, stored internally in the SI unit, with a unique quantity type and unit type per row/column. This is
     * the transposed version of type 32. After the byte with value 44, the matrix types have a 32-bit int indicating the number
     * of vectors in the array that follows, followed by a 32-bit int indicating the number of elements per vector. These
     * integers are not preceded by a byte indicating it is an int. Then a one-byte quantity type for vector (row) 1 follows
     * (see {@link QuantityType}) and a one-byte unit type for vector (row) 1 (see {@link UnitType}), followed by the data for
     * the vector as 8-byte double values. Then the quantity type, unit type and data for vector 2, etc. The internal storage of
     * the values that are transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |44|  |V|E|C|S|  |C|O|L|S|
     * |UT1|DT1|  |V|1|C|1|.|.|.|.|  |V|1|C|2|.|.|.|.| ... |V|1|C|n|.|.|.|.| 
     * |UT1|DT1|  |V|2|C|1|.|.|.|.|  |V|2|C|2|.|.|.|.| ... |V|2|C|n|.|.|.|.| 
     * ... 
     * |UTn|DTn|  |V|m|C|1|.|.|.|.|  |V|m|C|2|.|.|.|.| ... |V|m|C|n|.|.|.|.|
     * </pre>
     * 
     * This data type is ideal for, for instance, sending a time series of values, where vector 1 indicates the time, and vector
     * 2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010, 2011, 2012, 2013} and areas in
     * acres of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows:
     * 
     * <pre>
     * |32|    |0|0|0|4|  |0|0|0|2|
     * |0|0|   |0x40|0x9F|0x68|0x00|0x00|0x00|0x00|0x00| |0x40|0x9F|0x6C|0x00|0x00|0x00|0x00|0x00|
     *         |0x40|0x9F|0x70|0x00|0x00|0x00|0x00|0x00| |0x40|0x9F|0x74|0x00|0x00|0x00|0x00|0x00|
     * |5|18|  |0x40|0x79|0xFB|0x33|0x33|0x33|0x33|0x33| |0x40|0x7A|0x76|0x66|0x66|0x66|0x66|0x66|
     *         |0x40|0x7A|0xC0|0x00|0x00|0x00|0x00|0x00| |0x40|0x7A|0x91|0x99|0x99|0x99|0x99|0x9A|
     * </pre>
     * <p>
     * <b>Little-endian encoding</b> <br>
     * Dense little-endian double matrix, preceded by a 32-bit little-endian vector count int and a 32-bit little-endian column
     * count int, with a unique quantity type and unit type per vector row.
     * <p>
     * Note that this type is the transposed version of type 32, and can easily transfer a <code>Vector[]</code> array where
     * each vector has its own quantity and unit.
     * @since version 2.5
     */
    public static final byte DOUBLE_64_UNIT_VECTOR_ARRAY = 44;

    /**
     * Utility class, cannot be instantiated.
     */
    private FieldTypes()
    {
        // Utility class
    }

}
