# DJUTILS STATS project

The STATS project contains packages that consume values and compute statistical properties of those values.

The project consists of four packages:

* stats: interface for confidence interval and a table of the normal distribution with methods to compute cumulative probability and the inverse thereof
* summarizers: classes that ingest values, gather minimum, maximum, count number of values and compute mean, variance, etc.
* summarizers.event: clases for event ingesting summarizers
* summarizers.quantileaccumulator: classes that compute, or estimate quantiles (values that correspond to a cumulative probability)


## Maven use

Maven is one of the easiest ways to include DJUTILS-SERIALIZATION in a Java project. The Maven files for reside at [https://djutils.org/maven](https://djutils.org/maven). When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-SERIALIZATION:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-serialization</artifactId>
    <version>1.04.12</version>
  </dependency>
    ... other dependencies go here ...
</dependencies>
```

Of course, the version number (1.04.12 in the above example) needs to be replaced with the version that one wants to include in the project.

Right now, the DJUTILS-SERIALIZATION files are kept on a server at TU Delft, and are not yet made available on Maven Central. Therefore, the repository location has to be specified separately in the Maven POM-file:

```xml
<repositories>
  <repository>
    <name>djutils Public Repository</name>
    <id>djutils</id>
    <url>https://djutils.org/maven</url>
  </repository>
    ... other repositories go here ...
</repositories>
```


## Dependencies

DJUTILS-STATS is directly dependent on one external package

```xml
    <dependency>
      <groupId>com.tdunning</groupId>
      <artifactId>t-digest</artifactId>
      <version>3.2</version>
    </dependency>
```

