package org.djutils.metadata;

import org.djutils.exceptions.Throw;

/**
 * ObjectDescriptor: wrapper for name, description and class of one object. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class ObjectDescriptor
{

    /** Name. */
    private final String name;

    /** Description. */
    private final String description;

    /** Class. */
    private final Class<?> objectClass;

    /**
     * Construct a new FieldDescription object.
     * @param name String; name of the object
     * @param description String; description of the object
     * @param objectClass Class&lt;?&gt;; class of the object
     */
    ObjectDescriptor(final String name, final String description, final Class<?> objectClass)
    {
        Throw.whenNull(name, "name may not be null");
        Throw.whenNull(description, "description may not be null");
        Throw.whenNull(objectClass, "objectClass may not be null");
        this.name = name;
        this.description = description;
        this.objectClass = objectClass;
    }

    /**
     * Retrieve the name of the object.
     * @return String; description of the object
     */
    String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the description of the object.
     * @return String; description of the object
     */
    String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve the Class of the object.
     * @return String; class name of the object
     */
    Class<?> getObjectClass()
    {
        return this.objectClass;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ObjectDescriptor [name=" + this.name + ", description=" + this.description + ", objectClass=" + this.objectClass
                + "]";
    }

}
