package org.apfloat.internal;

import java.lang.reflect.Array;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class IntBuilderFactoryTest
    extends IntTestCase
{
    public IntBuilderFactoryTest(String methodName)
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

        suite.addTest(new IntBuilderFactoryTest("testBuilders"));

        return suite;
    }

    public static void testBuilders()
    {
        BuilderFactory builderFactory = new IntBuilderFactory();
        assertTrue("ApfloatBuilder", builderFactory.getApfloatBuilder() instanceof ApfloatBuilder);
        assertTrue("DataStorageBuilder", builderFactory.getDataStorageBuilder() instanceof DataStorageBuilder);
        assertTrue("AdditionBuilder", builderFactory.getAdditionBuilder(Integer.TYPE) instanceof AdditionBuilder);
        assertTrue("ConvolutionBuilder", builderFactory.getConvolutionBuilder() instanceof ConvolutionBuilder);
        assertTrue("NTTBuilder", builderFactory.getNTTBuilder() instanceof NTTBuilder);
        assertTrue("MatrixBuilder", builderFactory.getMatrixBuilder() instanceof MatrixBuilder);
        assertTrue("CarryCRTBuilder", builderFactory.getCarryCRTBuilder(int[].class) instanceof CarryCRTBuilder);

        assertEquals("getElementType()", Integer.TYPE, builderFactory.getElementType());
        assertEquals("getElementArrayType()", int[].class, builderFactory.getElementArrayType());
        assertEquals("getElementSize()", 4, builderFactory.getElementSize());

        Class<?>[] types = { Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
        for (Class<?> type : types)
        {
            if (!type.equals(Integer.TYPE))
            {
                try
                {
                    builderFactory.getAdditionBuilder(type);
                    fail("Invalid AdditonStrategy type accepted");
                }
                catch (IllegalArgumentException iae)
                {
                    // OK: should not be allowed
                }

                try
                {
                    builderFactory.getCarryCRTBuilder(Array.newInstance(type, 0).getClass());
                    fail("Invalid CarryCRTBuilder type accepted");
                }
                catch (IllegalArgumentException iae)
                {
                    // OK: should not be allowed
                }
            }
        }
    }
}
