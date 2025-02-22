package org.djutils.math.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Draw text in unicode superscript glyphs. Much of this is based on
 * <a href="https://rupertshepherd.info/resource_pages/superscript-letters-in-unicode">Superscript letters in unicode</a>
 * <p>
 * Copyright (c) 2024-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public final class SuperScript
{
    /** The translation map. */
    private final Map<Character, Character> translate;
    
    /**
     * Create the SuperScript converter.
     */
    public SuperScript()
    {
        // @formatter:off
        char[][] table = new char[][] {
            // Capital letters are missing a lot, but we have an E (needed to write numbers in scientific notation)
            {'A', '\u1d2c'},
            {'B', 'ᴮ'}, // from https://lingojam.com/SuperscriptGenerator
            {'C', '\u1d9c'}, // lower case - there is no superscript upper case C
            {'D', '\u1d30'},
            {'E', '\u1d31'},
            {'F', '\u1da0'}, // lower case - there is no superscript upper case F
            {'G', '\u1d33'},
            {'H', '\u1d34'},
            {'I', '\u1d35'},
            {'J', '\u1d36'},
            {'K', '\u1d37'},
            {'L', '\u1d38'},
            {'M', '\u1d39'},
            {'N', '\u1d3a'},
            {'O', '\u1d3c'},
            {'P', '\u1d3e'},
            {'Q', 'ᑫ'}, // approximation of lower case q from https://lingojam.com/SuperscriptGenerator
            {'R', '\u1d3f'},
            {'S', 'ˢ'}, // approximation of lower case s from https://lingojam.com/SuperscriptGenerator
            {'T', '\u1d40'},
            {'U', '\u1d41'},
            {'V', '\u2c7d'},
            {'W', '\u1d42'},
            {'X', '\u157d'}, // Canadian symbol
            {'Y', '\u1d31'},
            {'Z', '\u1d31'},
            {'a', '\u1d43'},
            {'b', '\u1d47'},
            {'c', '\u1d9c'},
            {'d', '\u1d48'},
            {'e', '\u1d49'},
            {'f', '\u1da0'},
            {'g', '\u1d4d'},
            {'h', '\u02b0'},
            {'i', '\u2071'},
            {'j', '\u02b2'},
            {'k', '\u1d4f'},
            {'l', '\u02e1'},
            {'m', '\u1d50'},
            {'n', '\u207f'},
            {'o', '\u1d52'},
            {'p', '\u1d56'},
            {'q', 'ᑫ'}, // Does not exist; this approximation is from https://lingojam.com/SuperscriptGenerator
            {'r', '\u02b3'},
            {'s', '\u02e2'},
            {'t', '\u1d57'},
            {'u', '\u1d58'},
            {'v', '\u1d5b'},
            {'w', '\u02b7'},
            {'x', '\u02e3'},
            {'y', '\u02b8'},
            {'z', '\u1dbb'},
            {'0', '\u2070'},
            {'1', '\u00b9'},
            {'2', '\u00b2'},
            {'3', '\u00b3'},
            {'4', '\u2074'},
            {'5', '\u2075'},
            {'6', '\u2076'},
            {'7', '\u2077'},
            {'8', '\u2078'},
            {'9', '\u2079'},
            {'+', '\u207a'},
            {'-', '\u207b'},
            {'.', '\u00b7'}, // https://www.compart.com/en/unicode/U+00B7 middle dot character
            {',', '\u02be'}, // https://stackoverflow.com/questions/34350441/is-there-an-unicode-symbol-for-superscript-comma
            {'(', '\u207d'},
            {')', '\u207e'}
            };
        // @formatter:on

        this.translate = new HashMap<>();
        for (int i = 0; i < table.length; i++)
        {
            this.translate.put(table[i][0], table[i][1]);
        }

    }

    /**
     * Translate one character to superscript.
     * @param in the character to translate
     * @return the character in superscript, some approximation thereof, or the input character if no translation was available
     */
    public char translate(final char in)
    {
        Character result = this.translate.get(in);
        if (null == result)
        {
            return in; // Sorry
        }
        return result;
    }

    /**
     * Translate a String into superscript.
     * @param in text to translate
     * @return superscripted text
     */
    public String translate(final String in)
    {
        StringBuilder result = new StringBuilder();
        for (int pos = 0; pos < in.length(); pos++)
        {
            result.append(translate(in.charAt(pos)));
        }
        return result.toString();
    }

}
