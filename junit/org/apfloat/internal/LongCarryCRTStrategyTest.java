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

public class LongCarryCRTStrategyTest
    extends LongTestCase
    implements LongModConstants, LongRadixConstants
{
    public LongCarryCRTStrategyTest(String methodName)
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

        suite.addTest(new LongCarryCRTStrategyTest("testFullLength"));
        suite.addTest(new LongCarryCRTStrategyTest("testTruncatedLength"));
        suite.addTest(new LongCarryCRTStrategyTest("testBigFullLength"));
        suite.addTest(new LongCarryCRTStrategyTest("testBigFullLengthParallel"));

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

    private static void check(String message, int radix, long[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getLongData()[arrayAccess.getOffset() + i]);
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

            DataStorage src0 = createDataStorage(new long[] { 0, value.mod(m0).longValue(), value.mod(m0).longValue(), 0 }),
                        src1 = createDataStorage(new long[] { 0, value.mod(m1).longValue(), value.mod(m1).longValue(), 0 }),
                        src2 = createDataStorage(new long[] { 0, value.mod(m2).longValue(), value.mod(m2).longValue(), 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 4);

            check("normal", radix, new long[] {(long) 1, (long) 3, (long) 5, (long) 3 }, dst);
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

            DataStorage src0 = createDataStorage(new long[] { 0, value.mod(m0).longValue(), (long) 1, 0 }),
                        src1 = createDataStorage(new long[] { 0, value.mod(m1).longValue(), (long) 1, 0 }),
                        src2 = createDataStorage(new long[] { 0, value.mod(m2).longValue(), (long) 1, 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 1);

            check("normal", radix, new long[] {(long) 1 }, dst);
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

            long[] data0 = new long[SIZE],
                      data1 = new long[SIZE],
                      data2 = new long[SIZE],
                      expected = new long[SIZE];

            long value0 = value.mod(m0).longValue(),
                    value1 = value.mod(m1).longValue(),
                    value2 = value.mod(m2).longValue();

            for (int i = 1; i < SIZE - 1; i++)
            {
                data0[i] = value0;
                data1[i] = value1;
                data2[i] = value2;
                expected[i] = BASE[radix] - (long) 1;
            }
            expected[0] = BASE[radix] - (long) 1;
            expected[1] = BASE[radix] - (long) 2;
            expected[SIZE - 2] = 0;
            expected[SIZE - 1] = (long) 1;

            DataStorage src0 = createDataStorage(data0),
                        src1 = createDataStorage(data1),
                        src2 = createDataStorage(data2);

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, SIZE);

            check("normal", radix, expected, dst);
        }
    }
}
