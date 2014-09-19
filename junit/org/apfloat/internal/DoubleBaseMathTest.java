package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleBaseMathTest
    extends DoubleTestCase
    implements DoubleRadixConstants
{
    public DoubleBaseMathTest(String methodName)
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

        suite.addTest(new DoubleBaseMathTest("testAdd"));
        suite.addTest(new DoubleBaseMathTest("testSubtract"));
        suite.addTest(new DoubleBaseMathTest("testMultiplyAdd"));
        suite.addTest(new DoubleBaseMathTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(double[] data)
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

    private static void check(String message, int radix, double[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getDoubleData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }),
                        src2 = createDataStorage(new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new double[4]);

            DoubleBaseMath math = new DoubleBaseMath(radix);
            double carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new double[] { (double) 4, (double) 6, (double) 8, (double) 10 }, dst);

            carry = (double) 1;

            carry = math.baseAdd(src9.iterator(DataStorage.READ, 0, 4),
                                 src9.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new double[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src2 carry", 0, (long) carry);
            check("src2", radix, new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " nulls carry", 0, (long) carry);
            check("nulls", radix, new double[] { 0, 0, 0, 0 }, dst);
        }
    }

    public static void testSubtract()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }),
                        src2 = createDataStorage(new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new double[4]);

            DoubleBaseMath math = new DoubleBaseMath(radix);
            double carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      src2.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new double[] { (double) 4, (double) 4, (double) 4, (double) 4 }, dst);

            carry = (double) 1;

            carry = math.baseSubtract(src9.iterator(DataStorage.READ, 0, 4),
                                      src9.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new double[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new double[] { (double) 4, (double) 5, (double) 6, (double) 7 }, dst);

            carry = 0;

            carry = math.baseSubtract(null,
                                      src2.iterator(DataStorage.READ, 4, 0),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 4, 0),
                                      4);

            assertEquals("radix " + radix + " src2 carry", 1, (long) carry);
            check("src2", radix, new double[] { BASE[radix] - (double) 1, BASE[radix] - (double) 2, BASE[radix] - (double) 3, BASE[radix] - (double) 3 }, dst);

            carry = 1;

            carry = math.baseSubtract(null,
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " nulls carry", 1, (long) carry);
            check("nulls", radix, new double[] { b1, b1, b1, b1 }, dst);
        }
    }

    public static void testMultiplyAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 1, (double) 2, (double) 3, (double) 4 }),
                        src2 = createDataStorage(new double[] { (double) 5, (double) 6, (double) 7, (double) 8 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new double[4]);

            DoubleBaseMath math = new DoubleBaseMath(radix);
            double carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         src2.iterator(DataStorage.READ, 0, 4),
                                         (double) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new double[] { (double) 14, (double) 24, (double) 34, (double) 44 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         src9.iterator(DataStorage.READ, 4, 0),
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max2 carry", (long) b1, (long) carry);
            check("max2", radix, new double[] { b1, b1, b1, 0 }, dst);

            carry = b1;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         null,
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max1 carry", (long) b1, (long) carry);
            check("max1", radix, new double[] { 0, 0, 0, 0 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         null,
                                         (double) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new double[] { (double) 9, (double) 18, (double) 27, (double) 36 }, dst);
        }
    }

    public static void testDivide()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            double b1 = BASE[radix] - (double) 1;
            DataStorage src1 = createDataStorage(new double[] { (double) 0, (double) 2, (double) 4, (double) 7 }),
                        src9 = createDataStorage(new double[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new double[4]);

            DoubleBaseMath math = new DoubleBaseMath(radix);
            double carry = 0;

            carry = math.baseDivide(src1.iterator(DataStorage.READ, 0, 4),
                                    (double) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " both carry", 1, (long) carry);
            check("both", radix, new double[] { (double) 0, (double) 1, (double) 2, (double) 3 }, dst);

            carry = b1 - (double) 1;

            carry = math.baseDivide(src9.iterator(DataStorage.READ, 0, 4),
                                    b1,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " max carry", (long) b1 - 1, (long) carry);
            check("max", radix, new double[] { b1, b1, b1, b1 }, dst);

            carry = (double) 1;

            carry = math.baseDivide(null,
                                    (double) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            if ((radix & 1) == 0)
            {
                assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
                check("src1", radix, new double[] { BASE[radix] / (double) 2, 0, 0, 0 }, dst);
            }
            else
            {
                assertEquals("radix " + radix + " src1 carry", 1, (long) carry);
                check("src1", radix, new double[] { b1 / (double) 2, b1 / (double) 2, b1 / (double) 2, b1 / (double) 2 }, dst);
            }
        }
    }
}
