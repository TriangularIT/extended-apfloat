package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleAdditionStrategyTest
    extends DoubleTestCase
    implements DoubleRadixConstants
{
    public DoubleAdditionStrategyTest(String methodName)
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

        suite.addTest(new DoubleAdditionStrategyTest("testAdd"));
        suite.addTest(new DoubleAdditionStrategyTest("testSubtract"));
        suite.addTest(new DoubleAdditionStrategyTest("testMultiplyAdd"));
        suite.addTest(new DoubleAdditionStrategyTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(double[] data)
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

    private static void check(String message, double[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals(message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals(message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getDoubleData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        DataStorage src1 = createDataStorage(new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }),
                    src2 = createDataStorage(new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }),
                    dst = createDataStorage(new double[4]);

        DoubleAdditionStrategy strategy = new DoubleAdditionStrategy(10);
        double carry = strategy.zero();

        carry = strategy.add(src1.iterator(DataStorage.READ, 0, 4),
                             src2.iterator(DataStorage.READ, 0, 4),
                             carry,
                             dst.iterator(DataStorage.WRITE, 0, 4),
                             4);

        assertEquals("carry", strategy.zero(), (Double) carry);
        check("result", new double[] { (double) 4, (double) 6, (double) 8, (double) 10 }, dst);
    }

    public static void testSubtract()
    {
        DataStorage src1 = createDataStorage(new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }),
                    src2 = createDataStorage(new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }),
                    dst = createDataStorage(new double[4]);

        DoubleAdditionStrategy strategy = new DoubleAdditionStrategy(10);
        double carry = strategy.zero();

        carry = strategy.subtract(src1.iterator(DataStorage.READ, 0, 4),
                                  src2.iterator(DataStorage.READ, 0, 4),
                                  carry,
                                  dst.iterator(DataStorage.WRITE, 0, 4),
                                  4);

        assertEquals("carry", strategy.zero(), (Double) carry);
        check("result", new double[] { (double) 4, (double) 4, (double) 4, (double) 4 }, dst);
    }

    public static void testMultiplyAdd()
    {
        DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                    src2 = createDataStorage(new double[] { (double) 5, (double) 6, (double) 7, (double) 8 }),
                    dst = createDataStorage(new double[4]);

        DoubleAdditionStrategy strategy = new DoubleAdditionStrategy(10);
        double carry = strategy.zero();

        carry = strategy.multiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                     src2.iterator(DataStorage.READ, 0, 4),
                                     (double) 9,
                                     carry,
                                     dst.iterator(DataStorage.WRITE, 0, 4),
                                     4);

        assertEquals("carry", strategy.zero(), (Double) carry);
        check("result", new double[] { (double) 14, (double) 24, (double) 34, (double) 44 }, dst);
    }

    public static void testDivide()
    {
        DataStorage src1 = createDataStorage(new double[] { (double) 0, (double) 2, (double) 4, (double) 7 }),
                    dst = createDataStorage(new double[4]);

        DoubleAdditionStrategy strategy = new DoubleAdditionStrategy(10);
        double carry = strategy.zero();

        carry = strategy.divide(src1.iterator(DataStorage.READ, 0, 4),
                                (double) 2,
                                carry,
                                dst.iterator(DataStorage.WRITE, 0, 4),
                                4);

        assertEquals("carry", 1, (long) carry);
        check("result", new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }, dst);
    }
}
