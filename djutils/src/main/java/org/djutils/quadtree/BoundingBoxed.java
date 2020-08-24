package org.djutils.quadtree;

/**
 * Interface that must be implemented by objects that need to be stored in a quad tree.
 * <p>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface BoundingBoxed
{
    /**
     * Determine the bounding box. The result must be constant; it may not vary from one call to the next. Objects implementing
     * this interface should probably cache the result of this method.
     * @return Rectangle; the bounding box
     */
    Rectangle getBoundingRectangle();

    /**
     * Determine if this 2D object intersects the given rectangle.
     * @param rectangle Rectangle; the rectangle
     * @return boolean; true if this object intersects the rectangle; false if the object does not intersect the rectangle
     */
    boolean intersects(Rectangle rectangle);

}
