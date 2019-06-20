package org.djutils.serialization;

import java.util.List;

/**
 * Interface that must be implemented by objects that
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <T> type of object that implements this interface
 */
public interface SerializableObject<T>
{
    /**
     * Make all elements that need to be serialized available as a list. The size of this list and the types of the elements in
     * it <b>must</b> be consistent. Meaning that the size of the list and content types of the list are always the same.
     * @return List&lt;Object&gt;; list of the elements that need to serialized
     */
    List<Object> exportAsList();
    
}
