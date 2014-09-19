package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntBaseMathTest
    extends IntTestCase
    implements IntRadixConstants
{
    public IntBaseMathTest(String methodName)
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

        suite.addTest(new IntBaseMathTest("testAdd"));
        suite.addTest(new IntBaseMathTest("testSubtract"));
        suite.addTest(new IntBaseMathTest("testMultiplyAdd"));
        suite.addTest(new IntBaseMathTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(int[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 4);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    private static void check(String message, int radix, int[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getIntData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }),
                        src2 = createDataStorage(new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new int[4]);

            IntBaseMath math = new IntBaseMath(radix);
            int carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new int[] { (int) 4, (int) 6, (int) 8, (int) 10 }, dst);

            carry = (int) 1;

            carry = math.baseAdd(src9.iterator(DataStorage.READ, 0, 4),
                                 src9.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new int[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src2 carry", 0, (long) carry);
            check("src2", radix, new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " nulls carry", 0, (long) carry);
            check("nulls", radix, new int[] { 0, 0, 0, 0 }, dst);
        }
    }

    public static void testSubtract()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }),
                        src2 = createDataStorage(new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new int[4]);

            IntBaseMath math = new IntBaseMath(radix);
            int carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      src2.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new int[] { (int) 4, (int) 4, (int) 4, (int) 4 }, dst);

            carry = (int) 1;

            carry = math.baseSubtract(src9.iterator(DataStorage.READ, 0, 4),
                                      src9.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new int[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new int[] { (int) 4, (int) 5, (int) 6, (int) 7 }, dst);

            carry = 0;

            carry = math.baseSubtract(null,
                                      src2.iterator(DataStorage.READ, 4, 0),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 4, 0),
                                      4);

            assertEquals("radix " + radix + " src2 carry", 1, (long) carry);
            check("src2", radix, new int[] { BASE[radix] - (int) 1, BASE[radix] - (int) 2, BASE[radix] - (int) 3, BASE[radix] - (int) 3 }, dst);

            carry = 1;

            carry = math.baseSubtract(null,
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " nulls carry", 1, (long) carry);
            check("nulls", radix, new int[] { b1, b1, b1, b1 }, dst);
        }
    }

    public static void testMultiplyAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 1, (int) 2, (int) 3, (int) 4 }),
                        src2 = createDataStorage(new int[] { (int) 5, (int) 6, (int) 7, (int) 8 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new int[4]);

            IntBaseMath math = new IntBaseMath(radix);
            int carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         src2.iterator(DataStorage.READ, 0, 4),
                                         (int) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new int[] { (int) 14, (int) 24, (int) 34, (int) 44 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         src9.iterator(DataStorage.READ, 4, 0),
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max2 carry", (long) b1, (long) carry);
            check("max2", radix, new int[] { b1, b1, b1, 0 }, dst);

            carry = b1;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         null,
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max1 carry", (long) b1, (long) carry);
            check("max1", radix, new int[] { 0, 0, 0, 0 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         null,
                                         (int) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new int[] { (int) 9, (int) 18, (int) 27, (int) 36 }, dst);
        }
    }

    public static void testDivide()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            int b1 = BASE[radix] - (int) 1;
            DataStorage src1 = createDataStorage(new int[] { (int) 0, (int) 2, (int) 4, (int) 7 }),
                        src9 = createDataStorage(new int[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new int[4]);

            IntBaseMath math = new IntBaseMath(radix);
            int carry = 0;

            carry = math.baseDivide(src1.iterator(DataStorage.READ, 0, 4),
                                    (int) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " both carry", 1, (long) carry);
            check("both", radix, new int[] { (int) 0, (int) 1, (int) 2, (int) 3 }, dst);

            carry = b1 - (int) 1;

            carry = math.baseDivide(src9.iterator(DataStorage.READ, 0, 4),
                                    b1,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " max carry", (long) b1 - 1, (long) carry);
            check("max", radix, new int[] { b1, b1, b1, b1 }, dst);

            carry = (int) 1;

            carry = math.baseDivide(null,
                                    (int) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            if ((radix & 1) == 0)
            {
                assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
                check("src1", radix, new int[] { BASE[radix] / (int) 2, 0, 0, 0 }, dst);
            }
            else
            {
                assertEquals("radix " + radix + " src1 carry", 1, (long) carry);
                check("src1", radix, new int[] { b1 / (int) 2, b1 / (int) 2, b1 / (int) 2, b1 / (int) 2 }, dst);
            }
        }
    }
}
