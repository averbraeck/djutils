package org.djutils.stats.summarizers.quantileaccumulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.djutils.exceptions.Throw;
import org.djutils.stats.summarizers.Tally;

/**
 * Quantile accumulator that stores all values and computes exact (within a few ULP) results. <br>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class FullStorageAccumulator implements QuantileAccumulator
{
    /** Storage for the accumulated values. */
    private List<Double> accumulator = new ArrayList<>();

    /** Is the accumulator currently sorted? */
    private boolean isSorted = true;

    /**
     * Construct a new FullStorageAccumulator.
     */
    public FullStorageAccumulator()
    {
        // Nothing to do here
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final double value)
    {
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "accumulator can not accumlate NaN value");
        this.accumulator.add(value);
        this.isSorted = false;
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double getQuantile(final Tally tally, final double probability)
    {
        Throw.whenNull(tally, "tally cannot be null");
        Throw.when(probability < 0 || probability > 1, IllegalArgumentException.class,
                "Probability should be between 0.0 and 1.0 (inclusive); got {}", probability);
        if (!this.isSorted)
        {
            Collections.sort(this.accumulator);
            this.isSorted = true;
        }
        double doubleIndex = (this.accumulator.size() - 1) * probability;
        int index = Math.min((int) Math.floor(doubleIndex), this.accumulator.size() - 1);
        double v0 = this.accumulator.get(index);
        if (index >= this.accumulator.size() - 1)
        {
            return v0;
        }
        double v1 = this.accumulator.get(index + 1);
        return v1 * (doubleIndex - index) + v0 * (1.0 - (doubleIndex - index));
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        this.accumulator.clear();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "FullStorageAccumulator [accumulator size=" + this.accumulator.size() + ", isSorted=" + this.isSorted + "]";
    }

}
