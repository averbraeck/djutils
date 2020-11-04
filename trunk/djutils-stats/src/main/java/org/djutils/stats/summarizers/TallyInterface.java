package org.djutils.stats.summarizers;

import org.djutils.stats.ConfidenceInterval;

/**
 * The Tally interface defines the methods to be implemented by a tally object, which ingests a series of values and provides
 * mean, standard deviation, etc. of the ingested values.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
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
     * @return double; sum
     */
    double getSum();

    /**
     * Returns the sample mean of all observations since the initialization.
     * @return double; the sample mean
     */
    double getSampleMean();

    /**
     * Returns the population mean of all observations since the initialization.
     * @return double; the population mean
     */
    default double getPopulationMean()
    {
        return getSampleMean();
    }

    /**
     * Returns the current (unbiased) sample standard deviation of all observations since the initialization. The sample
     * standard deviation is defined as the square root of the sample variance.
     * @return double; the sample standard deviation
     */
    double getSampleStDev();

    /**
     * Returns the current (biased) population standard deviation of all observations since the initialization. The population
     * standard deviation is defined as the square root of the population variance.
     * @return double; the population standard deviation
     */
    double getPopulationStDev();

    /**
     * Returns the current (unbiased) sample variance of all observations since the initialization. The calculation of the
     * sample variance in relation to the population variance is undisputed. The formula is:<br>
     * &nbsp;&nbsp;<i>S<sup>2</sup> = (1 / (n - 1)) * [ &Sigma;x<sup>2</sup> - (&Sigma;x)<sup>2</sup> / n ] </i><br>
     * which can be calculated on the basis of the calculated population variance <i>&sigma;<sup>2</sup></i> as follows:<br>
     * &nbsp;&nbsp;<i>S<sup>2</sup> = &sigma;<sup>2</sup> * n / (n - 1)</i><br>
     * @return double; the current sample variance of this tally
     */
    double getSampleVariance();

    /**
     * Returns the current (biased) population variance of all observations since the initialization. The population variance is
     * defined as:<br>
     * <i>&sigma;<sup>2</sup> = (1 / n) * [ &Sigma;x<sup>2</sup> - (&Sigma;x)<sup>2</sup> / n ] </i>
     * @return double; the current population variance of this tally
     */
    double getPopulationVariance();

    /**
     * Return the (unbiased) sample skewness of the ingested data. There are different formulas to calculate the unbiased
     * (sample) skewness from the biased (population) skewness. Minitab, for instance calculates unbiased skewness as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>unbiased</sub> = Skew<sub>biased</sub> [ ( n - 1) / n ]<sup> 3/2</sup></i> <br>
     * whereas SAS, SPSS and Excel calculate it as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>unbiased</sub> = Skew<sub>biased</sub> &radic;[ n ( n - 1)] / (n - 2)</i> <br>
     * Here we follow the last mentioned formula. All formulas converge to the same value with larger n.
     * @return double; the sample skewness of the ingested data
     */
    double getSampleSkewness();

    /**
     * Return the (biased) population skewness of the ingested data. The population skewness is defined as:<br>
     * &nbsp;&nbsp;<i>Skew<sub>biased</sub> = [ &Sigma; ( x - &mu; ) <sup>3</sup> ] / [ n . S<sup>3</sup> ]</i><br>
     * where <i>S<sup>2</sup></i> is the <b>sample</b> variance. So the denominator is equal to <i>[ n .
     * sample_var<sup>3/2</sup> ]</i> .
     * @return double; the skewness of the ingested data
     */
    double getPopulationSkewness();

    /**
     * Return the sample kurtosis of the ingested data. The sample kurtosis can be defined in multiple ways. Here, we choose the
     * following formula:<br>
     * &nbsp;&nbsp;<i>Kurt<sub>unbiased</sub> = [ &Sigma; ( x - &mu; ) <sup>4</sup> ] / [ ( n - 1 ) . S<sup>4</sup> ]</i><br>
     * where <i>S<sup>2</sup></i> is the <u>sample</u> variance. So the denominator is equal to <i>[ ( n - 1 ) .
     * sample_var<sup>2</sup> ]</i> .
     * @return double; the sample kurtosis of the ingested data
     */
    double getSampleKurtosis();

    /**
     * Return the (biased) population kurtosis of the ingested data. The population kurtosis is defined as:<br>
     * &nbsp;&nbsp;<i>Kurt<sub>biased</sub> = [ &Sigma; ( x - &mu; ) <sup>4</sup> ] / [ n . &sigma;<sup>4</sup> ]</i><br>
     * where <i>&sigma;<sup>2</sup></i> is the <u>population</u> variance. So the denominator is equal to <i>[ n .
     * pop_var<sup>2</sup> ]</i> .
     * @return double; the population kurtosis of the ingested data
     */
    double getPopulationKurtosis();

    /**
     * Return the sample excess kurtosis of the ingested data. The sample excess kurtosis is the sample-corrected value of the
     * excess kurtosis. Several formulas exist to calculate the sample excess kurtosis from the population kurtosis. Here we
     * use:<br>
     * &nbsp;&nbsp;<i>ExcessKurt<sub>unbiased</sub> = ( n - 1 ) / [( n - 2 ) * ( n - 3 )] [ ( n + 1 ) *
     * ExcessKurt<sub>biased</sub> + 6]</i> <br>
     * This is the excess kurtosis that is calculated by, for instance, SAS, SPSS and Excel.
     * @return double; the sample excess kurtosis of the ingested data
     */
    double getSampleExcessKurtosis();

    /**
     * Return the population excess kurtosis of the ingested data. The kurtosis value of the normal distribution is 3. The
     * excess kurtosis is the kurtosis value shifted by -3 to be 0 for the normal distribution.
     * @return double; the population excess kurtosis of the ingested data
     */
    double getPopulationExcessKurtosis();

    /**
     * Compute the quantile for the given probability.
     * @param probability double; the probability for which the quantile is to be computed. The value should be between 0 and 1,
     *            inclusive.
     * @return double; the quantile for the probability
     * @throws IllegalArgumentException when the probability is less than 0 or larger than 1
     */
    double getQuantile(double probability);

    /**
     * returns the confidence interval on either side of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @return double[]; the confidence interval of this tally
     * @throws IllegalArgumentException when alpha is less than 0 or larger than 1
     */
    double[] getConfidenceInterval(double alpha);

    /**
     * returns the confidence interval based of the mean.
     * @param alpha double; Alpha is the significance level used to compute the confidence level. The confidence level equals
     *            100*(1 - alpha)%, or in other words, an alpha of 0.05 indicates a 95 percent confidence level.
     * @param side ConfidenceInterval; the side of the confidence interval with respect to the mean
     * @return double[]; the confidence interval of this tally
     * @throws IllegalArgumentException when alpha is less than 0 or larger than 1
     * @throws NullPointerException when side is null
     */
    double[] getConfidenceInterval(double alpha, ConfidenceInterval side);

}
