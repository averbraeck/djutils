package org.djutils.swing.multislider;

/**
 * MultiSlider implements a slider with multiple thumbs. The MultiSlider is implemented by drawing a number of sliders on top of
 * each other using an Swing {@code OverlayManager}, and passing the mouse events from a glass pane on top to the correct
 * slider(s). The class is a {@code ChangeListener} to listen to the changes of individual sliders underneath.
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
 */
public class MultiSlider extends AbstractMultiSlider<Integer>
{
    /** */
    private static final long serialVersionUID = 20241120L;

    /**
     * Creates a horizontal slider using the specified min, max and initial values.
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     * @param initialValues the initial values of the thumbs of the slider
     * @throws IllegalArgumentException if initial values are outside the min-max range, or if the number of thumbs is 0, or
     *             when the values are not in increasing order (which is important for restricting passing and overlap)
     */
    public MultiSlider(final int min, final int max, final int... initialValues)
    {
        this(min, max, true, initialValues);
    }

    /**
     * Creates a slider with the specified orientation and the specified minimum, maximum, and initial values. The orientation
     * can be either horizontal or vertical.
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     * @param horizontal the orientation of the slider; true for horizontal, false for vertical
     * @param initialValues the initial values of the thumbs of the slider
     * @throws IllegalArgumentException if initial values are outside the min-max range, or if the number of thumbs is 0, or
     *             when the values are not in increasing order (which is important for restricting passing and overlap)
     */
    public MultiSlider(final int min, final int max, final boolean horizontal, final int... initialValues)
    {
        super(min, max, horizontal, initialValues);
    }

    /** {@inheritDoc} */
    @Override
    protected Integer mapIndexToValue(final int index)
    {
        return index;
    }

    /** {@inheritDoc} */
    @Override
    protected int mapValueToIndex(final Integer value)
    {
        return value;
    }
}
