package org.djutils.stats.summarizers;

import java.io.Serializable;

/**
 * The Statistic interface defines the methods to implement for each of the statistics classes.
 * <p>
 * Copyright (c) 2023-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public interface Statistic extends Serializable
{
    /**
     * Initialize the statistic.
     */
    void initialize();

    /**
     * Returns the description of the statistic.
     * @return the description of the statistic
     */
    String getDescription();

    /**
     * Set a new description of the statistic.
     * @param description the new description of the statistic
     */
    void setDescription(String description);

    /**
     * Return the current number of observations.
     * @return the number of observations
     */
    long getN();

    /**
     * Return a string representing a line with important statistics values for this statistic, for a textual table with a
     * monospaced font that can contain multiple statistics.
     * @return line with most important values of the statistic
     */
    String reportLine();

    /**
     * Return a formatted string with 2 digits precision for a floating point value that fits the number of characters. The
     * formatter will fall back to scientific notation when the value does not fit with floating point notation.
     * @param value the value to format
     * @param numberCharacters the number of characters for the result
     * @return a string representation with the given number of characters
     */
    default String formatFixed(final double value, final int numberCharacters)
    {
        if (Double.isNaN(value) || Double.isInfinite(value) || value == 0.0 || Math.abs(value) >= 0.01)
        {
            String formatted = String.format("%" + numberCharacters + ".3f", value);
            if (formatted.length() == numberCharacters)
            {
                return formatted;
            }
        }
        return String.format("%" + numberCharacters + ".3e", value);
    }
}
