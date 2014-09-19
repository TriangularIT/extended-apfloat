package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntMediumConvolutionStrategyTest
    extends IntConvolutionStrategyTestCase
    implements IntRadixConstants
{
    public IntMediumConvolutionStrategyTest(String methodName)
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

        suite.addTest(new IntMediumConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4 }),
                        src2 = createDataStorage(new int[] { (int) 5, (int) 6 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1 }),
                        src99 = createDataStorage(new int[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new IntMediumConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new int[] { 0, (int) 5, (int) 16, (int) 27, (int) 38, (int) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new int[] { b1, b1, b1 - (int) 1, b1, b1, b1, 0, 0, (int) 1 }, result);
        }
    }
}
