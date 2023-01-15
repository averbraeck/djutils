package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.stats.summarizers.Tally;

/**
 * Interface for quantile accumulator. <br>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public interface QuantileAccumulator
{
    /**
     * Ingest one value with weight 1. Should be called only from the Tally object and AFTER processing the value in the tally.
     * @param value double; the value
     * @return double; the registered value
     * @throws IllegalArgumentException when the registered value is NaN
     */
    double register(double value);

    /**
     * Compute (or approximate) the value that corresponds to the given fraction (percentile).
     * @param tally Tally; the tally object that accumulates mean, minimum, maximum, count, etc.
     * @param probability double; value between 0.0 and 1.0 (both inclusive)
     * @return double; the computed or approximated quantile value
     * @throws IllegalArgumentException when the probability is less than 0 or larger than 1
     * @throws NullPointerException when tally is null
     */
    double getQuantile(Tally tally, double probability);
    
    /**
     * Get, or estimate fraction of registered values between -infinity up to and including a given quantile.
     * @param tally Tally; the tally object that accumulates mean, minimum, maximum, count, etc.
     * @param quantile double; the given quantile
     * @return double; the estimated or observed fraction of registered values between -infinity up to and including the given
     *         quantile. When this QuantileAccumulator has registered zero values; this method shall return NaN.
     * @throws IllegalArgumentException when quantile is NaN
     */
    double getCumulativeProbability(Tally tally, double quantile) throws IllegalArgumentException;

    /**
     * Reset (clear all accumulated information).
     */
    void initialize();

}
