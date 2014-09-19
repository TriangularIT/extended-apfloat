package org.apfloat.internal;

import java.util.Properties;

import org.apfloat.*;

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class DoubleTestCase
    extends TestCase
{
    public DoubleTestCase()
    {
    }

    public DoubleTestCase(String methodName)
    {
        super(methodName);
    }

    protected void setUp()
    {
        Properties properties = new Properties();
        properties.setProperty(ApfloatContext.BUILDER_FACTORY, "org.apfloat.internal.DoubleBuilderFactory");

        ApfloatContext.getContext().setProperties(properties);
    }
}
