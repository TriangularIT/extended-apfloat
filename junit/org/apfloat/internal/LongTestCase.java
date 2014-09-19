package org.apfloat.internal;

import java.util.Properties;

import org.apfloat.*;

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class LongTestCase
    extends TestCase
{
    public LongTestCase()
    {
    }

    public LongTestCase(String methodName)
    {
        super(methodName);
    }

    protected void setUp()
    {
        Properties properties = new Properties();
        properties.setProperty(ApfloatContext.BUILDER_FACTORY, "org.apfloat.internal.LongBuilderFactory");

        ApfloatContext.getContext().setProperties(properties);
    }
}
