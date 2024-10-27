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

    @Override
    public double register(final double value)
    {
        Throw.when(Double.isNaN(value), IllegalArgumentException.class, "accumulator can not accumlate NaN value");
        this.accumulator.add(value);
        this.isSorted = false;
        return value;
    }

    /**
     * Sort the values in the accumulator (if needed).
     */
    private void ensureSorted()
    {
        if (!this.isSorted)
        {
            Collections.sort(this.accumulator);
            this.isSorted = true;
        }
    }

    @Override
    public double getQuantile(final Tally tally, final double probability)
    {
        Throw.whenNull(tally, "tally cannot be null");
        Throw.when(probability < 0 || probability > 1, IllegalArgumentException.class,
                "Probability should be between 0.0 and 1.0 (inclusive); got {}", probability);
        ensureSorted();
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

    @Override
    public double getCumulativeProbability(final Tally tally, final double quantile)
    {
        Throw.when(Double.isNaN(quantile), IllegalArgumentException.class, "quantile may not be NaN");
        ensureSorted();
        // @formatter:off
        /*
         * Make sure to handle all these cases correctly:
         * 1: the accumulator list is empty (return NaN)
         * 2: quantile is less than the first value in the list (return 0.0)
         * 3: quantile is more than the last value in the list (return 1.0)
         * 4: quantile equals exactly one element in the list (return (1 + 2 * rank) / size / 2)
         * 5: quantile equals several (successive) elements in the list (return (1 + rankL + rankH) / size / 2)
         * 6: quantile lies between two (successive) elements in the list (return (rankL + rankH) / size)
         * 
         * Although the basic idea of binary search is comparatively straightforward, the details can be surprisingly tricky.
         * — Donald Knuth; Knuth 1998, §6.2.1 ("Searching an ordered table"), subsection "Binary search".
         * Knuth was right (PK).
         */
        // @formatter:on
        if (this.accumulator.size() == 0)
        {
            return Double.NaN; // case 1
        }
        int lowerBound = 0;
        int upperBound = this.accumulator.size();

        while (lowerBound < upperBound)
        {
            int guess = (lowerBound + upperBound) / 2;
            if (this.accumulator.get(guess) < quantile)
            {
                lowerBound = guess + 1;
            }
            else 
            {
                upperBound = guess;
            }
        }
        int b = lowerBound;
        upperBound = this.accumulator.size();
        while (b < upperBound)
        {
            int guess = (b + upperBound) / 2;
            double value = this.accumulator.get(guess);
            if (value > quantile)
            {
                upperBound = guess;
            }
            else 
            {
                b = guess + 1;
            }
        }
        upperBound--;
        if (upperBound < 0)
        {
            return 0.0; // case 2
        }
        if (lowerBound >= this.accumulator.size())
        {
            return 1.0; // case 3
        }
        double adjust = 0;
        if (upperBound >= lowerBound)
        {
            adjust = 1; // cases 4 and 5
        }
        if (upperBound < lowerBound)
        {
            adjust = 1; // case 6
        }
        return (adjust + upperBound + lowerBound) / this.accumulator.size() / 2; // cases 4, 5 and 6
    }
    
    @Override
    public void initialize()
    {
        this.accumulator.clear();
    }

    @Override
    public String toString()
    {
        return "FullStorageAccumulator [accumulator size=" + this.accumulator.size() + ", isSorted=" + this.isSorted + "]";
    }

}
