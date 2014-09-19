package org.apfloat.internal;

import java.util.Arrays;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class LongSixStepFNTStrategyTest
    extends LongNTTStrategyTestCase
{
    public LongSixStepFNTStrategyTest(String methodName)
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

        suite.addTest(new LongSixStepFNTStrategyTest("testForward"));
        suite.addTest(new LongSixStepFNTStrategyTest("testForwardBig"));
        suite.addTest(new LongSixStepFNTStrategyTest("testRoundTrip"));
        suite.addTest(new LongSixStepFNTStrategyTest("testRoundTripBig"));
        suite.addTest(new LongSixStepFNTStrategyTest("testRoundTripMultithread"));
        suite.addTest(new LongSixStepFNTStrategyTest("testRoundTripMultithreadBig"));

        return suite;
    }

    public static void testForward()
    {
        runTestForward(1024);
    }

    public static void testForwardBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMemoryThreshold(8192);
        runTestForward(4096);
    }

    private static void runTestForward(int size)
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            long[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            LongScramble.scramble(expectedTransform, 0, Scramble.createScrambleTable(size));
            Arrays.sort(expectedTransform);

            AbstractStepFNTStrategy nttStrategy = new SixStepFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            long[] actualTransform = getPlainArray(dataStorage);
            Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(1);
        runRoundTrip(1024);
    }

    public static void testRoundTripBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(1);
        int size = (int) Math.min(1 << 21, LongModConstants.MAX_TRANSFORM_LENGTH & -LongModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    public static void testRoundTripMultithread()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip(1024);
    }

    public static void testRoundTripMultithreadBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        int size = (int) Math.min(1 << 21, LongModConstants.MAX_TRANSFORM_LENGTH & -LongModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new SixStepFNTStrategy(), size);
    }
}
