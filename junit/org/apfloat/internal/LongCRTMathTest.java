package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongCRTMathTest
    extends LongTestCase
    implements LongModConstants
{
    public LongCRTMathTest(String methodName)
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

        suite.addTest(new LongCRTMathTest("testMultiply"));
        suite.addTest(new LongCRTMathTest("testCompare"));
        suite.addTest(new LongCRTMathTest("testAdd"));
        suite.addTest(new LongCRTMathTest("testSubtract"));
        suite.addTest(new LongCRTMathTest("testDivide"));

        return suite;
    }

    public static void testMultiply()
    {
        long b1 = MAX_POWER_OF_TWO_BASE - (long) 1;
        long[] src = { b1, b1 };
        long[] dst = new long[3];

        new LongCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new long[] { (long) 2, (long) 4 };

        new LongCRTMath(2).multiply(src, (long) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        long b1 = MAX_POWER_OF_TWO_BASE - (long) 1;
        long result = new LongCRTMath(2).compare(new long[] { (long) 1, (long) 1, (long) 1 },
                                                new long[] { (long) 2, (long) 1, (long) 1 });
        assertTrue("1st", result < 0);

        result = new LongCRTMath(2).compare(new long[] { (long) 1, (long) 2, (long) 1 },
                                                new long[] { (long) 1, (long) 1, (long) 1 });
        assertTrue("2nd", result > 0);

        result = new LongCRTMath(2).compare(new long[] { (long) 1, (long) 1, (long) 0 },
                                                new long[] { (long) 1, (long) 1, (long) b1 });
        assertTrue("3rd", result < 0);

        result = new LongCRTMath(2).compare(new long[] { (long) 1, (long) 1, (long) 1 },
                                                new long[] { (long) 1, (long) 1, (long) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        long b1 = MAX_POWER_OF_TWO_BASE - (long) 1;
        long[] src = { b1, b1, b1 };
        long[] srcDst = { b1, b1, b1 };

        long carry = new LongCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new long[] { (long) 2, (long) 4, (long) 6 };
        srcDst = new long[] { (long) 3, (long) 5, (long) 7 };

        carry = new LongCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        long b1 = MAX_POWER_OF_TWO_BASE - (long) 1;
        long[] src = { b1, b1, b1 };
        long[] srcDst = { b1, b1, b1 };

        new LongCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new long[] { 0, 0, (long) 1 };
        srcDst = new long[] { (long) 1, 0, 0 };

        new LongCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        long[] srcDst = new long[] { (long) 1, 0, 1 };

        long remainder = new LongCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }
}
