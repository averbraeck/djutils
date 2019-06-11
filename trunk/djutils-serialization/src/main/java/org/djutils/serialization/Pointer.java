package org.djutils.serialization;

/**
 * Container for an offset.
 */
class Pointer
{
    /** Current value of the offset. */
    private int offset;

    /**
     * Construct a new Pointer with specified initial offset.
     * @param initialOffset int; the initial offset
     */
    Pointer(final int initialOffset)
    {
        this.offset = initialOffset;
    }

    /**
     * Construct a new Pointer with offset 0.
     */
    Pointer()
    {
        this(0);
    }

    /**
     * Retrieve the offset.
     * @return int; the offset
     */
    int get()
    {
        return this.offset;
    }

    /**
     * Retrieve the current value of offset and increment it. The returned value is the value <b>before</b> applying the
     * increment.
     * @param increment int; the amount by which the offset must be incremented
     * @return int; the offset (before the increment was added)
     */
    int getAndIncrement(int increment)
    {
        int result = this.offset;
        this.offset += increment;
        return result;
    }

    /**
     * Increment the offset.
     * @param increment int; the amount by which the offset must be incremented
     */
    public void inc(final int increment)
    {
        this.offset += increment;
    }

    @Override
    public String toString()
    {
        return "Pointer [offset=" + offset + "]";
    }

}
