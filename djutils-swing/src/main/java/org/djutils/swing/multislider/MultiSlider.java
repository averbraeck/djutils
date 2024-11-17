package org.djutils.swing.multislider;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.djutils.exceptions.Throw;

/**
 * MultiSlider implements a slider with multiple thumbs. The MultiSlider is implemented by drawing a number of sliders on top of
 * each other using an Swing OverlayManager, and passing the mouse events from a glass pane on top to the correct slider(s)
 * underneath.
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
public class MultiSlider extends JComponent
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the sliders that are stacked on top of each other. */
    private JSlider[] sliders;

    /**
     * Creates a horizontal slider using the specified min, max and initial values.
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     * @param initialValues the initial values of the thumbs of the slider
     * @throws IllegalArgumentException if initial values are outside the min-max range, or if the number of thumbs is 0
     */
    public MultiSlider(final int min, final int max, final int[] initialValues)
    {
        this(SwingConstants.HORIZONTAL, min, max, initialValues);
    }

    /**
     * Creates a slider with the specified orientation and the specified minimum, maximum, and initial values. The orientation
     * can be either <code>SwingConstants.VERTICAL</code> or <code>SwingConstants.HORIZONTAL</code>.
     * @param orientation the orientation of the slider
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     * @param initialValues the initial values of the thumbs of the slider
     * @throws IllegalArgumentException if orientation is not one of {@code VERTICAL}, {@code HORIZONTAL}, if initial values are
     *             outside the min-max range, or if the number of thumbs is 0
     */
    public MultiSlider(final int orientation, final int min, final int max, final int[] initialValues)
    {
        Throw.when(initialValues.length == 0, IllegalArgumentException.class, "the number of thumbs cannot be zero");
        Throw.when(min >= max, IllegalArgumentException.class, "min should be less than max");
        for (int v : initialValues)
        {
            if (v < min || v > max)
            {
                throw new IllegalArgumentException("all initial value should be between min and max (inclusive)");
            }
        }

        // put a glass pane on top that dispatches the mouse event to all panes
        DispatcherPane dp = new DispatcherPane(this);
        dp.setPreferredSize(new Dimension(640, 320));
        add(dp);

        // make the sliders and show them. Slider 0 at the bottom. This one will get ticks, etc.
        setLayout(new OverlayLayout(this));
        this.sliders = new JSlider[initialValues.length];
        for (int i = 0; i < initialValues.length; i++)
        {
            var slider = new JSlider(orientation, min, max, initialValues[i]);
            this.sliders[i] = slider;
            slider.setOpaque(false);
            add(slider);
        }

    }

    /**
     * Return the individual sliders, where slider[0] contains the formatting.
     * @return the individual sliders
     */
    protected JSlider[] getSliders()
    {
        return this.sliders;
    }

    /**
     * The DispatcherPane class, which is a glass pane sitting on top of the sliders to dispatch the mouse event to the correct
     * slider class. Note that the mouse coordinates are relative to the component itself, so a translation is often needed. The
     * <code>SwingUtilities.convertPoint()</code> method can make the conversion.
     */
    protected static class DispatcherPane extends JComponent
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** the pointer to the multislider object. */
        private final MultiSlider multiSlider;

        /**
         * Create a glass pane on top of the sliders.
         * @param multiSlider the multislider for which this is the glass pane
         */
        public DispatcherPane(final MultiSlider multiSlider)
        {
            this.multiSlider = multiSlider;
            setOpaque(false);

            addMouseListener(new MouseListener()
            {
                /**
                 * @param e the MouseEvent to dispatch to the sliders.
                 */
                private void dispatch(final MouseEvent e)
                {
                    int i = 0;
                    for (var slider : DispatcherPane.this.multiSlider.getSliders())
                    {
                        Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
                        if (pSlider.x < 0 || pSlider.x > slider.getSize().width || pSlider.y < 0
                                || pSlider.y > slider.getSize().height)
                        {
                            return;
                        }
                        System.out.println("Slider[" + (i++) + "] - point: " + pSlider);
                        MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                                e.getModifiersEx(), pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        slider.dispatchEvent(meSlider);
                    }
                }

                @Override
                public void mouseReleased(final MouseEvent e)
                {
                    // Note that a mouse release should ALWAYS be communicated, also when it is outside of the slider
                    System.out.println("Release: " + e.getX() + ", " + e.getY());
                    int i = 0;
                    for (var slider : DispatcherPane.this.multiSlider.getSliders())
                    {
                        Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
                        System.out.println("Slider[" + (i++) + "] - point: " + pSlider);
                        MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                                e.getModifiersEx(), pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        slider.dispatchEvent(meSlider);
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e)
                {
                    System.out.println("Press: " + e.getX() + ", " + e.getY());
                    dispatch(e);
                }

                @Override
                public void mouseExited(final MouseEvent e)
                {
                    // Note that a mouse exited should ALWAYS be communicated, also when it is outside of the slider
                    System.out.println("Exit: " + e.getX() + ", " + e.getY());
                    int i = 0;
                    for (var slider : DispatcherPane.this.multiSlider.getSliders())
                    {
                        Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
                        System.out.println("Slider[" + (i++) + "] - point: " + pSlider);
                        MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                                e.getModifiersEx(), pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        slider.dispatchEvent(meSlider);
                    }
                }

                @Override
                public void mouseEntered(final MouseEvent e)
                {
                    System.out.println("Enter: " + e.getX() + ", " + e.getY());
                    dispatch(e);
                }

                @Override
                public void mouseClicked(final MouseEvent e)
                {
                    System.out.println("Click: " + e.getX() + ", " + e.getY());
                    dispatch(e);
                }
            });

            addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(final MouseEvent e)
                {
                    System.out.println("Drag: " + e.getX() + ", " + e.getY());
                    int i = 0;
                    for (var slider : DispatcherPane.this.multiSlider.getSliders())
                    {
                        Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
                        if (pSlider.x < 0 || pSlider.x > slider.getSize().width || pSlider.y < 0
                                || pSlider.y > slider.getSize().height)
                        {
                            return;
                        }
                        System.out.println("Slider[" + (i++) + "] - point: " + pSlider);
                        MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                                e.getModifiersEx(), pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        slider.dispatchEvent(meSlider);
                    }
                }

                @Override
                public void mouseMoved(final MouseEvent e)
                {
                    // Note: possibly we can trigger an 'mouse entered' when the mouse enters the slider pane,
                    // and a 'mouse exited' when the mouse exits the slider pane.
                    for (var slider : DispatcherPane.this.multiSlider.getSliders())
                    {
                        Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
                        if (pSlider.x < 0 || pSlider.x > slider.getSize().width || pSlider.y < 0
                                || pSlider.y > slider.getSize().height)
                        {
                            return;
                        }
                        MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                                e.getModifiersEx(), pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        slider.dispatchEvent(meSlider);
                    }
                }
            });
        }

    }
}
