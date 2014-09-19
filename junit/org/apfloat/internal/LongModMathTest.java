package org.apfloat.internal;

import java.util.Random;

import junit.framework.TestSuite;

/**
 * @version 1.0.1
 * @author Mikko Tommila
 */

public class LongModMathTest
    extends LongTestCase
    implements LongModConstants
{
    public LongModMathTest(String methodName)
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

        suite.addTest(new LongModMathTest("testCreateWTable"));
        suite.addTest(new LongModMathTest("testGetForwardNthRoot"));
        suite.addTest(new LongModMathTest("testGetInverseNthRoot"));
        suite.addTest(new LongModMathTest("testInverse"));
        suite.addTest(new LongModMathTest("testDivide"));
        suite.addTest(new LongModMathTest("testNegate"));
        suite.addTest(new LongModMathTest("testPow"));

        return suite;
    }

    public static void testCreateWTable()
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[0]);
        long[] wTable = math.createWTable((long) 2, 5);

        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", 2, (long) wTable[1]);
        assertEquals("[2]", 4, (long) wTable[2]);
        assertEquals("[3]", 8, (long) wTable[3]);
        assertEquals("[4]", 16, (long) wTable[4]);
    }

    public static void testGetForwardNthRoot()
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[0]);
        long w = math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
    }

    public static void testGetInverseNthRoot()
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[0]);
        long w = math.getInverseNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
        assertTrue("inverse vs. forward", math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4) != w);
    }

    public static void testInverse()
    {
        LongModMath math = new LongModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x;

            x = 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((long) x), (long) x));

            x = lm - 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((long) x), (long) x));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((long) x), (long) x));
            }
        }
    }

    public static void testDivide()
    {
        LongModMath math = new LongModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x, y;

            x = 0;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            x = 0;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            x = 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            x = lm - 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            x = 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            x = lm - 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;
                y = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((long) x, (long) y), (long) y));
            }
        }
    }

    public static void testNegate()
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("zero", 0, (long) math.negate(0));
        assertEquals("non-zero", (long) MODULUS[0] - 1, (long) math.negate((long) 1));
    }

    public static void testPow()
    {
        LongModMath math = new LongModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", 3125, (long) math.modPow((long) 5, (long) 5));
        assertEquals("overflow", ((long) MODULUS[0] + 1) / 2, (long) math.modPow((long) 2, (long) -1));
    }
}
