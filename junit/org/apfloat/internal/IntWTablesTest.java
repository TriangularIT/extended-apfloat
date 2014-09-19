package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class IntWTablesTest
    extends IntTestCase
    implements IntModConstants
{
    public IntWTablesTest(String methodName)
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

        suite.addTest(new IntWTablesTest("testGetWTable"));
        suite.addTest(new IntWTablesTest("testGetInverseWTable"));

        return suite;
    }

    public static void testGetWTable()
    {
        int[] wTable = IntWTables.getWTable(0, 4);

        IntModMath math = new IntModMath();
        math.setModulus(MODULUS[0]);
        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[1], 2));
        assertEquals("[2]", (long) MODULUS[0] - 1, (long) wTable[2]);
        assertEquals("[3]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[3], 2));
    }

    public static void testGetInverseWTable()
    {
        int[] wTable = IntWTables.getInverseWTable(0, 4);

        IntModMath math = new IntModMath();
        math.setModulus(MODULUS[0]);
        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[1], 2));
        assertEquals("[2]", (long) MODULUS[0] - 1, (long) wTable[2]);
        assertEquals("[3]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[3], 2));
    }
}
