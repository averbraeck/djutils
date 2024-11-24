package org.djutils.swing.multislider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * MultiSliderDemo demonstrates a horizontal slider and a vertical slider, as well as setValue() through a reset button.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MultiSliderRestrictionDemo extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    @SuppressWarnings("checkstyle:needbraces")
    public MultiSliderRestrictionDemo()
    {
        setPreferredSize(new Dimension(840, 640));
        setTitle("MultiSliderRestrictionDemo");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);
        getContentPane().add(panel);
        getContentPane().setBackground(new Color(204, 255, 204));

        JPanel sliderPanel = new JPanel();
        sliderPanel.setPreferredSize(new Dimension(480, 600));
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setOpaque(false);
        panel.add(sliderPanel);

        sliderPanel.add(Box.createVerticalGlue());

        var s1 = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {25, 50, 75, 80});
        s1.setPreferredSize(new Dimension(480, 10));
        s1.setMajorTickSpacing(25);
        s1.setMinorTickSpacing(5);
        s1.setPaintTicks(true);
        s1.setPaintLabels(true);
        sliderPanel.add(s1);
        s1.setThumbLabel(0, "a");
        s1.setThumbLabel(1, "b");
        s1.setThumbLabel(2, "c");
        s1.setDrawThumbLabels(true, 20);
        s1.setPassing(false);

        sliderPanel.add(Box.createVerticalGlue());

        var s2 = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {40, 60});
        s2.setPreferredSize(new Dimension(480, 10));
        s2.setMajorTickSpacing(20);
        s2.setMinorTickSpacing(5);
        s2.setPaintTicks(true);
        s2.setPaintLabels(true);
        s2.setPaintTrack(true);
        sliderPanel.add(s2);
        s2.setThumbLabel(0, "min");
        s2.setThumbLabel(1, "max");
        s2.setDrawThumbLabels(true, 20);
        s2.setOverlap(false);

        sliderPanel.add(Box.createVerticalGlue());

        var s3 = new MultiSlider(SwingConstants.HORIZONTAL, 0, 10, new int[] {2, 5, 7});
        s3.setPreferredSize(new Dimension(480, 10));
        s3.setMajorTickSpacing(1);
        s3.setMinorTickSpacing(1);
        s3.setPaintTicks(true);
        s3.setPaintLabels(true);
        sliderPanel.add(s3);
        s3.setThumbLabel(0, "x");
        s3.setThumbLabel(1, "y");
        s3.setThumbLabel(2, "z");
        s3.setDrawThumbLabels(true, 20);
        s3.setSnapToTicks(true);
        s3.setUI(new BasicSliderUI());
        s3.setPassing(false);
        s3.setOverlap(false);

        sliderPanel.add(Box.createVerticalGlue());

        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setPreferredSize(new Dimension(120, 600));
        panel.add(emptyPanel);

        var button = new JButton("RESET");
        button.setPreferredSize(new Dimension(100, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(200, 600));
        panel.add(buttonPanel);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(button);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.setOpaque(false);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                s1.resetToInitialValues();
                s2.resetToInitialValues();
                s3.resetToInitialValues();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        s1.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    if (!s.isBusy())
                        System.out.println("s1 Thumb " + i + ": " + s.getValue(i));
                }
            }
        });

        s2.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    if (!s.isBusy())
                        System.out.println("s2 Thumb " + i + ": " + s.getValue(i));
                }
            }
        });

        s3.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    if (!s.isBusy())
                        System.out.println("s3 Thumb " + i + ": " + s.getValue(i));
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
                new MultiSliderRestrictionDemo();
            }
        });
    }

}
