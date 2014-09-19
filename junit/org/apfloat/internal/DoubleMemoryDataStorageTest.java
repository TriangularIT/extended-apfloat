package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleMemoryDataStorageTest
    extends DoubleDataStorageTestCase
{
    private DoubleMemoryDataStorageTest()
    {
    }

    public DoubleMemoryDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new DoubleMemoryDataStorageTest().realSuite();

        suite.addTest(new DoubleDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new DoubleMemoryDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new DoubleMemoryDataStorage();
    }

    public static void testIsCached()
    {
        assertTrue(new DoubleDiskDataStorage().isCached());
    }
}
