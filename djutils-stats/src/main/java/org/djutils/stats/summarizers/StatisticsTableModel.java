package org.djutils.stats.summarizers;

import javax.swing.table.DefaultTableModel;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;

/**
 * The StatisticsTableModel class defines the tableModel used by the statistics objects.
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
public class StatisticsTableModel extends DefaultTableModel implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** eventTypes represent the eventTypes corresponding to the colmumns. */
    private EventType[] eventTypes = null;

    /**
     * constructs a new StatisticsTableModel.
     * @param columnNames Object[]; the names of the columns
     * @param eventTypes EventType[]; the eventTypes representing the column
     * @param rows int; the number of rows
     */
    public StatisticsTableModel(final Object[] columnNames, final EventType[] eventTypes, final int rows)
    {
        super(columnNames, rows);
        if (rows != eventTypes.length)
        {
            throw new IllegalArgumentException("eventTypes.length!=rows");
        }
        this.eventTypes = eventTypes;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        for (int i = 0; i < this.eventTypes.length; i++)
        {
            if (event.getType().equals(this.eventTypes[i]))
            {
                this.setValueAt(event.getContent(), i, 1);
            }
        }
    }
}
