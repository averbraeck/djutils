#  Summarizers

A summarizer is an object that ingests (double precision floating point) values and computes some statistics (summaries) of the ingested values. The following code example creates a `Tally` and feeds it 1000 uniformly distributed pseudo-random values.

```java
Tally tally = new Tally("Example tally");
Random random = new Random(1234);
for (int i = 0; i < 1000; i++)
{
    tally.ingest(random.nextDouble());
}
System.out.println("minimum:                       " + tally.getMin());
System.out.println("maximum:                       " + tally.getMax());
System.out.println("count:                         " + tally.getN());
System.out.println("sum:                           " + tally.getSum());
System.out.println("sample mean:                   " + tally.getSampleMean());
System.out.println("sample variance:               " + tally.getSampleVariance());
System.out.println("sample standard deviation:     " + tally.getSampleStDev());
System.out.println("sample skewness:               " + tally.getSampleSkewness());
System.out.println("sample kurtosis:               " + tally.getSampleKurtosis());
System.out.println("sample excess kurtosis:        " + tally.getSampleExcessKurtosis());
System.out.println("population mean:               " + tally.getPopulationMean());
System.out.println("population variance:           " + tally.getPopulationVariance());
System.out.println("population standard deviation: " + tally.getPopulationStDev());
System.out.println("population skewness:           " + tally.getPopulationSkewness());
System.out.println("population kurtosis:           " + tally.getPopulationKurtosis());
System.out.println("population excess kurtosis:    " + tally.getPopulationExcessKurtosis());
System.out.println("first quartile:                " + tally.getQuantile(0.25));
System.out.println("median:                        " + tally.getQuantile(0.5));
System.out.println("third quartile:                " + tally.getQuantile(0.75));
for (int bin = 0; bin <= 10; bin++)
{
    double value = bin / 10.0;
    System.out.println("Cumulative probability at " + value + "  " + tally.getCumulativeProbability(value));
}
```

When run, this program outputs something like

<pre>
minimum:                       4.463828850445051E-4
maximum:                       0.9993228356687273
count:                         1000
sum:                           487.6875254457159
sample mean:                   0.4876875254457153
sample variance:               0.0839370407429099
sample standard deviation:     0.28971889952660995
sample skewness:               0.03986803012965087
sample kurtosis:               1.743485723621789
sample excess kurtosis:        -1.2550414677445052
population mean:               0.4876875254457153
population variance:           0.08385310370216699
population standard deviation: 0.28957400384386545
population skewness:           0.03980820314948127
population kurtosis:           1.7452309545763656
population excess kurtosis:    -1.2547690454236344
first quartile:                0.29227267857053363
median:                        0.4876875254457153
third quartile:                0.683102372320897
Cumulative probability at 0.0  0.04647865786371996
Cumulative probability at 0.1  0.09012267246445238
Cumulative probability at 0.2  0.1610870595108309
Cumulative probability at 0.3  0.2578461108058647
Cumulative probability at 0.4  0.38208857781104744
Cumulative probability at 0.5  0.5159534368528308
Cumulative probability at 0.6  0.6517317265359823
Cumulative probability at 0.7  0.7673049076991025
Cumulative probability at 0.8  0.859928909911231
Cumulative probability at 0.9  0.9221961594734538
Cumulative probability at 1.0  0.9616364296371288
</pre>

With the same java runtime environment as we used to run this example, you should get the exact same output because the output of a pseudo-random generator is predictable and reproducible. In these results, the mean and the median are expected to be 0.5, the expected variance 0.083333 (=1/12), expected standard deviation 0.288675 (&radic;(1/12)), expected skewness 0.0, expected kurtosis 1.8 and the expected excess kurtosis -1.2. The differences with the observed values are reasonable for the sample size (1000).


## Population versus sample

The `getPopulationXXX` methods return a result as it should be computed when an entire population has been ingested. The `getSampleXXX` methods should be used when ingested values form _just a sample_ of the entire population. In the example above, the `Tally` has ingested 1000 values from a population that has infinite size (actually the number of double precision floating point values between 0.0 and 1.0 is not unlimited, but the sample of 1000 is nowhere close to the entire population). Thus, the `getSampleXXX` methods should be used to summarize the ingested values.

The `Cumulative probability` values are clearly not uniform; in fact they are normally distributed. That is to be expected as the printed values are _approximated_ from the accumulated population mean and population standard deviation values which is a bad way to represent uniformly distributed values.


## Skewness, kurtosis, excess kurtosis

[Skewness is a measure for the asymmetry of the distribution](https://en.wikipedia.org/wiki/Skewness). The skewness of a symmetric distribution is `0.0`. A negative skewness indicates that the distribution has a longer tail on the left, a positive skewness indicates a longer tail on the right.

[Kurtosis is a measure for the tailedness of the distribution](https://en.wikipedia.org/wiki/Kurtosis). The kurtosis of normally distributed population is `3.0`. The kurtosis of uniformly distributed population is `1.8`. Larger values of the kurtosis indicate that a distribution has long tails. A large kurtosis in observed sample values is often caused by the presence of outliers in the sample.

Excess kurtosis is defined as `kurtosis minus 3`. This makes the excess kurtosis of normally distributed values `0.0` and the excess kurtosis of uniformly distributed values `-1.2`.


## Drawing the result in a boxplot

A [box plot](https://en.wikipedia.org/wiki/Box_plot) is a nice way to graph minimum, first quartile, median, third quartile and maximum of a distribution. As shown above, the `Tally` collects minimum and maximum values. The quartiles and the median can only be approximated by the `Tally`. This is done by calling the getQuantile method:

 ```java
System.out.println("first quartile:                " + tally.getQuantile(0.25));
System.out.println("median:                        " + tally.getQuantile(0.5));
System.out.println("third quartile:                " + tally.getQuantile(0.75));
```

The output of these extra statements is something like:

<pre>
first quartile:                0.29227267857053363
median:                        0.4876875254457153
third quartile:                0.683102372320897
</pre>

The expected values for these quantiles are `0.25`, `0.5` and `0.75`; so what is going on? Again, the `Tally` assumes that the values are normally distributed and then estimates these quantile values from the observed mean and standard deviation. ([It is possible to improve that approximation by taking the skewness and kurtosis of the data into account](https://en.wikipedia.org/wiki/Probability_distribution_fitting).) In our case, the values that were fed to the tally were uniformly distributed. The difference is rather striking. To fix this, we should construct a `Tally` with a suitable quantile accumulator. In the quantileaccumulator package there are a couple to choose from:

* NoStorageAccumulator: this requires no memory and it is the one used when no specific quantile accumulator is specified at `Tally` construction time; e.g. the example above
* FullStorageAccumulator: as the name suggests, this stores all ingested samples (memory use grows linearly with the number of ingested values) and may require more memory than available
* FixedBinsAccumulator: this pre-defines and allocates a fixed number of bins corresponding to specific input values; memory requirement is proportional to the number of bins
* TDigestAccumulator: this one is the most complex, but strikes a good balance between memory use and accuracy


## Full storage accumulator

To create a `Tally` with `FullStorageAccumulator`, replace the first line of the program by:

```java
Tally tally = new Tally("Example tally with full storage accumulator", new FullStorageAccumulator());
```

The output for the quantiles is now something like

<pre>
first quartile:                0.22964815841745587
median:                        0.4753812701997516
third quartile:                0.7515290688493804
</pre>

The results differ from the expected values because we have only ingested a small fraction of the entire population. These values are exact (a.k.a. the _ground truth_). The `FullStorageAccumulator` is perfect when not too many values will be ingested. When the number of ingested values runs in the millions or billions and those values are in a known interval, the `FixedBinsAccumulator`, or the `TDigestAccumulator` can be used. When the interval is not known, or the distribution is very non-uniform, the `TDigestAccumulator` is the best choice.

The `Cumulative probability` values are much more uniform as well:

<pre>
Cumulative probability at 0.0  0.0
Cumulative probability at 0.1  0.104
Cumulative probability at 0.2  0.22
Cumulative probability at 0.3  0.314
Cumulative probability at 0.4  0.428
Cumulative probability at 0.5  0.522
Cumulative probability at 0.6  0.608
Cumulative probability at 0.7  0.7
Cumulative probability at 0.8  0.805
Cumulative probability at 0.9  0.916
Cumulative probability at 1.0  1.0
</pre>

These values should approximate the expected values as the number of ingested values gets larger.


## Fixed bins accumulator

To use the` FixedBinsAccumulator`, construct the `Tally` like this:

```java
tally = new Tally("Example tally with FixedBinsAccumulator using 1000 bins",
        new FixedBinsAccumulator(0.0005, 0.001, 1000));
```

The first argument of the constructor of the `FixedBinsAccumulator` is the center of the first bin; the second argument is the width of each bin and the final argument is the number of bins. We'll feed this tally one million uniformaly distributed values and then print the results of the first 10 bins (there are 1000 of them). The bin counts are not directly assessible, but the code derives them by retrieving the cumulative probabilities at the bin boundaries and multiplying by the total number of ingested values.

```java
random = new Random(1234);
for (int i = 0; i < 1000000; i++)
{
    tally.ingest(random.nextDouble());
}
System.out.println("0% quantile (should be close to 0.0):            " + tally.getQuantile(0.0));
System.out.println("25% quantile (should be close to 0.25):          " + tally.getQuantile(0.25));
System.out.println("50% quantile (should be close to 0.5):           " + tally.getQuantile(0.50));
System.out.println("100% quantile (should be close to 1.0):          " + tally.getQuantile(1.0));
for (int bin = 0; bin < 10; bin++) // only print the first 10 bin counts
{
    System.out.println("Number of values in bin " + bin + "                        " + (int) Math.rint(tally.getN()
            * (tally.getCumulativeProbability((bin + 1) / 1000.0) - tally.getCumulativeProbability(bin / 1000.0))));
}
```

The output of this code is:

<pre>
0% quantile (should be close to 0.0):            5.0E-4
25% quantile (should be close to 0.25):          0.2495
50% quantile (should be close to 0.5):           0.4995
100% quantile (should be close to 1.0):          0.9994999999999999
Number of values in bin 0                        1012
Number of values in bin 1                        1003
Number of values in bin 2                        996
Number of values in bin 3                        984
Number of values in bin 4                        968
Number of values in bin 5                        979
Number of values in bin 6                        979
Number of values in bin 7                        1012
Number of values in bin 8                        1006
Number of values in bin 9                        1022
</pre>

The expected number of values in each bin is 1000, but, because we only sampled a small sub set of the population; deviations from the expected number are to be expected.

As a demonstration of using a `Tally` for non-uniformly distributed values, have a look a this:

```java
tally = new Tally("Example tally with FixedBinsAccumulator using 1001 bins",
        new FixedBinsAccumulator(1.0, (Math.E - 1.0) / 1000, 1001));
```

We'll feed this `Tally` a set of exponentially distributed values in the interval `[1.0,e]`. The example code feeds the `Tally` one million and one values in numerically ascending order. This is done for simplicity of the code; the order in which the values are fed to this `Tally` does not affect the end result.

```java
// Feed the tally perfectly exponentially distributed values in the interval [1.0,e)
random = new Random(1234);
for (int i = 0; i <= 1000000; i++)
{
    tally.ingest(Math.exp(1.0 * i / 1000000));
}
System.out.println("0% quantile (should be 1.0):                     " + tally.getQuantile(0.0));
System.out.println("25% quantile (should be close to sqrt(sqrt(e))): " + tally.getQuantile(0.25));
System.out.println("50% quantile (should be close to sqrt(e)):       " + tally.getQuantile(0.50));
System.out.println("100% quantile (should be close to e):            " + tally.getQuantile(1.0));
for (int bin = 0; bin < 10; bin++)
{
    double value = 1 + bin / 10.0 * (Math.E - 1);
    System.out.println(String.format("Cumulative probability at %8f               ", value)
            + tally.getCumulativeProbability(value));
}
```

The output of this code is:

<pre>

0% quantile (should be 1.0):                     1.0
25% quantile (should be close to sqrt(sqrt(e))): 1.2835165016957424
50% quantile (should be close to sqrt(e)):       1.649510531157519
100% quantile (should be close to e):            2.718281828459045
Cumulative probability at 1.000000               4.294995705004295E-4
Cumulative probability at 1.171828               0.15856484143515856
Cumulative probability at 1.343656               0.2953942046057954
Cumulative probability at 1.515485               0.41573508426491573
Cumulative probability at 1.687313               0.5231369768630232
Cumulative probability at 1.859141               0.6201143798856201
Cumulative probability at 2.030969               0.7085122914877086
Cumulative probability at 2.202797               0.7897272102727898
Cumulative probability at 2.374625               0.8648391351608649
Cumulative probability at 2.546454               0.9347010652989346
</pre>


## TDigest accumulator

The `TDigestAccumulator` is based on the [algorithm by Ted Dunning](https://arxiv.org/abs/1902.04023). To use a `TDigestAccumulator`, construct the `Tally` like this:

```java
Tally tally = new Tally("Example tally with TDigest accumulator", new TDigestAccumulator());
random = new Random(1234);
for (int i = 0; i < 1000; i++)
{
    tally.ingest(random.nextDouble());
}
```

(This example uses uniformly distributed values for the demonstration code.) Unlike the previous quantile accumulators, the final state of the `TDigestAccumulator` does slightly depend on the order in which the values are fed to the `Tally`. The output for the quantiles is now

<pre>
first quartile:                0.229876868954619
median:                        0.4751306678330976
third quartile:                0.7507889885484719
</pre>

These values match the results of the `FullStorageAccumulator` (the ground truth) within `0.001`. For most applications such differences won't matter. For higher precision (at the cost of more memory and CPU-time), the TDigestAccumulator can be constructed with an integer argument (the `compression` setting) like:

```java
Tally tally = new Tally("Example tally with TDigest accumulator with higher precision", new TDigestAccumulator(1000));
```

The default value for the compression is `100`. The output using the TDigestAccumulator with compression set to 1000 is:

<pre>
first quartile:                0.22962779231833413
median:                        0.4753812701997516
third quartile:                0.7517244282398716
</pre>

This matches the output of the `FullStorageAccumulator` within `0.0002`.