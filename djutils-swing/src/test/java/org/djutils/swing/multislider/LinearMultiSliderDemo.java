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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Length;

/**
 * LinearMultiSliderDemo demonstrates a multislider with linear values, extending {@code Number}.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class LinearMultiSliderDemo extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    @SuppressWarnings("checkstyle:needbraces")
    public LinearMultiSliderDemo()
    {
        setPreferredSize(new Dimension(840, 640));
        setTitle("LinearMultiSliderDemo");
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

        sliderPanel.add(new JLabel("Double slider"));
        sliderPanel.add(new JLabel("     "));
        var s1 = new LinearMultiSlider<Double>(0.0, 50.0, 101, 10.0, 25.0, 30.0)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Double mapIndexToValue(final int index)
            {
                return Double.valueOf(index / 2.0);
            }
        };
        s1.setPreferredSize(new Dimension(480, 10));
        s1.setMajorTickSpacing(10);
        s1.setMinorTickSpacing(5);
        s1.setPaintTicks(true);
        s1.setPaintLabels(true);
        sliderPanel.add(s1);
        s1.setLabelTable(s1.createStandardLabels(10));
        s1.setThumbLabel(0, "a");
        s1.setThumbLabel(1, "b");
        s1.setThumbLabel(2, "c");
        s1.setDrawThumbLabels(true, 20);

        sliderPanel.add(new JLabel("Length slider"));
        sliderPanel.add(new JLabel("     "));
        var s2 = new LinearMultiSlider<Length>(Length.ZERO, new Length(50.0, LengthUnit.METER), 101,
                new Length(10.0, LengthUnit.METER), new Length(40.0, LengthUnit.METER))
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Length mapIndexToValue(final int index)
            {
                return Length.instantiateSI(index / 2.0);
            }

            /** {@inheritDoc} */
            @Override
            protected String format(final Length value)
            {
                return String.format("%d%s", (int) value.getInUnit(), value.getDisplayUnit().getDefaultDisplayAbbreviation());
            }
        };
        s2.setPreferredSize(new Dimension(480, 10));
        s2.setMajorTickSpacing(10);
        s2.setMinorTickSpacing(5);
        s2.setLabelTable(s2.createStandardLabels(10));
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
                for (int i = 0; i < s.getNumberOfThumbs(); i++)
                {
                    if (!s.isBusy())
                    {
                        System.out.println(name + " Thumb " + i + ": " + s.getValue(i));
                    }
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
                new LinearMultiSliderDemo();
            }
        });
    }

}
