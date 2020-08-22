package org.djutils.serialization;

/**
 * Exception for the DSOL ZeroMQ bridge.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class SerializationException extends Exception
{
    /** */
    private static final long serialVersionUID = 20190611L;

    /**
     * Create a serialization exception.
     */
    public SerializationException()
    {
    }

    /**
     * Create a serialization exception.
 * @param message String; the message
     */
    public SerializationException(final String message)
    {
        super(message);
    }

    /**
     * Create a serialization exception.
 * @param cause Throwable; the exception that caused the serialization exception
     */
    public SerializationException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a serialization exception.
 * @param message String; the message
 * @param cause Throwable; the exception that caused the serialization exception
     */
    public SerializationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Create a serialization exception.
 * @param message String; the message
 * @param cause Throwable; the exception that caused the serialization exception
 * @param enableSuppression boolean; to enable suppressions or not
 * @param writableStackTrace boolean; to have a writable stack trace or not
     */
    public SerializationException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
