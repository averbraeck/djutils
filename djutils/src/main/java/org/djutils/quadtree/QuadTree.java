package org.djutils.quadtree;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.djutils.exceptions.Throw;

/**
 * Quad tree for 2D objects. For now, this implementation needs an ultimate outer bounding box. No part of any 2D string object
 * may exceed that bounding box. A link to each stored 2D object will be stored in each sub-box that it intersects.
 * <p>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <T> Type of object stored in this quad tree
 */
public class QuadTree<T extends BoundingBoxed> implements Collection<T>
{
    /** Bounding box of this quad tree. */
    private final Rectangle boundingBox;

    /** Maximum number of line strings in one cell. Should probably configurable. */
    private static final int MAX_LOAD = 10;

    /** Minimum width and height of a QuadTree bounding box. */
    private static final double MINIMUM_SIZE = 10.0;

    /** Current number of objects in this quad tree. */
    private int size = 0;

    /** South West child of this node. */
    private QuadTree<T> southWestChild = null;

    /** South East child of this node. */
    private QuadTree<T> southEastChild = null;

    /** North West child of this node. */
    private QuadTree<T> northWestChild = null;

    /** North East child of this node. */
    private QuadTree<T> northEastChild = null;

    /** If the four children have been allocated, this array will be non-null and contain the four children. */
    private QuadTree<T>[] children = null;

    /** Elements stored at this node. */
    private Set<T> elements = new LinkedHashSet<>();

    /**
     * Create a new PathQuadTree object (or a sub-tree).
     * @param left double; the lowest X-coordinate that is allowed (inclusive)
     * @param bottom double; the lowest Y-coordinate that is allowed (inclusive)
     * @param right double; the highest X-coordinate that is allowed (exclusive)
     * @param top double; the highest Y-coordinate that is allowed (exclusive)
     */
    public QuadTree(final double left, final double bottom, final double right, final double top)
    {
        Throw.when(left >= right, IllegalArgumentException.class, "left (%f) must be less than right (%f)", left, right);
        Throw.when(bottom >= top, IllegalArgumentException.class, "bottom (%f) must be less than top (%f)", bottom, top);
        this.boundingBox = new Rectangle(left, bottom, right, top);
    }

    /**
     * Optimize the distribution of elements at this node and at sub-nodes.
     */
    @SuppressWarnings("unchecked")
    private void reBalance()
    {
        if (this.elements.size() < MAX_LOAD || this.boundingBox.getWidth() < MINIMUM_SIZE
                || this.boundingBox.getHeight() < MINIMUM_SIZE)
        {
            return;
        }
        // Count the number of elements that could be moved down to sub-trees
        double cX = (this.boundingBox.getLeft() + this.boundingBox.getRight()) / 2;
        double cY = (this.boundingBox.getBottom() + this.boundingBox.getTop()) / 2;
        int canMove = 0;
        for (T e : this.elements)
        {
            if (!e.getBoundingRectangle().contains(this.boundingBox))
            {
                canMove++;
            }
        }
        if (canMove == 0 || canMove < MAX_LOAD / 2 && this.children == null)
        {
            // System.out.println("reBalance: not moving " + canMove + " of " + this.elements.size());
            return;
        }
        // System.out.println("At start of reBalance of " + this.toString(1));
        if (this.children == null)
        {
            this.southWestChild = new QuadTree<T>(this.boundingBox.getLeft(), this.boundingBox.getBottom(), cX, cY);
            this.southEastChild = new QuadTree<T>(cX, this.boundingBox.getBottom(), this.boundingBox.getRight(), cY);
            this.northWestChild = new QuadTree<T>(this.boundingBox.getLeft(), cY, cX, this.boundingBox.getTop());
            this.northEastChild = new QuadTree<T>(cX, cY, this.boundingBox.getRight(), this.boundingBox.getTop());
            this.children =
                    new QuadTree[] { this.southWestChild, this.southEastChild, this.northWestChild, this.northEastChild };
            // for (QuadTree<T> sub : this.children)
            // {
            // System.out.println("new child " + sub);
            // }
        }
        Iterator<T> iterator = this.elements.iterator();
        while (iterator.hasNext())
        {
            T e = iterator.next();
            if (e.getBoundingRectangle().contains(this.boundingBox))
            {
                continue;
            }
            boolean added = false;
            for (QuadTree<T> child : this.children)
            {
                if (e.getBoundingRectangle().intersects(child.boundingBox))
                {
                    added |= child.add(e);
                }
            }
            if (added)
            {
                iterator.remove();
            }
            else
            {
                System.out.println("ERROR: Could not add " + e + " to any of the children");
            }
        }
        // System.out.println("At end of reBalanceof " + this.toString(1));
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * Recursively search for a particular object.
     * @param b BoundingBoxed; the object to search for
     * @param rect Rectangle2D.Double the bounding box of the object
     * @return boolean; true if this quad tree contains the object; false if this quad tree does not contain the object
     */
    private boolean recursiveContains(final BoundingBoxed b, final Rectangle rect)
    {
        if (!this.boundingBox.intersects(rect))
        {
            return false;
        }
        for (T element : this.elements)
        {
            if (element.equals(b))
            {
                return true;
            }
        }
        if (this.children == null)
        {
            return false;
        }
        for (QuadTree<T> child : this.children)
        {
            if (child.recursiveContains(b, rect))
            {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        if (!(o instanceof BoundingBoxed))
        {
            return false;
        }
        return recursiveContains((BoundingBoxed) o, ((BoundingBoxed) o).getBoundingRectangle());
    }

    /**
     * Recursively collect all elements that intersect the given rectangle.
     * @param rectangle Rectangle2D.Double; the rectangle
     * @return Set&lt;T&gt; all stored elements that intersect the given rectangle
     */
    private Set<T> recursiveCollect(final Rectangle rectangle)
    {
        Set<T> result = new LinkedHashSet<>();
        if (!this.boundingBox.intersects(rectangle))
        {
            return result;
        }
        for (T element : this.elements)
        {
            if (element.getBoundingRectangle().intersects(rectangle) && element.intersects(rectangle))
            {
                result.add(element);
            }
        }
        if (this.children != null)
        {
            for (QuadTree<T> child : this.children)
            {
                result.addAll(child.recursiveCollect(rectangle));
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator()
    {
        return recursiveCollect(this.boundingBox).iterator();
    }

    /**
     * Find all elements intersecting a given bounding box. This iterator cannot be used to remove elements, but the remove
     * method can be safely called while the iterator is active.
     * @param searchArea Rectangle; the bounding box
     * @return Iterator&lt;T&gt;; iterator that returns all elements that intersect the given bounding box
     */
    public Iterator<T> iterator(final Rectangle searchArea)
    {
        return recursiveCollect(searchArea).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return recursiveCollect(this.boundingBox).toArray();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(final T[] a)
    {
        return recursiveCollect(this.boundingBox).toArray(a);
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final T e)
    {
        if (contains(e))
        {
            return false;
        }
        this.elements.add(e);
        reBalance();
        this.size++;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        if (!(o instanceof BoundingBoxed))
        {
            return false;
        }
        if (this.elements.remove(o))
        {
            this.size--;
            return true;
        }
        boolean result = false;
        BoundingBoxed bb = (BoundingBoxed) o;
        Rectangle rectangle = bb.getBoundingRectangle();
        if (this.children != null)
        {
            for (QuadTree<T> child : this.children)
            {
                if (!child.boundingBox.intersects(rectangle))
                {
                    continue;
                }
                if (child.remove(o))
                {
                    result = true;
                }
            }
        }
        if (result)
        {
            this.size--;
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return recursiveCollect(this.boundingBox).containsAll(c); // Can be very expensive
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends T> c)
    {
        boolean result = false;
        for (T candidate : c)
        {
            if (add(candidate))
            {
                result = true;
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean result = false;
        for (Object candidate : c)
        {
            if (remove(candidate))
            {
                result = true;
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        throw new RuntimeException("Not (yet) implemented");
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        this.elements.clear();
        this.children = null;
        this.size = 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "QuadTree [boundingBox=" + this.boundingBox + ", size=" + this.size + ", children=" + this.children
                + ", elements.size=" + this.elements.size() + "]";
    }

    /**
     * Return a textual representation of this quad tree up to the specified depth.
     * @param expandDepth int; the maximum depth to expand
     * @return String; textual representation of this quad tree
     */
    public String toString(final int expandDepth)
    {
        if (expandDepth > 0)
        {
            return "QuadTree [boundingBox=" + this.boundingBox + ", size=" + this.size + ", children="
                    + (this.children != null ? "[SW:" + this.southWestChild.toString(expandDepth - 1) + ", SE:"
                            + this.southEastChild.toString(expandDepth - 1) + ", NW:"
                            + this.northWestChild.toString(expandDepth - 1) + ", NE:"
                            + this.northEastChild.toString(expandDepth - 1) + "]" : "null")
                    + ", elements.size=" + this.elements.size() + "]";
        }
        else
        {
            return toString();
        }
    }
    
    /**
     * Dump a quad tree.
     * @param indent String; prefix for each output line
     * @return String; textual description of this quad tree.
     */
    public String dump(final String indent)
    {
        StringBuilder result = new StringBuilder();
        result.append(indent);
        result.append("QuadTree [size=");
        result.append(this.size);
        result.append("] ");
        result.append(this.boundingBox);
        result.append("\n");
        String subIndent = indent + "    ";
        Iterator<T> iterator = this.elements.iterator();
        for (int i = 0; i < this.elements.size(); i++)
        {
            result.append(subIndent);
            result.append(i);
            result.append(" ");
            result.append(iterator.next());
            result.append("\n");
        }
        if (this.children != null)
        {
            result.append(subIndent);
            result.append("SW");
            result.append("\n");
            result.append(this.southWestChild.dump(subIndent));
            result.append(subIndent);
            result.append("SE");
            result.append("\n");
            result.append(this.southEastChild.dump(subIndent));
            result.append(subIndent);
            result.append("NW");
            result.append("\n");
            result.append(this.northWestChild.dump(subIndent));
            result.append(subIndent);
            result.append("NE");
            result.append("\n");
            result.append(this.northEastChild.dump(subIndent));
        }
        return result.toString();
    }

}
