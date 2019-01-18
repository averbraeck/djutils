package org.djutils.decoderdumper;

/**
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class FixedString implements Decoder
{
    /**
     * The String that will be returned by <code>getResult</code> on each invocation of getResult after at least one call to
     * append.
     */
    private final String fixedResult;
    
    /** Remember if the append method was ever called. */
    private boolean appendWasCalled = false;

    /**
     * Construct a Decoder that returns a fixed result in the <code>getResult</code> method.
     * @param fixedResult String; the String that the <code>getResult</code> method will return on each invocation
     */
    public FixedString(final String fixedResult)
    {
        this.fixedResult = fixedResult;
    }

    /** {@inheritDoc} */
    @Override
    public String getResult()
    {
        if (this.appendWasCalled)
        {
            this.appendWasCalled = false;
            return this.fixedResult;
        }
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public int getMaximumWidth()
    {
        return this.fixedResult.length();
    }

    /** {@inheritDoc} */
    @Override
    public boolean append(int address, byte theByte)
    {
        this.appendWasCalled = true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean ignoreForIdenticalOutputCheck()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "FixedString [fixedResult=" + this.fixedResult + ", appendWasCalled=" + this.appendWasCalled + "]";
    }

}
