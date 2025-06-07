package org.djutils.draw;

/**
 * InvalidProjectionException is an exception that is thrown when a projection results in an invalid object, or if a projection
 * cannot be carried out.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class InvalidProjectionException extends RuntimeException
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create an empty runtime projection exception.
     */
    public InvalidProjectionException()
    {
        super();
    }

    /**
     * Create a runtime projection exception with a custom message.
     * @param message the custom message.
     */
    public InvalidProjectionException(final String message)
    {
        super(message);
    }

    /**
     * Create a runtime projection exception with an underlying cause.
     * @param cause the cause of this exception to be thrown
     */
    public InvalidProjectionException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a runtime projection exception with a custom message and an underlying cause.
     * @param message the custom message
     * @param cause the cause of this exception to be thrown
     */
    public InvalidProjectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
