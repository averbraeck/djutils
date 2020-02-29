package org.djutils.stats.summarizers.quantileaccumulator;

import org.djutils.stats.summarizers.Tally;

/**
 * Interface for quantile accumulator.
 * <br>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public interface QuantileAccumulator
{
    /**
     * Ingest one value with weight 1. Should be called only from the Tally object and AFTER processing the value in the tally.
     * @param value double; the value
     * @return double; the ingested value
     */
    double ingest(double value);
    
    /**
     * Compute (or approximate) the value that corresponds to the given fraction (percentile).
     * @param tally Tally; the tally object that accumulates mean, minimum, maximum, count, etc.
     * @param probability double; value between 0.0 and 1.0 (both inclusive)
     * @return double; the computed or approximated quantile value
     */
    double getQuantile(Tally tally, double probability);
    
    /**
     * Reset (clear all accumulated information).
     */
    void initialize();
    
}
