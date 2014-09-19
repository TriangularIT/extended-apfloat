package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class LongDiskDataStorageTest
    extends LongDataStorageTestCase
{
    private LongDiskDataStorageTest()
    {
    }

    public LongDiskDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new LongDiskDataStorageTest().realSuite();

        suite.addTest(new LongDiskDataStorageTest("testGetPartialArray"));
        suite.addTest(new LongDiskDataStorageTest("testGetPartialArrayBig"));
        suite.addTest(new LongDiskDataStorageTest("testGetPartialArrayWide"));
        suite.addTest(new LongDiskDataStorageTest("testGetPartialArrayWideBig"));
        suite.addTest(new LongDiskDataStorageTest("testGetTransposedArray"));
        suite.addTest(new LongDiskDataStorageTest("testGetTransposedArrayBig"));
        suite.addTest(new LongDiskDataStorageTest("testGetTransposedArrayWide"));
        suite.addTest(new LongDiskDataStorageTest("testGetTransposedArrayWideBig"));
        suite.addTest(new LongDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new LongDiskDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new LongDiskDataStorage();
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
        assertFalse(new LongDiskDataStorage().isCached());
    }

    private static void runGetPartialArray(int n1, int n2, int b)
    {
        int size = n1 * n2;
        DataStorage dataStorage = new LongDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getLongData()[arrayAccess.getOffset() + i] = (long) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", n1 * b, arrayAccess.getLength());
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < b; j++)
            {
                long value = arrayAccess.getLongData()[arrayAccess.getOffset() + b * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * i + 2 * b + j + 1, (int) value);
                arrayAccess.getLongData()[arrayAccess.getOffset() + b * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                long value = arrayAccess.getLongData()[arrayAccess.getOffset() + n2 * i + j],
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
        DataStorage dataStorage = new LongDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getLongData()[arrayAccess.getOffset() + i] = (long) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getTransposedArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", b * n1, arrayAccess.getLength());
        for (int i = 0; i < b; i++)
        {
            for (int j = 0; j < n1; j++)
            {
                long value = arrayAccess.getLongData()[arrayAccess.getOffset() + n1 * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * j + 2 * b + i + 1, (int) value);
                arrayAccess.getLongData()[arrayAccess.getOffset() + n1 * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                long value = arrayAccess.getLongData()[arrayAccess.getOffset() + n2 * i + j],
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
