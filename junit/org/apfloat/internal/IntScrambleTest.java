package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntScrambleTest
    extends IntTestCase
{
    public IntScrambleTest(String methodName)
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

        suite.addTest(new IntScrambleTest("testScramble"));

        return suite;
    }

    public static void testScramble()
    {
        int[] permutationTable = Scramble.createScrambleTable(8);
        int[] ints = { (int) -1, (int) 0, (int) 1, (int) 2, (int) 3, (int) 4, (int) 5, (int) 6, (int) 7 };
        IntScramble.scramble(ints, 1, permutationTable);
        assertEquals("[0]", 0, (int) ints[1]);
        assertEquals("[1]", 4, (int) ints[2]);
        assertEquals("[2]", 2, (int) ints[3]);
        assertEquals("[3]", 6, (int) ints[4]);
        assertEquals("[4]", 1, (int) ints[5]);
        assertEquals("[5]", 5, (int) ints[6]);
        assertEquals("[6]", 3, (int) ints[7]);
        assertEquals("[7]", 7, (int) ints[8]);
    }
}
