package org.djutils.math.means;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.djutils.exceptions.Throw;

/**
 * Methods and fields common to all implementations of Mean. Mean implements various kinds of mean. For an excellent discussion
 * on this subject read <a href=
 * "https://towardsdatascience.com/on-average-youre-using-the-wrong-average-geometric-harmonic-means-in-data-analysis-2a703e21ea0"
 * >On Average, You’re Using the Wrong Average: Geometric &amp; Harmonic Means in Data Analysis</a>
 * <p>
 * Copyright (c) 2013-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @param <MT> mean type
 * @param <V> value type
 * @param <W> weight type
 */
public abstract class AbstractMean<MT, V extends Number, W extends Number>
{
    /** Weighted sum of values. Interpretation varies with the kind of mean. */
    private double weightedSumOfValues;

    /** Sum of weights. */
    private double sumOfWeights;

    /**
     * Constructor.
     */
    public AbstractMean()
    {
        // Nothing to initialize here; the double fields are created with value 0.0 and the unityWeight is initialized to 1.
    }

    /**
     * Returns the weighted mean of accumulated data.
     * @return weighted mean of accumulated data
     */
    public abstract double getMean();

    /**
     * Accumulate some data.
     * @param value the value to add to the <code>weightedSumOfValues</code>
     * @param weight the weight to assign to the <code>value</code>
     */
    final void increment(final double value, final double weight)
    {
        this.weightedSumOfValues += value;
        this.sumOfWeights += weight;
    }

    /**
     * Returns the weighted sum of available data. Meaning varies per type of mean.
     * @return weighted sum of accumulated data
     */
    public final double getSum()
    {
        return this.weightedSumOfValues;
    }

    /**
     * Returns the sum of the weights.
     * @return sum of the weights
     */
    public final double getSumOfWeights()
    {
        return this.sumOfWeights;
    }

    /**
     * Adds a value with weight.
     * @param value the value
     * @param weight the weight
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final V value, final W weight)
    {
        return addImpl(value, weight);
    }

    /**
     * Adds a value with weight.
     * @param value the value
     * @param weight the weight
     * @return for method chaining
     */
    protected abstract AbstractMean<MT, V, W> addImpl(V value, Number weight);

    /** Unity weight. */
    private final Number unityWeight = Integer.valueOf(1);

    /**
     * Add a value with weight 1.
     * @param value the value
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final V value)
    {
        return addImpl(value, this.unityWeight);
    }

    /**
     * Adds weighted values. Note that iteration order is pivotal in correct operations. This method should not be used with
     * instances of {@code HashMap} or {@code HashSet}.
     * @param values values
     * @param weights weights
     * @return for method chaining
     * @throws IllegalArgumentException if the number of values is not equal to the number of weights
     */
    public final AbstractMean<MT, V, W> add(final Iterable<V> values, final Iterable<W> weights) throws IllegalArgumentException
    {
        Iterator<V> itV = values.iterator();
        Iterator<W> itW = weights.iterator();
        while (itV.hasNext())
        {
            Throw.when(!itW.hasNext(), IllegalArgumentException.class, "Unequal number of values and weights.");
            addImpl(itV.next(), itW.next());
        }
        Throw.when(itW.hasNext(), IllegalArgumentException.class, "Unequal number of values and weights.");
        return this;
    }

    /**
     * Adds weighted values.
     * @param values values
     * @param weights weights
     * @return for method chaining
     * @throws IllegalArgumentException if the number of values is not equal to the number of weights
     */
    public final AbstractMean<MT, V, W> add(final V[] values, final W[] weights) throws IllegalArgumentException
    {
        Throw.when(values.length != weights.length, IllegalArgumentException.class, "Unequal number of values and weights.");
        for (int i = 0; i < values.length; i++)
        {
            addImpl(values[i], weights[i]);
        }
        return this;
    }

    /**
     * Adds each key value from a map weighted with the mapped to value.
     * @param map map
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final Map<V, W> map)
    {
        for (Entry<V, W> entry : map.entrySet())
        {
            addImpl(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds each value with a weight obtained by calling the provided <code>weights</code> function.
     * @param collection values
     * @param weights weights
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final Collection<V> collection, final Function<V, W> weights)
    {
        for (V v : collection)
        {
            addImpl(v, weights.apply(v));
        }
        return this;
    }

    /**
     * Adds each value (obtained by calling the <code>values</code> function on each object in a Collection) with a weight
     * (obtained by calling the <code> weights</code> function on the same object from the Collection).
     * @param collection collection of source objects
     * @param values values
     * @param weights weights
     * @param <S> type of source object
     * @return for method chaining
     */
    public final <S> AbstractMean<MT, V, W> add(final Collection<S> collection, final Function<S, V> values,
            final Function<S, W> weights)
    {
        for (S s : collection)
        {
            addImpl(values.apply(s), weights.apply(s));
        }
        return this;
    }

    /**
     * Add values with weight 1.
     * @param values the values to add
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final Iterable<V> values)
    {
        Iterator<V> itV = values.iterator();
        while (itV.hasNext())
        {
            addImpl(itV.next(), this.unityWeight);
        }
        return this;
    }

    /**
     * Add values with weight 1.
     * @param values the values to add
     * @return for method chaining
     */
    public final AbstractMean<MT, V, W> add(final V[] values)
    {
        for (int i = 0; i < values.length; i++)
        {
            addImpl(values[i], this.unityWeight);
        }
        return this;
    }

}
