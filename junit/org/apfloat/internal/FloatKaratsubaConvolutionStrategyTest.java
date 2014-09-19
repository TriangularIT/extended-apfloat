package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.4
 * @author Mikko Tommila
 */

public class FloatKaratsubaConvolutionStrategyTest
    extends FloatConvolutionStrategyTestCase
    implements FloatRadixConstants
{
    public FloatKaratsubaConvolutionStrategyTest(String methodName)
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

        suite.addTest(new FloatKaratsubaConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4, (float) 5, (float) 6, (float) 7, (float) 8 }),
                        src2 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4, (float) 5, (float) 6 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 }),
                        src99 = createDataStorage(new float[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new FloatKaratsubaConvolutionStrategy(radix);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 6
            DataStorage result = convolutionStrategy.convolute(src1, src2, 14);

            check("normal", radix, new float[] { 0, (float) 1, (float) 4, (float) 10, (float) 20, (float) 35, (float) 56, (float) 77, (float) 98, (float) 110, (float) 112, (float) 103, (float) 82, (float) 48 }, result);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 18
            result = convolutionStrategy.convolute(src9, src99, 63);

            check("max", radix, new float[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 - (float) 1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (float) 1 }, result);
        }
    }
}
