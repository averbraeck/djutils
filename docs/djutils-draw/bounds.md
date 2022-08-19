# Bounds

A `Bounds` object stores the spatial extent of one or more `Drawable` objects. `Bounds` objects come in two types: `Bounds2d` for two-dimensional `Drawable` objects and `Bounds3d` for three-dimensional `Drawable` objects. A `Bounds` object may contain infinity values. `Bounds` objects are intended to quickly assess whether any part of a `Drawable` might intersect (be visible in) the space that is currently in view.

All `Drawable` objects can produce their `Bounds` object with the `getBounds()` method (in fact, even a `Bounds` objects implement `getBounds()`, simply returning `this`). It is also possible to create a `Bounds` object directly. There are a couple of constructors:

```java
    Bounds2d b1 = new Bounds2d(3.4, 6.7, -5.6, 2.3); // Arguments are the absolute minimum and maximum values
    System.out.println("b1: " + b1);
    Bounds2d b2 = new Bounds2d(12.3, 23.4); // Arguments are ranges, symmetrically around 0.0
    System.out.println("b2: " + b2);
    PolyLine2d line = new PolyLine2d(new Point2d(1, 2), new Point2d(3, 4), new Point2d(-5, 12));
    Bounds2d b3 = new Bounds2d(line.getPoints()); // Argument is Iterator&lt;Point2d&gt;
    System.out.println("b3: " + b3);
    Bounds2d b4 = line.getBounds(); // Of course, the PolyLine2d can create a Bounds2d by itself
    System.out.println("b4: " + b4);
    Point2d[] pointArray = new Point2d[] { new Point2d(1, 2), new Point2d(3, 4), new Point2d(-5, 12) };
    Bounds2d b5 = new Bounds2d(pointArray);
    System.out.println("b5: " + b5);
    Collection<Drawable2d> drawableCollection = new LinkedHashSet<>();
    drawableCollection.add(new Point2d(1, 2));
    drawableCollection.add(new Point2d(3, 4));
    drawableCollection.add(new Point2d(-5, 12));
    Bounds2d b6 = new Bounds2d(drawableCollection);
    System.out.println("b6: " + b6);
    Bounds2d b7 = new Bounds2d(new Point2d(1, 2), new LineSegment2d(3, 4, -5, 12));
    System.out.println("b7: " + b7);
```

This example code prints:

<pre>
b1: Bounds2d [absoluteX[3.400000 : 6.700000], absoluteY[-5.600000 : 2.300000]]
b2: Bounds2d [absoluteX[-6.150000 : 6.150000], absoluteY[-11.700000 : 11.700000]]
b3: Bounds2d [absoluteX[-5.000000 : 3.000000], absoluteY[2.000000 : 12.000000]]
b4: Bounds2d [absoluteX[-5.000000 : 3.000000], absoluteY[2.000000 : 12.000000]]
b5: Bounds2d [absoluteX[-5.000000 : 3.000000], absoluteY[2.000000 : 12.000000]]
b6: Bounds2d [absoluteX[-5.000000 : 3.000000], absoluteY[2.000000 : 12.000000]]
b7: Bounds2d [absoluteX[-5.000000 : 3.000000], absoluteY[2.000000 : 12.000000]]
</pre>

A `Bounds` object implements `Drawable`. Therefore it can produce an iterator that produces a series of `Point` objects. These points are the vertices of the bound space (2D or 3D as appropriate). The iterator of a `Bounds2d` will produce 4 `Point2d` objects; the iterator of a `Bounds3d` will produce 8 `Point3d` objects.

```java
    Bounds2d bounds = new Bounds2d(new Ray2d(1, 2, Math.toRadians(45)));
    for (Iterator<Point2d> iterator = bounds.getPoints(); iterator.hasNext();)
    {
        System.out.println(iterator.next());
    }
```

Outputs:

<pre>
Point2d [x=1.000000, y=2.000000]
Point2d [x=1.000000, y=Infinity]
Point2d [x=Infinity, y=2.000000]
Point2d [x=Infinity, y=Infinity]
</pre>

Please do not write code that depends on the order in which this iterator produces those points.

The actual boundaries of a `Bounds` object can be accessed with methods and by directly reading the internal fields. E.g. `getMinX()` returns the minimum x value, which can also be obtained by accessing the `minX` field.


## Using a `Bounds` object

`Bounds` objects implement a couple of set-like operations that all return a boolean value:

|Method|Description|
|----------|--------------|
|`contains(double x, double y`[`, double z`]`)`|Report whether a the Point with these coordinates is contained within the `Bounds` (on an edge/surface is considered outside)|
|`contains(Point point)`|Report whether a `Point` is contained within the `Bounds` (on an edge/surface is considered outside)|
|`contains(Drawable drawable)`|Report whether a `Drawable` is completely contained in the `Bounds` (no `Point` outside, or on an edge/surface)|
|`covers(double x, double y`[`, double z`]`)`|Report wether the Point with these coordinates is contained within the `Bounds`, or on an edge/surface of the `Bounds`|
|`covers(Point point)`|Report whether a `Point` is contained within the `Bounds`, or on an edge/surface of the `Bounds`|
|`covers(Drawable drawable)`|Report whether no part of a `Drawable` is outside the `Bounds`|
|`disjoint(Bounds otherBounds)`|Report whether this `Bounds` is disjoint from another `Bounds`. Only touching an edge/surface is considered disjoint|
|`intersects(Bounds otherBounds)`|Report whether this `Bounds` object intersects another `Bounds` object. Only touching an edge/surface is not considered intersecting|

The method `intersection(Bounds otherBounds)` creates a new `Bounds` object that is the intersection of both. If the `Bounds` objects do not intersect (or only touch), this method returns `null`.
