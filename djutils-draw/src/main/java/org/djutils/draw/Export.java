package org.djutils.draw;

import java.util.Locale;

import org.djutils.draw.line.LineSegment2d;
import org.djutils.draw.line.LineSegment3d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.PolyLine3d;
import org.djutils.draw.line.Polygon2d;
import org.djutils.draw.line.Polygon3d;

/**
 * Export methods for djutils-draw objects.
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
public final class Export
{
    /**
     * Utility class; do not instantiate.
     */
    private Export()
    {
        // Do not instantiate
    }

    /**
     * Convert a LineSegment2d to Peter's plot format.
     * @param lineSegment LineSegment2d; the line segment to convert
     * @return String; the line segment in Peter's plot format
     * @throws NullPointerException when <code>lineSegment</code> is <code>null</code>
     */
    public static String toPlot(final LineSegment2d lineSegment)
    {
        return String.format(Locale.US, "M%.3f,%.3fL%.3f,%.3f\n", lineSegment.startX, lineSegment.startY, lineSegment.endX,
                lineSegment.endY);
    }

    /**
     * Convert a PolyLine2d to Peter's plot format.
     * @param polyLine PolyLine2d; the polyline to convert
     * @return String; the polyline in Peter's plot format
     * @throws NullPointerException when <code>polyLine</code> is <code>null</code>
     */
    public static String toPlot(final PolyLine2d polyLine)
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < polyLine.size(); i++)
        {
            result.append(String.format(Locale.US, "%s%.3f,%.3f", 0 == result.length() ? "M" : " L", polyLine.getX(i),
                    polyLine.getY(i)));
        }
        result.append("\n");
        return result.toString();
    }

    /**
     * Convert a Polygon2d into Peter's plot format.
     * @param polygon Polygon2d; the polygon to convert
     * @return String; the polygon in Peter's plot format
     * @throws NullPointerException when <code>polygon</code> is <code>null</code>
     */
    public static String toPlot(final Polygon2d polygon)
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < polygon.size(); i++)
        {
            result.append(String.format(Locale.US, "%s%.3f,%.3f", 0 == result.length() ? "M" : " L", polygon.getX(i),
                    polygon.getY(i)));
        }
        result.append(String.format(Locale.US, " L%.3f,%.3f", polygon.getX(0), polygon.getY(0)));
        result.append("\n");
        return result.toString();
    }

    /**
     * Convert a LineSegment2d into something that a TSV parser can handle.
     * @param lineSegment LineSegment2d; the line segment to convert
     * @return String; the line segment in TSV format
     * @throws NullPointerException when <code>lineSegment</code> is <code>null</code>
     */
    public static String toTsv(final LineSegment2d lineSegment)
    {
        return lineSegment.startX + "\t" + lineSegment.startY + "\n" + lineSegment.endX + "\t" + lineSegment.endY + "\n";
    }

    /**
     * Convert a PolyLine2d into something that a TSV parser can handle.
     * @param polyLine PolyLine2d; the polyline to convert
     * @return String; the polyline in TSV format
     * @throws NullPointerException when <code>polyLine</code> is <code>null</code>
     */
    public static String toTsv(final PolyLine2d polyLine)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < polyLine.size(); i++)
        {
            s.append(polyLine.getX(i) + "\t" + polyLine.getY(i) + "\n");
        }
        return s.toString();
    }

    /**
     * Convert a Polygon2d into something that a TSV parser can handle.
     * @param polygon Polygon2d; the polygon to convert
     * @return String; the polygon in TSV format
     * @throws NullPointerException when <code>polygon</code> is <code>null</code>
     */
    public static String toTsv(final Polygon2d polygon)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < polygon.size(); i++)
        {
            s.append(polygon.getX(i) + "\t" + polygon.getY(i) + "\n");
        }
        s.append(polygon.getX(0) + "\t" + polygon.getY(0) + "\n");
        return s.toString();
    }

    /**
     * Convert a LineSegment3d into something that a TSV parser can handle.
     * @param lineSegment LineSegment3d; the line segment to convert
     * @return String; the line segment in TSV format
     * @throws NullPointerException when <code>lineSegment</code> is <code>null</code>
     */
    public static String toTsv(final LineSegment3d lineSegment)
    {
        return lineSegment.startX + "\t" + lineSegment.startY + "\t" + lineSegment.startZ + "\n" + lineSegment.endX + "\t"
                + lineSegment.endY + "\t" + lineSegment.endZ + "\n";
    }

    /**
     * Convert a PolyLine3d into something that a TSV parser can handle.
     * @param polyline PolyLine3d; the polyline to convert
     * @return String; the polyline in TSV format
     * @throws NullPointerException when <code>polyline</code> is <code>null</code>
     */
    public static String toTsv(final PolyLine3d polyline)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < polyline.size(); i++)
        {
            s.append(polyline.getX(i) + "\t" + polyline.getY(i) + "\t" + polyline.getZ(i) + "\n");
        }
        return s.toString();
    }

    /**
     * Convert a Polygon3d into something that a TSV parser can handle.
     * @param polygon Polygon3d; the polygon to convert
     * @return String; the polygon in TSV format
     * @throws NullPointerException when <code>polygon</code> is <code>null</code>
     */
    public static String toTsv(final Polygon3d polygon)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < polygon.size(); i++)
        {
            s.append(polygon.getX(i) + "\t" + polygon.getY(i) + "\t" + polygon.getZ(i) + "\n");
        }
        s.append(polygon.getX(0) + "\t" + polygon.getY(0) + "\t" + polygon.getZ(0) + "\n");
        return s.toString();
    }

}
