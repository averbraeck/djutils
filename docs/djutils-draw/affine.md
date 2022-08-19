# Affine transformations

An [affine transformation](https://en.wikipedia.org/wiki/Affine_transformation) is a geometrical transformation that preserves lines (they remain straight). It may alter distances, angles, scales, orientations. Affine transforms are linear transformations and commonly implemented using matrix algebra.

The `Transform2d` class in djutils-draw performs affine transforms on 2D objects; the `Transform3d` class operates on 3D objects. Unlike all other objects in the djutils-draw package, the transform objects are mutable. The djutils-draw transformations are constructed as the identity transformation, then modified by applying some sequence of translations, rotations, shear-operations and reflections. As each of these modifications of a transform returns the resulting transform; these operations can be chained. Finally the transformation can operate on a `Point` or `Bounds` object or on an `Iterator<Point>`. (This will probably be extended to all `Drawable` objects.)

Setting up a `Transform2d` or `Transform3d` involves some math that is relatively expensive when rotations are involved. Subsequently applying such a transformation to coordinates is very fast as that only involves a few multiplications and additions.


## Translating and Rotating

In this example, a unity Transform3d object is constructed and then a translation is added to it, and then a rotation. The net effect on the `Point3d` is that is _first_ rotated and _then_ translated.

```java
    Point3d point = new Point3d(1, 0, 0);
    System.out.println(point);
    Transform3d transform1 = new Transform3d();
    transform1.translate(2, 5, 8); // Translate
    System.out.println(transform1.transform(point));
    transform1.rotY(Math.PI / 2);
    System.out.println(transform1.transform(point));
```

Outputs

<pre>
Point3d [x=1.000000, y=0.000000, z=0.000000]
Point3d [x=3.000000, y=5.000000, z=8.000000]
Point3d [x=2.000000, y=5.000000, z=7.000000]
</pre>


The resulting transformation could have been created in a single line of Java code using chaining:

```java
    Transform3d transform2 = new Transform3d().translate(2, 5, 8).rotY(Math.PI / 2);
    System.out.println(transform2.transform(point));
```

Outputs:

<pre>
Point3d [x=2.000000, y=5.000000, z=7.000000]
</pre>

Of course, the `Transform3d` class also implements rotations around the X and Z axes. As the `Transform2d` class only implements rotation perpendicular to the X-Y plane this method is simply named `rotation`.


## Shearing

The shear operator transforms a rectangle into a parallelogram. The `Transform2d` class implements one `shear(double, double)` method. The `Transform3d` class implements three shear methods: `shearXY(double, double)`, `shearYZ(double, double)` and `shearZX(double, double)`.

```java
    Transform2d transform = new Transform2d().shear(2, 3);
    for (int x = 0; x < 2; x++)
    {
        for (int y = 0; y < 2; y++)
        {
            Point2d p = new Point2d(x, y);
            System.out.println(p + " -> " + transform.transform(p));
        }
    }
```

Outputs:

<pre>
Point2d [x=0.000000, y=0.000000] -> Point2d [x=0.000000, y=0.000000]
Point2d [x=0.000000, y=1.000000] -> Point2d [x=2.000000, y=1.000000]
Point2d [x=1.000000, y=0.000000] -> Point2d [x=1.000000, y=3.000000]
Point2d [x=1.000000, y=1.000000] -> Point2d [x=3.000000, y=4.000000]
</pre>


The X-coordinates are incremented by the first parameter of the shear times the Y-coordinate. The Y-coordinates are incremented by the second parameter of the shear times the X-coordinate.

```java
    Transform3d transform = new Transform3d().shearXY(2, 3);
    for (int x = 0; x < 2; x++)
    {
        for (int y = 0; y < 2; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                Point3d p = new Point3d(x, y, z);
                System.out.println(p + " -> " + transform.transform(p));
            }
        }
    }
```

Outputs:

<pre>
Point3d [x=0.000000, y=0.000000, z=0.000000] -> Point3d [x=0.000000, y=0.000000, z=0.000000]
Point3d [x=0.000000, y=0.000000, z=1.000000] -> Point3d [x=2.000000, y=3.000000, z=1.000000]
Point3d [x=0.000000, y=1.000000, z=0.000000] -> Point3d [x=0.000000, y=1.000000, z=0.000000]
Point3d [x=0.000000, y=1.000000, z=1.000000] -> Point3d [x=2.000000, y=4.000000, z=1.000000]
Point3d [x=1.000000, y=0.000000, z=0.000000] -> Point3d [x=1.000000, y=0.000000, z=0.000000]
Point3d [x=1.000000, y=0.000000, z=1.000000] -> Point3d [x=3.000000, y=3.000000, z=1.000000]
Point3d [x=1.000000, y=1.000000, z=0.000000] -> Point3d [x=1.000000, y=1.000000, z=0.000000]
Point3d [x=1.000000, y=1.000000, z=1.000000] -> Point3d [x=3.000000, y=4.000000, z=1.000000]
</pre>

The `shearXY` operation does not modify the Z coordinates. The X-coordinates are incremented by the first parameter of the shear times the Z value. The Y-coordinates are incremented by the second parameter of the shear times the Z value. Similarly, there `shearYZ` operation preserves the X-coordinate values and the `shearZX` operation preserves the Y-coordinate values.


## Scaling

A transform can also perform scaling by independent factors along each axis.

```java
    Transform2d transform = new Transform2d().scale(4, 6);
    for (int x = 0; x < 2; x++)
    {
        for (int y = 0; y < 2; y++)
        {
            Point2d p = new Point2d(x, y);
            System.out.println(p + " -> " + transform.transform(p));
        }
    }
```

Outputs:

<pre>
Point2d [x=0.000000, y=0.000000] -> Point2d [x=0.000000, y=0.000000]
Point2d [x=0.000000, y=1.000000] -> Point2d [x=0.000000, y=6.000000]
Point2d [x=1.000000, y=0.000000] -> Point2d [x=4.000000, y=0.000000]
Point2d [x=1.000000, y=1.000000] -> Point2d [x=4.000000, y=6.000000]
</pre>


## Reflecting
The `Transform2d` object implements `reflectX()` and `reflectY()`. the `Transform3d` object additionally implements `reflectZ()`. These methods invert that particular coordinate.


## Transforming multi-point Drawable objects

All preceding examples of using transforms showed how a transform can be applied to a `Point`. Each `Drawable` object implements `getPoints()` which constructs an `Iterator<Point>` that will yields all points of the `Drawable`. Also, `Drawable` objects that are constructed from a varying number of points have a constructor that takes such an iterator. This can be used to construct a new transformed `Drawable` with a single line of code:

```java
    PolyLine2d line = new PolyLine2d(new Point2d(1, 2), new Point2d(2, 3), new Point2d(5, 0));
    System.out.println(line);
    Transform2d transform = new Transform2d().scale(2, 3);
    PolyLine2d transformedLine = new PolyLine2d(transform.transform(line.getPoints()));
    System.out.println(transformedLine);
```

Outputs:

<pre>
PolyLine2d [x=1.000000, y=2.000000, x=2.000000, y=3.000000, x=5.000000, y=0.000000]
PolyLine2d [x=2.000000, y=6.000000, x=4.000000, y=9.000000, x=10.000000, y=0.000000]
</pre>
