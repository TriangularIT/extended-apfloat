package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.4
 * @author Mikko Tommila
 */

public class LongKaratsubaConvolutionStrategyTest
    extends LongConvolutionStrategyTestCase
    implements LongRadixConstants
{
    public LongKaratsubaConvolutionStrategyTest(String methodName)
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

        suite.addTest(new LongKaratsubaConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4, (long) 5, (long) 6, (long) 7, (long) 8 }),
                        src2 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4, (long) 5, (long) 6 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 }),
                        src99 = createDataStorage(new long[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new LongKaratsubaConvolutionStrategy(radix);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 6
            DataStorage result = convolutionStrategy.convolute(src1, src2, 14);

            check("normal", radix, new long[] { 0, (long) 1, (long) 4, (long) 10, (long) 20, (long) 35, (long) 56, (long) 77, (long) 98, (long) 110, (long) 112, (long) 103, (long) 82, (long) 48 }, result);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 18
            result = convolutionStrategy.convolute(src9, src99, 63);

            check("max", radix, new long[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 - (long) 1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (long) 1 }, result);
        }
    }
}
