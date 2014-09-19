package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class IntAdditionStrategyTest
    extends IntTestCase
    implements IntRadixConstants
{
    public IntAdditionStrategyTest(String methodName)
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

        suite.addTest(new IntAdditionStrategyTest("testAdd"));
        suite.addTest(new IntAdditionStrategyTest("testSubtract"));
        suite.addTest(new IntAdditionStrategyTest("testMultiplyAdd"));
        suite.addTest(new IntAdditionStrategyTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(int[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 4);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    private static void check(String message, int[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals(message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals(message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getIntData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        DataStorage src1 = createDataStorage(new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }),
                    src2 = createDataStorage(new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }),
                    dst = createDataStorage(new int[4]);

        IntAdditionStrategy strategy = new IntAdditionStrategy(10);
        int carry = strategy.zero();

        carry = strategy.add(src1.iterator(DataStorage.READ, 0, 4),
                             src2.iterator(DataStorage.READ, 0, 4),
                             carry,
                             dst.iterator(DataStorage.WRITE, 0, 4),
                             4);

        assertEquals("carry", strategy.zero(), (Integer) carry);
        check("result", new int[] { (int) 4, (int) 6, (int) 8, (int) 10 }, dst);
    }

    public static void testSubtract()
    {
        DataStorage src1 = createDataStorage(new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }),
                    src2 = createDataStorage(new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }),
                    dst = createDataStorage(new int[4]);

        IntAdditionStrategy strategy = new IntAdditionStrategy(10);
        int carry = strategy.zero();

        carry = strategy.subtract(src1.iterator(DataStorage.READ, 0, 4),
                                  src2.iterator(DataStorage.READ, 0, 4),
                                  carry,
                                  dst.iterator(DataStorage.WRITE, 0, 4),
                                  4);

        assertEquals("carry", strategy.zero(), (Integer) carry);
        check("result", new int[] { (int) 4, (int) 4, (int) 4, (int) 4 }, dst);
    }

    public static void testMultiplyAdd()
    {
        DataStorage src1 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4 }),
                    src2 = createDataStorage(new int[] { (int) 5, (int) 6, (int) 7, (int) 8 }),
                    dst = createDataStorage(new int[4]);

        IntAdditionStrategy strategy = new IntAdditionStrategy(10);
        int carry = strategy.zero();

        carry = strategy.multiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                     src2.iterator(DataStorage.READ, 0, 4),
                                     (int) 9,
                                     carry,
                                     dst.iterator(DataStorage.WRITE, 0, 4),
                                     4);

        assertEquals("carry", strategy.zero(), (Integer) carry);
        check("result", new int[] { (int) 14, (int) 24, (int) 34, (int) 44 }, dst);
    }

    public static void testDivide()
    {
        DataStorage src1 = createDataStorage(new int[] { (int) 0, (int) 2, (int) 4, (int) 7 }),
                    dst = createDataStorage(new int[4]);

        IntAdditionStrategy strategy = new IntAdditionStrategy(10);
        int carry = strategy.zero();

        carry = strategy.divide(src1.iterator(DataStorage.READ, 0, 4),
                                (int) 2,
                                carry,
                                dst.iterator(DataStorage.WRITE, 0, 4),
                                4);

        assertEquals("carry", 1, (long) carry);
        check("result", new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }, dst);
    }
}
