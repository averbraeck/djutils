package org.djutils.draw;

/**
 * InvalidProjectionException is a special type of DrawRuntimeException, that is thrown if a projection results in an invalid
 * object, or if a projection cannot be carried out.
 * <p>
 * Copyright (c) 2020-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class InvalidProjectionException extends DrawRuntimeException
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create an empty runtime drawing exception.
     */
    public InvalidProjectionException()
    {
        super();
    }

    /**
     * Create a runtime drawing exception with a custom message.
     * @param message String; the custom message.
     */
    public InvalidProjectionException(final String message)
    {
        super(message);
    }

    /**
     * Create a runtime drawing exception with an underlying cause.
     * @param cause Throwable; the cause of this exception to be thrown
     */
    public InvalidProjectionException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a runtime drawing exception with a custom message and an underlying cause.
     * @param message String; the custom message
     * @param cause Throwable; the cause of this exception to be thrown
     */
    public InvalidProjectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
