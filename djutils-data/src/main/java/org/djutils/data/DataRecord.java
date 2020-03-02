package org.djutils.data;

/**
 * Consistent set of values corresponding to columns. 
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public interface DataRecord
{
    
    /**
     * Returns the column value of this record.
     * @param column Column&lt;T&gt;; column
     * @param <T> value type
     * @return T; the column value in this record
     */
    <T> T getValue(DataColumn<T> column);
    
    /**
     * Returns the column value of this record.
     * @param id String; column id
     * @return Object; the column value in this record
     */
    Object getValue(String id);

    /**
     * Returns the column values of this record in the natural order of the columns.
     * @return the column value in this record
     */
    Object[] getValues();

    /**
     * Returns the column values of this record as an array of Strings in the natural order of the columns. For each value, the
     * toString() method is used to determine the value to put in the array. If the value type is an array type, 
     * @return the column value in this record
     */
    String[] getValuesAsStrings();

}