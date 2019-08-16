package org.djutils.serialization;

import org.djutils.exceptions.Throw;

/**
 * Serializable name for the next item in the stream, data, whatever. This serializer serializes strings that are no longer than
 * 255 characters and contain only one-byte ASCII characters.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://sim0mq.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Jun 18, 2019 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class ItemName implements Serializer<String>
{
    /** The string (not final; because it can be modified by calling the deSerialize method). */
    private String string;

    /**
     * Construct a new ItemName.
     * @param string String; name of the item
     * @throws SerializationException when the <code>string</code> is too long, or contains illegal characters
     */
    public ItemName(final String string) throws SerializationException
    {
        Throw.when(string.length() > 255, SerializationException.class, "ItemName may not be longer than 255 characters");
        for (int i = 0; i < string.length(); i++)
        {
            Throw.when(string.charAt(i) > 255, SerializationException.class, "Character at position %d is out of range", i);
        }
        this.string = string;
    }

    /**
     * Construct a new ItemName from serialized data.
     * @param buffer byte[]; the data
     * @param pointer Pointer; position in the data
     */
    public ItemName(final byte[] buffer, final Pointer pointer)
    {
        deSerialize(buffer, pointer, null);
    }

    /** {@inheritDoc} */
    @Override
    public int size(final String object) throws SerializationException
    {
        return 1 + this.string.length();
    }

    /** {@inheritDoc} */
    @Override
    public int sizeWithPrefix(final String object) throws SerializationException
    {
        return 1 + size(object);
    }

    /** {@inheritDoc} */
    @Override
    public byte fieldType()
    {
        return 33;
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final String object, final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
            throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = (byte) this.string.length();
        for (int i = 0; i < this.string.length(); i++)
        {
            buffer[pointer.getAndIncrement(1)] = (byte) (this.string.charAt(i) & 0xff);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serializeWithPrefix(final String object, final byte[] buffer, final Pointer pointer,
            final EndianUtil endianUtil) throws SerializationException
    {
        buffer[pointer.getAndIncrement(1)] = fieldType();
        serialize(object, buffer, pointer, endianUtil);
    }

    /** {@inheritDoc} */
    @Override
    public String deSerialize(final byte[] buffer, final Pointer pointer, final EndianUtil endianUtil)
    {
        int length = buffer[pointer.getAndIncrement(1)] & 0xff;
        char[] chars = new char[length];
        for (int i = 0; i < length; i++)
        {
            chars[i] = (char) (buffer[pointer.getAndIncrement(1)] & 0xff);
        }
        this.string = new String(chars);
        return this.string;
    }

    /** {@inheritDoc} */
    @Override
    public String dataClassName()
    {
        return "Name";
    }

    @Override
    public final int getNumberOfDimensions()
    {
        return 0;
    }

}
