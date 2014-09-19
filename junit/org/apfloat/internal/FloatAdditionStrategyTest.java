package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class FloatAdditionStrategyTest
    extends FloatTestCase
    implements FloatRadixConstants
{
    public FloatAdditionStrategyTest(String methodName)
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

        suite.addTest(new FloatAdditionStrategyTest("testAdd"));
        suite.addTest(new FloatAdditionStrategyTest("testSubtract"));
        suite.addTest(new FloatAdditionStrategyTest("testMultiplyAdd"));
        suite.addTest(new FloatAdditionStrategyTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(float[] data)
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

    private static void check(String message, float[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals(message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals(message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getFloatData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        DataStorage src1 = createDataStorage(new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }),
                    src2 = createDataStorage(new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }),
                    dst = createDataStorage(new float[4]);

        FloatAdditionStrategy strategy = new FloatAdditionStrategy(10);
        float carry = strategy.zero();

        carry = strategy.add(src1.iterator(DataStorage.READ, 0, 4),
                             src2.iterator(DataStorage.READ, 0, 4),
                             carry,
                             dst.iterator(DataStorage.WRITE, 0, 4),
                             4);

        assertEquals("carry", strategy.zero(), (Float) carry);
        check("result", new float[] { (float) 4, (float) 6, (float) 8, (float) 10 }, dst);
    }

    public static void testSubtract()
    {
        DataStorage src1 = createDataStorage(new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }),
                    src2 = createDataStorage(new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }),
                    dst = createDataStorage(new float[4]);

        FloatAdditionStrategy strategy = new FloatAdditionStrategy(10);
        float carry = strategy.zero();

        carry = strategy.subtract(src1.iterator(DataStorage.READ, 0, 4),
                                  src2.iterator(DataStorage.READ, 0, 4),
                                  carry,
                                  dst.iterator(DataStorage.WRITE, 0, 4),
                                  4);

        assertEquals("carry", strategy.zero(), (Float) carry);
        check("result", new float[] { (float) 4, (float) 4, (float) 4, (float) 4 }, dst);
    }

    public static void testMultiplyAdd()
    {
        DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                    src2 = createDataStorage(new float[] { (float) 5, (float) 6, (float) 7, (float) 8 }),
                    dst = createDataStorage(new float[4]);

        FloatAdditionStrategy strategy = new FloatAdditionStrategy(10);
        float carry = strategy.zero();

        carry = strategy.multiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                     src2.iterator(DataStorage.READ, 0, 4),
                                     (float) 9,
                                     carry,
                                     dst.iterator(DataStorage.WRITE, 0, 4),
                                     4);

        assertEquals("carry", strategy.zero(), (Float) carry);
        check("result", new float[] { (float) 14, (float) 24, (float) 34, (float) 44 }, dst);
    }

    public static void testDivide()
    {
        DataStorage src1 = createDataStorage(new float[] { (float) 0, (float) 2, (float) 4, (float) 7 }),
                    dst = createDataStorage(new float[4]);

        FloatAdditionStrategy strategy = new FloatAdditionStrategy(10);
        float carry = strategy.zero();

        carry = strategy.divide(src1.iterator(DataStorage.READ, 0, 4),
                                (float) 2,
                                carry,
                                dst.iterator(DataStorage.WRITE, 0, 4),
                                4);

        assertEquals("carry", 1, (long) carry);
        check("result", new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }, dst);
    }
}
