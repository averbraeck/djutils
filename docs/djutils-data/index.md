# DJUTILS-DATA project

The DJUTILS DATA project contains packages for managing, writing and reading tabular structured data. Data can be written and read in several formats:
* [CSV](https://en.wikipedia.org/wiki/Comma-separated_values) (comma-separated values)
* [TSV](https://en.wikipedia.org/wiki/Tab-separated_values) (tab-separated values)
* [JSON](https://en.wikipedia.org/wiki/JSON) (JavaScript Object Notation)
* [XML](https://en.wikipedia.org/wiki/XML) (Extensible Markup Language)

This could be extended with storage in various databases, etc.

No matter what storage is used, the stored data is accompanied by some meta-data that describes the data in each column. This meta-data is needed to reconstruct the tabular structed data when reading. For CSV and TSV data, the meta-data is stored in a separate file, for JSON and XML, the meta-data is stored in the same file.

When in-memory, the data can be read and written using the functions defined in the java Interface. It is not possible to delete particular records. Of course, an entire in-memory table can be deleted by dropping all references to it (and letting the garbage collector do its work). A table on disk can be deleted by deleting the file(s).

## Maven use

Maven is one of the easiest ways to include DJUTILS-DATA in a Java project. The Maven files for reside at [https://djutils.org/maven](https://djutils.org/maven). When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-DATA:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-data</artifactId>
    <version>1.04.12</version>
  </dependency>
    ... other dependencies go here ...
</dependencies>
```

Of course, the version number (1.04.12 in the above example) needs to be replaced with the version that one wants to include in the project.

Right now, the DJUTILS-DATA files are kept on a server at TU Delft, and are not yet made available on Maven Central. Therefore, the repository location has to be specified separately in the Maven POM-file:

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
