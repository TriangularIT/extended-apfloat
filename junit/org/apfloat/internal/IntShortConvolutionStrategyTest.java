package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntShortConvolutionStrategyTest
    extends IntConvolutionStrategyTestCase
    implements IntRadixConstants
{
    public IntShortConvolutionStrategyTest(String methodName)
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

        suite.addTest(new IntShortConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4 }),
                        src2 = createDataStorage(new int[] { (int) 5 }),
                        src3 = createDataStorage(new int[] { (int) 1 }),
                        src9 = createDataStorage(new int[] { b1 }),
                        src99 = createDataStorage(new int[] { b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new IntShortConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 5);

            check("normal", radix, new int[] { 0, (int) 5, (int) 10, (int) 15, (int) 20 }, result);

            result = convolutionStrategy.convolute(src9, src99, 5);

            check("max", radix, new int[] { b1 - (int) 1, b1, b1, b1, (int) 1 }, result);

            result = convolutionStrategy.convolute(src3, src1, 5);

            check("one", radix, new int[] { 0, (int) 1, (int) 2, (int) 3, (int) 4 }, result);
        }
    }
}
