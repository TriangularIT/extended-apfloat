package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntCRTMathTest
    extends IntTestCase
    implements IntModConstants
{
    public IntCRTMathTest(String methodName)
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

        suite.addTest(new IntCRTMathTest("testMultiply"));
        suite.addTest(new IntCRTMathTest("testCompare"));
        suite.addTest(new IntCRTMathTest("testAdd"));
        suite.addTest(new IntCRTMathTest("testSubtract"));
        suite.addTest(new IntCRTMathTest("testDivide"));

        return suite;
    }

    public static void testMultiply()
    {
        int b1 = MAX_POWER_OF_TWO_BASE - (int) 1;
        int[] src = { b1, b1 };
        int[] dst = new int[3];

        new IntCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new int[] { (int) 2, (int) 4 };

        new IntCRTMath(2).multiply(src, (int) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        int b1 = MAX_POWER_OF_TWO_BASE - (int) 1;
        int result = new IntCRTMath(2).compare(new int[] { (int) 1, (int) 1, (int) 1 },
                                                new int[] { (int) 2, (int) 1, (int) 1 });
        assertTrue("1st", result < 0);

        result = new IntCRTMath(2).compare(new int[] { (int) 1, (int) 2, (int) 1 },
                                                new int[] { (int) 1, (int) 1, (int) 1 });
        assertTrue("2nd", result > 0);

        result = new IntCRTMath(2).compare(new int[] { (int) 1, (int) 1, (int) 0 },
                                                new int[] { (int) 1, (int) 1, (int) b1 });
        assertTrue("3rd", result < 0);

        result = new IntCRTMath(2).compare(new int[] { (int) 1, (int) 1, (int) 1 },
                                                new int[] { (int) 1, (int) 1, (int) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        int b1 = MAX_POWER_OF_TWO_BASE - (int) 1;
        int[] src = { b1, b1, b1 };
        int[] srcDst = { b1, b1, b1 };

        int carry = new IntCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new int[] { (int) 2, (int) 4, (int) 6 };
        srcDst = new int[] { (int) 3, (int) 5, (int) 7 };

        carry = new IntCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        int b1 = MAX_POWER_OF_TWO_BASE - (int) 1;
        int[] src = { b1, b1, b1 };
        int[] srcDst = { b1, b1, b1 };

        new IntCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new int[] { 0, 0, (int) 1 };
        srcDst = new int[] { (int) 1, 0, 0 };

        new IntCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        int[] srcDst = new int[] { (int) 1, 0, 1 };

        int remainder = new IntCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }
}
