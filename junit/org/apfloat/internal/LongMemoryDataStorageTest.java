package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class LongMemoryDataStorageTest
    extends LongDataStorageTestCase
{
    private LongMemoryDataStorageTest()
    {
    }

    public LongMemoryDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new LongMemoryDataStorageTest().realSuite();

        suite.addTest(new LongDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new LongMemoryDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new LongMemoryDataStorage();
    }

    public static void testIsCached()
    {
        assertTrue(new LongDiskDataStorage().isCached());
    }
}
