package org.djutils.swing.multislider;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.BeanProperty;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

import org.djutils.exceptions.Throw;

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
public class MultiSlider extends JComponent implements ChangeListener
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the sliders that are stacked on top of each other. */
    private JSlider[] sliders;

    /** the current slider number in process (e.g., drag operation, mouse down). */
    private transient int busySlider = -1;

    /** whether an operation is busy or not. */
    private transient boolean busy = false;

    /** the 'glass pane' on top of the sliders to dispatch the mouse clicks and drags to the correct slider. */
    private final DispatcherPane dispatcherPane;

    /** the panel in which the thumb labels can be drawn if this is wanted. */
    private final JPanel labelPanel;

    /** the initial values of the labels for the reset function. */
    private final int[] initialValues;

    /** the labels per thumb (per underlying slider). */
    private final Map<Integer, String> thumbLabels = new HashMap<>();

    /** whether we draw thumb labels or not. */
    private boolean drawThumbLabels = false;

    /**
     * Only one <code>ChangeEvent</code> is needed for the {@code MultiSlider} since the event's only (read-only) state is the
     * source property. The source of events generated here is always "this". The event is created the first time that an event
     * notification is fired.
     */
    private transient ChangeEvent changeEvent = null;

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

        // store the initial value array in a safe copy
        this.initialValues = new int[initialValues.length];

        // based on the orientation, add a JPanel with two subpanels: one for the labels, and one for the sliders
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(
                new BoxLayout(topPanel, (orientation == SwingConstants.VERTICAL) ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        add(topPanel, BorderLayout.CENTER);

        // create the label panel
        this.labelPanel = new JPanel();
        this.labelPanel.setOpaque(false);
        topPanel.add(this.labelPanel);

        // create the slider panel
        JPanel sliderPanel = new JPanel();
        sliderPanel.setOpaque(false);
        topPanel.add(sliderPanel);

        // put a glass pane on top that dispatches the mouse event to all panes
        this.dispatcherPane = new DispatcherPane(this);
        sliderPanel.add(this.dispatcherPane);

        // make the sliders and show them. Slider 0 at the bottom. This one will get ticks, etc.
        sliderPanel.setLayout(new OverlayLayout(sliderPanel));
        this.sliders = new JSlider[initialValues.length];
        for (int i = initialValues.length - 1; i >= 0; i--)
        {
            this.initialValues[i] = initialValues[i]; // create copy
            var slider = new JSlider(orientation, min, max, initialValues[i]);
            this.sliders[i] = slider;
            slider.setOpaque(false);
            slider.setPaintTrack(i == 0);
            sliderPanel.add(slider);

            // add this class as a change listener for all slider events of the individual sliders
            slider.addChangeListener(this);

            // ensure movability of the slider by setting it again
            slider.setValue(initialValues[i]);
        }

        this.dispatcherPane.setPreferredSize(new Dimension(this.sliders[0].getSize()));
        invalidate();
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
     * Return an individual slider with index i.
     * @param i the index for which to retrieve the slider
     * @return the individual slider with index i
     */
    public JSlider getSlider(final int i)
    {
        return this.sliders[i];
    }

    /**
     * Return the number of thumbs on this multislider.
     * @return the number of thumbs on this multislider
     */
    public int getNumberOfThumbs()
    {
        return this.sliders.length;
    }

    /**
     * Indicate that slider i is busy (e.g., mouse-down or a drag operation).
     * @param i the slider number that is busy
     */
    protected void setBusySlider(final int i)
    {
        this.busySlider = i;
    }

    /**
     * Return whether slider i is busy (e.g., mouse-down or a drag operation).
     * @param i the slider number to check
     * @return whether slier i is busy or not
     */
    protected boolean isBusySlider(final int i)
    {
        return this.busySlider == i;
    }

    /**
     * Return which slider is busy (e.g., mouse-down or a drag operation). The function returns -1 if no slider is busy.
     * @return the slider number of the busy slider, or -1 if no slider is busy with an action
     */
    protected int getBusySlider()
    {
        return this.busySlider;
    }

    /**
     * Return whether one of the sliders is busy (e.g., mouse-down or a drag operation). Note that the 'busy' flag is set BEFORE
     * the mouse event (e.g., mouse released, mouse exited) is handled. This means that when 'busy' is true, no operation is
     * taking place.
     * @return whether one of the sliders is busy or not
     */
    public boolean isBusy()
    {
        return this.busy;
    }

    /**
     * Set whether one of the sliders is busy (e.g., mouse-down or a drag operation). Note that the 'busy' flag has to be set
     * BEFORE the mouse event (e.g., mouse released, mouse exited) is handled. This means that when 'busy' is true, no operation
     * is taking place.
     * @param busy set whether one of the sliders is busy or not
     */
    protected void setBusy(final boolean busy)
    {
        this.busy = busy;
    }

    /**
     * Reset the slider values to the initial values.
     */
    public void resetToInitialValues()
    {
        for (int i = 0; i < this.sliders.length; i++)
        {
            this.sliders[i].setValue(this.initialValues[i]);
        }
    }

    /**
     * Return the label panel in which thumb labels can be drawn. The labels move with the thumbs.
     * @return the label panel in which thumb labels can be drawn
     */
    protected JPanel getLabelPanel()
    {
        return this.labelPanel;
    }

    /**
     * Set the thumb label for thumb i to the given label.
     * @param i the thumb number
     * @param label the label to display
     */
    public void setThumbLabel(final int i, final String label)
    {
        this.thumbLabels.put(i, label);
    }
    
    /**
     * Turn the thumb label display on or off.
     * @param b whether the thumbs are displayed or not
     */
    public void setDrawThumbLabels(final boolean b)
    {
        this.drawThumbLabels = b;
    }
    
    @Override
    public void stateChanged(final ChangeEvent e)
    {
        fireStateChanged();
    }

    /**
     * Gets the UI object which implements the L&amp;F for this component.
     * @return the SliderUI object that implements the Slider L&amp;F
     */
    @Override
    public SliderUI getUI()
    {
        return this.sliders[0].getUI();
    }

    /**
     * Sets the UI object which implements the L&amp;F for all underlying sliders.
     * @param ui the SliderUI L&amp;F object
     */
    public void setUI(final SliderUI ui)
    {
        for (var slider : this.sliders)
        {
            slider.setUI(ui);
        }
    }

    /**
     * Resets the UI property to a value from the current look and feel for all underlying sliders.
     */
    @Override
    public void updateUI()
    {
        for (var slider : this.sliders)
        {
            slider.updateUI();
        }
    }

    /**
     * Returns the name of the L&amp;F class that renders this component.
     * @return the string "SliderUI"
     */
    @Override
    @BeanProperty(bound = false)
    public String getUIClassID()
    {
        return this.sliders[0].getUIClassID();
    }

    /**
     * Adds a ChangeListener to the multislider.
     * @param listener the ChangeListener to add
     */
    public void addChangeListener(final ChangeListener listener)
    {
        this.listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Removes a ChangeListener from the multislider.
     * @param listener the ChangeListener to remove
     */
    public void removeChangeListener(final ChangeListener listener)
    {
        this.listenerList.remove(ChangeListener.class, listener);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added to this MultiSlider with addChangeListener().
     * @return all of the <code>ChangeListener</code>s added or an empty array if no listeners have been added
     */
    @BeanProperty(bound = false)
    public ChangeListener[] getChangeListeners()
    {
        return this.listenerList.getListeners(ChangeListener.class);
    }

    /**
     * Send a {@code ChangeEvent}, whose source is this {@code MultiSlider}, to all {@code ChangeListener}s that have registered
     * interest in {@code ChangeEvent}s. This method is called each time a {@code ChangeEvent} is received from the model of one
     * of the underlying sliders.
     * <p>
     * The event instance is created if necessary, and stored in {@code changeEvent}.
     * </p>
     */
    protected void fireStateChanged()
    {
        // Note that the listener array has the classes at the even places, and the listeners at the odd places (yuck).
        Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == ChangeListener.class)
            {
                if (this.changeEvent == null)
                {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    /**
     * Returns the {@code BoundedRangeModel} that handles the slider's three fundamental properties: minimum, maximum, value.
     * @return the data model for this component
     */
    public BoundedRangeModel getModel()
    {
        return this.sliders[0].getModel();
    }

    /**
     * Sets the {@code BoundedRangeModel} for all the underlying sliders. This handles the slider's three fundamental
     * properties: minimum, maximum, value.
     * <p>
     * Attempts to pass a {@code null} model to this method result in undefined behavior, and, most likely, exceptions.
     * </p>
     * @param newModel the new, {@code non-null} <code>BoundedRangeModel</code> to use
     */
    @BeanProperty(description = "The sliders BoundedRangeModel.")
    public void setModel(final BoundedRangeModel newModel)
    {
        for (var slider : this.sliders)
        {
            slider.setModel(newModel);
        }
    }

    /**
     * Returns the slider's current value for slider[i] from the {@code BoundedRangeModel}.
     * @param i the thumb to retrieve the value from
     * @return the current value of slider[i]
     */
    public int getValue(final int i)
    {
        return this.sliders[i].getModel().getValue();
    }

    /**
     * Sets the slider's current value to {@code n}. This method forwards the new value to the model.
     * <p>
     * The data model (an instance of {@code BoundedRangeModel}) handles any mathematical issues arising from assigning faulty
     * values. See the {@code BoundedRangeModel} documentation for details.
     * </p>
     * If the new value is different from the previous value, all change listeners are notified.
     * @param i the thumb to set the value for
     * @param n the new value
     */
    @BeanProperty(bound = false, preferred = true, description = "The sliders current value.")
    public void setValue(final int i, final int n)
    {
        this.sliders[i].setValue(n);
    }

    /**
     * Returns the minimum value supported by the slider from the <code>BoundedRangeModel</code>.
     * @return the value of the model's minimum property
     */
    public int getMinimum()
    {
        return this.sliders[0].getMinimum();
    }

    /**
     * Sets the slider's minimum value to {@code minimum}. This method forwards the new minimum value to the models of all
     * underlying sliders.
     * <p>
     * The data model (an instance of {@code BoundedRangeModel}) handles any mathematical issues arising from assigning faulty
     * values. See the {@code BoundedRangeModel} documentation for details.
     * </p>
     * If the new minimum value is different from the previous minimum value, all change listeners are notified.
     * @param minimum the new minimum
     */
    @BeanProperty(preferred = true, description = "The sliders minimum value.")
    public void setMinimum(final int minimum)
    {
        int oldMin = getMinimum();
        for (var slider : this.sliders)
        {
            slider.setMinimum(minimum);
        }
        firePropertyChange("minimum", Integer.valueOf(oldMin), Integer.valueOf(minimum));
    }

    /**
     * Returns the maximum value supported by the slider from the <code>BoundedRangeModel</code>.
     * @return the value of the model's maximum property
     */
    public int getMaximum()
    {
        return this.sliders[0].getMaximum();
    }

    /**
     * Sets the slider's maximum value to {@code maximum}. This method forwards the new maximum value to the models of all
     * underlying sliders.
     * <p>
     * The data model (an instance of {@code BoundedRangeModel}) handles any mathematical issues arising from assigning faulty
     * values. See the {@code BoundedRangeModel} documentation for details.
     * </p>
     * If the new maximum value is different from the previous maximum value, all change listeners are notified.
     * @param maximum the new maximum
     */
    @BeanProperty(preferred = true, description = "The sliders maximum value.")
    public void setMaximum(final int maximum)
    {
        int oldMax = getMaximum();
        for (var slider : this.sliders)
        {
            slider.setMaximum(maximum);
        }
        firePropertyChange("maximum", Integer.valueOf(oldMax), Integer.valueOf(maximum));
    }

    /**
     * Returns the "extent" from the <code>BoundedRangeModel</code>. This represents the range of values "covered" by the thumb.
     * @return an int representing the extent
     */
    public int getExtent()
    {
        return this.sliders[0].getExtent();
    }

    /**
     * Sets the size of the range "covered" by the thumb for all underlying slider objects. Most look and feel implementations
     * will change the value by this amount if the user clicks on either side of the thumb. This method just forwards the new
     * extent value to the model.
     * <p>
     * The data model (an instance of {@code BoundedRangeModel}) handles any mathematical issues arising from assigning faulty
     * values. See the {@code BoundedRangeModel} documentation for details.
     * </p>
     * If the new extent value is different from the previous extent value, all change listeners are notified.
     * @param extent the new extent
     */
    @BeanProperty(bound = false, expert = true, description = "Size of the range covered by the thumb.")
    public void setExtent(final int extent)
    {
        for (var slider : this.sliders)
        {
            slider.setExtent(extent);
        }
    }

    /**
     * Return this multislider's vertical or horizontal orientation.
     * @return {@code SwingConstants.VERTICAL} or {@code SwingConstants.HORIZONTAL}
     */
    public int getOrientation()
    {
        return this.sliders[0].getOrientation();
    }

    /**
     * Set the slider's orientation to either {@code SwingConstants.VERTICAL} or {@code SwingConstants.HORIZONTAL}.
     * @param orientation {@code HORIZONTAL} or {@code VERTICAL}
     * @throws IllegalArgumentException if orientation is not one of {@code VERTICAL}, {@code HORIZONTAL}
     */
    @BeanProperty(preferred = true, visualUpdate = true, enumerationValues = {"JSlider.VERTICAL", "JSlider.HORIZONTAL"},
            description = "Set the scrollbars orientation to either VERTICAL or HORIZONTAL.")
    public void setOrientation(final int orientation)
    {
        for (var slider : this.sliders)
        {
            slider.setOrientation(orientation);
        }
        this.dispatcherPane.setPreferredSize(new Dimension(this.sliders[0].getSize()));
        invalidate();
    }

    @Override
    public void setFont(final Font font)
    {
        for (var slider : this.sliders)
        {
            slider.setFont(font);
        }
    }

    @Override
    public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y, final int w, final int h)
    {
        boolean retvalue = true;
        for (var slider : this.sliders)
        {
            retvalue |= slider.imageUpdate(img, infoflags, x, y, w, h);
        }
        return retvalue;
    }

    /**
     * Returns the dictionary of what labels to draw at which values.
     * @return the <code>Dictionary</code> containing labels and where to draw them
     */
    @SuppressWarnings("rawtypes")
    public Dictionary getLabelTable()
    {
        return this.sliders[0].getLabelTable();
    }

    /**
     * Specify what label will be drawn at any given value. The key-value pairs are of this format:
     * <code>{ Integer value, java.swing.JComponent label }</code>. An easy way to generate a standard table of value labels is
     * by using the {@code createStandardLabels} method.
     * @param labels new {@code Dictionary} of labels, or {@code null} to remove all labels
     */
    @BeanProperty(hidden = true, visualUpdate = true, description = "Specifies what labels will be drawn for any given value.")
    @SuppressWarnings("rawtypes")
    public void setLabelTable(final Dictionary labels)
    {
        for (var slider : this.sliders)
        {
            slider.setLabelTable(labels);
        }
    }

    /**
     * Creates a {@code Hashtable} of numerical text labels, starting at the slider minimum, and using the increment specified.
     * For example, if you call <code>createStandardLabels( 10 )</code> and the slider minimum is zero, then labels will be
     * created for the values 0, 10, 20, 30, and so on.
     * <p>
     * For the labels to be drawn on the slider, the returned {@code Hashtable} must be passed into {@code setLabelTable}, and
     * {@code setPaintLabels} must be set to {@code true}.
     * <p>
     * For further details on the makeup of the returned {@code Hashtable}, see the {@code setLabelTable} documentation.
     * @param increment distance between labels in the generated hashtable
     * @return a new {@code Hashtable} of labels
     * @throws IllegalArgumentException if {@code increment} is less than or equal to zero
     */
    public Hashtable<Integer, JComponent> createStandardLabels(final int increment)
    {
        return createStandardLabels(increment, getMinimum());
    }

    /**
     * Creates a {@code Hashtable} of numerical text labels, starting at the starting point specified, and using the increment
     * specified. For example, if you call <code>createStandardLabels( 10, 2 )</code>, then labels will be created for the
     * values 2, 12, 22, 32, and so on.
     * <p>
     * For the labels to be drawn on the slider, the returned {@code Hashtable} must be passed into {@code setLabelTable}, and
     * {@code setPaintLabels} must be set to {@code true}.
     * <p>
     * For further details on the makeup of the returned {@code Hashtable}, see the {@code setLabelTable} documentation.
     * @param increment distance between labels in the generated hashtable
     * @param start value at which the labels will begin
     * @return a new {@code Hashtable} of labels
     * @exception IllegalArgumentException if {@code start} is out of range, or if {@code increment} is less than or equal to
     *                zero
     */
    public Hashtable<Integer, JComponent> createStandardLabels(final int increment, final int start)
    {
        return this.sliders[0].createStandardLabels(increment, start);
    }

    /**
     * Returns true if the value-range shown for the slider is reversed.
     * @return true if the slider values are reversed from their normal order
     */
    public boolean getInverted()
    {
        return this.sliders[0].getInverted();
    }

    /**
     * Specify true to reverse the value-range shown for the slider and false to put the value range in the normal order. The
     * order depends on the slider's <code>ComponentOrientation</code> property. Normal (non-inverted) horizontal sliders with a
     * <code>ComponentOrientation</code> value of <code>LEFT_TO_RIGHT</code> have their maximum on the right. Normal horizontal
     * sliders with a <code>ComponentOrientation</code> value of <code>RIGHT_TO_LEFT</code> have their maximum on the left.
     * Normal vertical sliders have their maximum on the top. These labels are reversed when the slider is inverted.
     * <p>
     * By default, the value of this property is {@code false}.
     * @param b true to reverse the slider values from their normal order
     */
    @BeanProperty(visualUpdate = true, description = "If true reverses the slider values from their normal order")
    public void setInverted(final boolean b)
    {
        for (var slider : this.sliders)
        {
            slider.setInverted(b);
        }
    }

    /**
     * This method returns the major tick spacing. The number that is returned represents the distance, measured in values,
     * between each major tick mark. If you have a slider with a range from 0 to 50 and the major tick spacing is set to 10, you
     * will get major ticks next to the following values: 0, 10, 20, 30, 40, 50.
     * @return the number of values between major ticks
     */
    public int getMajorTickSpacing()
    {
        return this.sliders[0].getMajorTickSpacing();
    }

    /**
     * This method sets the major tick spacing. The number that is passed in represents the distance, measured in values,
     * between each major tick mark. If you have a slider with a range from 0 to 50 and the major tick spacing is set to 10, you
     * will get major ticks next to the following values: 0, 10, 20, 30, 40, 50.
     * <p>
     * In order for major ticks to be painted, {@code setPaintTicks} must be set to {@code true}.
     * </p>
     * This method will also set up a label table for you. If there is not already a label table, and the major tick spacing is
     * {@code > 0}, and {@code getPaintLabels} returns {@code true}, a standard label table will be generated (by calling
     * {@code createStandardLabels}) with labels at the major tick marks. For the example above, you would get text labels: "0",
     * "10", "20", "30", "40", "50". The label table is then set on the slider by calling {@code setLabelTable}.
     * @param n new value for the {@code majorTickSpacing} property
     */
    @BeanProperty(visualUpdate = true, description = "Sets the number of values between major tick marks.")
    public void setMajorTickSpacing(final int n)
    {
        int oldValue = getMajorTickSpacing();
        for (var slider : this.sliders)
        {
            slider.setMajorTickSpacing(n);
        }
        firePropertyChange("majorTickSpacing", oldValue, n);
    }

    /**
     * This method returns the minor tick spacing. The number that is returned represents the distance, measured in values,
     * between each minor tick mark. If you have a slider with a range from 0 to 50 and the minor tick spacing is set to 10, you
     * will get minor ticks next to the following values: 0, 10, 20, 30, 40, 50.
     * @return the number of values between minor ticks
     */
    public int getMinorTickSpacing()
    {
        return this.sliders[0].getMinorTickSpacing();
    }

    /**
     * This method sets the minor tick spacing. The number that is passed in represents the distance, measured in values,
     * between each minor tick mark. If you have a slider with a range from 0 to 50 and the minor tick spacing is set to 10, you
     * will get minor ticks next to the following values: 0, 10, 20, 30, 40, 50.
     * <p>
     * In order for minor ticks to be painted, {@code setPaintTicks} must be set to {@code true}.
     * @param n new value for the {@code minorTickSpacing} property
     * @see #getMinorTickSpacing
     * @see #setPaintTicks
     */
    @BeanProperty(visualUpdate = true, description = "Sets the number of values between minor tick marks.")
    public void setMinorTickSpacing(final int n)
    {
        int oldValue = getMinorTickSpacing();
        for (var slider : this.sliders)
        {
            slider.setMinorTickSpacing(n);
        }
        firePropertyChange("minorTickSpacing", oldValue, n);
    }

    /**
     * Returns true if the thumb (and the data value it represents) resolve to the closest tick mark next to where the user
     * positioned the thumb.
     * @return true if the value snaps to the nearest tick mark, else false
     */
    public boolean getSnapToTicks()
    {
        return this.sliders[0].getSnapToTicks();
    }

    /**
     * Specifying true makes the thumb (and the data value it represents) resolve to the closest tick mark next to where the
     * user positioned the thumb. By default, this property is {@code false}.
     * @param b true to snap the thumb to the nearest tick mark
     */
    @BeanProperty(description = "If true snap the thumb to the nearest tick mark.")
    public void setSnapToTicks(final boolean b)
    {
        boolean oldValue = getSnapToTicks();
        for (var slider : this.sliders)
        {
            slider.setSnapToTicks(b);
        }
        firePropertyChange("snapToTicks", oldValue, b);
    }

    /**
     * Tells if tick marks are to be painted.
     * @return true if tick marks are painted, else false
     */
    public boolean getPaintTicks()
    {
        return this.sliders[0].getPaintTicks();
    }

    /**
     * Determines whether tick marks are painted on the slider. By default, this property is {@code false}.
     * @param b whether or not tick marks should be painted
     */
    @BeanProperty(visualUpdate = true, description = "If true tick marks are painted on the slider.")
    public void setPaintTicks(final boolean b)
    {
        boolean oldValue = getPaintTicks();
        for (var slider : this.sliders)
        {
            slider.setPaintTicks(b);
        }
        firePropertyChange("paintTicks", oldValue, b);
    }

    /**
     * Tells if the track (area the slider slides in) is to be painted.
     * @return true if track is painted, else false
     */
    public boolean getPaintTrack()
    {
        return this.sliders[0].getPaintTrack();
    }

    /**
     * Determines whether the track is painted on the slider. By default, this property is {@code true}. It is up to the look
     * and feel to honor this property, some may choose to ignore it.
     * @param b whether or not to paint the slider track
     * @see #getPaintTrack
     */
    @BeanProperty(visualUpdate = true, description = "If true, the track is painted on the slider.")
    public void setPaintTrack(final boolean b)
    {
        boolean oldValue = getPaintTrack();
        this.sliders[0].setPaintTrack(b);
        firePropertyChange("paintTrack", oldValue, b);
    }

    /**
     * Tells if labels are to be painted.
     * @return true if labels are painted, else false
     */
    public boolean getPaintLabels()
    {
        return this.sliders[0].getPaintLabels();
    }

    /**
     * Determines whether labels are painted on the slider.
     * <p>
     * This method will also set up a label table for you. If there is not already a label table, and the major tick spacing is
     * {@code > 0}, a standard label table will be generated (by calling {@code createStandardLabels}) with labels at the major
     * tick marks. The label table is then set on the slider by calling {@code setLabelTable}.
     * </p>
     * By default, this property is {@code false}.
     * @param b whether or not to paint labels
     */
    @BeanProperty(visualUpdate = true, description = "If true labels are painted on the slider.")
    public void setPaintLabels(final boolean b)
    {
        boolean oldValue = getPaintLabels();
        for (var slider : this.sliders)
        {
            slider.setPaintLabels(b);
        }
        firePropertyChange("paintLabels", oldValue, b);
    }

    /**
     * The DispatcherPane class, which is a glass pane sitting on top of the sliders to dispatch the mouse event to the correct
     * slider class. Note that the mouse coordinates are relative to the component itself, so a translation might be needed. The
     * <code>SwingUtilities.convertPoint()</code> method can make the conversion.
     */
    protected static class DispatcherPane extends JComponent
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** the pointer to the multislider object; protected to access it by the mouse handlers. */
        @SuppressWarnings("checkstyle:visibilitymodifier")
        protected final MultiSlider multiSlider;

        /**
         * Calculate x (horizontal) or y (vertical) of thumb[i].
         * @param i the slider number
         * @return the x (horizontal) or y (vertical) of thumb[i]
         */
        int thumbPosition(final int i)
        {
            JSlider slider = this.multiSlider.getSlider(i);
            int value = slider.getValue();
            int min = slider.getMinimum();
            int max = slider.getMaximum();
            if (slider.getOrientation() == SwingConstants.HORIZONTAL)
            {
                int w = slider.getWidth();
                // System.out.println("ThumbPosition[w] " + i + " = " + (int) (1.0 * w * value / (max - min)));
                if (this.multiSlider.getInverted())
                {
                    return (int) (1.0 * w * (max - value) / (max - min));
                }
                else
                {
                    return (int) (1.0 * w * (value - min) / (max - min));
                }
            }
            else
            {
                int h = slider.getHeight();
                // System.out.println("thumbPosition[h] " + i + " = " + (int) (1.0 * h * value / (max - min)));
                if (this.multiSlider.getInverted())
                {
                    return (int) (1.0 * h * (max - value) / (max - min));
                }
                else
                {
                    return (int) (1.0 * h * (value - min) / (max - min));
                }
            }
        }

        /**
         * Return the closest slider number based on x (horizontal) or y (vertical) of the thumbs.
         * @param p the point (e.g., of a mouse position)
         * @return the index of the closest slider
         */
        int closestSliderIndex(final Point p)
        {
            if (this.multiSlider.getBusySlider() >= 0)
            {
                return this.multiSlider.getBusySlider();
            }

            int ret = -1;
            int mindist = Integer.MAX_VALUE;
            if (this.multiSlider.getOrientation() == SwingConstants.HORIZONTAL)
            {
                for (int i = 0; i < this.multiSlider.getNumberOfThumbs(); i++)
                {
                    int pos = thumbPosition(i);
                    int dist = Math.abs(pos - p.x);
                    if (dist < mindist)
                    {
                        mindist = dist;
                        ret = i;
                    }
                }
            }
            else
            {
                for (int i = 0; i < this.multiSlider.getNumberOfThumbs(); i++)
                {
                    int pos = thumbPosition(i);
                    int dist = Math.abs(pos - p.y);
                    if (dist < mindist)
                    {
                        mindist = dist;
                        ret = i;
                    }
                }
                ret = this.multiSlider.getNumberOfThumbs() - ret - 1;
            }
            this.multiSlider.setBusySlider(ret);
            return ret;
        }

        /**
         * @param e the MouseEvent to dispatch to the sliders.
         * @param always indicates whether we always need to send the event
         */
        void dispatch(final MouseEvent e, final boolean always)
        {
            var slider = DispatcherPane.this.multiSlider.getSlider(0);
            Point pSlider = SwingUtilities.convertPoint(DispatcherPane.this, e.getPoint(), slider);
            if (always || (pSlider.x >= 0 && pSlider.x <= slider.getSize().width && pSlider.y >= 0
                    && pSlider.y <= slider.getSize().height))
            {
                int index = closestSliderIndex(pSlider);
                MouseEvent meSlider = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        pSlider.x, pSlider.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
                DispatcherPane.this.multiSlider.getSlider(index).dispatchEvent(meSlider);
            }
        }

        /**
         * Reset the busy slider -- action over. Call this method BEFORE processing the MouseEvent. In that way, the
         * ChangeListener will fire a StateChange while the busy slider is -1 -- indicating that there is a final value.
         */
        void resetBusySlider()
        {
            this.multiSlider.setBusySlider(-1);
        }

        /**
         * Indicate whether the multislider is busy with handling an action (e.g., drag, mouse down). Note that the busy flag
         * has to be set BEFORE the mouse event is handled, to allow a listener to the ChangeEvent to only react when an action
         * is completed.
         * @param b whether the multislider is busy with handling an action or not
         */
        void setBusy(final boolean b)
        {
            this.multiSlider.setBusy(b);
        }

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
                @Override
                public void mouseReleased(final MouseEvent e)
                {
                    // Note that a mouse release should ALWAYS be communicated, also when it is outside of the slider
                    setBusy(false);
                    dispatch(e, true);
                    resetBusySlider();
                }

                @Override
                public void mousePressed(final MouseEvent e)
                {
                    setBusy(true);
                    dispatch(e, false);
                }

                @Override
                public void mouseExited(final MouseEvent e)
                {
                    // Note that a mouse exited should ALWAYS be communicated, also when it is outside of the slider
                    setBusy(false);
                    dispatch(e, true);
                    resetBusySlider();
                }

                @Override
                public void mouseEntered(final MouseEvent e)
                {
                    // no action
                }

                @Override
                public void mouseClicked(final MouseEvent e)
                {
                    setBusy(false);
                    dispatch(e, false);
                    resetBusySlider();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(final MouseEvent e)
                {
                    setBusy(true);
                    dispatch(e, false);
                }

                @Override
                public void mouseMoved(final MouseEvent e)
                {
                    // Note: possibly we can trigger an 'mouse entered' when the mouse enters the slider pane,
                    // and a 'mouse exited' when the mouse exits the slider pane.
                }
            });
        }

    }
}
