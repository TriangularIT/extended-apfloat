package org.apfloat.internal;

import java.lang.reflect.Array;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class DoubleBuilderFactoryTest
    extends DoubleTestCase
{
    public DoubleBuilderFactoryTest(String methodName)
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

        suite.addTest(new DoubleBuilderFactoryTest("testBuilders"));

        return suite;
    }

    public static void testBuilders()
    {
        BuilderFactory builderFactory = new DoubleBuilderFactory();
        assertTrue("ApfloatBuilder", builderFactory.getApfloatBuilder() instanceof ApfloatBuilder);
        assertTrue("DataStorageBuilder", builderFactory.getDataStorageBuilder() instanceof DataStorageBuilder);
        assertTrue("AdditionBuilder", builderFactory.getAdditionBuilder(Double.TYPE) instanceof AdditionBuilder);
        assertTrue("ConvolutionBuilder", builderFactory.getConvolutionBuilder() instanceof ConvolutionBuilder);
        assertTrue("NTTBuilder", builderFactory.getNTTBuilder() instanceof NTTBuilder);
        assertTrue("MatrixBuilder", builderFactory.getMatrixBuilder() instanceof MatrixBuilder);
        assertTrue("CarryCRTBuilder", builderFactory.getCarryCRTBuilder(double[].class) instanceof CarryCRTBuilder);

        assertEquals("getElementType()", Double.TYPE, builderFactory.getElementType());
        assertEquals("getElementArrayType()", double[].class, builderFactory.getElementArrayType());
        assertEquals("getElementSize()", 8, builderFactory.getElementSize());

        Class<?>[] types = { Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
        for (Class<?> type : types)
        {
            if (!type.equals(Double.TYPE))
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
