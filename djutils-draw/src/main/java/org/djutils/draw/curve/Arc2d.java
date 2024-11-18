package org.djutils.draw.curve;

import org.djutils.draw.function.ContinuousPiecewiseLinearFunction;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.math.AngleUtil;

/**
 * Continuous definition of an arc in 2d.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Arc2d implements Curvature, Curve2d, OffsetCurve2d
{

    /** Starting point. */
    private final DirectedPoint2d startPoint;

    /** Curve radius. */
    private final double radius;

    /** Angle of the curve. */
    private final double angle;

    /** Sign to use for offsets and angles, which depends on the left/right direction. */
    private final double sign;

    /** Center point of circle, as calculated in constructor. */
    private final Point2d center;

    /**
     * Define arc by starting point, radius, curve direction, and angle.
     * @param startPoint starting point
     * @param radius radius (must be positive)
     * @param left <code>true</code>for left turning curve; <code>false</code> for for right turning curve
     * @param angle angle of arc (must be positive)
     */
    public Arc2d(final DirectedPoint2d startPoint, final double radius, final boolean left, final double angle)
    {
        Throw.whenNull(startPoint, "startPoint");
        Throw.when(radius < 0.0, IllegalArgumentException.class, "radius must be positive");
        Throw.when(angle < 0.0, IllegalArgumentException.class, "angle must be positive");
        this.startPoint = startPoint;
        this.radius = radius;
        this.sign = left ? 1.0 : -1.0;
        this.angle = angle;
        double dx = Math.cos(startPoint.dirZ) * this.sign * radius;
        double dy = Math.sin(startPoint.dirZ) * this.sign * radius;
        this.center = new Point2d(startPoint.x - dy, startPoint.y + dx);
    }

    @Override
    public double getStartCurvature()
    {
        return 1.0 / this.radius;
    }

    @Override
    public double getEndCurvature()
    {
        return getStartCurvature();
    }

    @Override
    public double getStartRadius()
    {
        return this.radius;
    }

    @Override
    public double getEndRadius()
    {
        return this.radius;
    }

    /**
     * Does this arc bend to the left?
     * @return <code>true</code> if this Arc bends to the left; <code>false</code> if this Arc bends to the right
     */
    public boolean isLeft()
    {
        return this.sign > 0;
    }

    /**
     * Retrieve the total change of direction on this Arc.
     * @return the total change of direction on this Arc
     */
    public double getAngle()
    {
        return this.angle;
    }

    /**
     * Compute the point at the provided fraction of this Arc while applying the provided lateral offset.
     * @param fraction the fraction along this Arc
     * @param offset the lateral offset to apply
     * @return the point at the provided fraction of this Arc with the provided lateral offset applied
     */
    private Point2d getPoint(final double fraction, final double offset)
    {
        double len = this.radius - this.sign * offset;
        double a = this.startPoint.dirZ + this.sign * this.angle * fraction;
        double dx = this.sign * Math.cos(a) * len;
        double dy = this.sign * Math.sin(a) * len;
        return new Point2d(this.center.x + dy, this.center.y - dx);
    }

    @Override
    public Point2d getPoint(final double fraction)
    {
        return getPoint(fraction, 0);
    }

    @Override
    public Point2d getPoint(final double fraction, final ContinuousPiecewiseLinearFunction of)
    {
        return getPoint(fraction, of.get(fraction));
    }

    @Override
    public Double getDirection(final double fraction)
    {
        return AngleUtil.normalizeAroundZero(this.startPoint.dirZ + this.sign * this.angle * fraction);
    }

    @Override
    public double getDirection(final double fraction, final ContinuousPiecewiseLinearFunction of)
    {
        /*-
         * x = cos(phi) * (r - s(phi))
         * y = sin(phi) * (r - s(phi)) 
         * 
         * with,
         *   phi    = angle of circle arc point at fraction, relative to circle center
         *   r      = radius
         *   s(phi) = offset at phi (or at fraction)
         * 
         * then using the product rule: 
         * 
         * x' = -sin(phi) * (r - s(phi)) - cos(phi) * s'(phi)
         * y' = cos(phi) * (r - s(phi)) - sin(phi) * s'(phi)
         */
        double phi = (Arc2d.this.startPoint.dirZ + Arc2d.this.sign * (Arc2d.this.angle * fraction - Math.PI / 2));
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double sPhi = Arc2d.this.sign * of.get(fraction);
        double sPhiD = of.getDerivative(fraction) / Arc2d.this.angle;
        double dx = -sinPhi * (Arc2d.this.radius - sPhi) - cosPhi * sPhiD;
        double dy = cosPhi * (Arc2d.this.radius - sPhi) - sinPhi * sPhiD;
        double direction = Math.atan2(Arc2d.this.sign * dy, Arc2d.this.sign * dx);
        return direction;
    }

    @Override
    public PolyLine2d toPolyLine(final Flattener2d flattener)
    {
        Throw.whenNull(flattener, "Flattener");
        return flattener.flatten(this);
    }

    @Override
    public PolyLine2d toPolyLine(final OffsetFlattener2d flattener, final ContinuousPiecewiseLinearFunction offsets)
    {
        Throw.whenNull(offsets, "Offsets");
        return flattener.flatten(this, offsets);
    }

    @Override
    public double getLength()
    {
        return this.angle * this.radius;
    }

    @Override
    public String toString()
    {
        return "Arc [startPoint=" + this.startPoint + ", radius=" + this.radius + ", angle=" + this.angle + ", left="
                + (this.sign > 0.0) + "]";
    }

}
