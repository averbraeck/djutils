package org.djutils.serialization;

import java.util.List;

/**
 * Interface that must be implemented by objects that can be serialized.
 * <p>
 * Copyright (c) 2019-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
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
