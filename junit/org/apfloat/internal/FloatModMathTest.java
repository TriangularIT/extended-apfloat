package org.apfloat.internal;

import java.util.Random;

import junit.framework.TestSuite;

/**
 * @version 1.0.1
 * @author Mikko Tommila
 */

public class FloatModMathTest
    extends FloatTestCase
    implements FloatModConstants
{
    public FloatModMathTest(String methodName)
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

        suite.addTest(new FloatModMathTest("testCreateWTable"));
        suite.addTest(new FloatModMathTest("testGetForwardNthRoot"));
        suite.addTest(new FloatModMathTest("testGetInverseNthRoot"));
        suite.addTest(new FloatModMathTest("testInverse"));
        suite.addTest(new FloatModMathTest("testDivide"));
        suite.addTest(new FloatModMathTest("testNegate"));
        suite.addTest(new FloatModMathTest("testPow"));

        return suite;
    }

    public static void testCreateWTable()
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[0]);
        float[] wTable = math.createWTable((float) 2, 5);

        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", 2, (long) wTable[1]);
        assertEquals("[2]", 4, (long) wTable[2]);
        assertEquals("[3]", 8, (long) wTable[3]);
        assertEquals("[4]", 16, (long) wTable[4]);
    }

    public static void testGetForwardNthRoot()
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[0]);
        float w = math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
    }

    public static void testGetInverseNthRoot()
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[0]);
        float w = math.getInverseNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
        assertTrue("inverse vs. forward", math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4) != w);
    }

    public static void testInverse()
    {
        FloatModMath math = new FloatModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x;

            x = 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((float) x), (float) x));

            x = lm - 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((float) x), (float) x));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((float) x), (float) x));
            }
        }
    }

    public static void testDivide()
    {
        FloatModMath math = new FloatModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x, y;

            x = 0;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            x = 0;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            x = 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            x = lm - 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            x = 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            x = lm - 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;
                y = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((float) x, (float) y), (float) y));
            }
        }
    }

    public static void testNegate()
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("zero", 0, (long) math.negate(0));
        assertEquals("non-zero", (long) MODULUS[0] - 1, (long) math.negate((float) 1));
    }

    public static void testPow()
    {
        FloatModMath math = new FloatModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", 3125, (long) math.modPow((float) 5, (float) 5));
        assertEquals("overflow", ((long) MODULUS[0] + 1) / 2, (long) math.modPow((float) 2, (float) -1));
    }
}
