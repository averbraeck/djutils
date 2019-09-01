package org.djutils.means;

/**
 * Compute the geometric (weighted) mean of a set of values. Geometric mean can not handle negative or zero values.
 * <p>
 * Copyright (c) 2013-2016 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version Oct 26, 2018 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <V> value type
 * @param <W> weight type
 */
public class GeometricMean<V extends Number, W extends Number> extends AbstractMean<GeometricMean<V, W>, V, W>
{

    /** {@inheritDoc} */
    @Override
    public final double getMean()
    {
        return Math.exp(getSum() / getSumOfWeights());
    }

    /** {@inheritDoc} */
    @Override
    public final GeometricMean<V, W> addImpl(final V value, final Number weight)
    {
        increment(Math.log(value.doubleValue()) * weight.doubleValue(), weight.doubleValue());
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "GeometricMean [current sum of logarithmic values=" + getSum() + ", current sum of weights="
                + getSumOfWeights() + ", current geometric mean=" + getMean() + "]";
    }

}
