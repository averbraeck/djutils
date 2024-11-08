package org.djutils.draw.curve;

/**
 * A continuous curve defines a line in an exact manner, from which numerically approximated polylines can be derived. The
 * continuous definition is useful to accurately connect different lines, e.g. based on the direction of the point where they
 * meet. Moreover, this direction may be accurately be determined by either of the lines. For example, an arc can be defined up
 * to a certain angle. Whatever the angle of the last line segment in a polyline for the arc may be, the continuous line
 * contains the final direction exactly. The continuous definition is also useful to define accurate offset lines, which depend
 * on accurate directions especially at the line end points.
 * <p>
 * Copyright (c) 2023-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <DP> the <code>DirectedPoint</code> type
 * @param <DIR> the direction type. In 2d this is a <code>java.lang.Double</code>; in 3d this is a <code>Direction3d</code>
 *            object
 */
public interface Curve<DP, DIR>
{
    /**
     * Start point of this Curve.
     * @return start point of this Curve
     */
    DP getStartPoint();

    /**
     * End point of this Curve.
     * @return end point of this Curve
     */
    DP getEndPoint();

    /**
     * Return the length of this line.
     * @return double; length of this line
     */
    double getLength();

    /**
     * Start direction of this Curve.
     * @return start direction of this Curve
     */
    DIR getStartDirection();

    /**
     * End direction of this Curve.
     * @return end direction of this Curve
     */
    DIR getEndDirection();
    
}
