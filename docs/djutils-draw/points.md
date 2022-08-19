# Points

A point is a 2D or 3D object that has a location, but no surface, or volume. An `OrientedPoint` is a point with an orientation. This orientation describes the rotations, relative to the reference orientation of the object, that must be applied to draw an object at its location.

In the code examples below, please note that the djutils `Point2d` class is written with a lower-case `d`; unlike the `Point2D` class in `java.awt.geom`.

To create a 2D point and print it write:

```java
    Point2d point2d = new Point2d(123.45, 234.56);
    System.out.println(point2d);
```

This outputs:

<pre>
Point2d [x=123.450000, y=234.560000]
</pre>

The individual coordinates can be retrieved by calling the `getX` and `getY` methods, but they are also directly accessible as `point2d.x` and `point2d.y`. The latter ways are not significantly better or faster, but your source code tends to be shorter and more readable and, when debugging the code, the debugger will not have to step into those getters.

Like all `Drawable` objects the `Point2d` class also implements the `getPoints` method which creates an `Iterator<Point2d>` that generates all points of the object in sequence. For a `Point` that `Iterator` will only provide one object. The `Point2d` object also implements the `getBounds` method that creates a `Bounds2d` object.

There are three constructors for `Point2d` objects:

```java
    Point2d point1 = new Point2d(123.45, 234.56);
    System.out.println(point1);
    double[] coordinates = new double[] { 123.45, 234.56 };
    Point2d point2 = new Point2d(coordinates);
    System.out.println(point2);
    java.awt.geom.Point2D awtPoint2D = new java.awt.geom.Point2D.Double(123.45, 234.56);
    Point2d point3 = new Point2d(awtPoint2D);
    System.out.println(point3);
    java.awt.geom.Point2D awtPoint2DCopy = point3.toPoint2D();
    System.out.println(awtPoint2DCopy);
```

Outputs:

<pre>
Point2d [x=123.450000, y=234.560000]
Point2d [x=123.450000, y=234.560000]
Point2d [x=123.450000, y=234.560000]
</pre>

That last example created a djutils `Point2d` object from a `java.awt.geom.Point2D` object. The inverse is also possible:

```java
    java.awt.geom.Point2D awtPoint2DCopy = point2d.toPoint2D();
```

Which outputs:

<pre>
Point2D.Double[123.45, 234.56]
</pre>

With two Point2d objects many operations become possible:

```java
    Point2d point2d = new Point2d(123.45, 234.56);
    Point2d secondPoint2d = new Point2d(234.56, 345.78);
    System.out.println("Direction to: " + point2d.directionTo(secondPoint2d));
    System.out.println("Distance: " + point2d.distance(secondPoint2d));
    System.out.println("Distance squared " + point2d.distanceSquared(secondPoint2d));
    System.out.println("Interpolation at fraction 0.3: " + point2d.interpolate(secondPoint2d, 0.3));
```

This outputs:

<pre>
Direction to: 0.7858929233984577
Distance: 157.21106990285384
Distance squared 24715.320499999994
Interpolation at fraction 0.3: Point2d [x=156.783000, y=267.926000]
</pre>

The direction is in radians where the X-axis direction is 0, and the Y-axis direction is &pi;/2; etc. The distance is computed using the `Math.hypot` method; this suffers less from loss of precision problems than taking the square root of the squares of the x-difference and the y-difference. The `distanceSquared` is the sum of the squares of the differences in x and y.
The interpolate method can just as easy perform extrapolation. When the fraction (`0.3` in the example) is `0.0`; a new `Point2d` at the same location the existing `Point2d` is created. fraction `1.0` returns a new `Point2d` at the location of `secondPoint2d`. Fraction `-1.0` creates a new `Point2d` that is the distance between the points _before_ `point2d`; etc.

Computations with double precision coordinates generally leads to rounding errors. In some cases it is necessary to compare points allowing for some loss of precision. This can be done with the `epsilonEquals` method:

```java
    Point2d point1 = new Point2d(123.45, 234.56);
    System.out.println("Almost equals to another Point2d: " + point1.epsilonEquals(new Point2d(123, 235), 0.5));
```

This example prints:

<pre>
Almost equals to another Point2d: true
</pre>

A Point can not be modified (it is immutable), but there are various methods that create a translated, normalized, or scaled version of a Point.

```java
    System.out.println("The point: " + point1);
    System.out.println("The point translated over 10, -20: " + point1.translate(10, -20));
    System.out.println("The point scaled by 2: " + point1.scale(2.0));
    System.out.println("The point negated: " + point1.neg());
    System.out.println("The point normalized: " + point1.normalize());
    System.out.println("The point with absolute values: " + point1.abs());
    System.out.println("The point negated, then absolute: " + point1.neg().abs());
```

Output:

<pre>
The point: Point2d [x=123.450000, y=234.560000]
The point translated over 10, -20: Point2d [x=133.450000, y=214.560000]
The point scaled by 2: Point2d [x=246.900000, y=469.120000]
The point negated: Point2d [x=-123.450000, y=-234.560000]
The point normalized: Point2d [x=0.465739, y=0.884922]
The point with absolute values: Point2d [x=123.450000, y=234.560000]
The point negated, then absolute: Point2d [x=123.450000, y=234.560000]
</pre>

A Point2d has two directly accessible fields: `x` and `y`. There are also getters for these: `getX()` and `getY()`. Using the former access method leads to slightly shorter code with fewer parenthesis. When stepping through code with a debugger, using the former makes life a bit easier. With modern compilers, we do not believe there is a runtime speed advantage in using the fields directly.

A `Point3d` is a `Point` with three coordinates (x, y, z). A `Point3d` is very similar to a `Point2d`. The main difference is that everything is done with three coordinates. The `directionTo` method does not make much sense. It is replaced by two methods: `horizontalDirection` (the angle from the X-axis) and `verticalDirection` (the angle from the Z-axis). The z coordinate can be accessed directly as the `z` field, or with the `getZ()` getter method.

An `OrientedPoint2d` is a `Point2d` with an direction value. This direction is a rotation from the X-axis. The X-axis has rotation `0`, the Y-axis has rotation `π/2`, etc.. The rotation of the `OrientedPoint2d` is totally independent of the `x` and `y` values. The rotation can be retrieved by directly accessing the `dirZ` field, or calling the `getDirZ()` method. An `OrientedPoint2d` can be used to describe the position and rotation of an object in 2D-space.

```java
    OrientedPoint2d orientedPoint2d = new OrientedPoint2d(123.45, 234.56, -0.2);
    System.out.println(orientedPoint2d);
```

Prints

<pre>
OrientedPoint2d [x=123.450000, y=234.560000, rot=-0.200000]
</pre>

There is also an `OrientedPoint3d` class. This object stores x, y, z, dirX, dirY, dirZ. The `dirZ` property is exactly like the `dirZ` property of an OrientedPoint2d. The `dir` values can be interpreted as `roll`, `pitch` and `jaw`. There are several such naming conventions depending on the field of use: [see Wikipedia](https://en.wikipedia.org/wiki/Davenport_chained_rotations)

It is important to understand that combining rotations is not commutative. The `OriendedPoint3d` class does _not_ define the order in which these rotations are to be applied. When you use `Oriented` points in a project, you should clearly document the ordering that your project uses.