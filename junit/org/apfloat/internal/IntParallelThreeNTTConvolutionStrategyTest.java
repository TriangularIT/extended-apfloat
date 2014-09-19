package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class IntParallelThreeNTTConvolutionStrategyTest
    extends IntThreeNTTConvolutionStrategyTest
{
    public IntParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new IntParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
