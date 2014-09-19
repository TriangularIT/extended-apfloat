package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FloatShortConvolutionStrategyTest
    extends FloatConvolutionStrategyTestCase
    implements FloatRadixConstants
{
    public FloatShortConvolutionStrategyTest(String methodName)
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

        suite.addTest(new FloatShortConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                        src2 = createDataStorage(new float[] { (float) 5 }),
                        src3 = createDataStorage(new float[] { (float) 1 }),
                        src9 = createDataStorage(new float[] { b1 }),
                        src99 = createDataStorage(new float[] { b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new FloatShortConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 5);

            check("normal", radix, new float[] { 0, (float) 5, (float) 10, (float) 15, (float) 20 }, result);

            result = convolutionStrategy.convolute(src9, src99, 5);

            check("max", radix, new float[] { b1 - (float) 1, b1, b1, b1, (float) 1 }, result);

            result = convolutionStrategy.convolute(src3, src1, 5);

            check("one", radix, new float[] { 0, (float) 1, (float) 2, (float) 3, (float) 4 }, result);
        }
    }
}
