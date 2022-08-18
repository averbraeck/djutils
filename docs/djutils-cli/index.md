# DJUTILS-CLI project

## What is in the DJUTILS CLI project?

DJUTILS-CLI makes it easy to extend a Java program with a `public static main()` method into a program with settable parameters and a help screen, so it can be called with these parameters, e.g. as:

```text
java -jar FederateStarter.jar --port=5000 --server=local
```

What makes it easy to use is that this will set an integer field called 'port' to the value 5000, and a String field called server to the value "local". In addition, default values are available for parameters not explicitly set by a user, and the program can print an overview of the parameters and their meaning. All it takes is an `@Option` annotation for the field in the main class:

```java
@Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "80")
private int port;
```


## Maven use

Maven is one of the easiest ways to include DJUTILS-CLI in a Java project. The Maven files for DJUTILS-CLI reside at [https://djutils.org/maven](https://djutils.org/maven). When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-cli</artifactId>
    <version>1.04.12</version>
  </dependency>
</dependencies>
```

Of course, the version number (1.04.12 in the above example) needs to be replaced with the version that one wants to include in the project.

Right now, the DJUTILS-CLI files are kept on a server at TU Delft, and are not yet made available on Maven Central. Therefore, the repository location has to be specified separately in the Maven POM-file:

```xml
<repositories>
  <repository>
    <name>djutils Public Repository</name>
    <id>djutils</id>
    <url>https://djutils.org/maven</url>
  </repository>
</repositories>
```


## Dependencies

DJUTILS is directly dependent on the following packages, which can have further dependencies:

* [djutils](https://djutils.org/manual/djutils-project) for generic classes to support djutils-cli.
* [djunits](https://djunits.org/manual/) for being able to specify command line parameters with usits, such as `--length=20.5cm`.
* [picocli](https://picocli.info/) that does the actual work for specifying and parsing the command line parameters

If the DJUTILS-CLI library is used as a part of a Maven project, all dependencies will be automatically resolved, and the programmer / user does not have to worry about finding the libraries.


## Documentation and test reports

DJUTILS-CLI documentation and test reports for the current version can be found at [https://djutils.org/docs/current/djutils-cli](https://djutils.org/docs/current/djutils-cli) and the API can be found at [https://djutils.org/docs/current/djutils-cli/apidocs/index.html](https://djutils.org/docs/current/djutils-cli/apidocs/index.html).
