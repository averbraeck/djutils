package org.djutils.swing.multislider;

import java.util.List;

import org.djutils.exceptions.Throw;
import org.djutils.immutablecollections.ImmutableArrayList;
import org.djutils.immutablecollections.ImmutableList;

/**
 * CategorialMultiSlider implements a slider with multiple thumbs and categorial values. The slider returns instances of a given
 * type. The MultiSlider is implemented by drawing a number of sliders on top of each other using an Swing
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
public class CategorialMultiSlider<T> extends AbstractMultiSlider<T>
{
    /** */
    private static final long serialVersionUID = 20241124L;

    /** the categorial scale of this multislider. */
    private final ImmutableList<T> scale;

    /**
     * Creates a horizontal slider using the specified scale and initial values.
     * @param scale the list of categorial values for the scale
     * @param initialValues the initial values of the slider.
     * @throws IllegalArgumentException if the initial values are not part of the scale, or if the number of thumbs is 0, or
     *             when the values are not in increasing scale order (which is important for restricting passing and overlap),
     *             or when the scale has duplicate values
     */
    @SafeVarargs
    public CategorialMultiSlider(final List<T> scale, final T... initialValues)
    {
        this(true, scale, initialValues);
    }

    /**
     * Creates a horizontal slider using the specified min, max and initial values.
     * @param horizontal the orientation of the slider; true for horizontal, false for vertical
     * @param scale the list of categorial values for the scale
     * @param initialValues the initial values of the slider.
     * @throws IllegalArgumentException if the initial values are not part of the scale, or if the number of thumbs is 0, or
     *             when the values are not in increasing scale order (which is important for restricting passing and overlap),
     *             or when the scale has duplicate values
     */
    @SafeVarargs
    public CategorialMultiSlider(final boolean horizontal, final List<T> scale, final T... initialValues)
    {
        super(0, scale.size() - 1, horizontal, intArray(scale, initialValues));
        this.scale = new ImmutableArrayList<T>(scale);
        setLabelTable(createStandardLabels(1));
    }

    /**
     * Make an int array from the initial values given the scale.
     * @param scale the list of categorial values for the scale
     * @param initialValues the initial values of the slider.
     * @param <T> the type of objects for the categorial scale
     * @return an int array with the index vales of the initial values on the scale
     * @throws IllegalArgumentException if the initial values are not part of the scale, or when the scale has duplicate values
     */
    @SafeVarargs
    private static <T> int[] intArray(final List<T> scale, final T... initialValues)
    {
        int[] ret = new int[initialValues.length];
        Throw.when(initialValues.length == 0, IllegalArgumentException.class, "the number of thumbs cannot be zero");

        // check for duplicate values in the scale
        for (int i = 0; i < scale.size(); i++)
        {
            for (int j = 0; j < scale.size(); j++)
            {
                if (i != j && scale.get(i).equals(scale.get(j)))
                {
                    throw new IllegalArgumentException(
                            "Two values on the CategorialMultiSlider scale are the same: " + scale.get(i));
                }
            }
        }

        // create the indices
        for (int i = 0; i < initialValues.length; i++)
        {
            int index = scale.indexOf(initialValues[i]);
            if (index == -1)
            {
                throw new IllegalArgumentException(
                        "Initial value " + initialValues[i] + " not found on the CategorialMultiSlider scale: " + scale);
            }
            ret[i] = index;
        }

        return ret;
    }

    /** {@inheritDoc} */
    @Override
    protected T mapIndexToValue(final int index)
    {
        Throw.when(index < 0 || index >= this.scale.size(), IllegalArgumentException.class,
                "CategorialMultiSlider scale: index < 0 || index > scale size");
        return this.scale.get(index);
    }

    /** {@inheritDoc} */
    @Override
    protected int mapValueToIndex(final T value)
    {
        int index = this.scale.indexOf(value);
        if (index == -1)
        {
            throw new IllegalArgumentException(
                    "Value " + value + " not found on the CategorialMultiSlider scale: " + this.scale);
        }
        return index;
    }

}
