package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class IntMemoryDataStorageTest
    extends IntDataStorageTestCase
{
    private IntMemoryDataStorageTest()
    {
    }

    public IntMemoryDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new IntMemoryDataStorageTest().realSuite();

        suite.addTest(new IntDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new IntMemoryDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new IntMemoryDataStorage();
    }

    public static void testIsCached()
    {
        assertTrue(new IntDiskDataStorage().isCached());
    }
}
