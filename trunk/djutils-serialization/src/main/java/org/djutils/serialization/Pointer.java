package org.djutils.serialization;

/**
 * Container for an offset.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$,  <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
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
    int getAndIncrement(final int increment)
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

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Pointer [offset=" + this.offset + "]";
    }

}
