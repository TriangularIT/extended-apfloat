package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.4
 * @author Mikko Tommila
 */

public class IntKaratsubaConvolutionStrategyTest
    extends IntConvolutionStrategyTestCase
    implements IntRadixConstants
{
    public IntKaratsubaConvolutionStrategyTest(String methodName)
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

        suite.addTest(new IntKaratsubaConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4, (int) 5, (int) 6, (int) 7, (int) 8 }),
                        src2 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4, (int) 5, (int) 6 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 }),
                        src99 = createDataStorage(new int[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new IntKaratsubaConvolutionStrategy(radix);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 6
            DataStorage result = convolutionStrategy.convolute(src1, src2, 14);

            check("normal", radix, new int[] { 0, (int) 1, (int) 4, (int) 10, (int) 20, (int) 35, (int) 56, (int) 77, (int) 98, (int) 110, (int) 112, (int) 103, (int) 82, (int) 48 }, result);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 18
            result = convolutionStrategy.convolute(src9, src99, 63);

            check("max", radix, new int[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 - (int) 1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (int) 1 }, result);
        }
    }
}
