package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongShortConvolutionStrategyTest
    extends LongConvolutionStrategyTestCase
    implements LongRadixConstants
{
    public LongShortConvolutionStrategyTest(String methodName)
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

        suite.addTest(new LongShortConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                        src2 = createDataStorage(new long[] { (long) 5 }),
                        src3 = createDataStorage(new long[] { (long) 1 }),
                        src9 = createDataStorage(new long[] { b1 }),
                        src99 = createDataStorage(new long[] { b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new LongShortConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 5);

            check("normal", radix, new long[] { 0, (long) 5, (long) 10, (long) 15, (long) 20 }, result);

            result = convolutionStrategy.convolute(src9, src99, 5);

            check("max", radix, new long[] { b1 - (long) 1, b1, b1, b1, (long) 1 }, result);

            result = convolutionStrategy.convolute(src3, src1, 5);

            check("one", radix, new long[] { 0, (long) 1, (long) 2, (long) 3, (long) 4 }, result);
        }
    }
}
