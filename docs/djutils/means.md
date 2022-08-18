# Means package

The means package contains Java classes that can compute three different kinds of mean value:

* Arithmetic mean
* Geometric mean
* Harmonic mean

For an excellent discussion about when to use which of these three kinds of mean read "[On Average, You’re Using the Wrong Average: Geometric & Harmonic Means in Data Analysis](https://towardsdatascience.com/on-average-youre-using-the-wrong-average-geometric-harmonic-means-in-data-analysis-2a703e21ea0)" by [Daniel McNichol](https://towardsdatascience.com/@dnlmc). The writing of this package was inspired by that article.

To use this package, one must construct an `ArithmeticMean`, `GeometricMean`, or `HarmonicMean` object, specifying actual types for the generic V (value) type and for the generic W (weight) type. Then feed it values, or weighted values and finally (or repeatedly) obtain the resulting mean value with the getMean() method. There are a bunch of methods provided to feed data and these are all named add (but they have different method signatures):

| Method signature | Description |
| ----------------------- | --------------- |
| `add(V value)` | Add the value with weight 1 |
| `add(V value, W weight)` | Add the value with weight `weight` |
| `add(V[] values)` | Add all the items of the values array, each with weight 1 |
| `add(V[] values, W[] weights)` | Add all the items of the values array, each with the corresponding weight from the weights array (throws `IllegalArgumentException` when the arrays do not have the same length) |
| `add(Iterable<V> values)` | Iterate over values and add each with weight 1 |
| `add(Iterable<V> values, Iterable<W> weights)` | Iterate over values and simultaneously iterate over weights and add each value with the corresponding weight (throws `IllegalArgumentException` when the iterables do not yield the same number of items) |
| `add(Map<V,W> map)` | Iterate over the keys of the map and add those keys, each with a weight obtained by calling the `get` method of the map for that key (beware of name confusion: the keys of the map are the values in the accumulation and the values of the map are the weights used in the accumulation) |
| `add(Collection<V>, Function<V, W>)` | Add the values of the `Collection`, each with a weight obtained by invoking the provided function |
| `add(Collection<S>, Function<S, V> values, Function<S, W> weights)` | Iterate over the items in the `Collection` and obtain a value for each item in the collection by invoking the `values` function on the item and add it with a weight obtained by calling the `weights` function on the item |


The accumulated results can be retrieved with:

| Method | Description |
| ---------- | --------------- |
| `public double getMean()` | Retrieve the mean (regretably this returns a double; not a V) |
| `public double getSum()` | Retrieve the accumulated sum; the relation of this with the result of getMean depends on the type of mean (regretably this returns a double; not a V) |
| `public double getSumOfWeights()` | Retrieve the accumulated weight (regretably this returns a double; not a W) |

The implementations of the three kinds of mean nicely illustrate the use of an abstract class that leaves the very minimum to be implemented to the classes that extend that to implement a particular kind of mean. The add methods that have Function arguments make use of a lambda expression (and, therefore, this package requires at least java version 8 to compile).
    