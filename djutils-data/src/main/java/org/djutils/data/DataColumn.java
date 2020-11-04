package org.djutils.data;

import org.djutils.base.Identifiable;

/**
 * Meta data of data in a column.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <T> type of value
 */
public interface DataColumn<T> extends Identifiable
{

    /**
     * Returns the column description.
     * @return String; column description
     */
    String getDescription();

    /**
     * Returns the type of the values in the column.
     * @return Class&lt;?&gt;; type of the values in the column
     */
    Class<T> getValueType();

}
