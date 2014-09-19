package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public abstract class IntNTTStrategyTestCase
    extends IntTestCase
    implements IntModConstants
{
    protected IntNTTStrategyTestCase(String methodName)
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
            arrayAccess.getIntData()[arrayAccess.getOffset() + i] = (int) (i + 1);
        }
        arrayAccess.close();

        return dataStorage;
    }

    protected static int[] getPlainArray(DataStorage dataStorage)
    {
        int size = (int) dataStorage.getSize();
        int[] data = new int[size];
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        System.arraycopy(arrayAccess.getIntData(), arrayAccess.getOffset(), data, 0, size);
        arrayAccess.close();

        return data;
    }

    protected static int[] ntt(int[] data, int modulus)
    {
        IntModMath math = new IntModMath();
        math.setModulus(MODULUS[modulus]);

        int[] transform = new int[data.length];
        int w = math.getForwardNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (int) 1;

        for (int i = 0; i < data.length; i++)
        {
            int wj = (int) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static int[] inverseNtt(int[] data, int modulus)
    {
        IntModMath math = new IntModMath();
        math.setModulus(MODULUS[modulus]);

        int[] transform = new int[data.length];
        int w = math.getInverseNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (int) 1;

        for (int i = 0; i < data.length; i++)
        {
            int wj = (int) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            transform[i] = math.modDivide(transform[i], (int) data.length);

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

            assertTrue("transformed [0]", 6 != (long) iterator.getInt());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("round-tripped [" + i + "]", i + 6, (long) iterator.getInt());
                iterator.next();
            }
        }
    }
}
