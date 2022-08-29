package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.exceptions.Throw;
import org.djutils.stats.DistNormalTable;
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
    public double register(final double value)
    {
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "accumulator can not accumulate NaN value");
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double getQuantile(final Tally tally, final double probability)
    {
        Throw.whenNull(tally, "tally cannot be null");
        Throw.when(probability < 0 || probability > 1, IllegalArgumentException.class,
                "probability should be between 0 and 1 (inclusive)");
        return DistNormalTable.getInverseCumulativeProbability(tally.getSampleMean(), Math.sqrt(tally.getSampleVariance()),
                probability);
    }

    /** {@inheritDoc} */
    @Override
    public double getCumulativeProbability(final Tally tally, final double quantile) throws IllegalArgumentException
    {
        if (tally.getN() == 0)
        {
            return Double.NaN;
        }
        return DistNormalTable.getCumulativeProbability(tally.getPopulationMean(), tally.getPopulationStDev(), quantile);
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
