package org.djutils.swing.multislider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

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
        
        BoundedRangeModel model = ms.getModel();
        assertEquals(0, model.getMinimum());
        assertEquals(100, model.getMaximum());
        assertEquals(25, model.getValue());
        ms.setModel(new DefaultBoundedRangeModel(50, 2, 10, 90));
        assertEquals(10, ms.getMinimum());
        assertEquals(90, ms.getMaximum());
        assertEquals(50, ms.getValue(0));
        assertEquals(50, ms.getValue(1));
        assertEquals(50, ms.getValue(2));
        ms.setMinimum(0);
        ms.setMaximum(100);
        ms.resetToInitialValues();
        assertEquals(0, ms.getMinimum());
        assertEquals(100, ms.getMaximum());
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
        assertEquals(oldExtent + 2, model.getExtent());
        ms.setExtent(oldExtent);
        assertEquals(oldExtent, ms.getExtent());
        assertEquals(oldExtent, model.getExtent());
    }
}
