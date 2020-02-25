package org.djutils.stats.summarizers;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.table.TableModel;

import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;

/**
 * The StatisticsObject class defines a statistics object. This abstract class is used to create general table representations
 * for the Counter, the Tally and the Persistent.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */

public abstract class StatisticsObject extends EventProducer implements EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /**
     * represents the statistics object as Table.
     * @return TableModel the result
     * @throws RemoteException on network failure
     */
    public abstract TableModel getTable() throws RemoteException;

}
