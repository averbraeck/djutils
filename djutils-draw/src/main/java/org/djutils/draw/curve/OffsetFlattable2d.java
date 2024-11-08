package org.djutils.draw.curve;

import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.Point2d;

/**
 * An OffsetFlattable2d has the required methods to allow it to be converted to a PolyLine2d with a piece-wise-linear offset
 * using an OffsetF2dlattener.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public interface OffsetFlattable2d extends Flattable2d
{

    /**
     * Flatten a OffsetFlattable line while offsetting with the provided continuous line offset into a PolyLine2d.
     * Implementations should use the flattener when relevant and possible.
     * @param flattener OffsetFlattener
     * @param offsets offset data
     * @return PolyLine2d; approximation of this line with offset as a PolyLine2d
     * @throws NullPointerException when <code>flattener</code>, or <code>offsets</code> is <code>null</code>
     */
    PolyLine2d toPolyLine(OffsetFlattener2d flattener, PieceWiseLinearOffset2d offsets);

    /**
     * Returns the point at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a B&eacute;zier
     * curve, <i>s</i> in a Clothoid, or simply the fraction of length.
     * @param fraction double; the fraction
     * @param fld FractionalLengthData; provides fraction-dependent lateral offset to the point
     * @return Point2d; the point at the given <code>fraction</code>
     */
    Point2d getPoint(double fraction, PieceWiseLinearOffset2d fld);

    /**
     * Returns the direction at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a
     * B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length. The default implementation performs a
     * numerical approach by looking at the direction between the points at fraction, and a point 1e-6 away.
     * @param fraction double; the fraction
     * @param fld FractionalLengthData; provides fraction-dependent lateral offset to the curve
     * @return double; the direction at the given <code>fraction</code>
     */
    default double getDirection(final double fraction, final PieceWiseLinearOffset2d fld)
    {
        Point2d p1, p2;
        if (fraction < 0.5) // to prevent going above 1.0
        {
            p1 = getPoint(fraction, fld);
            p2 = getPoint(fraction + 1e-6, fld);
        }
        else
        {
            p1 = getPoint(fraction - 1e-6, fld);
            p2 = getPoint(fraction, fld);
        }
        return p1.directionTo(p2);
    }

    /**
     * Convert a position along the curve to a t-value in the <code>OffsetFlattableLine2d</code> domain. For <code>Arc</code>
     * and <code>Straight</code>, these t-values are the same. For <code>BezierCubic</code> they're not.
     * @param position t-value in the <code>FractionalLengthData</code> domain
     * @return double; t-value in the <code>OffsetFlattableLine2d</code> domain
     */
    default double getT(final double position)
    {
        return position / getLength();
    }

}
