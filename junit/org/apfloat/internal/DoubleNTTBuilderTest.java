package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleNTTBuilderTest
    extends DoubleTestCase
{
    public DoubleNTTBuilderTest(String methodName)
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

        suite.addTest(new DoubleNTTBuilderTest("testCreate"));

        return suite;
    }

    public static void testCreate()
    {
        NTTBuilder nttBuilder = new DoubleNTTBuilder();
        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheSize = ctx.getCacheL1Size() / 8;
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize() / 8;

        assertTrue("Fits in cache", nttBuilder.createNTT(cacheSize / 2) instanceof DoubleTableFNTStrategy);
        assertTrue("Fits in memory", nttBuilder.createNTT(maxMemoryBlockSize) instanceof SixStepFNTStrategy);
        assertTrue("Does not fit in memory", nttBuilder.createNTT(maxMemoryBlockSize * 2) instanceof TwoPassFNTStrategy);
        assertTrue("Factor 3", nttBuilder.createNTT(3) instanceof Factor3NTTStrategy);

        assertTrue("NTTStepStrategy", nttBuilder.createNTTSteps() instanceof NTTStepStrategy);
        assertTrue("NTTConvolutionStepStrategy", nttBuilder.createNTTConvolutionSteps() instanceof NTTConvolutionStepStrategy);
        assertTrue("Factor3NTTStepStrategy", nttBuilder.createFactor3NTTSteps() instanceof Factor3NTTStepStrategy);

        ctx.setMaxMemoryBlockSize(0x80000000L * 8);
        assertTrue("Does not fit in array", nttBuilder.createNTT(0x80000000L) instanceof TwoPassFNTStrategy);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize * 8);
    }
}
