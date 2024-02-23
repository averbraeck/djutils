package org.djutils.data.serialization;

/**
 * TextSerializationException is the exception thrown on errors when (de)serializing objects.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TextSerializationException extends Exception
{

    /** */
    private static final long serialVersionUID = 20200302L;

    /**
     * Constructor for TextSerializationException.
     */
    public TextSerializationException()
    {
    }

    /**
     * Constructor for TextSerializationException.
     * @param message String; explanation of the exception
     * @param cause Throwable; underlying exception
     */
    public TextSerializationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for TextSerializationException.
     * @param message String; explanation of the exception
     */
    public TextSerializationException(final String message)
    {
        super(message);
    }

    /**
     * Constructor for TextSerializationException.
     * @param cause Throwable; underlying exception
     */
    public TextSerializationException(final Throwable cause)
    {
        super(cause);
    }

}
