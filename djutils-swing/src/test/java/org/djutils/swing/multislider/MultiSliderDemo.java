package org.djutils.swing.multislider;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * MultiSliderTest.java.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MultiSliderDemo extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public MultiSliderDemo()
    {
        setPreferredSize(new Dimension(640, 640));
        getContentPane().setLayout(new BorderLayout(10, 10));

        var horSlider = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {25, 50, 75});
        horSlider.setMajorTickSpacing(25);
        horSlider.setMinorTickSpacing(5);
        horSlider.setPaintTicks(true);
        horSlider.setPaintLabels(true);
        getContentPane().add(horSlider, BorderLayout.NORTH);
        
        var vertSlider = new MultiSlider(SwingConstants.VERTICAL, 0, 1000, new int[] {400, 600});
        vertSlider.setMajorTickSpacing(250);
        vertSlider.setMinorTickSpacing(50);
        vertSlider.setPaintTicks(true);
        vertSlider.setPaintLabels(true);
        getContentPane().add(vertSlider, BorderLayout.WEST);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        horSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    System.out.println("Horizontal Thumb " + i + ": " + s.getValue(i));
                }
            }
        });
        
        vertSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    System.out.println("Vertical Thumb " + i + ": " + s.getValue(i));
                }
            }
        });

    }

    /**
     * @param args args
     */
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new MultiSliderDemo();
            }
        });
    }

}
