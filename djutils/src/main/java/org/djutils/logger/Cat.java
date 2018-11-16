package org.djutils.logger;

/**
 * Predefined categories for Category logging. <br>
 * <p>
 * Copyright (c) 2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public final class Cat
{
    /** */
    private Cat()
    {
        // Utility class
    }

    /** DSOL project. Category is used for dsol-base, dsol-core, dsol-demo, dsol-interpreter, dsol-introspection. */
    public static final LogCategory DSOL = new LogCategory("DSOL");

    /** DSOL-NAMING project. */
    public static final LogCategory NAMING = new LogCategory("NAMING");

    /** DSOL-EVENT project. */
    public static final LogCategory EVENT = new LogCategory("ROAD");

    /** DSOL-SWING project. */
    public static final LogCategory SWING = new LogCategory("SWING");

    /** DSOL-WEB project. */
    public static final LogCategory WEB = new LogCategory("WEB");

    /** DSOL-HLA project. */
    public static final LogCategory HLA = new LogCategory("HLA");

}
