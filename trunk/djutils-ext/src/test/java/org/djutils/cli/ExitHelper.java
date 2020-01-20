package org.djutils.cli;

import java.security.Permission;

/**
 * ExitHelper assists in testing System.exit() calls. Normally System.exit() calls cannot be tested by Maven SureFire, see
 * <a href= "https://maven.apache.org/surefire/maven-surefire-plugin/faq.html#vm-termination">
 * https://maven.apache.org/surefire/maven-surefire-plugin/faq.html#vm-termination</a><br>
 * Based on <a href="https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit">
 * https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit</a><br>
 * Other solutions can be found at
 * <a href="https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit">
 * https://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit</a><br>
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class ExitHelper
{
    /** Exception that will be thrown instead of the System.exit() call. */
    public static class ExitException extends SecurityException
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** the exit code. */
        @SuppressWarnings("checkstyle:visibilitymodifier")
        public final int status;

        /**
         * Instantiate the exception.
         * @param status the exit code
         */
        public ExitException(final int status)
        {
            super("System.exit(" + status + ") called");
            this.status = status;
        }
    }

    /** The security manager for the exit exception. */
    public static class NoExitSecurityManager extends SecurityManager
    {
        @Override
        public void checkPermission(final Permission perm)
        {
            // allow anything.
        }

        @Override
        public void checkPermission(final Permission perm, final Object context)
        {
            // allow anything.
        }

        /** {@inheritDoc} */
        @Override
        public void checkExit(final int status)
        {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }
}
