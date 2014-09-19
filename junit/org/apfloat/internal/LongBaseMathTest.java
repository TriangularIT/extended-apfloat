package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongBaseMathTest
    extends LongTestCase
    implements LongRadixConstants
{
    public LongBaseMathTest(String methodName)
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

        suite.addTest(new LongBaseMathTest("testAdd"));
        suite.addTest(new LongBaseMathTest("testSubtract"));
        suite.addTest(new LongBaseMathTest("testMultiplyAdd"));
        suite.addTest(new LongBaseMathTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(long[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * 8);
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    private static void check(String message, int radix, long[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getLongData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }),
                        src2 = createDataStorage(new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new long[4]);

            LongBaseMath math = new LongBaseMath(radix);
            long carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new long[] { (long) 4, (long) 6, (long) 8, (long) 10 }, dst);

            carry = (long) 1;

            carry = math.baseAdd(src9.iterator(DataStorage.READ, 0, 4),
                                 src9.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new long[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src2 carry", 0, (long) carry);
            check("src2", radix, new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " nulls carry", 0, (long) carry);
            check("nulls", radix, new long[] { 0, 0, 0, 0 }, dst);
        }
    }

    public static void testSubtract()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }),
                        src2 = createDataStorage(new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new long[4]);

            LongBaseMath math = new LongBaseMath(radix);
            long carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      src2.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new long[] { (long) 4, (long) 4, (long) 4, (long) 4 }, dst);

            carry = (long) 1;

            carry = math.baseSubtract(src9.iterator(DataStorage.READ, 0, 4),
                                      src9.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new long[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new long[] { (long) 4, (long) 5, (long) 6, (long) 7 }, dst);

            carry = 0;

            carry = math.baseSubtract(null,
                                      src2.iterator(DataStorage.READ, 4, 0),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 4, 0),
                                      4);

            assertEquals("radix " + radix + " src2 carry", 1, (long) carry);
            check("src2", radix, new long[] { BASE[radix] - (long) 1, BASE[radix] - (long) 2, BASE[radix] - (long) 3, BASE[radix] - (long) 3 }, dst);

            carry = 1;

            carry = math.baseSubtract(null,
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " nulls carry", 1, (long) carry);
            check("nulls", radix, new long[] { b1, b1, b1, b1 }, dst);
        }
    }

    public static void testMultiplyAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 1, (long) 2, (long) 3, (long) 4 }),
                        src2 = createDataStorage(new long[] { (long) 5, (long) 6, (long) 7, (long) 8 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new long[4]);

            LongBaseMath math = new LongBaseMath(radix);
            long carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         src2.iterator(DataStorage.READ, 0, 4),
                                         (long) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new long[] { (long) 14, (long) 24, (long) 34, (long) 44 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         src9.iterator(DataStorage.READ, 4, 0),
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max2 carry", (long) b1, (long) carry);
            check("max2", radix, new long[] { b1, b1, b1, 0 }, dst);

            carry = b1;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         null,
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max1 carry", (long) b1, (long) carry);
            check("max1", radix, new long[] { 0, 0, 0, 0 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         null,
                                         (long) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new long[] { (long) 9, (long) 18, (long) 27, (long) 36 }, dst);
        }
    }

    public static void testDivide()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            long b1 = BASE[radix] - (long) 1;
            DataStorage src1 = createDataStorage(new long[] { (long) 0, (long) 2, (long) 4, (long) 7 }),
                        src9 = createDataStorage(new long[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new long[4]);

            LongBaseMath math = new LongBaseMath(radix);
            long carry = 0;

            carry = math.baseDivide(src1.iterator(DataStorage.READ, 0, 4),
                                    (long) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " both carry", 1, (long) carry);
            check("both", radix, new long[] { (long) 0, (long) 1, (long) 2, (long) 3 }, dst);

            carry = b1 - (long) 1;

            carry = math.baseDivide(src9.iterator(DataStorage.READ, 0, 4),
                                    b1,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " max carry", (long) b1 - 1, (long) carry);
            check("max", radix, new long[] { b1, b1, b1, b1 }, dst);

            carry = (long) 1;

            carry = math.baseDivide(null,
                                    (long) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            if ((radix & 1) == 0)
            {
                assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
                check("src1", radix, new long[] { BASE[radix] / (long) 2, 0, 0, 0 }, dst);
            }
            else
            {
                assertEquals("radix " + radix + " src1 carry", 1, (long) carry);
                check("src1", radix, new long[] { b1 / (long) 2, b1 / (long) 2, b1 / (long) 2, b1 / (long) 2 }, dst);
            }
        }
    }
}
