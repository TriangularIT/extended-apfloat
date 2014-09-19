package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongMemoryArrayAccessTest
    extends LongTestCase
{
    public LongMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new LongMemoryArrayAccessTest("testGet"));
        suite.addTest(new LongMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        long[] data = { (long) 1, (long) 2, (long) 3, (long) 4 };
        ArrayAccess arrayAccess = new LongMemoryArrayAccess(data, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof long[]);
        assertEquals("[0]", 1, (int) arrayAccess.getLongData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getLongData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 3, (int) arrayAccess.getLongData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 4, (int) arrayAccess.getLongData()[arrayAccess.getOffset() + 3]);
        assertEquals("length", 4, arrayAccess.getLength());
    }

    public static void testSubsequence()
    {
        long[] data = { (long) 1, (long) 2, (long) 3, (long) 4 };
        ArrayAccess arrayAccess = new LongMemoryArrayAccess(data, 0, 4);
        arrayAccess = arrayAccess.subsequence(1, 2);

        assertTrue("class", arrayAccess.getData() instanceof long[]);
        assertEquals("[0]", 2, (int) arrayAccess.getLongData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getLongData()[arrayAccess.getOffset() + 1]);
        assertEquals("length", 2, arrayAccess.getLength());
    }
}
