package org.apfloat.internal;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class IntCarryCRTStrategyTest
    extends IntTestCase
    implements IntModConstants, IntRadixConstants
{
    public IntCarryCRTStrategyTest(String methodName)
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

        suite.addTest(new IntCarryCRTStrategyTest("testFullLength"));
        suite.addTest(new IntCarryCRTStrategyTest("testTruncatedLength"));
        suite.addTest(new IntCarryCRTStrategyTest("testBigFullLength"));
        suite.addTest(new IntCarryCRTStrategyTest("testBigFullLengthParallel"));

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

    private static void check(String message, int radix, int[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getIntData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testFullLength()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = BigInteger.valueOf(1).multiply(base).add(BigInteger.valueOf(2)).multiply(base).add(BigInteger.valueOf(3));

            DataStorage src0 = createDataStorage(new int[] { 0, value.mod(m0).intValue(), value.mod(m0).intValue(), 0 }),
                        src1 = createDataStorage(new int[] { 0, value.mod(m1).intValue(), value.mod(m1).intValue(), 0 }),
                        src2 = createDataStorage(new int[] { 0, value.mod(m2).intValue(), value.mod(m2).intValue(), 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 4);

            check("normal", radix, new int[] {(int) 1, (int) 3, (int) 5, (int) 3 }, dst);
        }
    }

    public static void testTruncatedLength()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = BigInteger.valueOf(1).multiply(base).add(BigInteger.valueOf(2)).multiply(base).add(BigInteger.valueOf(3));

            DataStorage src0 = createDataStorage(new int[] { 0, value.mod(m0).intValue(), (int) 1, 0 }),
                        src1 = createDataStorage(new int[] { 0, value.mod(m1).intValue(), (int) 1, 0 }),
                        src2 = createDataStorage(new int[] { 0, value.mod(m2).intValue(), (int) 1, 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 1);

            check("normal", radix, new int[] {(int) 1 }, dst);
        }
    }

    public static void testBigFullLength()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public static void testBigFullLengthParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ExecutorService executorService = ctx.getExecutorService();
        ctx.setNumberOfProcessors(4);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(executorService);
    }

    private static void runBig()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       bm1 = BigInteger.valueOf((long) BASE[radix] - 1),
                       bm2 = BigInteger.valueOf((long) BASE[radix] - 2),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = bm2.multiply(base).add(bm1).multiply(base).add(BigInteger.valueOf(1));

            final int SIZE = 500;

            int[] data0 = new int[SIZE],
                      data1 = new int[SIZE],
                      data2 = new int[SIZE],
                      expected = new int[SIZE];

            int value0 = value.mod(m0).intValue(),
                    value1 = value.mod(m1).intValue(),
                    value2 = value.mod(m2).intValue();

            for (int i = 1; i < SIZE - 1; i++)
            {
                data0[i] = value0;
                data1[i] = value1;
                data2[i] = value2;
                expected[i] = BASE[radix] - (int) 1;
            }
            expected[0] = BASE[radix] - (int) 1;
            expected[1] = BASE[radix] - (int) 2;
            expected[SIZE - 2] = 0;
            expected[SIZE - 1] = (int) 1;

            DataStorage src0 = createDataStorage(data0),
                        src1 = createDataStorage(data1),
                        src2 = createDataStorage(data2);

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, SIZE);

            check("normal", radix, expected, dst);
        }
    }
}
