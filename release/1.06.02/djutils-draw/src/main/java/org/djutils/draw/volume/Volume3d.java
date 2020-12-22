package org.djutils.draw.volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.djutils.draw.line.PolyLine3d;

/**
 * Volume3d.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class Volume3d implements Serializable
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Return the lines making up the wireframe of this object.
     * @return List&lt;Line&gt;; the A list of lines making up the wireframe of the object
     */
    public List<PolyLine3d> getWireframeLines()
    {
        // TODO program
        return new ArrayList<>();
    }

}
