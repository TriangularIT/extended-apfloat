package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FloatCRTMathTest
    extends FloatTestCase
    implements FloatModConstants
{
    public FloatCRTMathTest(String methodName)
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

        suite.addTest(new FloatCRTMathTest("testMultiply"));
        suite.addTest(new FloatCRTMathTest("testCompare"));
        suite.addTest(new FloatCRTMathTest("testAdd"));
        suite.addTest(new FloatCRTMathTest("testSubtract"));
        suite.addTest(new FloatCRTMathTest("testDivide"));

        return suite;
    }

    public static void testMultiply()
    {
        float b1 = MAX_POWER_OF_TWO_BASE - (float) 1;
        float[] src = { b1, b1 };
        float[] dst = new float[3];

        new FloatCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new float[] { (float) 2, (float) 4 };

        new FloatCRTMath(2).multiply(src, (float) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        float b1 = MAX_POWER_OF_TWO_BASE - (float) 1;
        float result = new FloatCRTMath(2).compare(new float[] { (float) 1, (float) 1, (float) 1 },
                                                new float[] { (float) 2, (float) 1, (float) 1 });
        assertTrue("1st", result < 0);

        result = new FloatCRTMath(2).compare(new float[] { (float) 1, (float) 2, (float) 1 },
                                                new float[] { (float) 1, (float) 1, (float) 1 });
        assertTrue("2nd", result > 0);

        result = new FloatCRTMath(2).compare(new float[] { (float) 1, (float) 1, (float) 0 },
                                                new float[] { (float) 1, (float) 1, (float) b1 });
        assertTrue("3rd", result < 0);

        result = new FloatCRTMath(2).compare(new float[] { (float) 1, (float) 1, (float) 1 },
                                                new float[] { (float) 1, (float) 1, (float) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        float b1 = MAX_POWER_OF_TWO_BASE - (float) 1;
        float[] src = { b1, b1, b1 };
        float[] srcDst = { b1, b1, b1 };

        float carry = new FloatCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new float[] { (float) 2, (float) 4, (float) 6 };
        srcDst = new float[] { (float) 3, (float) 5, (float) 7 };

        carry = new FloatCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        float b1 = MAX_POWER_OF_TWO_BASE - (float) 1;
        float[] src = { b1, b1, b1 };
        float[] srcDst = { b1, b1, b1 };

        new FloatCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new float[] { 0, 0, (float) 1 };
        srcDst = new float[] { (float) 1, 0, 0 };

        new FloatCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        float[] srcDst = new float[] { (float) 1, 0, 1 };

        float remainder = new FloatCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }
}
