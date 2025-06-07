package org.djutils.cli;

/**
 * CliRuntimeException for exceptions of the CommandLine Interpreter. <br>
 * <br>
 * Copyright (c) 2018-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class CliRuntimeException extends RuntimeException
{

    /** */
    private static final long serialVersionUID = 20190813L;

    /**
     * Standard CliException without explanation.
     */
    public CliRuntimeException()
    {
        super();
    }

    /**
     * @param message the description of the exception
     */
    public CliRuntimeException(final String message)
    {
        super(message);
    }

    /**
     * @param cause the cause of the exception
     */
    public CliRuntimeException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message the description of the exception
     * @param cause the cause of the exception
     */
    public CliRuntimeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
