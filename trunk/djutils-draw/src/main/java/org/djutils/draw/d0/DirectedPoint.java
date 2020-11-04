package org.djutils.draw.d0;

import org.djutils.base.AngleUtil;
import org.djutils.exceptions.Throw;

/**
 * DirectedPoint.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface DirectedPoint extends Point
{
    /**
     * Return the rotation around the x-axis in radians, which will be 0.0 for a DirectedPoint2d.
     * @return double; the rotation around the x-axis in radians
     */
    public double getRotX();

    /**
     * Return the rotation around the y-axis in radians, which will be 0.0 for a DirectedPoint2d.
     * @return double; the rotation around the y-axis in radians
     */
    public double getRotY();

    /**
     * Return the rotation around the z-axis in radians.
     * @return double; the rotation around the z-axis in radians
     */
    public double getRotZ();

    /**
     * Interpolate the coordinates and rotation between this point and the given point. It is allowed for fraction to be less
     * than zero or larger than 1. In that case the interpolation turns into an extrapolation. The rotations along the x, y, and
     * z-axes are also interpolated or extrapolated in a clockwise fashion and normalized between -&pi; and &pi;.
     * @param point DirectedPoint; the other point
     * @param fraction double; the factor for interpolation between <code>this</code> DirectedPoint and <code>point</code>. When
     *            fraction is between 0 and 1, it is an interpolation, otherwise an extrapolation. When <code>fraction</code> is
     *            0 this method returns <code>this</code> ; when <code>fraction</code> is 1 this method returns the
     *            <code>point</code> parameter
     * @return DirectedPoint; the point that is "fraction" away on the line between this point and the given point
     * @throws NullPointerException when point is null
     */
    DirectedPoint interpolate(final DirectedPoint point, final double fraction);

    /**
     * Interpolate between two points with a fraction. It is allowed for fraction to be less than zero or larger than 1. In that
     * case the interpolation turns into an extrapolation. The rotations along the x, y, and z-axes are also interpolated or
     * extrapolated in a clockwise fashion and normalized between -&pi; and &pi;.
     * @param p1 DirectedPoint; the first point
     * @param p2 DirectedPoint; the second point
     * @param fraction double; the factor for interpolation between p1 and p2. When fraction is between 0 and 1, it is an
     *            interpolation, otherwise an extrapolation. When <code>fraction</code> is 0 this method returns the
     *            <code>p1</code> parameter; when <code>fraction</code> is 1 this method returns the <code>p2</code> parameter
     * @return DirectedPoint; the point that is "fraction" away on the line between p1 and p2
     * @throws NullPointerException when p1 or p2 is null
     */
    static DirectedPoint interpolate(final DirectedPoint p1, final DirectedPoint p2, final double fraction)
    {
        Throw.whenNull(p1, "p1 cannot be null");
        return p1.interpolate(p2, fraction);
    }

    /**
     * Return a new DirectedPoint with an in-place rotation around the z-axis by the provided delta. The resulting rotation will
     * be normalized between -&pi; and &pi;.
     * @param deltaRotZ double; the rotation around the z-axis
     * @return DirectedPoint; a new point with the same coordinates and applied rotation
     */
    DirectedPoint rotate(double deltaRotZ);

    /**
     * Return a new DirectedPoint3d point with an in-place rotation by the provided deltaRotX, deltaRotY, and deltaRotZ. The
     * resulting rotations will be normalized between -&pi; and &pi;.
     * @param deltaRotX double; the rotation around the x-axis
     * @param deltaRotY double; the rotation around the y-axis
     * @param deltaRotZ double; the rotation around the z-axis
     * @return DirectedPoint3d; a new point with the same coordinates and applied rotations
     */
    default DirectedPoint3d rotate(double deltaRotX, double deltaRotY, double deltaRotZ)
    {
        return new DirectedPoint3d(getX(), getY(), getZ(), AngleUtil.normalizeAroundZero(getRotX() + deltaRotX),
                AngleUtil.normalizeAroundZero(getRotY() + deltaRotY), AngleUtil.normalizeAroundZero(getRotZ() + deltaRotZ));
    }

    /**
     * Compare this DirectedPoint with another DirectedPoint and return true of each of the coordinates is less than
     * epsilonCoordinate apart, and the rotations are (normalized) less that epsilonRotation apart.
     * @param point DirectedPoint; the point to compare with
     * @param epsilonCoordinate double; the upper bound of difference for one of the coordinates
     * @param epsilonRotation double; the upper bound of difference for one of the rotations
     * @return boolean; true if x, y, and z are less than epsilonCoordinate apart, and rotX, rotY and rotZ are less than
     *         epsilonRotation apart, otherwise false
     * @throws NullPointerException when point is null
     */
    default boolean epsilonEquals(final DirectedPoint point, final double epsilonCoordinate, final double epsilonRotation)
    {
        Throw.whenNull(point, "point cannot be null");
        if (!epsilonEquals(point, epsilonCoordinate))
        {
            return false;
        }

        double diff = AngleUtil.normalizeAroundZero(getRotX() - point.getRotX());
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilonRotation)
            return false;
        diff = AngleUtil.normalizeAroundZero(getRotY() - point.getRotY());
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilonRotation)
            return false;
        diff = AngleUtil.normalizeAroundZero(getRotZ() - point.getRotZ());
        if (Double.isNaN(diff))
            return false;
        if ((diff < 0 ? -diff : diff) > epsilonRotation)
            return false;

        return true;
    }

}
