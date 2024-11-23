package org.djutils.swing.multislider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSliderUI;

import org.junit.jupiter.api.Test;

/**
 * MultiSliderTest tests the functions of the MultiSlider.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MultiSliderTest
{

    /**
     * Create a MultiSlider with three underlying sliders and test the basic getters and setters.
     */
    @Test
    public void testGetSet()
    {
        testGetSet(SwingConstants.HORIZONTAL);
        testGetSet(SwingConstants.VERTICAL);
        testGetSet(-1);
    }

    /**
     * Test get/set of a multislider with an orientation.
     * @param initialOrientation the orientation
     */
    private void testGetSet(final int initialOrientation)
    {
        MultiSlider ms;
        int orientation = initialOrientation;
        if (initialOrientation == -1)
        {
            ms = new MultiSlider(0, 100, new int[] {25, 50, 75});
            orientation = SwingConstants.HORIZONTAL;
        }
        else
        {
            ms = new MultiSlider(orientation, 0, 100, new int[] {25, 50, 75});
        }
        assertEquals(0, ms.getMinimum());
        assertEquals(100, ms.getMaximum());
        assertEquals(3, ms.getNumberOfThumbs());
        assertEquals(25, ms.getValue(0));
        assertEquals(50, ms.getValue(1));
        assertEquals(75, ms.getValue(2));
        assertEquals(orientation, ms.getOrientation());
        assertFalse(ms.isBusy());

        JSlider js0 = ms.getSlider(0);
        assertEquals(25, js0.getValue());
        JSlider js1 = ms.getSliders()[1];
        assertEquals(50, js1.getValue());

        ms.setThumbLabel(0, "min");
        ms.setThumbLabel(2, "max");
        assertEquals("min", ms.getThumbLabel(0));
        assertEquals("", ms.getThumbLabel(1));
        assertEquals("max", ms.getThumbLabel(2));
        ms.setDrawThumbLabels(true, 20);
        assertTrue(ms.isDrawThumbLabels());
        ms.setDrawThumbLabels(false, 0);
        assertFalse(ms.isDrawThumbLabels());

        ms.setValue(1, 40);
        assertEquals(40, ms.getValue(1));
        assertEquals(40, js1.getValue());
        assertEquals(25, ms.getValue(0));
        assertEquals(75, ms.getValue(2));
        ms.resetToInitialValues();
        assertEquals(25, ms.getValue(0));
        assertEquals(50, ms.getValue(1));
        assertEquals(75, ms.getValue(2));

        ms.setMinimum(10);
        ms.setMaximum(90);
        assertEquals(10, ms.getMinimum());
        assertEquals(90, ms.getMaximum());
        assertEquals(25, ms.getValue(0));
        assertEquals(50, ms.getValue(1));
        assertEquals(75, ms.getValue(2));
        ms.setMinimum(0);
        ms.setMaximum(100);

        int oldExtent = ms.getExtent();
        ms.setExtent(oldExtent + 2);
        assertEquals(oldExtent + 2, ms.getExtent());
        ms.setExtent(oldExtent);
        assertEquals(oldExtent, ms.getExtent());

        assertFalse(ms.getInverted());
        ms.setInverted(true);
        assertTrue(ms.getInverted());
        ms.setInverted(false);
        assertFalse(ms.getInverted());

        ms.setMajorTickSpacing(10);
        assertEquals(10, ms.getMajorTickSpacing());
        ms.setMajorTickSpacing(20);
        assertEquals(20, ms.getMajorTickSpacing());
        ms.setMinorTickSpacing(10);
        assertEquals(10, ms.getMinorTickSpacing());
        ms.setMinorTickSpacing(5);
        assertEquals(5, ms.getMinorTickSpacing());

        ms.setPaintTicks(false);
        assertFalse(ms.getPaintTicks());
        ms.setPaintTicks(true);
        assertTrue(ms.getPaintTicks());

        ms.setPaintTrack(false);
        assertFalse(ms.getPaintTrack());
        ms.setPaintTrack(true);
        assertTrue(ms.getPaintTrack());

        ms.setSnapToTicks(true);
        assertTrue(ms.getSnapToTicks());
        ms.setSnapToTicks(false);
        assertFalse(ms.getSnapToTicks());

        ms.setPaintLabels(true);
        assertTrue(ms.getPaintLabels());
        ms.setPaintLabels(false);
        assertFalse(ms.getPaintLabels());

        assertEquals(ms.getOrientation() == SwingConstants.HORIZONTAL, ms.isHorizontal());
        assertEquals(ms.getOrientation() == SwingConstants.VERTICAL, ms.isVertical());
        ms.setOrientation(1 - orientation);
        assertNotEquals(orientation, ms.getOrientation());
        assertEquals(ms.getOrientation() == SwingConstants.HORIZONTAL, ms.isHorizontal());
        assertEquals(ms.getOrientation() == SwingConstants.VERTICAL, ms.isVertical());
        ms.setOrientation(orientation);
        assertEquals(orientation, ms.getOrientation());
        assertEquals(ms.getOrientation() == SwingConstants.HORIZONTAL, ms.isHorizontal());
        assertEquals(ms.getOrientation() == SwingConstants.VERTICAL, ms.isVertical());
    }

    /**
     * Test mouse clicks for a multislider with a horizontal and vertical orientation and an inverted and non-inverted
     * direction.
     * @throws AWTException on AWT or Swing error
     * @throws InterruptedException when sleep is interrupted
     * @throws InvocationTargetException on error in invokeAndWait
     */
    @Test
    public void testMouseClicks() throws AWTException, InterruptedException, InvocationTargetException
    {
        testMouseClicks(SwingConstants.HORIZONTAL, false);
        testMouseClicks(SwingConstants.VERTICAL, false);
        testMouseClicks(SwingConstants.HORIZONTAL, true);
        testMouseClicks(SwingConstants.VERTICAL, true);
    }

    /**
     * Test mouse clicks for a multislider with the given orientation.
     * @param orientation the orientation
     * @param inverted whether the scale is inverted or not
     * @throws AWTException on AWT or Swing error
     * @throws InterruptedException when sleep is interrupted
     * @throws InvocationTargetException on error in invokeAndWait
     */
    public void testMouseClicks(final int orientation, final boolean inverted)
            throws AWTException, InterruptedException, InvocationTargetException
    {
        JFrame frame = new JFrame();
        frame.setVisible(false);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 400));
        MultiSlider ms = new MultiSlider(orientation, 0, 100, new int[] {25, 50, 75});
        ms.setInverted(inverted);
        panel.add(ms, orientation == SwingConstants.VERTICAL ? BorderLayout.WEST : BorderLayout.NORTH);
        frame.add(panel);
        frame.validate();
        frame.pack();

        Thread.sleep(500);

        int x = 10;
        int y = 10;
        for (var mml : ms.getDispatcherPane().getMouseMotionListeners())
        {
            SwingUtilities.invokeAndWait(() ->
            { mml.mouseMoved(new MouseEvent(ms, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false)); });
        }

        for (var ml : ms.getDispatcherPane().getMouseListeners())
        {
            for (int i = 0; i < 10; i++)
            {
                ml.mousePressed(new MouseEvent(ms, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y, x, y, 1,
                        false, MouseEvent.BUTTON1));
                Thread.sleep(50);
                ml.mouseReleased(new MouseEvent(ms, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, x, y, 1,
                        false, MouseEvent.BUTTON1));
                int counter = 0;
                while (ms.isBusy() && counter++ < 100)
                {
                    Thread.sleep(10);
                }
                assertTrue(counter < 100);
                Thread.sleep(50);
            }
        }

        Thread.sleep(250);

        String s = "0=" + ms.getValue(0) + ", 1=" + ms.getValue(1) + ", 2=" + ms.getValue(2);
        if (orientation == SwingConstants.HORIZONTAL)
        {
            if (inverted)
            {
                assertEquals(85, ms.getValue(2), "Hor, inv - " + s);
            }
            else
            {
                assertEquals(15, ms.getValue(0), "Hor, not - " + s);
            }
        }
        else
        {
            if (inverted)
            {
                assertEquals(15, ms.getValue(0), "vert, inv - " + s);
            }
            else
            {
                assertEquals(85, ms.getValue(2), "vert, not - " + s);
            }
        }

        frame.dispose();
    }

    /**
     * Test label panel of a multislider with a horizontal orientation.
     * @throws AWTException on AWT or Swing error
     * @throws InterruptedException when sleep is interrupted
     */
    @Test
    public void testLabelPanelHorizontal() throws AWTException, InterruptedException
    {
        JFrame frame = new JFrame();
        frame.setVisible(false);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 400));
        MultiSlider ms = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {25, 50, 75});
        ms.setDrawThumbLabels(false, 0);
        panel.add(ms, BorderLayout.NORTH);
        frame.add(panel);
        frame.validate();
        frame.pack();
        Thread.sleep(500);
        int oldHeight = ms.getHeight();

        ms.setThumbLabel(0, "a");
        ms.setThumbLabel(1, "b");
        ms.setThumbLabel(2, "c");
        ms.setDrawThumbLabels(true, 20);
        frame.revalidate();
        ms.getLabelPanel().paintComponent(ms.getGraphics());
        ms.setValue(0, 20);
        Thread.sleep(500);
        assertTrue(ms.getHeight() > oldHeight, "ms.GetHeight() = " + ms.getHeight() + "; oldHeight = " + oldHeight);

        frame.dispose();

    }

    /**
     * Test font and UI.
     * @throws InterruptedException when sleep is interrupted
     * @throws UnsupportedLookAndFeelException when MetalLookAndFeel unavailable
     * @throws IllegalAccessException when MetalLookAndFeel unavailable
     * @throws InstantiationException when MetalLookAndFeel unavailable
     * @throws ClassNotFoundException when MetalLookAndFeel unavailable
     */
    @Test
    public void testUI() throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException
    {
        JFrame frame = new JFrame();
        frame.setVisible(false);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 400));
        MultiSlider ms = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {25, 50, 75});
        ms.setDrawThumbLabels(false, 0);
        panel.add(ms, BorderLayout.NORTH);
        frame.add(panel);
        frame.validate();
        frame.pack();
        Thread.sleep(500);

        ms.setUI(new BasicSliderUI());
        assertEquals("BasicSliderUI", ms.getUI().getClass().getSimpleName());
        assertEquals("SliderUI", ms.getUIClassID());
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        ms.updateUI();
        assertEquals("MetalSliderUI", ms.getUI().getClass().getSimpleName());

        ms.setFont(new Font("Dialog", Font.ITALIC, 12));
    }
}
