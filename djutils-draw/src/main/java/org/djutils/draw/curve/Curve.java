package org.djutils.draw.curve;

import java.util.Set;

import org.djutils.draw.line.PolyLine;
import org.djutils.draw.point.Point;

/**
 * A Curve defines a line in an exact, continuous manner, from which numerically approximated polylines can be derived. The
 * continuous definition is useful to accurately connect different lines, e.g. based on the direction of the point where they
 * meet. Moreover, this direction may be accurately be determined by either of the lines. For example, an arc can be defined up
 * to a certain angle. Whatever the angle of the last line segment in a polyline for the arc may be, the continuous line
 * contains the final direction exactly. The continuous definition is also useful to define accurate offset lines, which depend
 * on accurate directions especially at the line end points.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
 * @param <P> the <code>Point</code> type
 * @param <F> the <code>Flattener</code> type
 * @param <PL> the <code>PolyLine</code> type
 */
public interface Curve<DP, DIR, P extends Point<P>, F extends Flattener<F, ?, PL, P, DIR>, PL extends PolyLine<?, P, ?, ?, ?>>
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
     * Return the length of this Curve.
     * @return double; length of this Curve
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

    /**
     * Flatten a Curve into a PolyLine. Implementations should use the flattener when relevant and possible.
     * @param flattener Flattener; the flattener
     * @return PolyLine; approximation of this <code>Curve</code> as a <code>PolyLine</code>
     */
    PL toPolyLine(F flattener);

    /**
     * Returns the point at the given fraction of this Curve. The fraction may represent any parameter, such as <i>t</i> in
     * a B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length.
     * @param fraction double; the fraction
     * @return P; the point at the given <code>fraction</code>
     */
    P getPoint(double fraction);

    /**
     * If this Curve has knots, this method must return the fractions where those knots occur. The <code>default</code>
     * implementation works for Curves that have <b>no</b> knots.
     * @return Set&lt;Double&gt; the fractions where knots in the offset function occur, may be empty or <code>null</code>
     */
    default Set<Double> getKnots()
    {
        return null;
    }

    /**
     * Returns the direction at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a
     * B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length. The default implementation performs a
     * numerical approach by looking at the direction between the points at fraction, and a point 1e-6 away.
     * @param fraction double; the fraction
     * @return double; the direction at the given <code>fraction</code>
     */
    DIR getDirection(double fraction);

}
