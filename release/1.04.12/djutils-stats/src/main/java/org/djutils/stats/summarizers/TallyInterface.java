package org.djutils.stats.summarizers;

/**
 * The Tally interface defines the methods to be implemented by a tally object, which ingests a series of values and provides
 * mean, standard deviation, etc. of the ingested values.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public interface TallyInterface extends BasicTallyInterface
{
    /**
     * Process one observed value.
     * @param value double; the value to process
     * @return double; the value
     */
    double ingest(double value);

    /**
     * Return the sum of the values of the observations.
     * @return double sum
     */
    double getSum();

    /**
     * Returns the sample mean of all observations since the initialization.
     * @return double the sample mean
     */
    double getSampleMean();
    
    /**
     * Returns the mean of all observations since the initialization.
     * @return double the  mean
     */
    default double getMean()
    {
        return getSampleMean();
    }

    /**
     * Returns the current tally sample standard deviation.
     * @return double the sample standard deviation
     */
    double getSampleStDev();

    /**
     * Returns the current tally standard deviation.
     * @return double the standard deviation
     */
    double getStDev();

    /**
     * Returns the current sample variance of this tally.
     * @return double; the current sample variance of this tally
     */
    double getSampleVariance();

    /**
     * Returns the current variance of this tally.
     * @return double; the current variance of this tally
     */
    double getVariance();

    /**
     * Return the sample skewness of the ingested data.
     * @return double; the sample skewness of the ingested data
     */
    double getSampleSkewness();
    
    /**
     * Return the skewness of the ingested data.
     * @return double; the skewness of the ingested data
     */
    double getSkewness();
    
    /**
     * Return the sample excess kurtosis of the ingested data.
     * @return double; the sample excess kurtosis of the ingested data
     */
    double getSampleKurtosis();
    
    /**
     * Return the excess kurtosis of the ingested data.
     * @return double; the excess kurtosis of the ingested data
     */
    double getKurtosis();
    
    /**
     * Compute a quantile.
     * @param probability double; the probability for which the quantile is to be computed
     * @return double; the quantile for the probability
     */
    double getQuantile(double probability);

    /**
     * returns the confidence interval on either side of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @return double[] the confidence interval of this tally
     */
    double[] getConfidenceInterval(double alpha);

    /**
     * returns the confidence interval based of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @param side short; the side of the confidence interval with respect to the mean
     * @return double[] the confidence interval of this tally
     */
    double[] getConfidenceInterval(double alpha, ConfidenceInterval side);

}
