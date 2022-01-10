package org.djutils.stats.summarizers.event;

import java.io.Serializable;

import org.djutils.event.EventType;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

/**
 * StatisticsEvents defines the standard events for statistics. <br>
 * <br>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    public static final EventType INITIALIZED_EVENT =
            new EventType("INITIALIZED_EVENT", new MetaData("No arguments", "null", new ObjectDescriptor[0]));

    /**
     * OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. The event should define the Statistic as the
     * source and the observation value as the content.
     */
    public static final EventType OBSERVATION_ADDED_EVENT =
            new EventType("OBSERVATION_ADDED_EVENT", new MetaData("Ingested value", "Ingested Number value",
                    new ObjectDescriptor("Ingested value", "Ingested Number value", Number.class)));

    /**
     * WEIGTHED_OBSERVATION_ADDED_EVENT is fired whenever a weighted observation is processed. The event should define the
     * Statistic as the source and an Object[] with { weight, observation_value } as the content.
     */
    public static final EventType WEIGHTED_OBSERVATION_ADDED_EVENT = new EventType("WEIGHTED_OBSERVATION_ADDED_EVENT",
            new MetaData("Weight and value", "Double Weight and Double value",
                    new ObjectDescriptor[] {new ObjectDescriptor("Weight", "Double weight", Double.class),
                            new ObjectDescriptor("Value", "Double value", Double.class)}));

    /**
     * OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. The event should define the Statistic as the
     * source and an Object[] with { timestamp, observation_value } as the content. This event is also fired at the end of the
     * observations to signal the final values.
     */
    public static final EventType TIMESTAMPED_OBSERVATION_ADDED_EVENT = new EventType("TIMESTAMPED_OBSERVATION_ADDED_EVENT",
            new MetaData("Time stamp and value", "Time stamp and Double value",
                    new ObjectDescriptor[] {new ObjectDescriptor("TimeStamp", "Time stamp", Serializable.class),
                            new ObjectDescriptor("Value", "Double value", Double.class)}));

    /* The following statistics are, e.g., used to draw graphs of the development of a statistical value. */

    /**
     * N_EVENT is fired whenever n is updated. The event should define the Statistic as the source and the current (Long) value
     * of n as the content.
     */
    public static final EventType N_EVENT = new EventType("N_EVENT", new MetaData("count", "Number of ingested events",
            new ObjectDescriptor("eventCount", "Long event count", Long.class)));

    /**
     * COUNT_EVENT is fired whenever there is an observation that potentially updates the count of the Counter. The event should
     * define the Statistic as the source and the current (Long) count value as the content.
     */
    public static final EventType COUNT_EVENT = new EventType("COUNT_EVENT", new MetaData("count", "Number of ingested events",
            new ObjectDescriptor("count", "Number of ingested events", Long.class)));

    /**
     * MIN_EVENT is fired whenever there is an observation that potentially updates the lowest observed value of the statistic.
     * The event should define the Statistic as the source and the current minimum observed value as the content.
     */
    public static final EventType MIN_EVENT = new EventType("MIN_EVENT", new MetaData("Minimum value", "Minimum Double value",
            new ObjectDescriptor("Minimum value", "Minimum Double value", Double.class)));

    /**
     * MAX_EVENT is fired whenever there is an observation that potentially updates the highest observed value of the statistic.
     * The event should define the Statistic as the source and the current maximum observed value as the content.
     */
    public static final EventType MAX_EVENT = new EventType("MAX_EVENT", new MetaData("Maximum value", "Maximum Double value",
            new ObjectDescriptor("Maximum value", "Maximum Double value", Double.class)));

    /**
     * MEAN_EVENT is fired whenever there is an observation that potentially updates the population mean value of the statistic.
     * The event should define the Statistic as the source and the current population mean as the content.
     */
    public static final EventType POPULATION_MEAN_EVENT = new EventType("POPULATION_MEAN_EVENT", new MetaData("Mean value",
            "Mean Double value", new ObjectDescriptor("Mean value", "Mean Double value", Double.class)));

    /**
     * VARIANCE_EVENT is fired whenever there is an observation that potentially updates the population variance of the
     * statistic. The event should define the Statistic as the source and the current population variance as the content.
     */
    public static final EventType POPULATION_VARIANCE_EVENT =
            new EventType("POPULATION_VARIANCE_EVENT", new MetaData("Variance value", "Variance Double value",
                    new ObjectDescriptor("Variance value", "Variance Double value", Double.class)));

    /**
     * SKEWNESS_EVENT is fired whenever there is an observation that potentially updates the population skewness of the
     * statistic. The event should define the Statistic as the source and the current population skewness as the content.
     */
    public static final EventType POPULATION_SKEWNESS_EVENT =
            new EventType("POPULATION_SKEWNESS_EVENT", new MetaData("Skewness value", "Skewness Double value",
                    new ObjectDescriptor("Skewness value", "Skewness Double value", Double.class)));

    /**
     * KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the population kurtosis of the
     * statistic. The event should define the Statistic as the source and the current population kurtosis as the content.
     */
    public static final EventType POPULATION_KURTOSIS_EVENT =
            new EventType("POPULATION_KURTOSIS_EVENT", new MetaData("Kurtosis value", "Kurtosis Double value",
                    new ObjectDescriptor("Kurtosis value", "Kurtosis Double value", Double.class)));

    /**
     * EXCESS_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the population excess kurtosis
     * of the statistic. The event should define the Statistic as the source and the current population excess kurtosis as the
     * content.
     */
    public static final EventType POPULATION_EXCESS_KURTOSIS_EVENT = new EventType("POPULATION_EXCESS_KURTOSIS_EVENT",
            new MetaData("Excess kurtosis value", "Excess kurtosis Double value",
                    new ObjectDescriptor("Excess kurtosis value", "Excess kurtosis Double value", Double.class)));

    /**
     * STDEV_EVENT is fired whenever there is an observation that potentially updates the population standard deviation of the
     * statistic. The event should define the Statistic as the source and the current population standard deviation as the
     * content.
     */
    public static final EventType POPULATION_STDEV_EVENT = new EventType("POPULATION_STDEV_EVENT", new MetaData("StdDev value",
            "StdDev Double value", new ObjectDescriptor("StdDev value", "StdDev Double value", Double.class)));

    /**
     * SUM_EVENT is fired whenever there is an observation that potentially updates the sum value of the statistic. The event
     * should define the Statistic as the source and the current sum as the content.
     */
    public static final EventType SUM_EVENT = new EventType("SUM_EVENT",
            new MetaData("Sum value", "Sum Double value", new ObjectDescriptor("Sum value", "Sum Double value", Double.class)));

    /**
     * SAMPLE_MEAN_EVENT is fired whenever there is an observation that potentially updates the sample mean value of the
     * statistic. The event should define the Statistic as the source and the current sample mean as the content.
     */
    public static final EventType SAMPLE_MEAN_EVENT = new EventType("SAMPLE_MEAN_EVENT", new MetaData("Sample mean value",
            "Sample mean Double value", new ObjectDescriptor("Sample mean value", "Sample mean Double value", Double.class)));

    /**
     * SAMPLE_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the sample variance of the
     * statistic. The event should define the Statistic as the source and the current sample variance as the content.
     */
    public static final EventType SAMPLE_VARIANCE_EVENT =
            new EventType("SAMPLE_VARIANCE_EVENT", new MetaData("Sample variance value", "Sample variance Double value",
                    new ObjectDescriptor("Sample variance value", "Sample variance Double value", Double.class)));

    /**
     * SAMPLE_SKEWNESS_EVENT is fired whenever there is an observation that potentially updates the sample skewness of the
     * statistic. The event should define the Statistic as the source and the current sample skewness as the content.
     */
    public static final EventType SAMPLE_SKEWNESS_EVENT =
            new EventType("SAMPLE_SKEWNESS_EVENT", new MetaData("Sample skewness value", "Sample skewness Double value",
                    new ObjectDescriptor("Sample skewness value", "Sample skewness Double value", Double.class)));

    /**
     * SAMPLE_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the sample kurtosis of the
     * statistic. The event should define the Statistic as the source and the current sample kurtosis as the content.
     */
    public static final EventType SAMPLE_KURTOSIS_EVENT =
            new EventType("SAMPLE_KURTOSIS_EVENT", new MetaData("Sample kurtosis value", "Sample kurtosis Double value",
                    new ObjectDescriptor("sample kurtosis value", "Sample kurtosis Double value", Double.class)));

    /**
     * SAMPLE_KURTOSIS_EVENT is fired whenever there is an observation that potentially updates the sample excess kurtosis of
     * the statistic. The event should define the Statistic as the source and the current sample excess kurtosis as the content.
     */
    public static final EventType SAMPLE_EXCESS_KURTOSIS_EVENT = new EventType("SAMPLE_EXCESS_KURTOSIS_EVENT",
            new MetaData("Sample excess kurtosis value", "Sample excess kurtosis Double value",
                    new ObjectDescriptor("Sample excess kurtosis value", "Sample excess kurtosis Double value", Double.class)));

    /**
     * SAMPLE_STDEV_EVENT is fired whenever there is an observation that potentially updates the sample standard deviation of
     * the statistic. The event should define the Statistic as the source and the current sample standard deviation as the
     * content.
     */
    public static final EventType SAMPLE_STDEV_EVENT =
            new EventType("SAMPLE_STDEV_EVENT", new MetaData("Sample stdDev value", "Sample stdDev Double value",
                    new ObjectDescriptor("Sample stdDev value", "Sample stdDev Double value", Double.class)));

    /**
     * WEIGHTED_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted population mean value
     * of the statistic. The event should define the Statistic as the source and the current weighted population mean as the
     * content.
     */
    public static final EventType WEIGHTED_POPULATION_MEAN_EVENT = new EventType("WEIGHTED_POPULATION_MEAN_EVENT", new MetaData(
            "Weighted population mean value", "Weighed population mean Double value",
            new ObjectDescriptor("Weighted population mean value", "Weighted population mean Double value", Double.class)));

    /**
     * WEIGHTED_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted population
     * variance of the statistic. The event should define the Statistic as the source and the current weighted population
     * variance as the content.
     */
    public static final EventType WEIGHTED_POPULATION_VARIANCE_EVENT = new EventType("WEIGHTED_POPULATION_VARIANCE_EVENT",
            new MetaData("Weighted population variance value", "Weighted population variance Double value",
                    new ObjectDescriptor("Weighted population variance value", "Weighted population variance Double value",
                            Double.class)));

    /**
     * WEIGHTED_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted population standard
     * deviation of the statistic. The event should define the Statistic as the source and the current weighted population
     * standard deviation as the content.
     */
    public static final EventType WEIGHTED_POPULATION_STDEV_EVENT =
            new EventType("WEIGHTED_POPULATION_STDEV_EVENT",
                    new MetaData("Weighted population stdDev value", "Weighted population stdDev Double value",
                            new ObjectDescriptor("Weighted population stdDev value", "Weighted population stdDev Double value",
                                    Double.class)));

    /**
     * WEIGHTED_SUM_EVENT is fired whenever there is an observation that potentially updates the weighted sum value of the
     * statistic. The event should define the Statistic as the source and the current weighted sum as the content.
     */
    public static final EventType WEIGHTED_SUM_EVENT =
            new EventType("WEIGHTED_SUM_EVENT", new MetaData("Weighted sum value", "Weighted sum Double value",
                    new ObjectDescriptor("Weighted sum value", "Weighted sum Double value", Double.class)));

    /**
     * WEIGHTED_SAMPLE_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted sample mean
     * value of the statistic. The event should define the Statistic as the source and the current weighted sample mean as the
     * content.
     */
    public static final EventType WEIGHTED_SAMPLE_MEAN_EVENT = new EventType("WEIGHTED_SAMPLE_MEAN_EVENT",
            new MetaData("Weighted sample mean value", "Weighted sample mean Double value",
                    new ObjectDescriptor("Weighted sample mean value", "Weighted sample mean Double value", Double.class)));

    /**
     * WEIGHTED_SAMPLE_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * variance of the statistic. The event should define the Statistic as the source and the current weighted sample variance
     * as the content.
     */
    public static final EventType WEIGHTED_SAMPLE_VARIANCE_EVENT = new EventType("WEIGHTED_SAMPLE_VARIANCE_EVENT", new MetaData(
            "Weighted sample variance value", "Weighted sample variance Double value",
            new ObjectDescriptor("Weighted sample variance value", "Weighted sample variance Double value", Double.class)));

    /**
     * WEIGHTED_SAMPLE_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * standard deviation of the statistic. The event should define the Statistic as the source and the current weighted sample
     * standard deviation as the content.
     */
    public static final EventType WEIGHTED_SAMPLE_STDEV_EVENT = new EventType("WEIGHTED_SAMPLE_STDEV_EVENT",
            new MetaData("Weighted sample stdDev value", "Weighted sample stdDev Double value",
                    new ObjectDescriptor("Weighted sample stdDev value", "Weighted sample stdDev Double value", Double.class)));

    /* ********************* TIMESTANPED VERSIONS OF EVENTS FOR THE TIMESTAMPWEIGHTEDTALLY ************************ */

    /**
     * TIMED_N_EVENT is fired whenever n is updated. The event should define the Statistic as the source and the current (Long)
     * value of n as the content.
     */
    public static final TimedEventType TIMED_N_EVENT = new TimedEventType("TIMED_N_EVENT", new MetaData("count",
            "Number of ingested events", new ObjectDescriptor("eventCount", "Long event count", Long.class)));

    /**
     * TIMED_MIN_EVENT is fired whenever there is an observation that potentially updates the lowest observed value of the
     * statistic. The event should define the Statistic as the source and the current minimum observed value as the content.
     */
    public static final TimedEventType TIMED_MIN_EVENT = new TimedEventType("TIMED_MIN_EVENT", new MetaData("Minimum value",
            "Minimum Double value", new ObjectDescriptor("Minimum value", "Minimum Double value", Double.class)));

    /**
     * TIMED_MAX_EVENT is fired whenever there is an observation that potentially updates the highest observed value of the
     * statistic. The event should define the Statistic as the source and the current maximum observed value as the content.
     */
    public static final TimedEventType TIMED_MAX_EVENT = new TimedEventType("TIMED_MAX_EVENT", new MetaData("Maximum value",
            "Maximum Double value", new ObjectDescriptor("Maximum value", "Maximum Double value", Double.class)));

    /**
     * TIMED_WEIGHTED_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted population mean
     * value of the statistic. The event should define the Statistic as the source and the current weighted population mean as
     * the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_POPULATION_MEAN_EVENT =
            new TimedEventType("TIMED_WEIGHTED_POPULATION_MEAN_EVENT",
                    new MetaData("Weighted population mean value", "Weighed population mean Double value", new ObjectDescriptor(
                            "Weighted population mean value", "Weighted population mean Double value", Double.class)));

    /**
     * TIMED_WEIGHTED_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted population
     * variance of the statistic. The event should define the Statistic as the source and the current weighted population
     * variance as the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT =
            new TimedEventType("TIMED_WEIGHTED_POPULATION_VARIANCE_EVENT",
                    new MetaData("Weighted population variance value", "Weighted population variance Double value",
                            new ObjectDescriptor("Weighted population variance value",
                                    "Weighted population variance Double value", Double.class)));

    /**
     * TIMED_WEIGHTED_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted population
     * standard deviation of the statistic. The event should define the Statistic as the source and the current weighted
     * population standard deviation as the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_POPULATION_STDEV_EVENT =
            new TimedEventType("TIMED_WEIGHTED_POPULATION_STDEV_EVENT",
                    new MetaData("Weighted population stdDev value", "Weighted population stdDev Double value",
                            new ObjectDescriptor("Weighted population stdDev value", "Weighted population stdDev Double value",
                                    Double.class)));

    /**
     * TIMED_WEIGHTED_SUM_EVENT is fired whenever there is an observation that potentially updates the weighted sum value of the
     * statistic. The event should define the Statistic as the source and the current weighted sum as the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_SUM_EVENT =
            new TimedEventType("TIMED_WEIGHTED_SUM_EVENT", new MetaData("Weighted sum value", "Weighted sum Double value",
                    new ObjectDescriptor("Weighted sum value", "Weighted sum Double value", Double.class)));

    /**
     * TIMED_WEIGHTED_SAMPLE_MEAN_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * mean value of the statistic. The event should define the Statistic as the source and the current weighted sample mean as
     * the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_SAMPLE_MEAN_EVENT = new TimedEventType("TIMED_WEIGHTED_SAMPLE_MEAN_EVENT",
            new MetaData("Weighted sample mean value", "Weighted sample mean Double value",
                    new ObjectDescriptor("Weighted sample mean value", "Weighted sample mean Double value", Double.class)));

    /**
     * TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT is fired whenever there is an observation that potentially updates the weighted
     * sample variance of the statistic. The event should define the Statistic as the source and the current weighted sample
     * variance as the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT =
            new TimedEventType("TIMED_WEIGHTED_SAMPLE_VARIANCE_EVENT",
                    new MetaData("Weighted sample variance value", "Weighted sample variance Double value",
                            new ObjectDescriptor("Weighted sample variance value", "Weighted sample variance Double value",
                                    Double.class)));

    /**
     * TIMED_WEIGHTED_SAMPLE_STDEV_EVENT is fired whenever there is an observation that potentially updates the weighted sample
     * standard deviation of the statistic. The event should define the Statistic as the source and the current weighted sample
     * standard deviation as the content.
     */
    public static final TimedEventType TIMED_WEIGHTED_SAMPLE_STDEV_EVENT = new TimedEventType(
            "TIMED_WEIGHTED_SAMPLE_STDEV_EVENT",
            new MetaData("Weighted sample stdDev value", "Weighted sample stdDev Double value",
                    new ObjectDescriptor("Weighted sample stdDev value", "Weighted sample stdDev Double value", Double.class)));

}
