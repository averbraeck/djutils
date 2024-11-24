package org.djutils.swing.multislider;

import org.djutils.exceptions.Throw;

/**
 * LinearMultiSlider implements a slider with multiple thumbs and liner values based on the class Number. The slider returns
 * instances of a given type. The MultiSlider is implemented by drawing a number of sliders on top of each other using an Swing
 * {@code OverlayManager}, and passing the mouse events from a glass pane on top to the correct slider(s). The class is a
 * {@code ChangeListener} to listen to the changes of individual sliders underneath.
 * <p>
 * Several models exist to indicate whether thumbs can pass each other or not, or be on top of each other or not.
 * </p>
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <T> The type of the labels for the multislider
 */
public abstract class LinearMultiSlider<T extends Number & Comparable<T>> extends AbstractMultiSlider<T>
{
    /** */
    private static final long serialVersionUID = 20241124L;

    /** the lowest value of the linear scale. */
    private T min;

    /** the highest value of the linear scale. */
    private T max;

    /** the number of steps of the linear scale. */
    private int steps;

    /**
     * Creates a horizontal slider using the specified interval, number of steps, and initial values.
     * @param min the lowest value of the linear scale
     * @param max the highest value of the linear scale
     * @param steps the number of steps of the linear scale
     * @param initialValues the initial values of the slider
     * @throws IllegalArgumentException if the initial values are not part of the scale, or if the number of thumbs is 0, or
     *             when the values are not in increasing scale order (which is important for restricting passing and overlap)
     */
    @SafeVarargs
    public LinearMultiSlider(final T min, final T max, final int steps, final T... initialValues)
    {
        this(min, max, steps, true, initialValues);
    }

    /**
     * Creates a horizontal or vertical slider using the specified min, max and initial values.
     * @param horizontal the orientation of the slider; true for horizontal, false for vertical
     * @param min the lowest value of the linear scale
     * @param max the highest value of the linear scale
     * @param steps the number of steps of the linear scale
     * @param initialValues the initial values of the slider.
     * @throws IllegalArgumentException if the initial values are not part of the scale, or if the number of thumbs is 0, or
     *             when the values are not in increasing scale order (which is important for restricting passing and overlap)
     */
    @SafeVarargs
    public LinearMultiSlider(final T min, final T max, final int steps, final boolean horizontal, final T... initialValues)
    {
        super(0, steps - 1, horizontal, intArray(min, max, steps, initialValues));
        this.min = min;
        this.max = max;
        this.steps = steps;
        setLabelTable(createStandardLabels(1));
    }

    /**
     * Make an int array from the initial values given the linear scale.
     * @param min the lowest value of the linear scale
     * @param max the highest value of the linear scale
     * @param steps the number of steps of the linear scale
     * @param initialValues the initial values of the slider.
     * @param <T> the type of objects for the categorial scale
     * @return an int array with the index vales of the initial values on the scale
     * @throws IllegalArgumentException if the initial values are not part of the scale, or when the scale has duplicate values
     */
    @SafeVarargs
    private static <T extends Number & Comparable<T>> int[] intArray(final T min, final T max, final int steps,
            final T... initialValues)
    {
        int[] ret = new int[initialValues.length];
        Throw.when(initialValues.length == 0, IllegalArgumentException.class, "the number of thumbs cannot be zero");

        // create the indices; map to closest step on the linear scale
        for (int i = 0; i < initialValues.length; i++)
        {
            T iv = initialValues[i];
            Throw.when(iv.compareTo(min) < 0, IllegalArgumentException.class,
                    "initial value %s less than minimum scale value %s", iv.toString(), min.toString());
            Throw.when(iv.compareTo(max) > 0, IllegalArgumentException.class,
                    "initial value %s more than maximum scale value %s", iv.toString(), max.toString());
            double div = iv.doubleValue();
            double dmin = min.doubleValue();
            double dmax = max.doubleValue();
            double dsteps = Double.valueOf(steps);
            int index = (int) Math.round(dsteps * (div - dmin) / (dmax - dmin));
            ret[i] = index;
        }
        return ret;
    }

    @Override
    protected int mapValueToIndex(final T value)
    {
        Throw.when(value.compareTo(this.min) < 0, IllegalArgumentException.class,
                "initial value %s less than minimum scale value %s", value.toString(), this.min.toString());
        Throw.when(value.compareTo(this.max) > 0, IllegalArgumentException.class,
                "initial value %s more than maximum scale value %s", value.toString(), this.max.toString());
        double div = value.doubleValue();
        double dmin = this.min.doubleValue();
        double dmax = this.max.doubleValue();
        double dsteps = Double.valueOf(this.steps);
        int index = (int) Math.round(dsteps * (div - dmin) / (dmax - dmin));
        return index;
    }
    
}
