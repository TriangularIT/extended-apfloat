package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleDiskDataStorageTest
    extends DoubleDataStorageTestCase
{
    private DoubleDiskDataStorageTest()
    {
    }

    public DoubleDiskDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new DoubleDiskDataStorageTest().realSuite();

        suite.addTest(new DoubleDiskDataStorageTest("testGetPartialArray"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetPartialArrayBig"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetPartialArrayWide"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetPartialArrayWideBig"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetTransposedArray"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetTransposedArrayBig"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetTransposedArrayWide"));
        suite.addTest(new DoubleDiskDataStorageTest("testGetTransposedArrayWideBig"));
        suite.addTest(new DoubleDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new DoubleDiskDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new DoubleDiskDataStorage();
    }

    public static void testGetPartialArray()
    {
        runGetPartialArray(64, 128, 8);
    }

    public static void testGetPartialArrayBig()
    {
        runGetPartialArray(1024, 2048, 32);
    }

    public static void testGetPartialArrayWide()
    {
        runGetPartialArray(8, 64, 16);
    }

    public static void testGetPartialArrayWideBig()
    {
        runGetPartialArray(32, 2048, 128);
    }

    public static void testGetTransposedArray()
    {
        runGetTransposedArray(64, 128, 8);
    }

    public static void testGetTransposedArrayBig()
    {
        runGetTransposedArray(1024, 2048, 32);
    }

    public static void testGetTransposedArrayWide()
    {
        runGetTransposedArray(8, 64, 16);
    }

    public static void testGetTransposedArrayWideBig()
    {
        runGetTransposedArray(32, 2048, 128);
    }

    public static void testIsCached()
    {
        assertFalse(new DoubleDiskDataStorage().isCached());
    }

    private static void runGetPartialArray(int n1, int n2, int b)
    {
        int size = n1 * n2;
        DataStorage dataStorage = new DoubleDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getDoubleData()[arrayAccess.getOffset() + i] = (double) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", n1 * b, arrayAccess.getLength());
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < b; j++)
            {
                double value = arrayAccess.getDoubleData()[arrayAccess.getOffset() + b * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * i + 2 * b + j + 1, (int) value);
                arrayAccess.getDoubleData()[arrayAccess.getOffset() + b * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                double value = arrayAccess.getDoubleData()[arrayAccess.getOffset() + n2 * i + j],
                        expectedValue = n2 * i + j + 1;
                if (j >= 2 * b && j < 3 * b)
                {
                    assertEquals("[" + i + "][" + j + "]", (int) -expectedValue, (int) value);
                }
                else
                {
                    assertEquals("[" + i + "][" + j + "]", (int) expectedValue, (int) value);
                }
            }
        }
        arrayAccess.close();
    }

    private static void runGetTransposedArray(int n1, int n2, int b)
    {
        int size = n1 * n2;
        DataStorage dataStorage = new DoubleDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getDoubleData()[arrayAccess.getOffset() + i] = (double) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getTransposedArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", b * n1, arrayAccess.getLength());
        for (int i = 0; i < b; i++)
        {
            for (int j = 0; j < n1; j++)
            {
                double value = arrayAccess.getDoubleData()[arrayAccess.getOffset() + n1 * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * j + 2 * b + i + 1, (int) value);
                arrayAccess.getDoubleData()[arrayAccess.getOffset() + n1 * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                double value = arrayAccess.getDoubleData()[arrayAccess.getOffset() + n2 * i + j],
                        expectedValue = n2 * i + j + 1;
                if (j >= 2 * b && j < 3 * b)
                {
                    assertEquals("[" + i + "][" + j + "]", (int) -expectedValue, (int) value);
                }
                else
                {
                    assertEquals("[" + i + "][" + j + "]", (int) expectedValue, (int) value);
                }
            }
        }
        arrayAccess.close();
    }
}
