package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleThreeNTTConvolutionStrategyTest
    extends DoubleConvolutionStrategyTestCase
    implements DoubleRadixConstants
{
    public DoubleThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testFullHugeParallel"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testAutoBigParallel"));
        suite.addTest(new DoubleThreeNTTConvolutionStrategyTest("testAutoHugeParallel"));

        return suite;
    }

    public void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                        src2 = createDataStorage(new double[] { (double) 5, (double) 6 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1 }),
                        src99 = createDataStorage(new double[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new DoubleTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new double[] { 0, (double) 5, (double) 16, (double) 27, (double) 38, (double) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new double[] { b1, b1, b1 - (double) 1, b1, b1, b1, 0, 0, (double) 1 }, result);
        }
    }

    public void testTruncated()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                        src2 = createDataStorage(new double[] { (double) 5, (double) 6 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1 }),
                        src99 = createDataStorage(new double[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new DoubleTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 3);

            check("normal", radix, new double[] { 0, (double) 5, (double) 16 }, result);

            result = convolutionStrategy.convolute(src9, src99, 6);

            check("max", radix, new double[] { b1, b1, b1 - (double) 1, b1, b1, b1 }, result);
        }
    }

    public void testAuto()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src9 = createDataStorage(new double[] { b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new DoubleTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 6);

            check("max", radix, new double[] { b1, b1, b1 - (double) 1, 0, 0, (double) 1 }, result);
        }
    }

    public void testFullBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testFullBigParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(4);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testFullHugeParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize();
        ctx.setNumberOfProcessors(4);
        ctx.setMaxMemoryBlockSize(65536);

        runBig(20000, 12000);

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize);
    }


    private void runBig()
    {
        runBig(500, 300);
    }

    private void runBig(int size1, int size2)
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            double[] array1 = new double[size1],
                      array2 = new double[size2],
                      array3 = new double[size1 + size2];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (double) (i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
                array3[i + size1] = (double) (i == size2 - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new SixStepFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src99, size1 + size2);

            check("max", radix, array3, result);
        }
    }

    public void testTruncatedBig()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            int size1 = 500,
                size2 = 300;
            double[] array1 = new double[size1],
                      array2 = new double[size2],
                      array3 = new double[size1];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (double) (i == size1 - 1 || i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new DoubleTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src99, src9, size1);

            check("max", radix, array3, result);
        }
    }

    public void testAutoBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runAutoBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testAutoBigParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(4);

        runAutoBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testAutoHugeParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize();
        ctx.setNumberOfProcessors(4);
        ctx.setMaxMemoryBlockSize(65536);

        runAutoBig(20000);

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize);
    }

    private void runAutoBig()
    {
        runAutoBig(500);
    }

    private void runAutoBig(int size)
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            double[] array1 = new double[size],
                      array2 = new double[2 * size];
            for (int i = 0; i < size; i++)
            {
                array1[i] = b1;
                array2[i] = b1 - (double) (i == size - 1 ? 1 : 0);
                array2[i + size] = (double) (i == size - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new DoubleTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 2 * size);

            check("max", radix, array2, result);
        }
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ThreeNTTConvolutionStrategy(radix, transform);
    }
}
