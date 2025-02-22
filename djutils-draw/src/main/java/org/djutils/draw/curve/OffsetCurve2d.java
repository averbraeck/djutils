package org.djutils.draw.curve;

import org.djutils.draw.function.ContinuousPiecewiseLinearFunction;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;

/**
 * OffsetCurve2d.java.
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface OffsetCurve2d extends Curve2d
{
    /**
     * Flatten a Curve2d while offsetting with the provided continuous offset into a PolyLine2d.
     * Implementations should use the flattener when relevant and possible.
     * @param flattener OffsetFlattener
     * @param offsets offset data
     * @return approximation of this <code>curve2d</code> with offset as a <code>PolyLine2d</code>
     * @throws NullPointerException when <code>flattener</code>, or <code>offsets</code> is <code>null</code>
     */
    PolyLine2d toPolyLine(OffsetFlattener2d flattener, ContinuousPiecewiseLinearFunction offsets);

    /**
     * Returns the point at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a B&eacute;zier
     * curve, <i>s</i> in a Clothoid, or simply the fraction of length.
     * @param fraction the fraction
     * @param of provides fraction-dependent lateral offset to the point
     * @return the point at the given <code>fraction</code>
     */
    Point2d getPoint(double fraction, ContinuousPiecewiseLinearFunction of);

    /**
     * Returns the direction at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a
     * B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length. The default implementation performs a
     * numerical approach by looking at the direction between the points at fraction, and a point 1e-6 away.
     * @param fraction the fraction
     * @param of provides fraction-dependent lateral offset to the curve
     * @return the direction at the given <code>fraction</code>
     */
    default double getDirection(final double fraction, final ContinuousPiecewiseLinearFunction of)
    {
        Point2d p1, p2;
        if (fraction < 0.5) // to prevent going above 1.0
        {
            p1 = getPoint(fraction, of);
            p2 = getPoint(fraction + 1e-6, of);
        }
        else
        {
            p1 = getPoint(fraction - 1e-6, of);
            p2 = getPoint(fraction, of);
        }
        return p1.directionTo(p2);
    }

    /**
     * Convert a position along the curve to a t-value in the <code>OffsetCurve2d</code> domain. For <code>Arc</code>
     * and <code>Straight</code>, these t-values are the same. For <code>BezierCubic</code> they're not.
     * @param position t-value in the <code>ContinuousPiecewiseLinearFunction</code> domain
     * @return t-value in the <code>ContinuousPiecewiseLinearFunction</code> domain
     */
    default double getT(final double position)
    {
        return position / getLength();
    }

}
