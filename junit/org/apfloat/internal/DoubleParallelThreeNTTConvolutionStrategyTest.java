package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleParallelThreeNTTConvolutionStrategyTest
    extends DoubleThreeNTTConvolutionStrategyTest
{
    public DoubleParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new DoubleParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
