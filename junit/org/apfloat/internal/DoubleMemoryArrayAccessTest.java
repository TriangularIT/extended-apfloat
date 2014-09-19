package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleMemoryArrayAccessTest
    extends DoubleTestCase
{
    public DoubleMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new DoubleMemoryArrayAccessTest("testGet"));
        suite.addTest(new DoubleMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        double[] data = { (double) 1, (double) 2, (double) 3, (double) 4 };
        ArrayAccess arrayAccess = new DoubleMemoryArrayAccess(data, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 4, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3]);
        assertEquals("length", 4, arrayAccess.getLength());
    }

    public static void testSubsequence()
    {
        double[] data = { (double) 1, (double) 2, (double) 3, (double) 4 };
        ArrayAccess arrayAccess = new DoubleMemoryArrayAccess(data, 0, 4);
        arrayAccess = arrayAccess.subsequence(1, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("length", 2, arrayAccess.getLength());
    }
}
