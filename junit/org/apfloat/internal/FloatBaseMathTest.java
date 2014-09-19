package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FloatBaseMathTest
    extends FloatTestCase
    implements FloatRadixConstants
{
    public FloatBaseMathTest(String methodName)
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

        suite.addTest(new FloatBaseMathTest("testAdd"));
        suite.addTest(new FloatBaseMathTest("testSubtract"));
        suite.addTest(new FloatBaseMathTest("testMultiplyAdd"));
        suite.addTest(new FloatBaseMathTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(float[] data)
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

    private static void check(String message, int radix, float[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getFloatData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public static void testAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }),
                        src2 = createDataStorage(new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new float[4]);

            FloatBaseMath math = new FloatBaseMath(radix);
            float carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new float[] { (float) 4, (float) 6, (float) 8, (float) 10 }, dst);

            carry = (float) 1;

            carry = math.baseAdd(src9.iterator(DataStorage.READ, 0, 4),
                                 src9.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new float[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src2 carry", 0, (long) carry);
            check("src2", radix, new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " nulls carry", 0, (long) carry);
            check("nulls", radix, new float[] { 0, 0, 0, 0 }, dst);
        }
    }

    public static void testSubtract()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }),
                        src2 = createDataStorage(new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new float[4]);

            FloatBaseMath math = new FloatBaseMath(radix);
            float carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      src2.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new float[] { (float) 4, (float) 4, (float) 4, (float) 4 }, dst);

            carry = (float) 1;

            carry = math.baseSubtract(src9.iterator(DataStorage.READ, 0, 4),
                                      src9.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new float[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new float[] { (float) 4, (float) 5, (float) 6, (float) 7 }, dst);

            carry = 0;

            carry = math.baseSubtract(null,
                                      src2.iterator(DataStorage.READ, 4, 0),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 4, 0),
                                      4);

            assertEquals("radix " + radix + " src2 carry", 1, (long) carry);
            check("src2", radix, new float[] { BASE[radix] - (float) 1, BASE[radix] - (float) 2, BASE[radix] - (float) 3, BASE[radix] - (float) 3 }, dst);

            carry = 1;

            carry = math.baseSubtract(null,
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " nulls carry", 1, (long) carry);
            check("nulls", radix, new float[] { b1, b1, b1, b1 }, dst);
        }
    }

    public static void testMultiplyAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 1, (float) 2, (float) 3, (float) 4 }),
                        src2 = createDataStorage(new float[] { (float) 5, (float) 6, (float) 7, (float) 8 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new float[4]);

            FloatBaseMath math = new FloatBaseMath(radix);
            float carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         src2.iterator(DataStorage.READ, 0, 4),
                                         (float) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new float[] { (float) 14, (float) 24, (float) 34, (float) 44 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         src9.iterator(DataStorage.READ, 4, 0),
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max2 carry", (long) b1, (long) carry);
            check("max2", radix, new float[] { b1, b1, b1, 0 }, dst);

            carry = b1;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         null,
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max1 carry", (long) b1, (long) carry);
            check("max1", radix, new float[] { 0, 0, 0, 0 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         null,
                                         (float) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new float[] { (float) 9, (float) 18, (float) 27, (float) 36 }, dst);
        }
    }

    public static void testDivide()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            float b1 = BASE[radix] - (float) 1;
            DataStorage src1 = createDataStorage(new float[] { (float) 0, (float) 2, (float) 4, (float) 7 }),
                        src9 = createDataStorage(new float[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new float[4]);

            FloatBaseMath math = new FloatBaseMath(radix);
            float carry = 0;

            carry = math.baseDivide(src1.iterator(DataStorage.READ, 0, 4),
                                    (float) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " both carry", 1, (long) carry);
            check("both", radix, new float[] { (float) 0, (float) 1, (float) 2, (float) 3 }, dst);

            carry = b1 - (float) 1;

            carry = math.baseDivide(src9.iterator(DataStorage.READ, 0, 4),
                                    b1,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " max carry", (long) b1 - 1, (long) carry);
            check("max", radix, new float[] { b1, b1, b1, b1 }, dst);

            carry = (float) 1;

            carry = math.baseDivide(null,
                                    (float) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            if ((radix & 1) == 0)
            {
                assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
                check("src1", radix, new float[] { BASE[radix] / (float) 2, 0, 0, 0 }, dst);
            }
            else
            {
                assertEquals("radix " + radix + " src1 carry", 1, (long) carry);
                check("src1", radix, new float[] { b1 / (float) 2, b1 / (float) 2, b1 / (float) 2, b1 / (float) 2 }, dst);
            }
        }
    }
}
