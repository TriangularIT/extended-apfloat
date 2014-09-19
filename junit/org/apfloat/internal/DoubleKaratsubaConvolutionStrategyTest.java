package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.4
 * @author Mikko Tommila
 */

public class DoubleKaratsubaConvolutionStrategyTest
    extends DoubleConvolutionStrategyTestCase
    implements DoubleRadixConstants
{
    public DoubleKaratsubaConvolutionStrategyTest(String methodName)
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

        suite.addTest(new DoubleKaratsubaConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4, (double) 5, (double) 6, (double) 7, (double) 8 }),
                        src2 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4, (double) 5, (double) 6 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 }),
                        src99 = createDataStorage(new double[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new DoubleKaratsubaConvolutionStrategy(radix);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 6
            DataStorage result = convolutionStrategy.convolute(src1, src2, 14);

            check("normal", radix, new double[] { 0, (double) 1, (double) 4, (double) 10, (double) 20, (double) 35, (double) 56, (double) 77, (double) 98, (double) 110, (double) 112, (double) 103, (double) 82, (double) 48 }, result);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 18
            result = convolutionStrategy.convolute(src9, src99, 63);

            check("max", radix, new double[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 - (double) 1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (double) 1 }, result);
        }
    }
}
