# Drawable interface

The `Drawable` interface specifies some basic properties of all drawable objects.

It specifies that each `Drawable` object shall implement a method `getPoints()` that returns an `Iterator<? extends Point>` that will yield a series of `Point` objects (or some subclass thereof) that describe the `Drawable`. As that description is self-referencing and therefore confusing, let us give a few examples:

```java
    Point2d p = new Point2d(12.34, 23.45);
    System.out.println("The iterator of a point yields one point (the point itself):");
    for (Iterator<? extends Point2d> iterator = p.getPoints(); iterator.hasNext();)
    {
        System.out.println(iterator.next());
    }
    LineSegment2d ls = new LineSegment2d(12.34, 23.45, 34.56, 45.67);
    System.out.println("The iterator of a line segment yields two points (the start point and the end point):");
    for (Iterator<? extends Point2d> iterator = ls.getPoints(); iterator.hasNext();)
    {
        System.out.println(iterator.next());
    }
    Bounds3d b = new Bounds3d(12.34, 23.45, 34.56, 45.67, 56.78, 67.89);
    System.out.println("The iterator of a bounds3d yields eight points (the vertices of the bounds):");
    for (Iterator<? extends Point3d> iterator = b.getPoints(); iterator.hasNext();)
    {
        System.out.println(iterator.next());
    }
```
Outputs:

<pre>
The iterator of a point yields one point (the point itself):
Point2d [x=12.340000, y=23.450000]
The iterator of a line segment yields two points (the start point and the end point):
Point2d [x=12.340000, y=23.450000]
Point2d [x=34.560000, y=45.670000]
The iterator of a bounds3d yields eight points (the vertices of the bounds):
Point3d [x=12.340000, y=34.560000, z=56.780000]
Point3d [x=12.340000, y=34.560000, z=67.890000]
Point3d [x=12.340000, y=45.670000, z=56.780000]
Point3d [x=12.340000, y=45.670000, z=67.890000]
Point3d [x=23.450000, y=34.560000, z=56.780000]
Point3d [x=23.450000, y=34.560000, z=67.890000]
Point3d [x=23.450000, y=45.670000, z=56.780000]
Point3d [x=23.450000, y=45.670000, z=67.890000]
</pre>


Please do not write code that depends on the order in which the iterator of a `Bounds` yields those points. The iterator of a `PolyLine` is guaranteed to yields the points in the order that these appear in the `PolyLine`. The iterator of a `LineSegment` is guaranteed to yield two points; first the start point and then the end point.

A `Drawable` must also implement a `size()` method that returns the number of points that the iterator will produce.

Any `Drawable` object can report the number of dimensions that it stores:

```java
    Point2d point2d = new Point2d(12.34, 23.45);
    System.out.println("2D object has two dimensions: " + point2d.getDimensions());
    Point3d point3d = new Point3d(12.34, 23.45, 34.56);
    System.out.println("3D object has three dimensions: " + point3d.getDimensions());
```

Outputs:

<pre>
2D object has two dimensions: 2
3D object has three dimensions: 3
</pre>


Finally, the `Drawable` interface demands implementation of a couple of `toString` methods:

`toString(String doubleFormat, boolean doNotIncludeClassName)`

`toString(String doubleFormat)`

`toString(boolean doNotIncludeClassName)`

The second and third variant have default implementations that call the first one. The first one is the most versatile. The `doubleFormat` argument should be something like `%6.3f`. This overrides the default (`%f`) format used to format the coordinate values and angles. When the `doNotIncludeClassName` argument is true; the name of the class is not included in the output. When false, the name of the class <i>is</i> included in the output.

Regrettably, it is not possible to add a default implementation for de zero-argument `toString()` method. This should be implemented explicitly in all classes that implement `Drawable`. The implementation usually looks like:

```java
public String toString()
{
    return toString("%f", false);
}
```
This implementation lets the two-parameter version do the real work.

None of the `Drawable` implementing classes implements it directly. In stead, these classes implement an extension of `Drawable`. The `Drawable2d` interface extends the `Drawable` interface by specifying implementation of a `getBounds()` method that must return a `Bounds2d` object. The `Drawable3d` interface extends the `Drawable` interface by specifying implementation of a `getBounds()` method that must return a `Bounds3d` object.

Therefore, all `Drawable` objects implement a `getBounds()` method that returns a suitable `Bounds` object. As a `Bounds2d` object implements `Drawable2d` it implements `getBounds()` (which simply returns `this`).