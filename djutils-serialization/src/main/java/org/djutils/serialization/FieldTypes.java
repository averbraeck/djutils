package org.djutils.serialization;

/**
 * Type numbers to encode different data types within djutils-serialization.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class FieldTypes
{

    /**
     * <p>
     * <b>Big endian and Little endian encoding</b>
     * </p>
     * Byte, 8 bit signed two's complement integer.
     */
    public static final byte BYTE_8 = 0;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Short, 16 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Short, 16 bit signed two's complement integer, little endian order.
     */
    public static final byte SHORT_16 = 1;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Integer, 32 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Integer, 32 bit signed two's complement integer, little endian order.
     */
    public static final byte INT_32 = 2;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Long, 64 bit signed two's complement integer.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Long, 64 bit signed two's complement integer, little endian order.
     */
    public static final byte LONG_64 = 3;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Float, single-precision 32-bit IEEE 754 floating point.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Float, single-precision 32-bit IEEE 754 floating point, little endian order.
     */
    public static final byte FLOAT_32 = 4;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Float, double-precision 64-bit IEEE 754 floating point.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Float, double-precision 64-bit IEEE 754 floating point, little endian order.
     */
    public static final byte DOUBLE_64 = 5;

    /**
     * <p>
     * <b>Big endian and Little endian encoding</b>
     * </p>
     * Boolean, sent / received as a byte; 0 = false, 1 = true.
     */
    public static final byte BOOLEAN_8 = 6;

    /**
     * <p>
     * <b>Big endian and Little endian encoding</b>
     * </p>
     * Char, 8-bit ASCII character. Note that not all characters can be represented in 8 bits.
     */
    public static final byte CHAR_8 = 7;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Char, 16-bit Unicode character, big endian order. Note that not all characters can be represented in two bytes using
     * UTF-16.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Char, 16-bit Unicode character, little-endian order for the 2 bytes.
     */
    public static final byte CHAR_16 = 8;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * String, number-preceded byte array of 8-bits characters. The string types are preceded by a 32-bit int indicating the
     * number of bytes in the array that follows. This int is itself not preceded by a byte indicating it is an int. An ASCII
     * string "Hello" is therefore coded as follows: |9|0|0|0|5|H|e|l|l|o|. Note that the int indicates the number of bytes, not
     * the number of characters. The string itself is coded with the first character at the start of the array and the last
     * character at the end.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * String, 32-bit little-endian number indicating the number of bytes, followed by a byte array of UTF-8 encoded characters.
     * Note that the length indicates the number of bytes in the encoding, not the number of characters in the string. The
     * string itself is coded with the first character at the start of the array and the last character at the end.
     */
    public static final byte STRING_UTF8 = 9;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * String, number-preceded char array of 16-bits characters, big-endian order. The string types are preceded by a 32-bit int
     * indicating the number of shorts in the array that follows. This int is itself not preceded by a byte indicating it is an
     * int. Note that the int indicates the number of shorts, not the number of characters; the number of bytes is therefore
     * equal to 2 * length. The string itself is coded with the first character at the start of the array and the last character
     * at the end.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * String, 32-bit little-endian number indicating the number of shorts in the encoding, followed by a byte array of UTF-16
     * encoded characters. Each 2-byte character is represented in little-endian order. Note that the length indicates the
     * number of shorts (2 bytes) in the encoding, not the number of characters in the string. The number of bytes to represent
     * the string in the encoding is therefore equal to 2 * length. The string itself is coded with the first character at the
     * start of the array and the last character at the end.
     */
    public static final byte STRING_UTF16 = 10;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded byte array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 8 bytes with numbers 1
     * through 8 is therefore coded as follows: |11|0|0|0|8|1|2|3|4|5|6|7|8|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Byte array, preceded by a 32-bit little-endian number indicating the number of bytes.
     */
    public static final byte BYTE_8_ARRAY = 11;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded short array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 8 shorts with numbers 100
     * through 107 is therefore coded as follows: |12|0|0|0|8|0|100|0|101|0|102|0|103|0|104|0|105|0|106|0|107|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Short array, preceded by a 32-bit little-endian number indicating the number of shorts, little-endian coded shorts.
     */
    public static final byte SHORT_16_ARRAY = 12;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded int array. The array types are preceded by a 32-bit int indicating the number of values in the array that
     * follows. This int is itself not preceded by a byte indicating it is an int. An array of 4 ints with numbers 100 through
     * 103 is therefore coded as follows: |13|0|0|0|4|0|0|0|100|0|0|0|101|0|0|0|102|0|0|0|103|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Integer array, preceded by a 32-bit little-endian number indicating the number of integers, little-endian coded ints.
     */
    public static final byte INT_32_ARRAY = 13;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded long array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int. An array of 3 longs with numbers 100
     * through 102 is therefore coded as follows: |14|0|0|0|3|0|0|0|0|0|0|0|100|0|0|0|0|0|0|0|101|0|0|0|0|0|0|0|102|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Long array, preceded by a 32-bit little-endian number indicating the number of longs, little-endian coded longs.
     */
    public static final byte LONG_64_ARRAY = 14;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded float array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Float array, preceded by a 32-bit little-endian number indicating the number of floats, little-endian coded floats.
     */
    public static final byte FLOAT_32_ARRAY = 15;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded double array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Double array, preceded by a 32-bit little-endian number indicating the number of doubles, little-endian coded doubles.
     */
    public static final byte DOUBLE_64_ARRAY = 16;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded boolean array. The array types are preceded by a 32-bit int indicating the number of values in the array
     * that follows. This int is itself not preceded by a byte indicating it is an int.
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Boolean array, preceded by a 32-bit little-endian number indicating the number of booleans.
     */
    public static final byte BOOLEAN_8_ARRAY = 17;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded byte matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of bytes 1-2-4 6-7-8 is therefore coded as
     * follows: |18|0|0|0|2|0|0|0|3|0|1|0|2|0|4|0|6|0|7|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Byte matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count.
     */
    public static final byte BYTE_8_MATRIX = 18;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded short matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of shorts 1-2-4 6-7-8 is therefore coded as
     * follows: |19|0|0|0|2|0|0|0|3|1|2|4|6|7|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Short matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded shorts.
     */
    public static final byte SHORT_16_MATRIX = 19;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded int matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of integers 1-2-4 6-7-8 is therefore coded
     * as follows: |20|0|0|0|2|0|0|0|3|0|0|0|1|0|0|0|2|0|0|0|4|0|0|0|6|0|0|0|7|0|0|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Integer matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded ints.
     */
    public static final byte INT_32_MATRIX = 20;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded long matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows. A matrix with 2 rows and 3 columns of long vales 1-2-4 6-7-8 is therefore
     * coded as follows:
     * |21|0|0|0|2|0|0|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|4|0|0|0|0|0|0|0|6|0|0|0|0|0|0|0|7|0|0|0|0|0|0|0|8|<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Long matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded longs.
     */
    public static final byte LONG_64_MATRIX = 21;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded float matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Float matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian coded floats.
     */
    public static final byte FLOAT_32_MATRIX = 22;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded double matrix. The matrix types are preceded by a 32-bit int indicating the
     * number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Double matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count,
     * little-endian doubles.
     */
    public static final byte DOUBLE_64_MATRIX = 23;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number of rows and number of columns preceded boolean matrix. The matrix types are preceded by a 32-bit int indicating
     * the number of rows, followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte
     * indicating it is an int. The number of values in the matrix that follows is rows * columns. The data is stored row by
     * row, without a separator between the rows.<br>
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col].
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Boolean matrix, preceded by a 32-bit little-endian number row count and a 32-bit little-endian number column count.
     */
    public static final byte BOOLEAN_8_MATRIX = 24;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Float, stored internally in the SI unit, with a unit type and display type attached. The internal storage of the value
     * that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte unit type, and a one-byte
     * display type. As an example: suppose the unit indicates that the type is a length, whereas the display type indicates
     * that the internally stored value 60000.0 should be displayed as 60.0 km, this is coded as follows:
     * |25|16|11|0x47|0x6A|0x60|0x00|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Float stored internally as a little-endian float in the corresponding SI unit, with unit type and display unit attached.
     * The total size of the object is 7 bytes.
     */
    public static final byte FLOAT_32_UNIT = 25;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Double, stored internally in the SI unit, with a unit type and display type attached. The internal storage of the value
     * that is transmitted is always in the SI (or standard) unit. The value is preceded by a one-byte unit type and a one-byte
     * display type. As an example: suppose the unit indicates that the type is a length, whereas the display type indicates
     * that the internally stored value 60000.0 should be displayed as 60.0 km, this is coded as follows:
     * |26|16|11|0x47|0x6A|0x60|0x00|0x00|0x00|0x00|0x00|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Double stored internally as a little-endian double in the corresponding SI unit, with unit type and display unit
     * attached. The total size of the object is 11 bytes.
     */
    public static final byte DOUBLE_64_UNIT = 26;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded dense float array, stored internally in the SI unit, with a unit type and display type. After the byte
     * with value 27, the array types have a 32-bit int indicating the number of values in the array that follows. This int is
     * itself not preceded by a byte indicating it is an int. Then a one-byte unit type follows and a one-byte display type .
     * The internal storage of the values that are transmitted after that always use the SI (or standard) unit. As an example:
     * when we send an array of two durations, 2.0 minutes and 2.5 minutes, this is coded as follows:
     * |27|0|0|0|2|25|7|0x40|0x00|0x00|0x00|0x40|0x20|0x00|0x00|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Dense float array, preceded by a little-endian 32-bit number indicating the number of floats, with unit type and display
     * unit attached to the entire float array. Each float is stored in little-endian order.
     */
    public static final byte FLOAT_32_UNIT_ARRAY = 27;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded dense double array, stored internally in the SI unit, with a unit type and display type. After the byte
     * with value 28, the array types have a 32-bit int indicating the number of values in the array that follows. This int is
     * itself not preceded by a byte indicating it is an int. Then a one-byte unit type follows and a one-byte display type. The
     * internal storage of the values that are transmitted after that always use the SI (or standard) unit. As an example: when
     * we send an array of two durations, 21.2 minutes and 21.5 minutes, this is coded as follows:
     * |28|0|0|0|2|25|7|0x40|0x35|0x33|0x33|0x3|0x33|0x33|0x33|0x40|0x35|0x80|0x00|0x00|0x00|0x00|0x00|
     * <p>
     * <b>Little-endian encoding</b>
     * </p>
     * Dense double array, preceded by a little-endian 32-bit number indicating the number of doubles, little-endian order, with
     * unit type and display unit attached to the entire double array. Each double is stored in little-endian order.
     */
    public static final byte DOUBLE_64_UNIT_ARRAY = 28;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Rows/Cols-preceded dense float array, stored internally in the SI unit, with a unit type and display type. After the byte
     * with value 29, the matrix types have a 32-bit int indicating the number of rows in the array that follows, followed by a
     * 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an int. Then a
     * one-byte unit type follows and a one-byte display type The internal storage of the values that are transmitted after that
     * always use the SI (or standard) unit. Summarized, the coding is as follows:
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
     * <b>Little-endian encoding</b>
     * </p>
     * Dense float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * unit type and display unit attached to the entire float matrix. Each float is stored in little-endian order.
     */
    public static final byte FLOAT_32_UNIT_MATRIX = 29;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Rows/Cols-preceded dense double array, stored internally in the SI unit, with a unit type and display type. After the
     * byte with value 30, the matrix types have a 32-bit int indicating the number of rows in the array that follows, followed
     * by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an int. Then
     * a one-byte unit type follows and a one-byte display type The internal storage of the values that are transmitted after
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
     * <b>Little-endian encoding</b>
     * </p>
     * Dense double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column count int, with
     * unit type and display unit attached to the entire double matrix. Each double is stored in little-endian order.
     */
    public static final byte DOUBLE_64_UNIT_MATRIX = 30;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded dense float array, stored internally in the SI unit, with a unique unit type and display type per row.
     * After the byte with value 31, the matrix types have a 32-bit int indicating the number of rows in the array that follows,
     * followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an
     * int. Then a one-byte unit type for column 1 follows and a one-byte display type for column 1. Then the unit type and
     * display type for column 2, etc. The internal storage of the values that are transmitted after that always use the SI (or
     * standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |31|  |R|O|W|S|  |C|O|L|S|
     * |UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
     * |R|1|C|1|  |R|1|C|2| ... |R|1|C|n| 
     * |R|2|C|1|  |R|2|C|2| ... |R|2|C|n| 
     * ... 
     * |R|m|C|1|  |R|m|C|2| ... |R|m|C|n|
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
     * <b>Little-endian encoding</b>
     * </p>
     * Dense little-endian float matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column
     * count int, with a unique unit type and display unit per column of the float matrix.
     */
    public static final byte FLOAT_32_UNIT_COLUMN_MATRIX = 31;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
     * Number-preceded dense double array, stored internally in the SI unit, with a unique unit type and display type per row.
     * After the byte with value 32, the matrix types have a 32-bit int indicating the number of rows in the array that follows,
     * followed by a 32-bit int indicating the number of columns. These integers are not preceded by a byte indicating it is an
     * int. Then a one-byte unit type for column 1 follows (see the table above) and a one-byte display type for column 1 (see
     * Appendix A). Then the unit type and display type for column 2, etc. The internal storage of the values that are
     * transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows:
     * 
     * <pre>
     * |32|  |R|O|W|S|  |C|O|L|S|
     * |UT1|DT1|  |UT2|DT2| ... |UTn|DTn|
     * |R|1|C|1|.|.|.|.|  |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.| 
     * |R|2|C|1|.|.|.|.|  |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.| 
     * ... 
     * |R|m|C|1|.|.|.|.|  |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
     * </pre>
     * 
     * In the language sending or receiving a matrix, the rows are denoted by the outer index, and the columns by the inner
     * index: matrix[row][col]. This data type is ideal for, for instance, sending a time series of values, where column 1
     * indicates the time, and column 2 the value. Suppose that we have a time series of 4 values at dimensionless years {2010,
     * 2011, 2012, 2013} and costs of dollars per acre of {415.7, 423.4, 428.0, 435.1}, then the coding is as follows:
     * 
     * <pre>
     * |32|  |0|0|0|4|  |0|0|0|2|
     * |0|0|  |101|150|18|
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
     * <b>Little-endian encoding</b>
     * </p>
     * Dense little-endian double matrix, preceded by a 32-bit little-endian row count int and a 32-bit little-endian column
     * count int, with a unique unit type and display unit per column of the double matrix.
     */
    public static final byte DOUBLE_64_UNIT_COLUMN_MATRIX = 32;

    /**
     * <p>
     * <b>Big endian encoding</b>
     * </p>
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
     * <b>Little-endian encoding</b>
     * </p>
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
     * <p>
     * <b>Big endian encoding</b>
     * </p>
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
     * <b>Little-endian encoding</b>
     * </p>
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
     * <p>
     * <b>Big endian encoding</b>
     * </p>
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
     * <b>Little-endian encoding</b>
     * </p>
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
     * <p>
     * <b>Big endian encoding</b>
     * </p>
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
     * <b>Little-endian encoding</b>
     * </p>
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
     * Utility class, cannot be instantiated.
     */
    private FieldTypes()
    {
        // Utility class
    }

}
