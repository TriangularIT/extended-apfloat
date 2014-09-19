package org.apfloat.internal;

import java.util.Random;
import java.math.BigInteger;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntElementaryModMathTest
    extends IntTestCase
    implements IntModConstants
{
    public IntElementaryModMathTest(String methodName)
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

        suite.addTest(new IntElementaryModMathTest("testMultiply"));
        suite.addTest(new IntElementaryModMathTest("testAdd"));
        suite.addTest(new IntElementaryModMathTest("testSubtract"));

        return suite;
    }

    public static void testMultiply()
    {
        IntElementaryModMath math = new IntElementaryModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            BigInteger m = BigInteger.valueOf((long) MODULUS[modulus]);
            long lm = (long) MODULUS[modulus];

            runOneMultiply(math, 0, 0, m);
            runOneMultiply(math, 1, 0, m);
            runOneMultiply(math, 0, 1, m);
            runOneMultiply(math, 0, lm - 1, m);
            runOneMultiply(math, lm - 1, 0, m);
            runOneMultiply(math, 1, lm - 1, m);
            runOneMultiply(math, lm - 1, 1, m);
            runOneMultiply(math, lm - 1, lm - 1, m);

            for (int i = 0; i < 1000; i++)
            {
                long x = Math.abs(random.nextLong()) % lm,
                     y = Math.abs(random.nextLong()) % lm;

                runOneMultiply(math, x, y, m);
                runOneMultiply(math, x, lm - 1, m);
                runOneMultiply(math, lm - 1, x, m);
            }
        }
    }

    private static void runOneMultiply(IntElementaryModMath math, long x, long y, BigInteger m)
    {
        long r = (long) math.modMultiply((int) x, (int) y);

        BigInteger xTrue = BigInteger.valueOf(x),
                   yTrue = BigInteger.valueOf(y),
                   rTrue = xTrue.multiply(yTrue).mod(m);

        assertEquals(x + " * " + y + " % " + m, rTrue.longValue(), r);
    }

    public static void testAdd()
    {
        IntElementaryModMath math = new IntElementaryModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", (long) MODULUS[0] - 5, (long) math.modAdd(MODULUS[0] - (int) 8, (int) 3));
        assertEquals("just no overflow", (long) MODULUS[0] - 1, (long) math.modAdd(MODULUS[0] - (int) 4, (int) 3));
        assertEquals("just overflow", 0, (long) math.modAdd(MODULUS[0] - (int) 3, (int) 3));
        assertEquals("overflow", 5, (long) math.modAdd(MODULUS[0] - (int) 3, (int) 8));
    }

    public static void testSubtract()
    {
        IntElementaryModMath math = new IntElementaryModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", 5, (long) math.modSubtract((int) 8, (int) 3));
        assertEquals("just no overflow", 0, (long) math.modSubtract((int) 3, (int) 3));
        assertEquals("just overflow", (long) MODULUS[0] - 1, (long) math.modSubtract((int) 3, (int) 4));
        assertEquals("overflow", (long) MODULUS[0] - 5, (long) math.modSubtract((int) 3, (int) 8));
    }
}
