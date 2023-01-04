/**
 * The d1 package provides an interface and different implementations for lines. <br>
 * Various types of line are implemented:
 * <ul>
 * <li>LineSegment: straight connection between two points</li>
 * <li>PolyLine: multiple consecutive line segments where the end point of each segment (except the last) is the start of the
 * next</li>
 * <li>Polygon: multiple consecutive line segments where the last end point equals the first start point</li>
 * <li>Ray: straight connection from one (finite) point to infinity (with a precisely defined direction)</li>
 * </ul>
 * Additionally there are generators to approximate B&eacute;zier curves and Clothoids.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
package org.djutils.draw.line;
