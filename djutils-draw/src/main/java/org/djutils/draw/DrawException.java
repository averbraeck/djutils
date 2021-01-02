package org.djutils.draw;

/**
 * DrawException is the root exception for drawing exceptions.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DrawException extends Exception
{

    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create an empty drawing exception.
     */
    public DrawException()
    {
        super();
    }

    /**
     * Create a drawing exception with a custom message.
     * @param message String; the custom message.
     */
    public DrawException(final String message)
    {
        super(message);
    }

    /**
     * Create a drawing exception with an underlying cause.
     * @param cause Throwable; the cause of this exception to be thrown
     */
    public DrawException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a drawing exception with a custom message and an underlying cause.
     * @param message String; the custom message
     * @param cause Throwable; the cause of this exception to be thrown
     */
    public DrawException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
