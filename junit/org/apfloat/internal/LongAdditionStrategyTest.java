package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class LongAdditionStrategyTest
    extends LongTestCase
    implements LongRadixConstants
{
    public LongAdditionStrategyTest(String methodName)
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

        suite.addTest(new LongAdditionStrategyTest("testAdd"));
        suite.addTest(new LongAdditionStrategyTest("testSubtract"));
        suite.addTest(new LongAdditionStrategyTest("testMultiplyAdd"));
        suite.addTest(new LongAdditionStrategyTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(long[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 8);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    private static void check(String message, long[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals(message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals(message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getLongData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        DataStorage src1 = createDataStorage(new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }),
                    src2 = createDataStorage(new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }),
                    dst = createDataStorage(new long[4]);

        LongAdditionStrategy strategy = new LongAdditionStrategy(10);
        long carry = strategy.zero();

        carry = strategy.add(src1.iterator(DataStorage.READ, 0, 4),
                             src2.iterator(DataStorage.READ, 0, 4),
                             carry,
                             dst.iterator(DataStorage.WRITE, 0, 4),
                             4);

        assertEquals("carry", strategy.zero(), (Long) carry);
        check("result", new long[] { (long) 4, (long) 6, (long) 8, (long) 10 }, dst);
    }

    public static void testSubtract()
    {
        DataStorage src1 = createDataStorage(new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }),
                    src2 = createDataStorage(new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }),
                    dst = createDataStorage(new long[4]);

        LongAdditionStrategy strategy = new LongAdditionStrategy(10);
        long carry = strategy.zero();

        carry = strategy.subtract(src1.iterator(DataStorage.READ, 0, 4),
                                  src2.iterator(DataStorage.READ, 0, 4),
                                  carry,
                                  dst.iterator(DataStorage.WRITE, 0, 4),
                                  4);

        assertEquals("carry", strategy.zero(), (Long) carry);
        check("result", new long[] { (long) 4, (long) 4, (long) 4, (long) 4 }, dst);
    }

    public static void testMultiplyAdd()
    {
        DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                    src2 = createDataStorage(new long[] { (long) 5, (long) 6, (long) 7, (long) 8 }),
                    dst = createDataStorage(new long[4]);

        LongAdditionStrategy strategy = new LongAdditionStrategy(10);
        long carry = strategy.zero();

        carry = strategy.multiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                     src2.iterator(DataStorage.READ, 0, 4),
                                     (long) 9,
                                     carry,
                                     dst.iterator(DataStorage.WRITE, 0, 4),
                                     4);

        assertEquals("carry", strategy.zero(), (Long) carry);
        check("result", new long[] { (long) 14, (long) 24, (long) 34, (long) 44 }, dst);
    }

    public static void testDivide()
    {
        DataStorage src1 = createDataStorage(new long[] { (long) 0, (long) 2, (long) 4, (long) 7 }),
                    dst = createDataStorage(new long[4]);

        LongAdditionStrategy strategy = new LongAdditionStrategy(10);
        long carry = strategy.zero();

        carry = strategy.divide(src1.iterator(DataStorage.READ, 0, 4),
                                (long) 2,
                                carry,
                                dst.iterator(DataStorage.WRITE, 0, 4),
                                4);

        assertEquals("carry", 1, (long) carry);
        check("result", new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }, dst);
    }
}
