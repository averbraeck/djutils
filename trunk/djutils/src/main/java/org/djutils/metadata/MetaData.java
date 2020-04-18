package org.djutils.metadata;

import java.io.Serializable;
import java.util.Arrays;

import org.djutils.event.EventType;
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
public class MetaData implements Serializable
{
    /** ... */
    private static final long serialVersionUID = 20200417L;

    /** Name of this MetaData object. */
    private final String name;

    /** Description of this MetaData object. */
    private final String description;

    /** The array of object descriptors. */
    private final ObjectDescriptor[] objectDescriptors;

    /** The single field descriptor. */
    private final ObjectDescriptor objectDescriptor;
    
    /** MetaData object that indicates no data is expected. */
    public static final MetaData EMPTY = new MetaData("No data", "No data", new ObjectDescriptor[0]);

    /**
     * Construct a new MetaData object that can check an array of Object.
     * @param name String; name of the new MetaData object
     * @param description String; description of the new MetaData object
     * @param objectDescriptors ObjectDescriptor[]; array of ObjectDescriptor. This constructor does <b>not</b> make a deep copy
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
        this.objectDescriptor = null;
    }

    /**
     * Construct a new MetaData object that can check a single Object..
     * @param name String; name of the new MetaData object
     * @param description String; description of the new MetaData object
     * @param objectDescriptor ObjectDescriptor; the descriptor for the object that the new MetaData object will accept as valid
     */
    public MetaData(final String name, final String description, final ObjectDescriptor objectDescriptor)
    {
        Throw.whenNull(name, "name may not be null");
        Throw.whenNull(description, "description may not be null");
        Throw.whenNull(objectDescriptor, "objectDescriptor may not be null");
        this.name = name;
        this.description = description;
        this.objectDescriptors = null;
        this.objectDescriptor = objectDescriptor;
    }

    /**
     * Retrieve the name of this MetaData object.
     * @return String; the name of this MetaData object
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the description of this MetaData object.
     * @return String; the description of this MetaData object
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve the length of described Object array.
     * @return int; the length of the described Object array; returns 0 if this MetaDataObject is not set up to validate an
     *         array of Object.
     */
    public int size()
    {
        return null == this.objectDescriptors ? 0 : this.objectDescriptors.length;
    }

    /**
     * Retrieve the name of one element in the Object array.
     * @param index int; index of the element in the Object array (must be 0 if this MetaData object is not set up to validate
     *            an array of Object)
     * @return String; name of the argument
     */
    public String getFieldName(final int index)
    {
        return getObjectDescriptor(index).getName();
    }

    /**
     * Retrieve the description of one element in the Object array.
     * @param index int; index of the element in the Object array (must be 0 if this MetaData object is not set up to validate
     *            an array of Object)
     * @return String; description of the argument
     */
    public String getObjectDescription(final int index)
    {
        return getObjectDescriptor(index).getDescription();
    }

    /**
     * Retrieve the java class of one element in the Object array.
     * @param index int; index of the element in the Object array (must be 0 if this MetaData object is not set up to validate
     *            an array of Object)
     * @return Class&lt;?&gt;; java class of the element
     */
    public Class<?> getObjectClass(final int index)
    {
        return getObjectDescriptor(index).getObjectClass();
    }

    /**
     * Select one of the ObjectDescriptors.
     * @param index int; index of the ObjectDescriptor (must be 0 in case this MetaData object is not set up to validate an
     *            array of Object)
     * @return ObjectDescriptor; the selected ObjectDescriptor
     */
    public ObjectDescriptor getObjectDescriptor(final int index)
    {
        if (null == this.objectDescriptors)
        {
            Throw.when(index != 0, IndexOutOfBoundsException.class, "Index must be 0");
            return this.objectDescriptor;
        }
        return this.objectDescriptors[index];
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
        if (this.equals(EventType.NO_META_DATA))
        {
            return;
        }
        Throw.when(objectArray.length != size(), IndexOutOfBoundsException.class,
                "objectArray for \"%s\" has wrong length (expected %d, got %d)", this.name, size(), objectArray.length);
        for (int index = 0; index < objectArray.length; index++)
        {
            Object object = objectArray[index];
            if ((null != object) && (!(getObjectClass(index).isAssignableFrom(object.getClass()))))
            {
                throw new ClassCastException(String.format("objectArray[%d] (%s) cannot be used for %s", index,
                        objectArray[index], getObjectClass(index).getName()));
            }
        }
    }

    /**
     * Verify that an Object has the prescribed composition.
     * @param object Object; the Object to verify.
     */
    public final void verifyComposition(final Object object)
    {
        Class<?> objectClass = getObjectClass(0);
        if (!(objectClass.isAssignableFrom(object.getClass())))
        {
            throw new ClassCastException(String.format("object (%s) cannot be used for %s", object, objectClass.getName()));
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
