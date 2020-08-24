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
    /** Maximum number of line strings in one cell. Should probably configurable. */
    private final int maximumLoad;

    /** Minimum width and height of a QuadTree bounding box. */
    private final double minimumSize;

    /** The actual quad tree. */
    private final SubTree<T> tree;

    /**
     * Create a new QuadTree object (or a sub-tree).
     * @param maximumLoad int; number of elements at any level that warrants investigating if the tree can be re-balanced
     * @param minimumSize double; minimum width or height of a sub tree Rectangle (smaller sub tree are never created)
     * @param left double; the lowest X-coordinate that is allowed (inclusive)
     * @param bottom double; the lowest Y-coordinate that is allowed (inclusive)
     * @param right double; the highest X-coordinate that is allowed (exclusive)
     * @param top double; the highest Y-coordinate that is allowed (exclusive)
     */
    public QuadTree(final int maximumLoad, final double minimumSize, final double left, final double bottom, final double right,
            final double top)
    {
        Throw.when(left >= right, IllegalArgumentException.class, "left (%f) must be less than right (%f)", left, right);
        Throw.when(bottom >= top, IllegalArgumentException.class, "bottom (%f) must be less than top (%f)", bottom, top);
        this.maximumLoad = maximumLoad;
        this.minimumSize = minimumSize;
        this.tree = new SubTree<T>(this, new Rectangle(left, bottom, right, top));
    }

    /**
     * Return the number of objects at which it is time to try to re-balance.
     * @return int; the number of objects at which it is time to try to re-balance
     */
    public int getMaxLoad()
    {
        return this.maximumLoad;
    }

    /**
     * Return the minimum sub-tree rectangle size.
     * @return double; the minimum sub-tree rectangle size
     */
    public double getMinimumSize()
    {
        return this.minimumSize;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.tree.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.tree.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        if (!(o instanceof BoundingBoxed))
        {
            return false;
        }
        return this.tree.recursiveContains((BoundingBoxed) o, ((BoundingBoxed) o).getBoundingRectangle());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator()
    {
        return this.tree.iterator();
    }

    /**
     * Find all elements intersecting a given bounding box. This iterator cannot be used to remove elements, but the remove
     * method can be safely called while the iterator is active.
     * @param searchArea Rectangle; the bounding box
     * @return Iterator&lt;T&gt;; iterator that returns all elements that intersect the given bounding box
     */
    public Iterator<T> iterator(final Rectangle searchArea)
    {
        return this.tree.recursiveCollect(searchArea).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return this.tree.recursiveCollect(this.tree.getBoundingBox()).toArray();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(final T[] a)
    {
        return this.tree.toArray(a);
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final T e)
    {
        return this.tree.add(e);
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        if (!(o instanceof BoundingBoxed))
        {
            return false;
        }
        return this.tree.remove(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.tree.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends T> c)
    {
        return this.tree.addAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        return this.tree.removeAll(c);
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
        this.tree.clear();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "QuadTree [maximumLoad=" + this.maximumLoad + ", minimumSize=" + this.minimumSize + ", tree=" + this.tree + "]";
    }

    /**
     * Make a textual description of this quad tree drilling down to the prescribed depth.
     * @param expandDepth int; maximum depth to descend
     * @return String; textual description of this quad tree
     */
    public String toString(final int expandDepth)
    {
        return "QuadTree [maximumLoad=" + this.maximumLoad + ", minimumSize=" + this.minimumSize + ", tree="
                + this.tree.toString(expandDepth) + "]";
    }

    /**
     * Dump a quad tree.
     * @param indent String; prefix for each output line
     * @return String; textual description of this quad tree.
     */
    public String dump(final String indent)
    {
        return this.tree.dump(indent);
    }

    /**
     * Sub tree of a quad tree.
     * @param <T> Type of object stored in this quad tree
     */
    @SuppressWarnings("hiding")
    class SubTree<T extends BoundingBoxed> implements Collection<T>
    {
        /** Root of the quad tree. */
        private final QuadTree<T> root;

        /** Bounding box of this quad tree. */
        private final Rectangle boundingBox;

        /** Current number of objects in this quad tree. Includes all children, counting each object exactly once. */
        private int size = 0;

        /** South West child of this node. */
        private SubTree<T> southWestChild = null;

        /** South East child of this node. */
        private SubTree<T> southEastChild = null;

        /** North West child of this node. */
        private SubTree<T> northWestChild = null;

        /** North East child of this node. */
        private SubTree<T> northEastChild = null;

        /** If the four children have been allocated, this array will be non-null and contain the four children. */
        private SubTree<T>[] children = null;

        /** Elements stored at this node. */
        private Set<T> elements = new LinkedHashSet<>();

        /**
         * Construct a new sub tree.
         * @param root QuadTree; the root
         * @param boundingBox Rectangle; the bounding box of the new sub tree
         */
        SubTree(final QuadTree<T> root, final Rectangle boundingBox)
        {
            this.root = root;
            this.boundingBox = boundingBox;
        }

        /**
         * Retrieve the bounding box of this sub tree.
         * @return Rectangle; the bounding box of this sub tree
         */
        public final Rectangle getBoundingBox()
        {
            return this.boundingBox;
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

        /** {@inheritDoc} */
        @Override
        public boolean add(final T e)
        {
            if (contains(e))
            {
                return false;
            }
            this.elements.add(e);
            this.size++;
            reBalance();
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public boolean remove(final Object o)
        {
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
                for (SubTree<T> child : this.children)
                {
                    if (!child.boundingBox.intersects(rectangle))
                    {
                        continue; // This is the time saver
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
        public void clear()
        {
            this.elements.clear();
            this.children = null;
            this.size = 0;
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
        public boolean contains(final Object o)
        {
            if (!(o instanceof BoundingBoxed))
            {
                return false;
            }
            return recursiveContains((BoundingBoxed) o, ((BoundingBoxed) o).getBoundingRectangle());
        }

        /** {@inheritDoc} */
        @Override
        public Iterator<T> iterator()
        {
            return recursiveCollect(this.boundingBox).iterator();
        }

        /** {@inheritDoc} */
        @Override
        public Object[] toArray()
        {
            return recursiveCollect(this.boundingBox).toArray();
        }

        /** {@inheritDoc} */
        @Override
        public <T> T[] toArray(final T[] a)
        {
            return recursiveCollect(this.boundingBox).toArray(a);
        }

        /** {@inheritDoc} */
        @Override
        public boolean containsAll(final Collection<?> c)
        {
            return recursiveCollect(this.boundingBox).containsAll(c); // Can be very expensive
        }

        /**
         * Recursively search for a particular object.
         * @param b BoundingBoxed; the object to search for
         * @param rect Rectangle2D.Double the bounding box of the object
         * @return boolean; true if this quad tree contains the object; false if this quad tree does not contain the object
         */
        boolean recursiveContains(final BoundingBoxed b, final Rectangle rect)
        {
            if (!this.boundingBox.intersects(rect))
            {
                return false; // This is the time saver
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
            for (SubTree<T> child : this.children)
            {
                if (child.recursiveContains(b, rect))
                {
                    return true;
                }
            }
            return false;
        }

        /**
         * Recursively collect all elements that intersect the given rectangle.
         * @param rectangle Rectangle2D.Double; the rectangle
         * @return Set&lt;T&gt; all stored elements that intersect the given rectangle
         */
        public Set<T> recursiveCollect(final Rectangle rectangle)
        {
            Set<T> result = new LinkedHashSet<>();
            if (!this.boundingBox.intersects(rectangle))
            {
                return result; // This is the time saver
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
                for (SubTree<T> child : this.children)
                {
                    result.addAll(child.recursiveCollect(rectangle));
                }
            }
            return result;
        }

        /**
         * Optimize the distribution of elements at this node and at sub-nodes.
         */
        @SuppressWarnings("unchecked")
        private void reBalance()
        {
            if (this.elements.size() < this.root.getMaxLoad() || this.boundingBox.getWidth() < this.root.getMinimumSize()
                    || this.boundingBox.getHeight() < this.root.getMinimumSize())
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
            if (canMove == 0 || canMove < this.root.getMaxLoad() / 2 && this.children == null)
            {
                // System.out.println("reBalance: not moving " + canMove + " of " + this.elements.size());
                return;
            }
            // System.out.println("At start of reBalance of " + this.toString(1));
            if (this.children == null)
            {
                this.southWestChild = new SubTree<T>(this.root,
                        new Rectangle(this.boundingBox.getLeft(), this.boundingBox.getBottom(), cX, cY));
                this.southEastChild = new SubTree<T>(this.root,
                        new Rectangle(cX, this.boundingBox.getBottom(), this.boundingBox.getRight(), cY));
                this.northWestChild =
                        new SubTree<T>(this.root, new Rectangle(this.boundingBox.getLeft(), cY, cX, this.boundingBox.getTop()));
                this.northEastChild = new SubTree<T>(this.root,
                        new Rectangle(cX, cY, this.boundingBox.getRight(), this.boundingBox.getTop()));
                this.children =
                        new SubTree[] { this.southWestChild, this.southEastChild, this.northWestChild, this.northEastChild };
                // for (SubTree<T> sub : this.children)
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
                for (SubTree<T> child : this.children)
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
        public String toString()
        {
            return "SubTree [boundingBox=" + this.boundingBox + ", size=" + this.size + ", children=" + this.children
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
                return "SubTree [boundingBox=" + this.boundingBox + ", size=" + this.size + ", children="
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
            result.append("SubTree [size=");
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

}
