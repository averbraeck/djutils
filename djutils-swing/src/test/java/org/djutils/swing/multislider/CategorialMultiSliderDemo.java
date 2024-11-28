package org.djutils.swing.multislider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * CategorialMultiSliderDemo demonstrates a multislider with categorial values.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CategorialMultiSliderDemo extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    @SuppressWarnings("checkstyle:needbraces")
    public CategorialMultiSliderDemo()
    {
        setPreferredSize(new Dimension(840, 640));
        setTitle("CategorialMultiSliderDemo");
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

        sliderPanel.add(new JLabel("categories A-J, labels(1)"));
        sliderPanel.add(new JLabel("     "));
        var s1 = new CategorialMultiSlider<String>(List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"), "C", "F");
        s1.setPreferredSize(new Dimension(480, 10));
        s1.setMajorTickSpacing(1);
        s1.setMinorTickSpacing(1);
        s1.setPaintTicks(true);
        s1.setPaintLabels(true);
        sliderPanel.add(s1);
        s1.setThumbLabel(0, "a");
        s1.setThumbLabel(1, "b");
        s1.setDrawThumbLabels(true, 20);

        sliderPanel.add(new JLabel("categories A-J, labels(2)"));
        sliderPanel.add(new JLabel("     "));
        var s2 = new CategorialMultiSlider<String>(List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"), "C", "F");
        s2.setPreferredSize(new Dimension(480, 10));
        s2.setMajorTickSpacing(1);
        s2.setMinorTickSpacing(1);
        s2.setLabelTable(s2.createStandardLabels(2));
        s2.setPaintTicks(true);
        s2.setPaintLabels(true);
        sliderPanel.add(s2);
        s2.setThumbLabel(0, "a");
        s2.setThumbLabel(1, "b");
        s2.setDrawThumbLabels(true, 20);

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
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        acl(s1, "s1");
        acl(s2, "s2");
    }

    /**
     * Add change listener.
     * @param ms the multislider
     * @param name the string with the variable name
     */
    void acl(final AbstractMultiSlider<?> ms, final String name)
    {
        ms.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(final ChangeEvent e)
            {
                AbstractMultiSlider<?> s = (AbstractMultiSlider<?>) e.getSource();
                System.out.print(name);
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    System.out.print(", Thumb " + i + ": " + s.getValue(i));
                }
                System.out.println();
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
                new CategorialMultiSliderDemo();
            }
        });
    }

}
