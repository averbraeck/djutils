# Programming

## Introduction

The library offers several methods to make it easy to encode an object into a byte string, according to the djutils-serialization standards.
The `Codec` class offers methods to encode and decode a single object. As an example:

```java
Length length = length.of(20.0, 'km');
byte[] buffer = Codec.encode(length, Endianness.BIG_ENDIAN);
// ...
Length decoded = Codec.decodeToObjectDataType(Endianness.BIG_ENDIAN, buffer);
System.out.println(String.format("%s -> %s, equals = %b", length.toString(), 
    decoded.toString(), length.equals(decoded)));
```

which prints:

```
20 km -> 20 km, equals = true
```

It is also possible to encode / decode an array of objects into / from one `byte[]` buffer:

```java
int[] array = new int[] {1, 2, 4, 8, 16, 32};
VectorN.Col<Area> vector = VectorN.Col.of(new double[] {12.0, 24.0}, Area.Unit.km2);
String string = "Hello world";
byte[] buffer = MessageCodec.encode(Endianness.BIG_ENDIAN, array, vector, string);
Object[] decoded = MessageCodec.decodeToPrimitiveDataTypes(Endianness.BIG_ENDIAN, buffer);
System.out.println(String.format("%s -> %s, equals = %b", Arrays.toString(array), 
    Arrays.toString((int[]) decoded[0]), Arrays.equals(array, (int[]) decoded[0])));
System.out.println(String.format("%s -> %s, equals = %b", 
    vector.format(VectorFormat.Row.instance()), 
    ((VectorN.Col<?>) decoded[1]).format(VectorFormat.Row.instance()), 
    vector.equals(decoded[1])));
System.out.println(String.format("%s -> %s, equals = %b", string, (String) decoded[2], 
    string.equals(decoded[2])));
```

which prints:

```
[1, 2, 4, 8, 16, 32] -> [1, 2, 4, 8, 16, 32], equals = true
[12, 24] km2 -> [12, 24] km2, equals = true
Hello world -> Hello world, equals = true
```


## Codecs

The actual work to (de)serialize content is done by the so-called `Codec` classes. Codec stands for coder-decoder. Similar 
codecs are grouped into the same class. The following `Codec` classes exist:
- `PrimitiveCodec` for primitive types `byte`, `short`, `int`, `long`, `float`, `double` and `char`, as well as the object wrappers `Byte`, `Short`, `Integer`, `Long`, `Float`, `Double` and `Character`.
- `PrimitiveArrayCodec` for arrays of primitive types `byte[]`, `short[]`, `int[]`, `long[]`, `float[]`, `double[]` and `char[]`.
- `PrimitiveMatrixCodec` for double arrays of primitive types `byte[][]`, `short[][]`, `int[][]`, `long[][]`, `float[][]`, `double[][]` and `char[][]`.
- `ObjectArrayCodec` for arrays of object wrappers of primitive types `Byte[]`, `Short[]`, `Integer[]`, `Long[]`, `Float[]`, and `Double[]`.
- `ObjectMatrixCodec` for double arrays of object wrappers of primitive types `Byte[][]`, `Short[][]`, `Integer[][]`, `Long[][]`, `Float[][]`, and `Ddouble[][]`.
- `StringCodec` for `String` objects (as UTF-8 or UTF-16).
- `StringArrayCodec` for `String[]` objects (as UTF-8 or UTF-16).
- `StringMatrixCodec` for `String[][]` objects (as UTF-8 or UTF-16).
- `QuantityCodec` for djunits `Quantity` objects (with float or double precision).
- `VectorCodec` for djunits `Vector` objects (with float or double precision).
- `MatrixCodec` for djunits `Matrix` objects (with float or double precision).
- `AbsQuantityCodec` for djunits `AbsQuantity` objects (with float or double precision).
- `AbsVectorCodec` for djunits `AbsVector` objects (with float or double precision).
- `AbsMatrixCodec` for djunits `AbsMatrix` objects (with float or double precision).
- `VectorArrayCodec` for row arrays or column arrays of djunits `Vector` objects (with float or double precision), where each row or column can be of a different quantity and/or unit.


## Short names for codecs

Each codec has a short name that can be requested with `getShortName()`. This can be helpful in quickly understanding or printing what the codec is meant to serialize or deserialize. The short names for the codecs are standardized as follows:

- primitive types: type name + "_" + number of bits, e.g., `int_32`, `boolean_8`, `double_64`.
- primitive object wrappers: class name + "_" + number of bits, e.g., `Integer_32`, `Short_16`, `Long_64`.
- primitive arrays: type name + "_" + number of bits + "_array", e.g., `int_32_array`, `float_32_array`.
- primitive object arrays: class name + "_" + number of bits + "_array", e.g., `Short_16_array`, `Byte_8_array`.
- primitive matrices: type name + "_" + number of bits + "_matrix", e.g., `int_32_matrix`, `float_32_matrix`.
- primitive object matrices: class name + "_" + number of bits + "_matrix", e.g., `Short_16_matrix`, `Byte_8_matrix`.
- Strings: "String" + "_" + number of bits for UTF type, i.e., `String_8` for UTF-8 and `String_16` for UTF-16.
- String arrays: "String" + "_" + number of bits for UTF type + "_array", i.e., `String_8_array` and `String_16_array`.
- String matrices: "String" + "_" + number of bits for UTF type + "_matrix", i.e., `String_8_matrix` and `String_16_matrix`.

All djunits quantity, vector and matrix types have a `float` and `double` variant, indicated by the number of bits (32 or 64), and carry the term `unit` to denote that a unit is attached. Absolute quantity, vector and matrix types have a short name that starts with `Abs`.

- Quantity: "Quantity_" + nr of bits + "_unit", i.e., `Quantity_32_unit` and `Quantity_64_unit`.
- Absolute Quantity: "AbsQuantity_" + nr of bits + "_unit", i.e., `AbsQuantity_32_unit` and `AbsQuantity_64_unit`.
- Vector: "Vector_" + nr of bits + "_unit", i.e., `Vector_32_unit` and `Vector_64_unit`.
- Absolute Vector: "AbsVector_" + nr of bits + "_unit", i.e., `AbsVector_32_unit` and `AbsVector_64_unit`.
- Matrix: "Matrix_" + nr of bits + "_unit", i.e., `Matrix_32_unit` and `Matrix_64_unit`.
- Absolute Matrix: "AbsMatrix_" + nr of bits + "_unit", i.e., `AbsMatrix_32_unit` and `AbsMatrix_64_unit`.

The vector arrays are special, since they distinguish between arrays of row vectors (with the unit per row), and arrays of column vectors (spreadsheet-like, with the unit per column). Therefore, they carry the following elements: `Vector`, `32` or `64`, `row` or `column`, `array`, and `unit`:

- Array of column vectors: `Vector_32_col_array_unit` and `Vector_64_col_array_unit`.
- Array of row vectors: `Vector_32_row_array_unit` and `Vector_64_row_array_unit`.


## Serializing and deserializing objects

## Serializing and deserializing messages

## Handling Strings

## Handling floating point precision

## Working with quantities, vectors and matrices

