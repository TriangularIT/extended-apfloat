package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongScrambleTest
    extends LongTestCase
{
    public LongScrambleTest(String methodName)
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

        suite.addTest(new LongScrambleTest("testScramble"));

        return suite;
    }

    public static void testScramble()
    {
        int[] permutationTable = Scramble.createScrambleTable(8);
        long[] ints = { (long) -1, (long) 0, (long) 1, (long) 2, (long) 3, (long) 4, (long) 5, (long) 6, (long) 7 };
        LongScramble.scramble(ints, 1, permutationTable);
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
