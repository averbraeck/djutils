package org.djutils.logger;

import java.util.function.BooleanSupplier;

/**
 * LoggerTest.java. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class LoggerTest
{
    /**
     * Test whether Logger works correctly.
     */
    // @Test
    public final void loggerTest()
    {
        LogCategory TEST = new LogCategory("TEST");
        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.filter(TEST).info("filter -- TEST off");
        CategoryLogger.addLogCategory(TEST);
        CategoryLogger.filter(TEST).info("filter -- TEST on");
        CategoryLogger.removeLogCategory(TEST);
        CategoryLogger.filter(TEST).info("filter -- TEST off");
        CategoryLogger.filter(LogCategory.ALL).info("filter -- ALL (should be off)");
        CategoryLogger.always().info("always");

        System.out.println("\nwhen(false)");
        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.when(false).filter(TEST).info("filter -- TEST off");
        CategoryLogger.addLogCategory(TEST);
        CategoryLogger.when(false).filter(TEST).info("filter -- TEST on");
        CategoryLogger.removeLogCategory(TEST);
        CategoryLogger.when(false).filter(TEST).info("filter -- TEST off");
        CategoryLogger.when(false).filter(LogCategory.ALL).info("filter -- ALL (should be off)");
        CategoryLogger.when(false).always().info("always");

        System.out.println("\nwhen(true)");
        CategoryLogger.removeLogCategory(LogCategory.ALL);
        CategoryLogger.when(true).filter(TEST).info("filter -- TEST off");
        CategoryLogger.addLogCategory(TEST);
        CategoryLogger.when(true).filter(TEST).info("filter -- TEST on");
        CategoryLogger.removeLogCategory(TEST);
        CategoryLogger.when(true).filter(TEST).info("filter -- TEST off");
        CategoryLogger.when(true).filter(LogCategory.ALL).info("filter -- ALL (should be off)");
        CategoryLogger.when(true).always().info("always");

        System.out.println("\nwhen(Supplier(evaluating to true)");
        CategoryLogger.when(new BooleanSupplier()
        {
            @Override
            public boolean getAsBoolean()
            {
                return true;
            }
        }).always().info("always");

        System.out.println("\nwhen(Supplier(evaluating to false)");
        CategoryLogger.when(new BooleanSupplier()
        {
            @Override
            public boolean getAsBoolean()
            {
                return false;
            }
        }).always().info("always");

    }

    /**
     * @param args should be empty
     */
    public static void main(final String[] args)
    {
        new LoggerTest().loggerTest();
    }
}
