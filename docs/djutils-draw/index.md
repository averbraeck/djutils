# DJUTILS DRAW project

The DJUTILS-DRAW project contains packages for handling points, poly-lines, line-segments, rays, in 2D and 3D. All objects in DJUTILS-DRAW (except the Transformations) are [immutable](https://javadevcentral.com/benefits-of-immutable-class-in-java). This is in major contrast to the java.awt.geom package where almost everything is mutable. In DJUTILS-DRAW, all coordinates are internally stored as double values. We do not intend to provide float versions.

DJUTILS-DRAW consists of a number of packages:

* / (top level): The `DrawRuntimeException` class and:
    * [Drawable](drawable.md): the interface that all `Drawable` objects must implement.
    * Directed and Oriented: `Directed` is the interface that all `Ray` objects implement; the `Oriented` is the interface that all `OrientedPoint` objects implement.
    * [Affine transformations](affine.md): the `Transform2d` and `Transform3d` classes that implement transformations using affine matrices.
* [bounds package](bounds.md): The classes that can hold the minimum and maximum of the coordinates of a `Drawable` object.
* [point package](points.md): The classes for points and oriented points (2D and 3D).
* [line package](lines.md): The classes for `LineSegment`, `PolyLine`, `Ray`, `Polygon` and the Bezi&eacute;r approximators of `PolyLines`.
* surface package: Intended for various surface types and generators of those (very rudimentary at this time).
* volume package: Intended for 3D objects and generators of those (very rudimentary at this time).

All `Drawable` 3D objects implement a `project()` method that constructs a corresponding 2D object (where this might fail, the `project()` method may throw a `DrawRuntimeException`).

## Maven use

Maven is one of the easiest ways to include DJUTILS-DATA in a Java project. The Maven files for reside at [https://djutils.org/maven](https://djutils.org/maven). When a POM-file is created for the project, the following snippet needs to be included to include DJUTILS-DATA:

```xml
<dependencies>
  <dependency>
    <groupId>org.djutils</groupId>
    <artifactId>djutils-draw</artifactId>
    <version>1.04.12</version>
  </dependency>
    ... other dependencies go here ...
</dependencies>
```

Of course, the version number (1.04.12 in the above example) needs to be replaced with the version that one wants to include in the project.

Right now, the DJUTILS-DRAW files are kept on a server at TU Delft, and are not yet made available on Maven Central. Therefore, the repository location has to be specified separately in the Maven POM-file:

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
