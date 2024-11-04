package org.djutils.draw.curve;

import java.util.Set;

import org.djutils.draw.line.PolyLine;
import org.djutils.draw.point.Point;

/**
 * A Flattable curve has the required methods to allow it to be converted to a PolyLine using a Flattener.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @param <F> the Flattener type
 * @param <PL> the PolyLine type
 * @param <P> the Point type
 * @param <DIR> the Direction type
 */
public interface Flattable<F extends Flattener<F, ?, PL, P>, PL extends PolyLine<?, P, ?, ?, ?>, P extends Point<P>, DIR>
{
    /**
     * Retrieve the length of this Flattable.
     * @return double; the length of this Flattable
     */
    double getLength();

    /**
     * Flatten a Flattable2d into a PolyLine2d. Implementations should use the flattener when relevant and possible.
     * @param flattener Flattener2d; flattener
     * @return PolyLine2d; approximation of this line as a PolyLine2d
     */
    PL toPolyLine(F flattener);

    /**
     * Returns the point at the given fraction of this Flattable. The fraction may represent any parameter, such as <i>t</i> in
     * a B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length.
     * @param fraction double; the fraction
     * @return P; the point at the given fraction
     */
    P getPoint(double fraction);

    /**
     * If this Flattable has kinks, this method must return the fractions where those kinks occur. The <code>default</code>
     * implementation works for Flattables that have <b>no</b> kinks.
     * @return Set&lt;Double&gt; the fractions where kinks in the offset function occur, may be empty or null
     */
    default Set<Double> getKinks()
    {
        return null;
    }

    /**
     * Returns the direction at the given fraction. The fraction may represent any parameter, such as <i>t</i> in a
     * B&eacute;zier curve, <i>s</i> in a Clothoid, or simply the fraction of length. The default implementation performs a
     * numerical approach by looking at the direction between the points at fraction, and a point 1e-6 away.
     * @param fraction double; the fraction
     * @return double; the direction at the given fraction
     */
    DIR getDirection(double fraction);

}
