package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntMemoryArrayAccessTest
    extends IntTestCase
{
    public IntMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new IntMemoryArrayAccessTest("testGet"));
        suite.addTest(new IntMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        int[] data = { (int) 1, (int) 2, (int) 3, (int) 4 };
        ArrayAccess arrayAccess = new IntMemoryArrayAccess(data, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof int[]);
        assertEquals("[0]", 1, (int) arrayAccess.getIntData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getIntData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 3, (int) arrayAccess.getIntData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 4, (int) arrayAccess.getIntData()[arrayAccess.getOffset() + 3]);
        assertEquals("length", 4, arrayAccess.getLength());
    }

    public static void testSubsequence()
    {
        int[] data = { (int) 1, (int) 2, (int) 3, (int) 4 };
        ArrayAccess arrayAccess = new IntMemoryArrayAccess(data, 0, 4);
        arrayAccess = arrayAccess.subsequence(1, 2);

        assertTrue("class", arrayAccess.getData() instanceof int[]);
        assertEquals("[0]", 2, (int) arrayAccess.getIntData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getIntData()[arrayAccess.getOffset() + 1]);
        assertEquals("length", 2, arrayAccess.getLength());
    }
}
