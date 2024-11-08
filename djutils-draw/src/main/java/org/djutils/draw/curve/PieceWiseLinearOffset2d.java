package org.djutils.draw.curve;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.djutils.exceptions.Throw;
import org.djutils.immutablecollections.ImmutableLinkedHashSet;
import org.djutils.immutablecollections.ImmutableNavigableSet;
import org.djutils.immutablecollections.ImmutableSet;
import org.djutils.immutablecollections.ImmutableTreeSet;

/**
 * Container for piece-wise linear offsets, defined by the offsets at particular fractional positions.
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class PieceWiseLinearOffset2d implements Iterable<Double>
{

    /** The underlying data. */
    private final NavigableMap<Double, Double> data = new TreeMap<>();

    /**
     * Create FractionalLengthData from an array of double values.
     * @param data fractional length - value pairs. Fractional lengths do not need to be in order
     * @throws NullPointerException when <code>data</code> is <code>null</code>
     * @throws IllegalArgumentException when the number of input values is not even or 0, or a fractional value is not in the
     *             range [0, 1], or an offset value is not finite, or multiple values are provided for the same fraction
     */
    public PieceWiseLinearOffset2d(final double... data)
    {
        Throw.when(data.length < 2 || data.length % 2 > 0, IllegalArgumentException.class,
                "Number of input values must be even and at least 2");
        for (int i = 0; i < data.length; i = i + 2)
        {
            Throw.when(data[i] < 0.0 || data[i] > 1.0, IllegalArgumentException.class,
                    "Fractional length %f is outside of range [0 ... 1]", data[i]);
            Throw.when(1 / data[0] < 0, IllegalArgumentException.class, "Fractional length data may not contain -0.0 fraction");
            Throw.when(!Double.isFinite(data[i + 1]), IllegalArgumentException.class, "values must be finite (got %f)",
                    data[i + 1]);
            Throw.when(this.data.get(data[i]) != null, IllegalArgumentException.class, "Duplicate fraction is not permitted");
            this.data.put(data[i], data[i + 1]);
        }
    }

    /**
     * Create FractionalLengthData from a Map of key-value pairs.
     * @param data fractional length - value pairs. Fractional lengths do not need to be in order.
     * @throws IllegalArgumentException when the input data is null or empty, or a fractional value is not in the range [0, 1],
     *             or an offset value is not finite
     */
    public PieceWiseLinearOffset2d(final Map<Double, Double> data)
    {
        Throw.when(data == null || data.isEmpty(), IllegalArgumentException.class, "Input data is empty or null");
        for (Entry<Double, Double> entry : data.entrySet())
        {
            Double key = entry.getKey();
            Throw.whenNull(key, "key in provided data may not be null");
            Throw.when(key < 0.0 || entry.getKey() > 1.0, IllegalArgumentException.class,
                    "Fractional length %s is outside of range [0 ... 1].", entry.getKey());
            Throw.when(1 / key < 0, IllegalArgumentException.class, "Fractional length data may not contain -0.0 fraction");
            Double value = entry.getValue();
            Throw.whenNull(value, "value in provided map may not be null");
            Throw.when(!Double.isFinite(value), IllegalArgumentException.class, "values must be finite (got %f)", value);
            Throw.when(this.data.get(key) != null, IllegalArgumentException.class, "Duplicate fraction is not permitted");
            this.data.put(key, value);
        }
    }

    /**
     * Returns the data at given fractional length. If only data beyond the fractional length is available, the first available
     * value is returned. If only data before the fractional length is available, the last available value is returned.
     * Otherwise data is linearly interpolated.
     * @param fractionalLength fractional length, may be outside range [0, 1].
     * @return interpolated or extended value.
     */
    public double get(final double fractionalLength)
    {
        Double exact = this.data.get(fractionalLength);
        if (exact != null)
        {
            return exact;
        }
        Entry<Double, Double> ceiling = this.data.ceilingEntry(fractionalLength);
        if (ceiling == null)
        {
            return this.data.lastEntry().getValue();
        }
        Entry<Double, Double> floor = this.data.floorEntry(fractionalLength);
        if (floor == null)
        {
            return this.data.firstEntry().getValue();
        }
        double w = (fractionalLength - floor.getKey()) / (ceiling.getKey() - floor.getKey());
        return (1.0 - w) * floor.getValue() + w * ceiling.getValue();
    }

    /**
     * Returns the derivative of the data with respect to fractional length.
     * @param fractionalLength fractional length, may be outside range [0, 1].
     * @return derivative of the data with respect to fractional length.
     */
    public double getDerivative(final double fractionalLength)
    {
        Entry<Double, Double> ceiling, floor;
        if (fractionalLength == 0.0)
        {
            ceiling = this.data.higherEntry(fractionalLength);
            floor = this.data.floorEntry(fractionalLength);
        }
        else
        {
            ceiling = this.data.ceilingEntry(fractionalLength);
            floor = this.data.lowerEntry(fractionalLength);
        }
        if (ceiling == null || floor == null)
        {
            return 0.0;
        }
        return (ceiling.getValue() - floor.getValue()) / (ceiling.getKey() - floor.getKey());
    }

    /**
     * Returns the fractional lengths in the underlying data. TODO get rid of this expensive method (callers should be rewritten
     * to use the <code>iterator()</code>)
     * @return fractional lengths in the underlying data
     */
    public ImmutableNavigableSet<Double> getFractionalLengths()
    {
        return new ImmutableTreeSet<>(this.data.keySet());
    }

    /**
     * Returns the values in the underlying data. TODO get rid of this method. Let callers use the iterator and the
     * <code>get()</code> method.
     * @return values in the underlying data
     */
    public ImmutableSet<Double> getValues()
    {
        return new ImmutableLinkedHashSet<>(this.data.values());
    }

    /**
     * Returns fractional lengths in array form, including 0.0 and 1.0.
     * @return fractional lengths
     */
    public double[] getFractionalLengthsAsArray()
    {
        NavigableMap<Double, Double> full = fullRange();
        double[] fractionalLengths = new double[full.size()];
        int i = 0;
        for (double f : full.navigableKeySet())
        {
            fractionalLengths[i++] = f;
        }
        return fractionalLengths;
    }

    /**
     * Returns fractional lengths in array form, including values at 0.0 and 1.0. TODO get rid of this method. Let callers use
     * the iterator and the <code>get()</code> method.
     * @return fractional lengths
     */
    public double[] getValuesAsArray()
    {
        NavigableMap<Double, Double> full = fullRange();
        double[] values = new double[full.size()];
        int i = 0;
        for (double f : full.navigableKeySet())
        {
            values[i++] = full.get(f);
        }
        return values;
    }

    /**
     * Returns the data including entries at 0.0 and 1.0.
     * @return data with fill range.
     */
    private NavigableMap<Double, Double> fullRange()
    {
        NavigableMap<Double, Double> full = new TreeMap<>(this.data);
        full.put(0.0, full.firstEntry().getValue());
        full.put(1.0, full.lastEntry().getValue());
        return full;
    }

    /**
     * Returns the number of data points.
     * @return number of data points.
     */
    public int size()
    {
        return this.data.size();
    }

    /**
     * Create FractionalLengthData.
     * @param data fractional length - value pairs. Fractional lengths do not need to be in order.
     * @return fractional length data.
     * @throws IllegalArgumentException when the number of input values is not even or 0, or when a fractional value is not in
     *             the range [0, 1].
     */
    public static PieceWiseLinearOffset2d of(final double... data)
    {
        return new PieceWiseLinearOffset2d(data);
    }

    @Override
    public Iterator<Double> iterator()
    {
        return new Iterator<Double>()
        {
            private Double nextEntry = PieceWiseLinearOffset2d.this.data.firstKey();

            @Override
            public boolean hasNext()
            {
                return this.nextEntry != null;
            }

            @Override
            public Double next()
            {
                Double result = this.nextEntry;
                Throw.when(null == result, NoSuchElementException.class, "Iterator is exhausted");
                this.nextEntry = PieceWiseLinearOffset2d.this.data.higherKey(result);
                return result;
            }
        };
    }

}
