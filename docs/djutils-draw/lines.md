# Lines

The DJUTILS-DRAW line package defines several line-like objects:
* PolyLine: lines with two end points and zero or more intermediate points.
* Polygon: closed PolyLine with at least two points.
* LineSegment: line segment defined by two points. This much like a PolyLine with no intermediate points.
* Ray: line with one finite end point; going all the way to infinity in the _other_ direction.

All line types exist in 2D and 3D versions. Additionally there is the `Bezier` class that generates `PolyLine` objects in 2D or 3D. More such generating classes will be added. E.g. for ellipses or segments thereof, [clothoids](https://en.wikipedia.org/wiki/Euler_spiral).

## PolyLine

A `PolyLine` can be constructed from a collection of suitable `Point` objects (a `PolyLine2d` can be constructed from `Point2d` objects). It can also be constructed from a `double[]` (double array) for each of the dimensions. The following code shows four ways to construct the exact same `PolyLine2d` object:

```java
    PolyLine2d pl1 = new PolyLine2d(new Point2d(1, 2), new Point2d(3, 4), new Point2d(20, -5));
    System.out.println(pl1);
    Point2d[] pointArray = new Point2d[] {new Point2d(1, 2), new Point2d(3, 4), new Point2d(20, -5)};
    PolyLine2d pl2 = new PolyLine2d(pointArray);
    System.out.println(pl2);
    double[] x = new double[] { 1, 3, 20 };
    double[] y = new double[] { 2, 4, -5 };
    PolyLine2d pl3 = new PolyLine2d(x, y);
    System.out.println(pl3);
    List<Point2d> pointList = new ArrayList<>();
    pointList.add(new Point2d(1, 2));
    pointList.add(new Point2d(3, 4));
    pointList.add(new Point2d(20, -5));
    PolyLine2d pl4 = new PolyLine2d(pointList);
    System.out.println(pl4);
```

This outputs:

<pre>
PolyLine2d [x=1.000000, y=2.000000, x=3.000000, y=4.000000, x=20.000000, y=-5.000000]
PolyLine2d [x=1.000000, y=2.000000, x=3.000000, y=4.000000, x=20.000000, y=-5.000000]
PolyLine2d [x=1.000000, y=2.000000, x=3.000000, y=4.000000, x=20.000000, y=-5.000000]
PolyLine2d [x=1.000000, y=2.000000, x=3.000000, y=4.000000, x=20.000000, y=-5.000000]
</pre>

A `PolyLine2d` can also be constructed from an `Iterator<Point2d>` and even from a `java.awt.geom.Path2d` object, provided it only contains SEG_MOVETO and SEG_LINETO segments. If there is a SEG_CLOSE segment, anything after that is ignored. Any other segment types will cause the constructor to throw a `DrawRuntimeException`.

A `PolyLine3d` is very much like a `PolyLine2d`, except that it has z-coordinates and cannot be constructed from a `java.awt.geom.Path2d` object.

An attempt to create a PolyLine with two _successive_, _idential_ points will fail with a `DrawRuntimeException`. Several `PolyLine` objects can be concatenated, provided the end point of each one matches the first point of the next. There is also a `concatenate` operation that allows an error margin for concatenation.
The `noiseFilteredLine` constructor allows a `PolyLine` to be constructed from a list or array of points while filtering out intermediate points that are within a specified margin from the preceding point.

The total length of a `PolyLine` can be obtained with the` getLength()` method. The number of points that make up a `PolyLine` can be obtained with the `size()` method. The individual points with the `get(int)` method; calling this method creates a new `Point` object. The start point can be obtained with the `getFirst()` method; the end point with the `getLast()` method. Each double coordinate value can be directly obtained (without creating a `Point` object) with `getX(int)`, `getY(int)` and (only for `PolyLine3d`) `getZ(int)`.
The cumulative length up to any of the points can be obtained with the `lengthAtIndex(int)` method. The result of `lengthAtIndex(0)` is always 0.0; the result of `lengthAtIndex(size() - 1)` is equal to `getLength()`.

A fragment of a `PolyLine` can be created with the `extract(double, double)` method. The parameters specify the distance from the start point and must be ordered by distance and be within the length of the `PolyLine`. The `extractFractional` method does the same, but the parameters are specified as fractions of the length of the `PolyLine`:

```java
    PolyLine2d polyLine2d = new PolyLine2d(new Point2d(1, 1), new Point2d(5, 1), new Point2d(5, 2), new Point2d(9, 5));
    System.out.println("PolyLine: " + polyLine2d);
    System.out.println("length: " + polyLine2d.getLength());
    System.out.println("fragment: " + polyLine2d.extract(2.0, 9.0));
    System.out.println("fragment: " + polyLine2d.extractFractional(0.2, 0.9));
```

Prints

<pre>
PolyLine: PolyLine2d [x=1.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=9.000000, y=5.000000]
length: 10.0
fragment: PolyLine2d [x=3.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=8.200000, y=4.400000]
fragment: PolyLine2d [x=3.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=8.200000, y=4.400000]
</pre>

Any location along the `PolyLine` can be obtained with the `getLocation(double)` method. The parameter must be between `0.0` and the length of the `PolyLine`. Extrapolation is also possible with the `getLocationExtended` method:

```java
    System.out.println("PolyLine: " + polyLine2d);
    System.out.println("location at distance 7.0: " + polyLine2d.getLocation(7.0));
    System.out.println("extended location at distance 15: " + polyLine2d.getLocationExtended(15.0));
    System.out.println("extended location at distance -8: " + polyLine2d.getLocationExtended(-8.0));
```

Prints

<pre>
PolyLine: PolyLine2d [x=1.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=9.000000, y=5.000000]
location at distance 7.0: Ray2d [x=6.6 y=3.2 phi=0.6435011087932843]
extended location at distance 15: Ray2d [x=13.0 y=8.0 phi=0.6435011087932844]
extended location at distance -8: Ray2d [x=-7.0 y=1.0 phi=0.0]
</pre>

As you can see, the `getLocation` and `getLocationExtended` methods return `Ray` objects. A ray is a point with a direction. The `getLocation` method sets the direction of the ray to the direction of the `PolyLine` at the requested point. Extrapolation for negative distances is done in the direction of the first segment of the `PolyLine`. Extrapolation after the length of the `PolyLine` uses the direction of the last segment of the `PolyLine`. If the location is at one of the intermediate points of the `PolyLine`, the direction of the returned ray may depend on rounding errors; it is either the direction of the preceding segment, or that of the succeeding segment.

A `PolyLine` has a method to extract any of the segments from which it is constructed with the` getSegment(int)` method. the parameter selects the first point of the segment. The valid range of this parameter is `0..size() - 2`.

```java
    System.out.println("PolyLine: " + polyLine2d);
    for (int index = 0; index < polyLine2d.size() - 1; index++)
    {
        System.out.println("segment " + index + ": " + polyLine2d.getSegment(index));
    }
```

Prints:

<pre>
PolyLine: PolyLine2d [x=1.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=9.000000, y=5.000000]
segment 0: LineSegment2d [startX=1.000000, startY=1.000000 - endX= 5.0, endY=1.000000]
segment 1: LineSegment2d [startX=5.000000, startY=1.000000 - endX= 5.0, endY=2.000000]
segment 2: LineSegment2d [startX=5.000000, startY=2.000000 - endX= 9.0, endY=5.000000]
</pre>

Often, it is necessary to find the point on a `PolyLine` that is closest to a given `Point`. This can be obtained with the `closestPointOnPolyline` method. In some cases one is only interested in the closest point if it is _not_ on one of the vertices of the `PolyLine`. In that case, one of the `projectOrthogonal` methods can be used. The `projectOrthogonal` methods return null if there is a closer point, but that projection is not orthogonal to the line segment on which it lies.

```java
    System.out.println("PolyLine: " + polyLine2d);
    System.out.println("closest point to (0,1): " + polyLine2d.closestPointOnPolyLine(new Point2d(0, 1)));
    System.out.println("closest point to (6,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(6, 0)));
    System.out.println("closest point to (10,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(10, 0)));
    System.out.println("closest point to (50,0): " + polyLine2d.closestPointOnPolyLine(new Point2d(50, 0)));
    System.out.println("project (0,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(0, 0)));
    System.out.println("project (4,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(4, 0)));
    System.out.println("project (5,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(5, 0)));
    System.out.println("project (6,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(6, 0)));
    System.out.println("project (10,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(10, 0)));
    System.out.println("project (50,0) orthogonal: " + polyLine2d.projectOrthogonal(new Point2d(50, 0)));
    System.out.println("project (50,0) orthogonal extended: " + polyLine2d.projectOrthogonalExtended(new Point2d(50, 0)));
```

Prints:

<pre>
PolyLine: PolyLine2d [x=1.000000, y=1.000000, x=5.000000, y=1.000000, x=5.000000, y=2.000000, x=9.000000, y=5.000000]
closest point to (0,1): Ray2d [x=1.0 y=1.0 phi=0.0]
closest point to (6,0): Ray2d [x=5.0 y=1.0 phi=1.5707963267948966]
closest point to (10,0): Ray2d [x=7.24 y=3.6800000000000006 phi=0.6435011087932843]
closest point to (50,0): Ray2d [x=9.0 y=5.0 phi=0.6435011087932844]
project (0,0) orthogonal: null
project (4,0) orthogonal: Ray2d [x=4.0 y=1.0 phi=0.0]
project (5,0) orthogonal: Ray2d [x=5.0 y=1.0 phi=1.5707963267948966]
project (6,0) orthogonal: null
project (10,0) orthogonal: Ray2d [x=7.24 y=3.6800000000000006 phi=0.6435011087932843]
project (50,0) orthogonal: null
project (50,0) orthogonal extended: Ray2d [x=32.839999999999996 y=22.879999999999995 phi=0.6435011087932844]
</pre>

There are also variants of these methods that return a fractional location along the `PolyLine`. A fractional location is a value that is scaled by dividing it by the length of the `PolyLine`. The result of those methods is a double value. For results that are within the range of the `PolyLine`, that value is in the range `0.0 .. 1.0`. The `extended` variants can return values outside that range. If there is no valid result, these methods return `Double.NaN`.

!!! todo
    Explain the offsetLine method of PolyLine2d.


## LineSegment

A `LineSegment` is effectively a `PolyLine` with only two end points and no intermediate points. A `LineSegment` can be constructed from two `Point` objects, or one `Point` and one set of coordinates, or two sets of coordinates:

```java
    LineSegment2d ls1 = new LineSegment2d(new Point2d(1, 2), new Point2d(5, 0));
    System.out.println("ls1: " + ls1);
    LineSegment2d ls2 = new LineSegment2d(new Point2d(1, 2), 5, 0);
    System.out.println("ls2: " + ls2);
    LineSegment2d ls3 = new LineSegment2d(1, 2, new Point2d(5, 0));
    System.out.println("ls3: " + ls3);
    LineSegment2d ls4 = new LineSegment2d(1, 2, 5, 0);
    System.out.println("ls4: " + ls4);
```

Prints:

<pre>
ls1: LineSegment2d [startX=1.000000, startY=2.000000 - endX= 5.0, endY=0.000000]
ls2: LineSegment2d [startX=1.000000, startY=2.000000 - endX= 5.0, endY=0.000000]
ls3: LineSegment2d [startX=1.000000, startY=2.000000 - endX= 5.0, endY=0.000000]
ls4: LineSegment2d [startX=1.000000, startY=2.000000 - endX= 5.0, endY=0.000000]
</pre>

The coordinates of the start and end points can be accessed directly as the `startX`, `startY`, `endX` and `endY` fields (and, in case of a LineSegment3d, the `startZ` and `endZ` fields). The start and end points can also be obtained with `getStartPoint()` and `getEndPoint()`. These methods construct a new `Point`.

Like the `PolyLine` classes, the `LineSegment` classes implement the `closestPointOnSegment`, `projectOrthogonal`, `getLocation` and `getLocationExtended` methods.


## Ray

A ray is a line with one finite end point. In the other direction a ray continues to infinity. In DJUTILS-DRAW, rays are available in 2D and 3D versions. The constructors take the (coordinates of the) finite end point and an additional _through point_, or a direction (just `phi` to construct a `Ray2d`, or `phi` and `theta` to construct a `Ray3d`). There are six main ways to construct a Ray:

```java
    Ray2d r1 = new Ray2d(new Point2d(1, 2), new Point2d(1, 6));
    System.out.println(r1);
    Ray2d r2 = new Ray2d(1, 2, 1, 6);
    System.out.println(r2);
    Ray2d r3 = new Ray2d(new Point2d(1, 2), 1, 6);
    System.out.println(r3);
    Ray2d r4 = new Ray2d(1, 2, new Point2d(1, 6));
    System.out.println(r4);
    Ray2d r5 = new Ray2d(1, 2, Math.PI / 2);
    System.out.println(r5);
    Ray2d r6 = new Ray2d(new Point2d(1, 2), Math.PI / 2);
    System.out.println(r6);
```

Prints:

<pre>
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
Ray2d [x=1.0 y=2.0 phi=1.5707963267948966]
</pre>

The direction of a Ray2d is encoded as an angle (in Radians) from the X-axis. These examples all create a ray that points along the Y axis. The Ray3d classes simply take extra arguments (two z coordinates, or one z coordinate and `theta`; the angle from the Z-axis).

The coordinates of the finite end point can be directly accessed as the `x`, `y` (and for `Ray3d`) `z` fields. There are also getters for these: `getX()`, `getY()` (and for `Ray3d` `getZ()`). The field `phi` (and for Ray3d `theta`) is also directly accessible, or with the method `getPhi()` (and for `Ray3d` `getTheta()`). The finite end point can also be obtained with the `getEndPoint()` method.

Rays implement `getLocation` and `getLocationExtended` methods. The first takes a non-negative distance argument and returns a new `Ray` which is coincident with the original, but starts the distance value from the start point of the first ray. The `getLocationExtended` method allows the argument to be negative (and will return a new `Ray` which is coincident with the original, but starts the distance value along, or before the original `Ray`). The `closestPointOnRay` method takes a `Point` as parameter and returns a new `Ray` that starts at some point along the original ray which is closest to the given `Point`.

Like the `LineSegment` classes, `Ray` classes implement four `projectOrthogonal` methods. If the orthogonal projection of the given `Point` falls _before_ the finite end point of the `Ray`, the `projectOrthogonal` method returns null and the `projectOrthogonalFractional` method returns `Double.NaN`. The `projectOrthogonalExtended` method will return negative values if the given `Point` projects _before_ the finite end point of the `Ray` and the `projectOrthogonalFractionalExtended` method will return negative values if that happens.

The `Bounds` of a `Ray` can be obtained with the `getBounds()` method and is finite is some directions and infinite in others.

The `flip()` method creates a new `Ray` with the same finite end point, but pointing in the opposite direction. The `neg()` method creates a new `Ray` at a point that has all coordinates inverted from the original and pointing in the opposite direction.

The `Ray` classes implement `epsilonEquals` to compare similar rays for equality with specified tolerances for the coordinates and the angles. Before that comparison takes place, the angles are normalized.