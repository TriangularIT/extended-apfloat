package org.apfloat.internal;

import java.util.Arrays;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntTableFNTStrategyTest
    extends IntNTTStrategyTestCase
{
    public IntTableFNTStrategyTest(String methodName)
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

        suite.addTest(new IntTableFNTStrategyTest("testForward"));
        suite.addTest(new IntTableFNTStrategyTest("testInverse"));
        suite.addTest(new IntTableFNTStrategyTest("testRoundTrip"));

        return suite;
    }

    public static void testForward()
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 8192;        // Good for memory treshold of 64kB
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            int[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            IntScramble.scramble(expectedTransform, 0, Scramble.createScrambleTable(size));
            //Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new IntTableFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            int[] actualTransform = getPlainArray(dataStorage);
            //Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testInverse()
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 8192;        // Good for memory treshold of 64kB
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            int[] data = getPlainArray(dataStorage);
            IntScramble.scramble(data, 0, Scramble.createScrambleTable(size));
            int[] expectedTransform = inverseNtt(data, modulus);
            Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new IntTableFNTStrategy();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            int[] actualTransform = getPlainArray(dataStorage);
            Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testRoundTrip()
    {
        int size = 8192;        // Good for memory treshold of 64kB
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            NTTStrategy nttStrategy = new IntTableFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getInt());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getInt());
                iterator.next();
            }
        }
    }
}
