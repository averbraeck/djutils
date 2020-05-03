package org.djutils.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * MetaDataTest.java. <br>
 * <br>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>. <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class MetaDataTest
{

    /**
     * Test the ObjectDescriptor class.
     */
    @Test
    public void testObjectDescriptor()
    {
        ObjectDescriptor objectDescriptor = new ObjectDescriptor("name", "description", Integer.class);
        assertEquals("name", "name", objectDescriptor.getName());
        assertEquals("description", "description", objectDescriptor.getDescription());
        assertEquals("class", Integer.class, objectDescriptor.getObjectClass());
        assertEquals(objectDescriptor, objectDescriptor);
        assertEquals(objectDescriptor.hashCode(), objectDescriptor.hashCode());
        assertEquals(objectDescriptor, new ObjectDescriptor("name", "description", Integer.class));
        assertNotEquals(objectDescriptor, null);
        assertNotEquals(objectDescriptor, new Object());
        assertNotEquals(objectDescriptor, new ObjectDescriptor("x", "description", Integer.class));
        assertNotEquals(objectDescriptor, new ObjectDescriptor("name", "x", Integer.class));
        assertNotEquals(objectDescriptor, new ObjectDescriptor("name", "description", Double.class));
        try
        {
            new ObjectDescriptor(null, "description", Integer.class);
            fail("null name should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new ObjectDescriptor("", "description", Integer.class);
            fail("empty name should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new ObjectDescriptor("name", null, Integer.class);
            fail("null description should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new ObjectDescriptor("name", "description", null);
            fail("null class should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        assertTrue("toString returns something descriptive", objectDescriptor.toString().startsWith("ObjectDescriptor"));
    }

    /**
     * Test the MetaData class.
     */
    @Test
    @SuppressWarnings("checkstyle:methodlength")
    public void testMetaData()
    {
        // Construct a MetaData object that specifies a String and a Double.
        MetaData metaData = new MetaData("meta data name", "meta data description",
                new ObjectDescriptor[] { new ObjectDescriptor("string", "the string", String.class),
                        new ObjectDescriptor("length", "the length", Double.class) });
        assertEquals("name", "meta data name", metaData.getName());
        assertEquals("description", "meta data description", metaData.getDescription());
        assertEquals("size", 2, metaData.size());
        assertEquals("name of element 0", "string", metaData.getFieldName(0));
        assertEquals("name of element 1", "length", metaData.getFieldName(1));
        assertEquals("description of element 0", "the string", metaData.getObjectDescription(0));
        assertEquals("description of element 1", "the length", metaData.getObjectDescription(1));
        assertEquals("class of element 0", String.class, metaData.getObjectClass(0));
        assertEquals("class of element 1", Double.class, metaData.getObjectClass(1));
        
        ObjectDescriptor[] descriptors = metaData.getObjectDescriptors();
        assertEquals(2, descriptors.length);
        assertEquals(new ObjectDescriptor("string", "the string", String.class), descriptors[0]);
        MetaData metaData2 = new MetaData("meta data name", "meta data description",
                new ObjectDescriptor[] { new ObjectDescriptor("string", "the string", String.class),
                        new ObjectDescriptor("length", "the length", Double.class) });
        assertEquals(metaData, metaData2);
        assertEquals(metaData.hashCode(), metaData2.hashCode());
        assertNotEquals(metaData, null);
        assertNotEquals(metaData, new Object());
        assertNotEquals(metaData, new MetaData("x", "x", new ObjectDescriptor[0]));
        assertNotEquals(metaData, new MetaData("meta data name", "x", new ObjectDescriptor[0]));
        assertNotEquals(metaData, new MetaData("meta data name", "meta data description", new ObjectDescriptor[0]));
        
        assertTrue("toString returns something descriptive", metaData.toString().startsWith("MetaData"));
        metaData.verifyComposition(new Object[] { "TestString", 123.456 });
        metaData.verifyComposition(new Object[] { null, 123.456 });
        try
        {
            metaData.verifyComposition(new Object[] { "TestString" }); // too short
            fail("Too short array should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.verifyComposition(new Object[] { "TestString", 123.456, "too many" }); // too long
            fail("Too long array should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.verifyComposition(new Object[] { 234.678, 123.456 }); // element 0 not a String
            fail("Wrong class should have thrown an ClassCastException");
        }
        catch (ClassCastException cce)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.verifyComposition(new Object[] { "TestString", "wrong class" }); // element 1 not a Double
            fail("Wrong class should have thrown an IndexOutOfBoundsException");
        }
        catch (ClassCastException cce)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getFieldName(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getFieldName(2);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectDescription(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectDescription(2);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectClass(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectClass(2);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            new MetaData(null, "description", new ObjectDescriptor[] {});
            fail("null name should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new MetaData("name", null, new ObjectDescriptor[] {});
            fail("null description should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new MetaData("name", "description", (ObjectDescriptor[]) null);
            fail("null objectDescriptors should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        try
        {
            new MetaData("name", "description", (ObjectDescriptor) null);
            fail("null objectDescriptors should have thrown a NullPointerException");
        }
        catch (NullPointerException npe)
        {
            // Ignore expected exception
        }

        metaData = new MetaData("name", "description", new ObjectDescriptor("integer", "integers only please", Integer.class));
        assertEquals("name", "name", metaData.getName());
        assertEquals("description", "description", metaData.getDescription());
        assertEquals("check name of object descriptor", "integer", metaData.getFieldName(0));
        assertEquals("check description of object descriptor", "integers only please", metaData.getObjectDescription(0));
        assertEquals("check class of object descriptor", Integer.class, metaData.getObjectClass(0));
        assertEquals("size should be 1", 1, metaData.size());
        try
        {
            metaData.getFieldName(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getFieldName(1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectDescription(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectDescription(1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectClass(-1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }

        try
        {
            metaData.getObjectClass(1);
            fail("Bad index should have thrown an IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            // Ignore expected exception
        }
        
        metaData = new MetaData("name", "description", new ObjectDescriptor[] {});
        metaData.verifyComposition(null); // this null is allowed
        metaData.verifyComposition(new Object[] {}); // empty Object array is too
        
        metaData = new MetaData("name", "description", new ObjectDescriptor("n", "d", Integer.class));
        assertEquals("name", "name", metaData.getName());
        assertEquals("description", "description", metaData.getDescription());
        assertEquals("class", Integer.class, metaData.getObjectClass(0));
        metaData.verifyComposition(123);
        try
        {
            metaData.verifyComposition("wrong");
            fail("Wrong class should have thrown a ClassCastException");
        }
        catch (ClassCastException cce)
        {
            // Ignore expected exception
        }
        
        try
        {
            metaData = new MetaData("", "description", new ObjectDescriptor[] {});
            fail("Empty name should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException cce)
        {
            // Ignore expected exception
        }
  
        try
        {
            metaData = new MetaData("", "description", new ObjectDescriptor("name", "desc", String.class));
            fail("Empty name should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException cce)
        {
            // Ignore expected exception
        }
    }

}
