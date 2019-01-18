package org.djutils.decoderdumper;

/**
 * Keep track of the address of the decoder-dumper and call flushLine when the last possible address of a line is received.
 * <p>
 * Copyright (c) 2013-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Jan 3, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class HexAddressDecoder implements Decoder
{
    /** Round all printed addresses down to a multiple of this value. */
    private final int roundToMultiple;

    /**
     * Construct a new HexAddressDecoder.
     * @param roundToMultiple int; if > 1 round addresses down to the nearest (lower) multiple of this value and the append
     *            method will return true when the last byte before such a multiple is added.
     */
    public HexAddressDecoder(final int roundToMultiple)
    {
        this.roundToMultiple = roundToMultiple > 0 ? roundToMultiple : 1;
    }

    /** Result returned by getResult. */
    String result = "";

    /** {@inheritDoc} */
    @Override
    public String getResult()
    {
        String retVal = this.result;
        this.result = "";
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public int getMaximumWidth()
    {
        return 8;
    }

    /** {@inheritDoc} */
    @Override
    public boolean append(int address, byte theByte)
    {
        this.result = String.format("%08x", address / this.roundToMultiple * this.roundToMultiple);
        return this.roundToMultiple > 1 && address % this.roundToMultiple == this.roundToMultiple - 1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean ignoreForIdenticalOutputCheck()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "HexAddressDecoder [result=" + this.result + "]";
    }

}
