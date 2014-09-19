package org.apfloat.internal;

import java.util.Properties;

import org.apfloat.*;

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class IntTestCase
    extends TestCase
{
    public IntTestCase()
    {
    }

    public IntTestCase(String methodName)
    {
        super(methodName);
    }

    protected void setUp()
    {
        Properties properties = new Properties();
        properties.setProperty(ApfloatContext.BUILDER_FACTORY, "org.apfloat.internal.IntBuilderFactory");

        ApfloatContext.getContext().setProperties(properties);
    }
}
