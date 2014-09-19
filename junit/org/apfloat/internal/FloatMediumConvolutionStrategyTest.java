package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FloatMediumConvolutionStrategyTest
    extends FloatConvolutionStrategyTestCase
    implements FloatRadixConstants
{
    public FloatMediumConvolutionStrategyTest(String methodName)
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

        suite.addTest(new FloatMediumConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                        src2 = createDataStorage(new float[] { (float) 5, (float) 6 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1 }),
                        src99 = createDataStorage(new float[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new FloatMediumConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new float[] { 0, (float) 5, (float) 16, (float) 27, (float) 38, (float) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new float[] { b1, b1, b1 - (float) 1, b1, b1, b1, 0, 0, (float) 1 }, result);
        }
    }
}
