package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongMediumConvolutionStrategyTest
    extends LongConvolutionStrategyTestCase
    implements LongRadixConstants
{
    public LongMediumConvolutionStrategyTest(String methodName)
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

        suite.addTest(new LongMediumConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                        src2 = createDataStorage(new long[] { (long) 5, (long) 6 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1 }),
                        src99 = createDataStorage(new long[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new LongMediumConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new long[] { 0, (long) 5, (long) 16, (long) 27, (long) 38, (long) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new long[] { b1, b1, b1 - (long) 1, b1, b1, b1, 0, 0, (long) 1 }, result);
        }
    }
}
