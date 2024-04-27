# DJUTILS STATS project

The STATS project contains packages that consume values and compute statistical properties of those values.

The project consists of four packages:

* stats: interface for confidence interval and a table of the normal distribution with methods to compute cumulative probability and the inverse thereof
* summarizers: classes that ingest values, gather minimum, maximum, count number of values and compute mean, variance, etc.
* summarizers.event: clases for event ingesting summarizers
* summarizers.quantileaccumulator: classes that compute, or estimate quantiles (values that correspond to a cumulative probability)


## Maven use

Maven is one of the easiest ways to include DJUTILS-STATS in a Java project. The Maven files for DJUTILS-STATS reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-STATS:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-stats</artifactId>
    <version>2.2.1</version>
  </dependency>
</dependencies>
```

Of course, the version number (2.2.1 in the above example) needs to be replaced with the version that one wants to include in the project.

DJUTILS-STATS jars before version 2 are kept on a server at TU Delft at [https://djutils.org/maven](https://djutils.org/maven).



## Dependencies

DJUTILS-STATS is directly dependent on one external package

```xml
    <dependency>
      <groupId>com.tdunning</groupId>
      <artifactId>t-digest</artifactId>
      <version>3.2</version>
    </dependency>
```

This package will automatically be included when djutils-stats is provided as a dependency for a project.

