package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class FloatMemoryDataStorageTest
    extends FloatDataStorageTestCase
{
    private FloatMemoryDataStorageTest()
    {
    }

    public FloatMemoryDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new FloatMemoryDataStorageTest().realSuite();

        suite.addTest(new FloatDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new FloatMemoryDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new FloatMemoryDataStorage();
    }

    public static void testIsCached()
    {
        assertTrue(new FloatDiskDataStorage().isCached());
    }
}
