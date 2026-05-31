# Programming

## Codecs

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

All djunits quantity, vector and matrix types have a `float` and `double` variant, indicated by the number of bits (32 or 64), and carry the term `unit` to denote that a unit is attached. Absolute quantity, vector and matrix types have a short name that starts with `abs_`.

- Quantity: "quantity_" + nr of bits + "_unit", i.e., `quantity_32_unit` and `quantity_64_unit`.
- Absolute Quantity: "abs_quantity_" + nr of bits + "_unit", i.e., `abs_quantity_32_unit` and `abs_quantity_64_unit`.
- Vector: "vector_" + nr of bits + "_unit", i.e., `vector_32_unit` and `vector_64_unit`.
- Absolute Vector: "abs_vector_" + nr of bits + "_unit", i.e., `abs_vector_32_unit` and `abs_vector_64_unit`.
- Matrix: "matrix_" + nr of bits + "_unit", i.e., `matrix_32_unit` and `matrix_64_unit`.
- Absolute Matrix: "abs_matrix_" + nr of bits + "_unit", i.e., `abs_matrix_32_unit` and `abs_matrix_64_unit`.

The vector arrays are special, since they distinguish between arrays of row vectors (with the unit per row), and arrays of column vectors (spreadsheet-like, with the unit per column). Therefore, they carry the following elements: `vector`, `32` or `64`, `row` or `column`, `array`, and `unit`:

- Array of column vectors: `vector_32_col_array_unit` and `vector_64_col_array_unit`.
- Array of row vectors: `vector_32_row_array_unit` and `vector_64_row_array_unit`.


## Serializing and deserializing objects

## Serializing and deserializing messages

## Handling Strings

## Handling floating point precision

## Working with quantities, vectors and matrices

