# DJUTILS MATH project

The MATH project contains classes for math calculations and functions. The project consists of several packages.

*  DJUTILS-MATH offers an implementation of [complex numbers](complex-numbers) and [complex math](complex-math).
*  DJUTILS-MATH has a package that implements computation of [three kinds of mean value](means).
*  DJUTILS-MATH can estimate the roots of polynomial functions.
*  DJUTILS-MATH can store and manipulate mathematical functions. 


## Maven use

Maven is one of the easiest ways to include DJUTILS-MATH in a Java project. The Maven files for DJUTILS-MATH reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-MATH:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-eval</artifactId>
    <version>2.3.0</version>
  </dependency>
</dependencies>
```

Of course, the version number (2.3.0 in the above example) needs to be replaced with the version that one wants to include in the project.


## Dependencies

DJUTILS-MATH is directly dependent on one external package:

```xml
  <dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>3.0.0</version>
  </dependency>
```

This package will automatically be included when djutils-eval is provided as a dependency for a project.

