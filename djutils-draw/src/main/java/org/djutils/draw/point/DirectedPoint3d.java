package org.djutils.draw.point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import org.djutils.base.AngleUtil;
import org.djutils.draw.Directed3d;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;

/**
 * DirectedPoint3d.java.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DirectedPoint3d extends Point3d implements Directed3d<DirectedPoint3d>
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /** The direction as rotation around the x-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double theta;

    /** The direction as rotation from the positive z-axis towards the x-y plane. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double phi;

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates and orientation dirX,dirY,dirZ.
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @param theta double; the complement of the slope
     * @throws IllegalArgumentException when x, y, z, dirX, dirY, or dirZ is NaN
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double phi, final double theta)
            throws IllegalArgumentException
    {
        super(x, y, z);
        Throw.when(Double.isNaN(phi) || Double.isNaN(theta), IllegalArgumentException.class,
                "phi and theta must be numbers (not NaN)");
        this.phi = phi;
        this.theta = theta;
    }

    /**
     * Create a new DirectedPoint3d with x, y, and z coordinates in a double[] and direction phi,theta.
     * @param xyz double[]; the x, y and z coordinates
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @param theta double; the complement of the slope
     * @throws NullPointerException when xyx is null
     * @throws IllegalArgumentException when the length of the xyz array is not 3, or contains a NaN value, or phi, or theta is
     *             NaN
     */
    public DirectedPoint3d(final double[] xyz, final double phi, final double theta)
            throws NullPointerException, IllegalArgumentException
    {
        super(xyz);
        Throw.when(Double.isNaN(phi) || Double.isNaN(theta), IllegalArgumentException.class,
                "phi and theta must be numbers (not NaN)");
        this.phi = phi;
        this.theta = theta;
    }

    /**
     * Create a new DirectedPoint3d from another Point3d and and direction phi,theta.
     * @param point Point3d; the point from which this OrientedPoint3d will be instantiated
     * @param phi double; the counter-clockwise rotation around the point in radians
     * @param theta double; the complement of the slope
     * @throws IllegalArgumentException when dirX, dirY, or dirZ is NaN
     */
    public DirectedPoint3d(final Point3d point, final double phi, final double theta) throws IllegalArgumentException
    {
        this(point.x, point.y, point.z, phi, theta);
    }

    /**
     * Construct a new DirectedPoint3d from three coordinates and the coordinates of a point that the direction goes through.
     * @param x double; the x coordinate of the new DirectedPoint
     * @param y double; the y coordinate of the new DirectedPoint
     * @param z double; the z coordinate of the new DirectedPoint
     * @param throughX double; the x-coordinate of a point that the direction goes through
     * @param throughY double; the y-coordinate of a point that the direction goes through
     * @param throughZ double; the z-coordinate of a point that the direction goes through
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>x</cite> and <cite>throughY</cite> == <cite>y</cite> and
     *             <cite>throughZ</cite> == <cite>z</cite>, or any through-value is NaN
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double throughX, final double throughY,
            final double throughZ) throws DrawRuntimeException
    {
        this(x, y, z, buildDirectionVector(throughX - x, throughY - y, throughZ - z));
    }

    /**
     * Build the direction vector.
     * @param dX double; x difference
     * @param dY double; y difference
     * @param dZ double; z difference
     * @return double[]; a two-element array containing phi and theta
     */
    private static double[] buildDirectionVector(final double dX, final double dY, final double dZ)
    {
        Throw.when(0 == dX && 0 == dY && 0 == dZ, IllegalArgumentException.class, "Through point may not be equal to point");
        return new double[] {Math.atan2(dY, dX), Math.atan2(Math.hypot(dX, dY), dZ)};
    }

    /**
     * Construct a new DirectedPoint3d form a Point3d and the coordinates that the direction goes through.
     * @param point Point3d; the point
     * @param throughX double; the x coordinate of a point that the direction goes through
     * @param throughY double; the y coordinate of a point that the direction goes through
     * @param throughZ double; the z coordinate of a point that the direction goes through
     * @throws DrawRuntimeException when <cite>throughX</cite> == <cite>point.x</cite> and <cite>throughY</cite> ==
     *             <cite>point.y</cite> and <cite>throughZ</cite> == <cite>point.z</cite>, or any through-value is NaN
     */
    public DirectedPoint3d(final Point3d point, final double throughX, final double throughY, final double throughZ)
            throws DrawRuntimeException
    {
        this(Throw.whenNull(point, "point may not be null").x, point.y, point.z, throughX, throughY, throughZ);
    }

    /**
     * Verify that a double array is not null, has two elements.
     * @param orientation double[]; the array to check
     * @return double; the first element of the argument
     * @throws NullPointerException when <code>orientation</code> is null
     * @throws IllegalArgumentException when the length of the <code>orientation</code> array is not 2
     */
    private static double checkOrientationVector(final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.when(orientation.length != 2, IllegalArgumentException.class, "length of orientation array must be 2");
        return orientation[0];
    }

    /**
     * Create a new DirectedPoint3d with x, y and z coordinates and orientation specified using a double array of three elements
     * (containing dirX,dirY,dirZ in that order).
     * @param x double; the x coordinate
     * @param y double; the y coordinate
     * @param z double; the z coordinate
     * @param orientation double[]; the two direction values (theta and phi) in a double array containing theta and phi in that
     *            order. Theta is the angle from the positive x-axis to the projection of the direction in the x-y-plane. Phi is
     *            the rotation from the positive z-axis to the direction.
     * @throws NullPointerException when <code>orientation</code> is null, or contains a NaN value
     * @throws IllegalArgumentException when the length of the <code>direction</code> array is not 2
     */
    public DirectedPoint3d(final double x, final double y, final double z, final double[] orientation)
            throws NullPointerException, IllegalArgumentException
    {
        this(x, y, z, checkOrientationVector(orientation), orientation[1]);
    }

    /**
     * Create a new OrientedPoint3d from x, y and z coordinates packed in a double array of three elements and a direction
     * specified using a double array of two elements.
     * @param xyz double[]; the <cite>x</cite>, <cite>y</cite> and <cite>z</cite> coordinates in that order
     * @param orientation double[]; the two orientation angles <cite>phi</cite> and <cite>theta</cite> in that order
     * @throws NullPointerException when <cite>xyx</cite> or <cite>orientation</cite> is null
     * @throws IllegalArgumentException when the length of the <cite>xyx</cite> array is not 3 or the length of the
     *             <cite>orientation</cite> array is not 2
     */
    public DirectedPoint3d(final double[] xyz, final double[] orientation) throws NullPointerException, IllegalArgumentException
    {
        this(xyz, checkOrientationVector(orientation), orientation[1]);
    }

    /**
     * Construct a new DirectedPoint3d from x, y and z coordinates and a point that the direction goes through.
     * @param x double; the x coordinate of the new DirectedPoint3d
     * @param y double; the y coordinate of the new DirectedPoint3d
     * @param z double; the z coordinate of the new DirectedPoint3d
     * @param throughPoint Point3d; a point that the direction goes through
     * @throws NullPointerException when throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at (x, y, z)
     */
    public DirectedPoint3d(final double x, final double y, final double z, final Point3d throughPoint)
            throws NullPointerException, DrawRuntimeException
    {
        this(x, y, z, Throw.whenNull(throughPoint, "througPoint may not be null").x, throughPoint.y, throughPoint.z);
    }

    /**
     * Construct a new DirectedPoint3d.
     * @param point Point3d; the location of the new DirectedPoint3d
     * @param throughPoint Point3d; another point that the direction goes through
     * @throws NullPointerException when point is null or throughPoint is null
     * @throws DrawRuntimeException when throughPoint is exactly at point
     */
    public DirectedPoint3d(final Point3d point, final Point3d throughPoint) throws NullPointerException, DrawRuntimeException
    {
        this(Throw.whenNull(point, "point may not be null").x, point.y, point.z,
                Throw.whenNull(throughPoint, "throughPoint may not be null").x, throughPoint.y, throughPoint.z);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d neg()
    {
        return new DirectedPoint3d(-getX(), -getY(), -getZ(), AngleUtil.normalizeAroundZero(this.phi + Math.PI),
                AngleUtil.normalizeAroundZero(this.theta + Math.PI));
    }

    /** {@inheritDoc} */
    @Override
    public double getPhi()
    {
        return this.phi;
    }

    /** {@inheritDoc} */
    @Override
    public double getTheta()
    {
        return this.theta;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<? extends DirectedPoint3d> getPoints()
    {
        return Arrays.stream(new DirectedPoint3d[] {this}).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return toString("%f", false);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final String doubleFormat, final boolean doNotIncludeClassName)
    {
        String format = String.format("%1$s[x=%2$s, y=%2$s, z=%2%s, phi=%2$s, theta=%2$s]",
                doNotIncludeClassName ? "" : "DirectedPoint3d ", doubleFormat);
        return String.format(Locale.US, format, this.x, this.y, this.z, this.phi, this.theta);
    }

    /** {@inheritDoc} */
    @Override
    public boolean epsilonEquals(final DirectedPoint3d other, final double epsilonCoordinate, final double epsilonRotation)
            throws NullPointerException, IllegalArgumentException
    {
        Throw.whenNull(other, "other point cannot be null");
        Throw.when(epsilonCoordinate < 0 || epsilonRotation < 0, IllegalArgumentException.class,
                "epsilonCoordinate and epsilongRotation may not be negative");
        Throw.when(Double.isNaN(epsilonCoordinate) || Double.isNaN(epsilonRotation), IllegalArgumentException.class,
                "epsilonCoordinate and epsilongRotation may not be NaN");
        if (Math.abs(this.x - other.x) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.y - other.y) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(this.z - other.z) > epsilonCoordinate)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.phi - other.phi)) > epsilonRotation)
        {
            return false;
        }
        if (Math.abs(AngleUtil.normalizeAroundZero(this.theta - other.theta)) > epsilonRotation)
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(this.phi, this.theta);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DirectedPoint3d other = (DirectedPoint3d) obj;
        return Double.doubleToLongBits(this.phi) == Double.doubleToLongBits(other.phi)
                && Double.doubleToLongBits(this.theta) == Double.doubleToLongBits(other.theta);
    }

}
