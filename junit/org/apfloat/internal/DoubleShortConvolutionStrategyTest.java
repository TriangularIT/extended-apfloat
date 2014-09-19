package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleShortConvolutionStrategyTest
    extends DoubleConvolutionStrategyTestCase
    implements DoubleRadixConstants
{
    public DoubleShortConvolutionStrategyTest(String methodName)
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

        suite.addTest(new DoubleShortConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                        src2 = createDataStorage(new double[] { (double) 5 }),
                        src3 = createDataStorage(new double[] { (double) 1 }),
                        src9 = createDataStorage(new double[] { b1 }),
                        src99 = createDataStorage(new double[] { b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new DoubleShortConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 5);

            check("normal", radix, new double[] { 0, (double) 5, (double) 10, (double) 15, (double) 20 }, result);

            result = convolutionStrategy.convolute(src9, src99, 5);

            check("max", radix, new double[] { b1 - (double) 1, b1, b1, b1, (double) 1 }, result);

            result = convolutionStrategy.convolute(src3, src1, 5);

            check("one", radix, new double[] { 0, (double) 1, (double) 2, (double) 3, (double) 4 }, result);
        }
    }
}
