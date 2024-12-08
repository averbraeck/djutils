# EVAL - AN EVALUATOR FOR MATHEMATICAL EXPRESSIONS

The EVAL project contains the building blocks for a very flexible calculator. It knows many physical constants, uses strong typing for physical values (so you cannot accidentally add a speed to a time). The following four-line program demonstrates strong typing:
```java
    public static void main(final String... args)
    {
        System.out.println(new Eval().evaluate("100[m/s] * 10 [s]"));
    }
```
The output of this program is
<pre>
1000.00000 m
</pre>

How does it know that the result should have the unit m (meter) attached? Eval uses our djunits project for the strong typing and this project contains the knowledge that a speed times a time yields a distance. It also ensures that you cannot add (or subtract) incompatible quantities. If you replace the * by a +, a RuntimeException will be thrown. The message of the exception describes in detail what was wrong
<pre>
Cannot add 100.000000 m/s to 10.0000000 s because the types are incompatible at position 17
</pre>
That number 17 is the position of the last closing ] in the expression. This is where the evaluator had to go before it could decide that the + operation cannot be performed.

The built-in unit parser only parses units expressed in the 7 basic SI units (rad, sr, kg, m, s, A, K, mol, cd). These can be written together with exponents and division signs to create any SI unit. If you need to parse other units (e.g. km/h, or mi/h), you must supply your own unit parser (that will take precedence over the built-in unit parser).

Expressions with plain (untyped) values cannot fail due to incompatible types. They can, of course, fail due to operands being invalid for the operation. In some cases this results in the special value NaN (Not-a-Number). E.g. 
<pre>
sqrt(-1)
</pre>
yields the value _NaN_ without throwing an exception.

Other cases, like division by zero _do_ throw an exception.

The evaluator can deal with boolean values. These can be created with the _TRUE()_ and _FALSE()_ functions, but also with the binary comparison operators like _<_, _<=_, _>_, _>=_, _==_, _!=_ and combined with boolean binary operators like _&&_ and _||_ . You can even write conditional expressions using the _?_ and _:_ notation of java, C, and many other programming languages.

## Using pre-defined constants
A long list of physical and mathematical constants are pre-defined in Eval. Using these looks like calling a zero-argument function like:
<pre>
new Eval.evaluate("2 * PI()")
</pre>
Currently, all pre-defined constants have names that are entirely upper-case.

The complete list of pre-defined constants can be found in _Eval.java_, search for the _F0_ entries in _builtinFunctions_.

## Using variables
Every decent desk calculator has a way to store and retrieve values. The retrieve operation of such a mechanism is embedded in Eval. The store operation is not. To allow Eval to find the value of a named variable, it has to be taught were to find them. This is done by providing a _RetrieveValue_ object. _RetrieveValue_ is an object that implements a _lookup_ method that takes a _String_ argument and returns an _Object_. The _Object_ can be a strongly typed djunits value, or a _Double_, a _Boolean_, an _Integer_, etc.
```java
        RetrieveValue values = new RetrieveValue() {
            @Override
            public Object lookup(final String name)
            {
                if (name.equals("myVariable"))
                {
                    return 123.456;
                }
                return null;
            }};
        new Eval().setRetrieveValue(values).evaluate("myVariable+20");
```
This trivial example creates a _RetrieveValue_ object that knows only one variable (_myVariable_). The _lookup_ method should return _null_ if the value is not known (which will result in a RuntimeException). The _setRetrieveValue_ method returns the Eval object which is useful for method chaining.

Actual implementations of _RetrieveValue_ should probably use a _HashMap&lt;String, Object&gt;_.

## Using built-in functions
The built-in functions have case-sensitive names. The usual trigonometric functions are implemented. E.g.
```java
new Eval().evaluate("sin(1)");
```
will yield a double value of approximately 0.84147098 (angles are in Radians).

The 2-parameter atan2 function is also available.

The complete list of one- and two-argument functions can be found in _Eval.java_. Look for the _F1_ and _F2_ entries in _builtinFunctions_.

One very special built-in function is
<pre>CURRENTTIME()</pre>
This returns the number of seconds and milliseconds since January 1st, 1970, 00:00:00 UTC. The result is a double value in seconds with a granularity of 1 ms. It is, of course, strongly typed so you cannot just add some value to it unless that value is a duration.
```java
System.out.println(new Eval().evaluate("CURRENTTIME()+5"));
```
will fail with an exception, while
```java
System.out.println(new Eval().evaluate("CURRENTTIME()+5[s]"));
```
prints a (rather large) number.

