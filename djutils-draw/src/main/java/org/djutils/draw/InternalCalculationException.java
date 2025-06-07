package org.djutils.draw;

/**
 * InternalCalculationException is an exception that is thrown when a a method fails during the construction of a drawable
 * object or during the calculation of a property of a drawable object.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class InternalCalculationException extends RuntimeException
{
    /** */
    private static final long serialVersionUID = 20200828L;

    /**
     * Create an empty runtime calculation exception, indicating an error during the construction of a drawable object or during
     * the calculation of a property of a drawable object.
     */
    public InternalCalculationException()
    {
        super();
    }

    /**
     * Create a runtime calculation exception with a custom message, indicating an error during the construction of a drawable
     * object or during the calculation of a property of a drawable object.
     * @param message the custom message.
     */
    public InternalCalculationException(final String message)
    {
        super(message);
    }

    /**
     * Create a runtime calculation exception with an underlying cause, indicating an error during the construction of a
     * drawable object or during the calculation of a property of a drawable object.
     * @param cause the cause of this exception to be thrown
     */
    public InternalCalculationException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a runtime calculation exception with a custom message and an underlying cause, indicating an error during the
     * construction of a drawable object or during the calculation of a property of a drawable object.
     * @param message the custom message
     * @param cause the cause of this exception to be thrown
     */
    public InternalCalculationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
