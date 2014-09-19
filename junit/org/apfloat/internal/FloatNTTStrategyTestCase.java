package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public abstract class FloatNTTStrategyTestCase
    extends FloatTestCase
    implements FloatModConstants
{
    protected FloatNTTStrategyTestCase(String methodName)
    {
        super(methodName);
    }

    protected static DataStorage createDataStorage(int size)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 4);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getFloatData()[arrayAccess.getOffset() + i] = (float) (i + 1);
        }
        arrayAccess.close();

        return dataStorage;
    }

    protected static float[] getPlainArray(DataStorage dataStorage)
    {
        int size = (int) dataStorage.getSize();
        float[] data = new float[size];
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        System.arraycopy(arrayAccess.getFloatData(), arrayAccess.getOffset(), data, 0, size);
        arrayAccess.close();

        return data;
    }

    protected static float[] ntt(float[] data, int modulus)
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[modulus]);

        float[] transform = new float[data.length];
        float w = math.getForwardNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (float) 1;

        for (int i = 0; i < data.length; i++)
        {
            float wj = (float) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static float[] inverseNtt(float[] data, int modulus)
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[modulus]);

        float[] transform = new float[data.length];
        float w = math.getInverseNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (float) 1;

        for (int i = 0; i < data.length; i++)
        {
            float wj = (float) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            transform[i] = math.modDivide(transform[i], (float) data.length);

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static void runRoundTrip(NTTStrategy nttStrategy, int size)
    {
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            nttStrategy.transform(dataStorage, modulus);

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getFloat());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("round-tripped [" + i + "]", i + 6, (long) iterator.getFloat());
                iterator.next();
            }
        }
    }
}
