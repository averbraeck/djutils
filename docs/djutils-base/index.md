# DJUTILS Project

## What is in the DJUTILS main project?

These are light-weight projects that do not depend on huge external libraries and can be used in all kinds of applications.

*  DJUTILS provides a framework for [decoding and dumping data](decoder-dumper) with decoders for hexadecimal and base64 data.
*  DJUTILS contains [immutable collections](immutable-collections), such as the ImmutableList, ImmutableSet and ImmutableMap.
*  DJUTILS [events](event) is a framework for publish and subscribe communication, including remote event handling.
*  DJUTILS offers several helper classes for [logging](logging) using the slf4j and logback packages.
*  DJUTILS provides several classes for data analysis, math, function evaluation and statistics.
*  DJUTILS has a class to easily [resolve URLs](urlresource) from a resource location, also when the resource is in a JAR file.
*  DJUTILS extends the [reflection classes](reflection) of Java with several new classes to easily work with Method, Field, and Class signatures.


## Maven use

Maven is one of the easiest ways to include DJUTILS in a Java project. The Maven files for DJUTILS reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-base</artifactId>
    <version>2.4.0</version>
  </dependency>
</dependencies>
```

Of course, the version number (2.4.0 in the above example) needs to be replaced with the version that one wants to include in the project.

Note that djutils is compliant with Java-17 from version 2.3.0 onward.

DJUTILS jars before version 2 are kept on a server at TU Delft at [https://djutils.org/maven](https://djutils.org/maven).


## Dependencies

DJUTILS-BASE is directly dependent on the following packages, which can have further dependencies:

* [slf4j](https://www.slf4j.org/) for providing an easy-to-use and fast logger facade.
* [logback](https://logback.qos.ch/) for providing an easy-to-use and fast logger implementation.

If the DJUTILS library is used as a part of a Maven project, all dependencies will be automatically resolved, and the programmer / user does not have to worry about finding the libraries.


## Documentation and test reports

DJUTILS documentation and test reports for the current version can be found at [https://djutils.org/docs/latest/djutils](https://djutils.org/docs/latest/djutils) and the API can be found at [https://djutils.org/docs/latest/djutils/apidocs/index.html](https://djutils.org/docs/latest/djutils/apidocs/index.html).

