package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class LongThreeNTTConvolutionStrategyTest
    extends LongConvolutionStrategyTestCase
    implements LongRadixConstants
{
    public LongThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testFullHugeParallel"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testAutoBigParallel"));
        suite.addTest(new LongThreeNTTConvolutionStrategyTest("testAutoHugeParallel"));

        return suite;
    }

    public void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                        src2 = createDataStorage(new long[] { (long) 5, (long) 6 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1 }),
                        src99 = createDataStorage(new long[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new LongTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new long[] { 0, (long) 5, (long) 16, (long) 27, (long) 38, (long) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new long[] { b1, b1, b1 - (long) 1, b1, b1, b1, 0, 0, (long) 1 }, result);
        }
    }

    public void testTruncated()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                        src2 = createDataStorage(new long[] { (long) 5, (long) 6 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1 }),
                        src99 = createDataStorage(new long[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new LongTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 3);

            check("normal", radix, new long[] { 0, (long) 5, (long) 16 }, result);

            result = convolutionStrategy.convolute(src9, src99, 6);

            check("max", radix, new long[] { b1, b1, b1 - (long) 1, b1, b1, b1 }, result);
        }
    }

    public void testAuto()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src9 = createDataStorage(new long[] { b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new LongTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 6);

            check("max", radix, new long[] { b1, b1, b1 - (long) 1, 0, 0, (long) 1 }, result);
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
            long b1 = BASE[radix] - (long) 1;
            long[] array1 = new long[size1],
                      array2 = new long[size2],
                      array3 = new long[size1 + size2];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (long) (i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
                array3[i + size1] = (long) (i == size2 - 1 ? 1 : 0);
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
            long b1 = BASE[radix] - (long) 1;
            int size1 = 500,
                size2 = 300;
            long[] array1 = new long[size1],
                      array2 = new long[size2],
                      array3 = new long[size1];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (long) (i == size1 - 1 || i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new LongTableFNTStrategy());

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
            long b1 = BASE[radix] - (long) 1;
            long[] array1 = new long[size],
                      array2 = new long[2 * size];
            for (int i = 0; i < size; i++)
            {
                array1[i] = b1;
                array2[i] = b1 - (long) (i == size - 1 ? 1 : 0);
                array2[i + size] = (long) (i == size - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new LongTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 2 * size);

            check("max", radix, array2, result);
        }
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ThreeNTTConvolutionStrategy(radix, transform);
    }
}
