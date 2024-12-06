# DJUTILS EVAL project

The EVAL project contains classes for making calculations based on a String with an expression, being able to parse the expression and return the value. It can do calculations like:

```
- "23.45*10"
- "NEUTRONMASS()-ELECTRONMASS()"
- "12 [ms-1] / 123[/s]"
- "12 [m/s] > 7 [m/s]"
- "(2>3)?5:1+100"
```


## Maven use

Maven is one of the easiest ways to include DJUTILS-EVAL in a Java project. The Maven files for DJUTILS-EVAL reside at Maven Central as of version 2.0.0. When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-EVAL:

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

DJUTILS-EVAL is directly dependent on one external package:

```xml
    <dependency>
      <groupId>org.djunits</groupId>
      <artifactId>djunits</artifactId>
      <version>5.2.0</version>
    </dependency>
```

This package will automatically be included when djutils-eval is provided as a dependency for a project.

