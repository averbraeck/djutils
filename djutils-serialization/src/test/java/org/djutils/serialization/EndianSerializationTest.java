package org.djutils.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * SerializationDemo.java.
 * <p>
 * Copyright (c) 2025-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EndianSerializationTest
{

    /**
     * Test decodeInt method.
     * @throws SerializationException on error
     */
    @Test
    public void testEncodeInt() throws SerializationException
    {
        int value = 1024;
        byte[] intSerBE = TypedMessage.encodeUTF8(EndianUtil.BIG_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerBE.length).map(i -> intSerBE[i] >= 0 ? intSerBE[i] : intSerBE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intBE = TypedMessage.decodeInt(intSerBE);
        assertEquals(value, intBE);

        byte[] intSerLE = TypedMessage.encodeUTF8(EndianUtil.LITTLE_ENDIAN, value);
        // System.out.println(IntStream.range(0, intSerLE.length).map(i -> intSerLE[i] >= 0 ? intSerLE[i] : intSerLE[i] + 256)
        // .boxed().collect(Collectors.toList()));
        int intLE = TypedMessage.decodeInt(intSerLE);
        assertEquals(value, intLE);

        assertNotEquals(intSerBE, intSerLE);
        assertEquals(intBE, intLE);
    }

}
