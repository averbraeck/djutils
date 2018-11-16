package org.djutils.immutablecollections;

import java.util.Set;

/**
 * A Set interface without the methods that can change it. The constructor of the ImmutableSet needs to be given an
 * initial Set.
 * <p>
 * Copyright (c) 2016-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> the type of content of this Set
 */
public interface ImmutableSet<E> extends ImmutableCollection<E>
{
    /**
     * Returns a modifiable copy of this immutable set.
     * @return a modifiable copy of this immutable set.
     */
    Set<E> toSet();

    /**
     * Force to redefine equals for the implementations of immutable collection classes.
     * @param obj Object; the object to compare this collection with
     * @return whether the objects are equal
     */
    @Override
    boolean equals(Object obj);

    /**
     * Force to redefine hashCode for the implementations of immutable collection classes.
     * @return the calculated hashCode
     */
    @Override
    int hashCode();
}
