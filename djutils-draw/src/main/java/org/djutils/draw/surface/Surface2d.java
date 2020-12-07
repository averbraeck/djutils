package org.djutils.draw.surface;

import java.util.Iterator;

import org.djutils.draw.Drawable2d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;

/**
 * Area2d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Surface2d implements Drawable2d
{

    /** {@inheritDoc} */
    @Override
    public Iterator<Point2d> getPoints()
    {
        return null; // TODO
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return 0; // TODO
    }

    /** {@inheritDoc} */
    @Override
    public Bounds2d getBounds()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Point2d getLocation()
    {
        return null;
    }
 
}

