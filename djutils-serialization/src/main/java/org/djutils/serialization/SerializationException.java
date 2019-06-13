package org.djutils.serialization;

/**
 * Exception for the DSOL ZeroMQ bridge.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$,  <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
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
        super();
    }

    /**
     * Create a serialization exception.
     * @param message the message
     */
    public SerializationException(final String message)
    {
        super(message);
    }

    /**
     * Create a serialization exception.
     * @param cause the exception that caused the serialization exception
     */
    public SerializationException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a serialization exception.
     * @param message the message
     * @param cause the exception that caused the serialization exception
     */
    public SerializationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Create a serialization exception.
     * @param message the message
     * @param cause the exception that caused the serialization exception
     * @param enableSuppression to enable suppressions or not
     * @param writableStackTrace to have a writable stack trace or not
     */
    public SerializationException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
