package org.djutils.draw;

/**
 * DrawRuntimeException is the root exception for drawing exceptions that do not have to be specified.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class DrawRuntimeException extends RuntimeException
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create an empty runtime drawing exception.
     */
    public DrawRuntimeException()
    {
        super();
    }

    /**
     * Create a runtime drawing exception with a custom message.
     * @param message the custom message.
     */
    public DrawRuntimeException(final String message)
    {
        super(message);
    }

    /**
     * Create a runtime drawing exception with an underlying cause.
     * @param cause the cause of this exception to be thrown
     */
    public DrawRuntimeException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a runtime drawing exception with a custom message and an underlying cause.
     * @param message the custom message
     * @param cause the cause of this exception to be thrown
     */
    public DrawRuntimeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
