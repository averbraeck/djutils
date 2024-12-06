# DJUTILS SWING project

The SWING project contains packages that provide extension components for user interfacing with Java Swing.

The project consists of one package at the moment:

* multislider: a slider with multiple thumbs. The slider can contain an integer scale, real valued scale, unit scale, or categorial scale. Several restrictions can be set on the thumbs of the slider, e.g., whether they can pass each other.


## Maven use

Maven is one of the easiest ways to include DJUTILS-SWING in a Java project. The Maven files for DJUTILS-SWING reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-SWING:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-swing</artifactId>
    <version>2.3.0</version>
  </dependency>
</dependencies>
```

Of course, the version number (2.3.0 in the above example) needs to be replaced with the version that one wants to include in the project.


## Dependencies

DJUTILS-SWING is directly dependent on two external packages

```xml
    <dependency>
      <groupId>jakarta.annotation</groupId>
      <artifactId>jakarta.annotation-api</artifactId>
      <version>3.0.0</version>
    </dependency>

    <dependency>
      <groupId>org.djunits</groupId>
      <artifactId>djunits</artifactId>
      <version>5.2.0</version>
    </dependency>
```

This package will automatically be included when djutils-swing is provided as a dependency for a project.

