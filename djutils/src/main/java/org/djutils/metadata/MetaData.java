package org.djutils.metadata;

import java.util.Arrays;

import org.djutils.exceptions.Throw;

/**
 * MetaDataInterface; documenting Object arrays. <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class MetaData
{
    /** Name of this MetaData object. */
    private final String name;

    /** Description of this MetaData object. */
    private final String description;

    /** The field descriptors. */
    private final ObjectDescriptor[] objectDescriptors;

    /**
     * Construct a new MetaData object.
     * @param name String; name of the new MetaData object
     * @param description String; description of the new MetaData object
     * @param objectDescriptors ObjectDescriptor[]; array of FieldDescriptor. This constructor does <b>not</b> make a deep copy
     *            of this array; subsequent modification of the contents of the provided <code>objectDescriptors</code> array
     *            will affect the behavior of the MetaData object.
     */
    public MetaData(final String name, final String description, final ObjectDescriptor[] objectDescriptors)
    {
        Throw.whenNull(name, "name may not be null");
        Throw.whenNull(description, "description may not be null");
        Throw.whenNull(objectDescriptors, "objectDescriptors may not be null");
        this.name = name;
        this.description = description;
        this.objectDescriptors = objectDescriptors;
    }

    /**
     * Retrieve the name of this MetaData object.
     * @return String; the name of this MetaData object
     */
    String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the description of this MetaData object.
     * @return String; the description of this MetaData object
     */
    String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve the length of described Object array.
     * @return int; the length of the described Object array
     */
    int size()
    {
        return this.objectDescriptors.length;
    }

    /**
     * Retrieve the name of one element in the Object array.
     * @param index int; index of the element in the Object array
     * @return String; name of the argument
     */
    String getFieldName(final int index)
    {
        return this.objectDescriptors[index].getName();
    }

    /**
     * Retrieve the description of one element in the Object array.
     * @param index int; index of the element in the Object array
     * @return String; description of the argument
     */
    String getFieldDescription(final int index)
    {
        return this.objectDescriptors[index].getDescription();
    }

    /**
     * Retrieve the java class of one element in the Object array.
     * @param index int; index of the element in the Object array
     * @return Class&lt;?&gt;; java class of the element
     */
    Class<?> getFieldClass(final int index)
    {
        return this.objectDescriptors[index].getObjectClass();
    }

    /**
     * Verify that an Object array has the prescribed composition.
     * @param objectArray Object[]; the Object array to verify. If the array is supposed to have 0 length, a null pointer is
     *            deemed OK.
     */
    public final void verifyComposition(final Object[] objectArray)
    {
        if (size() == 0 && objectArray == null)
        {
            return;
        }
        Throw.whenNull(objectArray, "objectArray may not be null");
        Throw.when(objectArray.length != size(), IndexOutOfBoundsException.class, "objectArray has wrong length");
        for (int index = 0; index < objectArray.length; index++)
        {
            if (!(getFieldClass(index).isAssignableFrom(objectArray[index].getClass())))
            {
                throw new ClassCastException(String.format("objectArray[%d] (%s) cannot be used for %s", index,
                        objectArray[index], getFieldClass(index).getName()));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "MetaData [name=" + this.name + ", description=" + this.description + ", objectDescriptors="
                + Arrays.toString(this.objectDescriptors) + "]";
    }

}
