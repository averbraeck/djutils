package org.djutils.stats.summarizers.event;

import org.djutils.event.EventType;

/**
 * StatisticsEvents defines the standard events for statistics. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class StatisticsEvents
{

    /** Utility constructor. */
    private StatisticsEvents()
    {
        // Utility constructor
    }

    /**
     * INITIALIZED_EVENT is fired whenever a statistic is (re-)initialized. The event should define the Statistic as the source
     * and null as the content.
     */
    public static final EventType INITIALIZED_EVENT = new EventType("INITIALIZED_EVENT", null);

    /**
     * OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. The event should define the Statistic as the
     * source and the observation value as the content.
     */
    public static final EventType OBSERVATION_ADDED_EVENT = new EventType("OBSERVATION_ADDED_EVENT", null);

    /**
     * WEIGTHED_OBSERVATION_ADDED_EVENT is fired whenever a weighted observation is processed. The event should define the
     * Statistic as the source and an Object[] with { weight, observation_value } as the content.
     */
    public static final EventType WEIGHTED_OBSERVATION_ADDED_EVENT = new EventType("WEIGTHED_OBSERVATION_ADDED_EVENT", null);

    /**
     * OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. The event should define the Statistic as the
     * source and an Object[] with { timestamp, observation_value } as the content. This event is also fired at the end of the
     * observations to signal the final values.
     */
    public static final EventType TIMESTAMPED_OBSERVATION_ADDED_EVENT = new EventType("TIMESTAMPED_OBSERVATION_ADDED_EVENT", null);

    /* The following statistics are, e.g., used to draw graphs of the development of a statistical value. */

    /**
     * N_EVENT is fired whenever n is updated. The event should define the Statistic as the source and the current value of n as
     * the content.
     */
    public static final EventType N_EVENT = new EventType("N_EVENT", null);

    /**
     * COUNT_EVENT is fired whenever there is an observation that potentially updates the count of the Counter. The event should
     * define the Statistic as the source and the current count value as the content.
     */
    public static final EventType COUNT_EVENT = new EventType("COUNT_EVENT", null);

    /**
     * MIN_EVENT is fired whenever there is an observation that potentially updates the lowest observed value of the statistic.
     * The event should define the Statistic as the source and the current minimum observed value as the content.
     */
    public static final EventType MIN_EVENT = new EventType("MIN_EVENT", null);

    /**
     * MAX_EVENT is fired whenever there is an observation that potentially updates the highest observed value of the statistic.
     * The event should define the Statistic as the source and the current maximum observed value as the content.
     */
    public static final EventType MAX_EVENT = new EventType("MAX_EVENT", null);

    /**
     * MEAN_EVENT is fired whenever there is an observation that potentially updates the population mean value of the statistic.
     * The event should define the Statistic as the source and the current population mean as the content.
     */
    public static final EventType POPULATION_MEAN_EVENT = new EventType("POPULATION_MEAN_EVENT", null);

    /**
     * VARIANCE_EVENT is fired whenever there is an observation that potentially updates the population variance of the
     * statistic. The event should define the Statistic as the source and the current population variance as the content.
     */
    public static final EventType POPULATION_VARIANCE_EVENT = new EventType("POPULATION_VARIANCE_EVENT", null);

    /**
     * SKEWNESS_EVENT is fired whenever there is an observation that potentially updates the population skewness of the
     * statistic. The event should define the Statistic as the source and the current population skewness as the content.
     */
    public static final EventType POPULATION_SKEWNESS_EVENT = new EventType("POPULATION_SKEWNESS_EVENT", null);

    /**
     * KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the population kurtosis of the
     * statistic. The event should define the Statistic as the source and the current population kurtosis as the content.
     */
    public static final EventType POPULATION_KURTOSIS_EVENT = new EventType("POPULATION_KURTOSIS_EVENT", null);

    /**
     * EXCESS_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the population excess kurtosis
     * of the statistic. The event should define the Statistic as the source and the current population excess kurtosis as the
     * content.
     */
    public static final EventType POPULATION_EXCESS_KURTOSIS_EVENT = new EventType("POPULATION_EXCESS_KURTOSIS_EVENT", null);

    /**
     * STDEV_EVENT is fired whenever there is an observation that potentially updates the population standard deviation of the
     * statistic. The event should define the Statistic as the source and the current population standard deviation as the
     * content.
     */
    public static final EventType POPULATION_STDEV_EVENT = new EventType("POPULATION_STDEV_EVENT", null);

    /**
     * SUM_EVENT is fired whenever there is an observation that potentially updates the sum value of the statistic. The event
     * should define the Statistic as the source and the current sum as the content.
     */
    public static final EventType SUM_EVENT = new EventType("SUM_EVENT", null);

    /**
     * SAMPLE_MEAN_EVENT is fired whenever there is an observation that potentially updates the sample mean value of the
     * statistic. The event should define the Statistic as the source and the current sample mean as the content.
     */
    public static final EventType SAMPLE_MEAN_EVENT = new EventType("SAMPLE_MEAN_EVENT", null);

    /**
     * SAMPLE_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the sample variance of the
     * statistic. The event should define the Statistic as the source and the current sample variance as the content.
     */
    public static final EventType SAMPLE_VARIANCE_EVENT = new EventType("SAMPLE_VARIANCE_EVENT", null);

    /**
     * SAMPLE_SKEWNESS_EVENT is fired whenever there is an observation that potentially updates the sample skewness of the
     * statistic. The event should define the Statistic as the source and the current sample skewness as the content.
     */
    public static final EventType SAMPLE_SKEWNESS_EVENT = new EventType("SAMPLE_SKEWNESS_EVENT", null);

    /**
     * SAMPLE_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the sample kurtosis of the
     * statistic. The event should define the Statistic as the source and the current sample kurtosis as the content.
     */
    public static final EventType SAMPLE_KURTOSIS_EVENT = new EventType("SAMPLE_KURTOSIS_EVENT", null);

    /**
     * SAMPLE_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the sample excess kurtosis of
     * the statistic. The event should define the Statistic as the source and the current sample excess kurtosis as the content.
     */
    public static final EventType SAMPLE_EXCESS_KURTOSIS_EVENT = new EventType("SAMPLE_EXCESS_KURTOSIS_EVENT", null);

    /**
     * SAMPLE_STDEV_EVENT is fired whenever there is an observation that potentially updates the sample standard deviation of
     * the statistic. The event should define the Statistic as the source and the current sample standard deviation as the
     * content.
     */
    public static final EventType SAMPLE_STDEV_EVENT = new EventType("SAMPLE_STDEV_EVENT", null);

    /**
     * WEIGHTED_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted population mean value
     * of the statistic. The event should define the Statistic as the source and the current weighted population mean as the
     * content.
     */
    public static final EventType WEIGHTED_POPULATION_MEAN_EVENT = new EventType("WEIGHTED_POPULATION_MEAN_EVENT", null);

    /**
     * WEIGHTED_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted population
     * variance of the statistic. The event should define the Statistic as the source and the current weighted population
     * variance as the content.
     */
    public static final EventType WEIGHTED_POPULATION_VARIANCE_EVENT = new EventType("WEIGHTED_POPULATION_VARIANCE_EVENT", null);

    /**
     * WEIGHTED_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted population standard
     * deviation of the statistic. The event should define the Statistic as the source and the current weighted population
     * standard deviation as the content.
     */
    public static final EventType WEIGHTED_POPULATION_STDEV_EVENT = new EventType("WEIGHTED_POPULATION_STDEV_EVENT", null);

    /**
     * WEIGHTED_SUM_EVENT is fired whenever there is an observation that potentially updates the weighted sum value of the
     * statistic. The event should define the Statistic as the source and the current weighted sum as the content.
     */
    public static final EventType WEIGHTED_SUM_EVENT = new EventType("WEIGHTED_SUM_EVENT", null);

    /**
     * WEIGHTED_SAMPLE_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted sample mean
     * value of the statistic. The event should define the Statistic as the source and the current weighted sample mean as the
     * content.
     */
    public static final EventType WEIGHTED_SAMPLE_MEAN_EVENT = new EventType("WEIGHTED_SAMPLE_MEAN_EVENT", null);

    /**
     * WEIGHTED_SAMPLE_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * variance of the statistic. The event should define the Statistic as the source and the current weighted sample variance
     * as the content.
     */
    public static final EventType WEIGHTED_SAMPLE_VARIANCE_EVENT = new EventType("WEIGHTED_SAMPLE_VARIANCE_EVENT", null);

    /**
     * WEIGHTED_SAMPLE_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * standard deviation of the statistic. The event should define the Statistic as the source and the current weighted sample
     * standard deviation as the content.
     */
    public static final EventType WEIGHTED_SAMPLE_STDEV_EVENT = new EventType("WEIGHTED_SAMPLE_STDEV_EVENT", null);

}
