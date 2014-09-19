package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

public abstract class IntConvolutionStrategyTestCase
    extends IntTestCase
    implements IntRadixConstants
{
    protected IntConvolutionStrategyTestCase(String methodName)
    {
        super(methodName);
    }

    protected static DataStorage createDataStorage(int[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 4);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    protected static void check(String message, int radix, int[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getIntData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }
}
