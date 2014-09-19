package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class FloatParallelThreeNTTConvolutionStrategyTest
    extends FloatThreeNTTConvolutionStrategyTest
{
    public FloatParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new FloatParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
