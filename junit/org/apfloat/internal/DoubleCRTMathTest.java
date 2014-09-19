package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleCRTMathTest
    extends DoubleTestCase
    implements DoubleModConstants
{
    public DoubleCRTMathTest(String methodName)
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

        suite.addTest(new DoubleCRTMathTest("testMultiply"));
        suite.addTest(new DoubleCRTMathTest("testCompare"));
        suite.addTest(new DoubleCRTMathTest("testAdd"));
        suite.addTest(new DoubleCRTMathTest("testSubtract"));
        suite.addTest(new DoubleCRTMathTest("testDivide"));

        return suite;
    }

    public static void testMultiply()
    {
        double b1 = MAX_POWER_OF_TWO_BASE - (double) 1;
        double[] src = { b1, b1 };
        double[] dst = new double[3];

        new DoubleCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new double[] { (double) 2, (double) 4 };

        new DoubleCRTMath(2).multiply(src, (double) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        double b1 = MAX_POWER_OF_TWO_BASE - (double) 1;
        double result = new DoubleCRTMath(2).compare(new double[] { (double) 1, (double) 1, (double) 1 },
                                                new double[] { (double) 2, (double) 1, (double) 1 });
        assertTrue("1st", result < 0);

        result = new DoubleCRTMath(2).compare(new double[] { (double) 1, (double) 2, (double) 1 },
                                                new double[] { (double) 1, (double) 1, (double) 1 });
        assertTrue("2nd", result > 0);

        result = new DoubleCRTMath(2).compare(new double[] { (double) 1, (double) 1, (double) 0 },
                                                new double[] { (double) 1, (double) 1, (double) b1 });
        assertTrue("3rd", result < 0);

        result = new DoubleCRTMath(2).compare(new double[] { (double) 1, (double) 1, (double) 1 },
                                                new double[] { (double) 1, (double) 1, (double) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        double b1 = MAX_POWER_OF_TWO_BASE - (double) 1;
        double[] src = { b1, b1, b1 };
        double[] srcDst = { b1, b1, b1 };

        double carry = new DoubleCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new double[] { (double) 2, (double) 4, (double) 6 };
        srcDst = new double[] { (double) 3, (double) 5, (double) 7 };

        carry = new DoubleCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        double b1 = MAX_POWER_OF_TWO_BASE - (double) 1;
        double[] src = { b1, b1, b1 };
        double[] srcDst = { b1, b1, b1 };

        new DoubleCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new double[] { 0, 0, (double) 1 };
        srcDst = new double[] { (double) 1, 0, 0 };

        new DoubleCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        double[] srcDst = new double[] { (double) 1, 0, 1 };

        double remainder = new DoubleCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }
}
