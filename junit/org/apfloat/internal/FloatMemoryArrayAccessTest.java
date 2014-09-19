package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FloatMemoryArrayAccessTest
    extends FloatTestCase
{
    public FloatMemoryArrayAccessTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new FloatMemoryArrayAccessTest("testGet"));
        suite.addTest(new FloatMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        float[] data = { (float) 1, (float) 2, (float) 3, (float) 4 };
        ArrayAccess arrayAccess = new FloatMemoryArrayAccess(data, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof float[]);
        assertEquals("[0]", 1, (int) arrayAccess.getFloatData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getFloatData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 3, (int) arrayAccess.getFloatData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 4, (int) arrayAccess.getFloatData()[arrayAccess.getOffset() + 3]);
        assertEquals("length", 4, arrayAccess.getLength());
    }

    public static void testSubsequence()
    {
        float[] data = { (float) 1, (float) 2, (float) 3, (float) 4 };
        ArrayAccess arrayAccess = new FloatMemoryArrayAccess(data, 0, 4);
        arrayAccess = arrayAccess.subsequence(1, 2);

        assertTrue("class", arrayAccess.getData() instanceof float[]);
        assertEquals("[0]", 2, (int) arrayAccess.getFloatData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getFloatData()[arrayAccess.getOffset() + 1]);
        assertEquals("length", 2, arrayAccess.getLength());
    }
}
