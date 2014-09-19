package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class LongParallelThreeNTTConvolutionStrategyTest
    extends LongThreeNTTConvolutionStrategyTest
{
    public LongParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new LongParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
