package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleMediumConvolutionStrategyTest
    extends DoubleConvolutionStrategyTestCase
    implements DoubleRadixConstants
{
    public DoubleMediumConvolutionStrategyTest(String methodName)
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

        suite.addTest(new DoubleMediumConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                        src2 = createDataStorage(new double[] { (double) 5, (double) 6 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1 }),
                        src99 = createDataStorage(new double[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new DoubleMediumConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new double[] { 0, (double) 5, (double) 16, (double) 27, (double) 38, (double) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new double[] { b1, b1, b1 - (double) 1, b1, b1, b1, 0, 0, (double) 1 }, result);
        }
    }
}
