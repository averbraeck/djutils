# DJUTILS Project

## What is in the DJUTILS main project?

These are light-weight projects that do not depend on huge external libraries and can be used in all kinds of applications.

*  DJUTILS provides a framework for [decoding and dumping data](https://djutils.org/manual/djutils/decoderdumper) with decoders for hexadecimal and base64 data.
*  DJUTILS contains [immutable collections](https://djutils.org/manual/djutils/immutable-collections), such as the ImmutableList, ImmutableSet and ImmutableMap.
*  DJUTILS [events](https://djutils.org/manual/djutils/event-package) is a framework for publish and subscribe communication, including remote event handling.
*  DJUTILS offers an implementation of [complex numbers and complex math](https://djutils.org/manual/djutils/complex).
*  DJUTILS offers several helper classes for [logging](https://djutils.org/manual/djutils/logging) using the tinylog package.
*  DJUTILS provides several classes for handling [exceptions](https://djutils.org/manual/djutils/exceptions) in an easy way, such as Throw and Try.
*  DJUTILS has a class to easily [resolve URLs](https://djutils.org/manual/djutils/urlresource) from a resource location, also when the resource is in a JAR file.
*  DJUTILS extends the [reflection classes](https://djutils.org/manual/djutils/reflection) of Java with several new classes to easily work with Method, Field, and Class signatures.
*  DJUTILS has a package that implements computation of [three kinds of mean value](https://djutils.org/manual/djutils/means).


## Maven use

Maven is one of the easiest ways to include DJUTILS in a Java project. The Maven files for DJUTILS reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils</artifactId>
    <version>2.0.1</version>
  </dependency>
</dependencies>
```

Of course, the version number (2.0.1 in the above example) needs to be replaced with the version that one wants to include in the project.

DJUTILS jars before version 2 are kept on a server at TU Delft at [https://djutils.org/maven](https://djutils.org/maven).


## Dependencies

DJUTILS is directly dependent on one package, which can have further dependencies:

* [tinylog](https://tinylog.org/v1/) for providing an easy-to-use and fast logger.

If the DJUTILS library is used as a part of a Maven project, all dependencies will be automatically resolved, and the programmer / user does not have to worry about finding the libraries.


## Documentation and test reports

DJUTILS documentation and test reports for the current version can be found at [https://djutils.org/docs/latest/djutils](https://djutils.org/docs/latest/djutils) and the API can be found at [https://djutils.org/docs/latest/djutils/apidocs/index.html](https://djutils.org/docs/latest/djutils/apidocs/index.html).

