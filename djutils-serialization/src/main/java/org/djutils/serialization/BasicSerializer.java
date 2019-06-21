package org.djutils.serialization;

/**
 * Basics of the serializer
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$,  <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> class
 */
public abstract class BasicSerializer<T extends Object> implements Serializer<T>
{
    /** The field type that usually prefixes the serialized data. */
    private final byte type;
    
    /** String returned by the dataClassName method. */
    private final String dataClassName;

    /**
     * Construct the BasicSerializer.
     * @param type byte; the field type (returned by the <code>fieldType</code> method)
     * @param dataClassName String; returned by the dataClassName method
     */
    public BasicSerializer(final byte type, final String dataClassName)
    {
        this.type = type;
        this.dataClassName = dataClassName;
    }
    
    @Override
    public final byte fieldType()
    {
        return this.type;
    }

    @Override
    public final String dataClassName()
    {
        return this.dataClassName;
    }

    @Override
    public String toString()
    {
        return "BasicSerializer [type=" + this.type + ", dataClassName=" + this.dataClassName + "]";
    }

}
