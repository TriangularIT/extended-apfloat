package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class DoubleTwoPassFNTStrategyTest
    extends DoubleNTTStrategyTestCase
{
    public DoubleTwoPassFNTStrategyTest(String methodName)
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

        suite.addTest(new DoubleTwoPassFNTStrategyTest("testRoundTrip"));
        suite.addTest(new DoubleTwoPassFNTStrategyTest("testRoundTripBig"));
        suite.addTest(new DoubleTwoPassFNTStrategyTest("testRoundTripMultithread"));
        suite.addTest(new DoubleTwoPassFNTStrategyTest("testRoundTripMultithreadBig"));

        return suite;
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip(131072);
    }

    public static void testRoundTripBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(DoubleModConstants.MAX_TRANSFORM_LENGTH)));
    }

    public static void testRoundTripMultithread()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip(131072);
    }

    public static void testRoundTripMultithreadBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(DoubleModConstants.MAX_TRANSFORM_LENGTH)));
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new TwoPassFNTStrategy(), size);
    }
}
