package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.stats.summarizers.DistNormalTable;
import org.djutils.stats.summarizers.Tally;

/**
 * The no storage accumulator accumulates nothing and estimates all requested quantiles from mean, standard deviation, etc. (as
 * accumulated by the Tally class object). This is sensible if the input values are normally distributed. Do not use this
 * accumulator when the input values are not normally distributes.<br>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class NoStorageAccumulator implements QuantileAccumulator
{
    /** {@inheritDoc} */
    @Override
    public double ingest(double value)
    {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double getQuantile(Tally tally, double probability)
    {
        return DistNormalTable.getInverseCumulativeProbability(tally.getSampleMean(),
                Math.sqrt(tally.getSampleVariance()), probability);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "NoStorageAccumulator";
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        // Do nothing
    }

}
