package org.djutils.swing.multislider;

import java.awt.BorderLayout;
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
public class MultiSliderDemo extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    @SuppressWarnings("checkstyle:needbraces")
    public MultiSliderDemo()
    {
        setPreferredSize(new Dimension(640, 640));
        setTitle("MultiSliderDemo");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);
        getContentPane().add(panel);
        getContentPane().setBackground(new Color(204, 255, 204));

        var horSlider = new MultiSlider(SwingConstants.HORIZONTAL, 0, 100, new int[] {25, 50, 75, 80});
        // horSlider.setUI(new BasicSliderUI());
        horSlider.setMajorTickSpacing(25);
        horSlider.setMinorTickSpacing(5);
        horSlider.setPaintTicks(true);
        horSlider.setPaintLabels(true);
        panel.add(horSlider, BorderLayout.NORTH);
        horSlider.setThumbLabel(0, "a");
        horSlider.setThumbLabel(1, "b");
        horSlider.setThumbLabel(2, "c");
        horSlider.setDrawThumbLabels(true, 20);

        var vertSlider = new MultiSlider(SwingConstants.VERTICAL, 0, 100, new int[] {40, 60});
        // vertSlider.setUI(new MetalSliderUI());
        vertSlider.setMajorTickSpacing(20);
        vertSlider.setMinorTickSpacing(5);
        vertSlider.setPaintTicks(true);
        vertSlider.setPaintLabels(true);
        vertSlider.setPaintTrack(true);
        panel.add(vertSlider, BorderLayout.WEST);
        vertSlider.setThumbLabel(0, "min");
        vertSlider.setThumbLabel(1, "max");
        vertSlider.setDrawThumbLabels(true, 35);

        var horSlider2 = new MultiSlider(SwingConstants.HORIZONTAL, 0, 10, new int[] {2, 5, 7});
        horSlider2.setMajorTickSpacing(1);
        horSlider2.setMinorTickSpacing(1);
        horSlider2.setPaintTicks(true);
        horSlider2.setPaintLabels(true);
        panel.add(horSlider2, BorderLayout.SOUTH);
        horSlider2.setThumbLabel(0, "x");
        horSlider2.setThumbLabel(1, "y");
        horSlider2.setThumbLabel(2, "z");
        horSlider2.setDrawThumbLabels(true, 20);
        horSlider2.setSnapToTicks(true);
        horSlider2.setUI(new BasicSliderUI());

        var button = new JButton("RESET");
        button.setPreferredSize(new Dimension(100, 25));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        panel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(button);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.setOpaque(false);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                horSlider2.resetToInitialValues();
                vertSlider.resetToInitialValues();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        horSlider2.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                MultiSlider s = (MultiSlider) e.getSource();
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    if (!s.isBusy())
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
                    if (!s.isBusy())
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
