# DJUTILS TEST project

The TEST project contains helper classes for unit tests.

The project consists of the following classes at the moment:

* `ClassList`: a class that can check whether all classes in a package implement a given interface or method. 
* `ExceptionTest`: since all unit tests of an `Exception` are quite similar, this class tests the relevant methods.
* `UnitTest`: this class has the methods to do a `testFail(lambda)` method for a unit test instead of an extensive `try..catch`.


## Maven use

Maven is one of the easiest ways to include DJUTILS-TEST in a Java project. The Maven files for DJUTILS-TEST reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-TEST:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-test</artifactId>
    <version>2.4.0</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Of course, the version number (2.4.0 in the above example) needs to be replaced with the version that one wants to include in the project.


## Dependencies

DJUTILS-TEST is directly dependent on four external packages. junit is providing the unit test framework, and classgraph is providing methods to find all packages, classes, methods, fields and annotations on the classpath of a project.

```xml
    <dependency>
      <groupId>jakarta.annotation</groupId>
      <artifactId>jakarta.annotation-api</artifactId>
      <version>3.0.0</version>
    </dependency>

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.13.0</version>
    </dependency>    

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.13.0</version>
    </dependency>

    <dependency>
      <groupId>io.github.classgraph</groupId>
      <artifactId>classgraph</artifactId>
      <version>4.8.181</version>
    </dependency>
```

These packages will automatically be included when djutils-test is provided as a (test) dependency for a project.

