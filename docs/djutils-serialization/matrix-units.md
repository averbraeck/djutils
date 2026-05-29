# Matrix types with unit (#29 - #32)

## 29a. Float matrix with unit (big endian)

After the byte with value 29, the matrix types have a 32-bit big-endian int indicating the number of rows in 
the matrix that follows, followed by a 32-bit big-endian int indicating the number of columns. These integers 
are not preceded by a byte indicating it is an int. Then a one-byte quantity type follows 
(see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type 
(see [Unit types](../unit-types)). The internal storage of the values that are 
transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows
(assuming *big-endian* encoding):

<pre>
|29|R|O|W|S|C|O|L|S|UT|DT|
|R|1|C|1|R|1|C|2| ... |R|1|C|n|
|R|2|C|1|R|2|C|2| ... |R|2|C|n|
...
|R|m|C|1|R|m|C|2| ... |R|m|C|n|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the first index, and the
columns by the second index: matrix\[row\]\[col\].


## 29b. Float matrix with unit (little endian)

After the byte with value 29, the matrix types have a 32-bit little-endian int indicating the number of rows in 
the matrix that follows, followed by a 32-bit little-endian int indicating the number of columns. These integers 
are not preceded by a byte indicating it is an int. Then a one-byte quantity type follows 
(see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type 
(see [Unit types](../unit-types)). The internal storage of the values that are 
transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows
(assuming *little-endian* encoding):

<pre>
|29|R|O|W|S|C|O|L|S|UT|DT|
|R|1|C|1|R|1|C|2| ... |R|1|C|n|
|R|2|C|1|R|2|C|2| ... |R|2|C|n|
...
|R|m|C|1|R|m|C|2| ... |R|m|C|n|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the first index, and the
columns by the second index: matrix\[row\]\[col\].


## 30a. Double matrix with unit (big endian)

After the byte with value 30, the matrix types have a 32-bit big endian int indicating the number of rows in 
the matrix that follows, followed by a 32-bit big endian int indicating the number of columns. These integers 
are not preceded by a byte indicating it is an int. Then a one-byte quantity type follows 
(see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type 
(see [Unit types](../unit-types)). The internal storage of the values that are 
transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows (assuming *big-endian* encoding):

<pre>
|30|R|O|W|S|C|O|L|S|UT|DT|
|R|1|C|1|.|.|.|.| |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.|
|R|2|C|1|.|.|.|.| |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.|
...
|R|m|C|1|.|.|.|.| |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the
columns by the inner index: matrix\[row\]\[col\].


## 30b. Double matrix with unit (little endian)

After the byte with value 30, the matrix types have a 32-bit little endian int indicating the number of rows in 
the matrix that follows, followed by a 32-bit little endian int indicating the number of columns. These integers 
are not preceded by a byte indicating it is an int. Then a one-byte quantity type follows 
(see the table in [Coding of quantities](../coding-quantities) ) and a one-byte display unit type 
(see [Unit types](../unit-types)). The internal storage of the values that are 
transmitted after that always use the SI (or standard) unit. Summarized, the coding is as follows (assuming *little-endian* encoding):

<pre>
|30|R|O|W|S|C|O|L|S|UT|DT|
|R|1|C|1|.|.|.|.| |R|1|C|2|.|.|.|.| ... |R|1|C|n|.|.|.|.|
|R|2|C|1|.|.|.|.| |R|2|C|2|.|.|.|.| ... |R|2|C|n|.|.|.|.|
...
|R|m|C|1|.|.|.|.| |R|m|C|2|.|.|.|.| ... |R|m|C|n|.|.|.|.|
</pre>

In the language sending or receiving a matrix, the rows are denoted by the outer index, and the
columns by the inner index: matrix\[row\]\[col\].

