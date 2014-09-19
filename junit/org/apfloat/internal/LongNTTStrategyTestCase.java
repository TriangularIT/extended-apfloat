package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public abstract class LongNTTStrategyTestCase
    extends LongTestCase
    implements LongModConstants
{
    protected LongNTTStrategyTestCase(String methodName)
    {
        super(methodName);
    }

    protected static DataStorage createDataStorage(int size)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 8);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getLongData()[arrayAccess.getOffset() + i] = (long) (i + 1);
        }
        arrayAccess.close();

        return dataStorage;
    }

    protected static long[] getPlainArray(DataStorage dataStorage)
    {
        int size = (int) dataStorage.getSize();
        long[] data = new long[size];
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        System.arraycopy(arrayAccess.getLongData(), arrayAccess.getOffset(), data, 0, size);
        arrayAccess.close();

        return data;
    }

    protected static long[] ntt(long[] data, int modulus)
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[modulus]);

        long[] transform = new long[data.length];
        long w = math.getForwardNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (long) 1;

        for (int i = 0; i < data.length; i++)
        {
            long wj = (long) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static long[] inverseNtt(long[] data, int modulus)
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[modulus]);

        long[] transform = new long[data.length];
        long w = math.getInverseNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (long) 1;

        for (int i = 0; i < data.length; i++)
        {
            long wj = (long) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            transform[i] = math.modDivide(transform[i], (long) data.length);

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

            assertTrue("transformed [0]", 6 != (long) iterator.getLong());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("round-tripped [" + i + "]", i + 6, (long) iterator.getLong());
                iterator.next();
            }
        }
    }
}
