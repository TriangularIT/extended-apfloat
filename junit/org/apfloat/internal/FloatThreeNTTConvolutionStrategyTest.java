package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class FloatThreeNTTConvolutionStrategyTest
    extends FloatConvolutionStrategyTestCase
    implements FloatRadixConstants
{
    public FloatThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testFullHugeParallel"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testAutoBigParallel"));
        suite.addTest(new FloatThreeNTTConvolutionStrategyTest("testAutoHugeParallel"));

        return suite;
    }

    public void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                        src2 = createDataStorage(new float[] { (float) 5, (float) 6 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1 }),
                        src99 = createDataStorage(new float[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new FloatTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new float[] { 0, (float) 5, (float) 16, (float) 27, (float) 38, (float) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new float[] { b1, b1, b1 - (float) 1, b1, b1, b1, 0, 0, (float) 1 }, result);
        }
    }

    public void testTruncated()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                        src2 = createDataStorage(new float[] { (float) 5, (float) 6 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1 }),
                        src99 = createDataStorage(new float[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new FloatTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 3);

            check("normal", radix, new float[] { 0, (float) 5, (float) 16 }, result);

            result = convolutionStrategy.convolute(src9, src99, 6);

            check("max", radix, new float[] { b1, b1, b1 - (float) 1, b1, b1, b1 }, result);
        }
    }

    public void testAuto()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src9 = createDataStorage(new float[] { b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new FloatTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 6);

            check("max", radix, new float[] { b1, b1, b1 - (float) 1, 0, 0, (float) 1 }, result);
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
            float b1 = BASE[radix] - (float) 1;
            float[] array1 = new float[size1],
                      array2 = new float[size2],
                      array3 = new float[size1 + size2];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (float) (i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
                array3[i + size1] = (float) (i == size2 - 1 ? 1 : 0);
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
            float b1 = BASE[radix] - (float) 1;
            int size1 = 500,
                size2 = 300;
            float[] array1 = new float[size1],
                      array2 = new float[size2],
                      array3 = new float[size1];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (float) (i == size1 - 1 || i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new FloatTableFNTStrategy());

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
            float b1 = BASE[radix] - (float) 1;
            float[] array1 = new float[size],
                      array2 = new float[2 * size];
            for (int i = 0; i < size; i++)
            {
                array1[i] = b1;
                array2[i] = b1 - (float) (i == size - 1 ? 1 : 0);
                array2[i + size] = (float) (i == size - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new FloatTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 2 * size);

            check("max", radix, array2, result);
        }
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ThreeNTTConvolutionStrategy(radix, transform);
    }
}
