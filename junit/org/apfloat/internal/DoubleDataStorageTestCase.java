package org.apfloat.internal;

import java.lang.reflect.Array;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public abstract class DoubleDataStorageTestCase
    extends DoubleTestCase
{
    protected DoubleDataStorageTestCase()
    {
    }

    protected DoubleDataStorageTestCase(String methodName)
    {
        super(methodName);
    }

    public TestSuite realSuite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(createTestCase("testGetArray"));
        suite.addTest(createTestCase("testSubsequence"));
        suite.addTest(createTestCase("testSubsequenceSubArray"));
        suite.addTest(createTestCase("testReadOnly"));
        suite.addTest(createTestCase("testCopyFrom"));
        suite.addTest(createTestCase("testCopyFromSelf"));
        suite.addTest(createTestCase("testCopyFromBig"));
        suite.addTest(createTestCase("testIterator"));
        suite.addTest(createTestCase("testIteratorBig"));
        suite.addTest(createTestCase("testSubsequenceIterator"));
        suite.addTest(createTestCase("testUnsupportedIterator"));
        suite.addTest(createTestCase("testGenericIterator"));

        return suite;
    }

    public abstract TestCase createTestCase(String methodName);

    public abstract DataStorage createDataStorage()
        throws ApfloatRuntimeException;

    public void testGetArray()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 4);

        assertEquals("data length", 4, dataStorage.getSize());

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("write array length", 4, arrayAccess.getLength());

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 1, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("read array length", 2, arrayAccess.getLength());
    }

    public void testSubsequence()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 4);

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.close();

        assertEquals("data length", 4, dataStorage.getSize());

        assertEquals("not yet subsequenced", false, dataStorage.isSubsequenced());

        DataStorage newDataStorage = dataStorage.subsequence(1, 2);

        assertEquals("base subsequenced", true, dataStorage.isSubsequenced());
        assertEquals("subsequence subsequenced", true, newDataStorage.isSubsequenced());

        assertEquals("base data length", 4, dataStorage.getSize());
        assertEquals("subsequence data length", 2, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ_WRITE, 0, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("sub[0]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("sub[1]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("subsequence array length", 2, arrayAccess.getLength());
        arrayAccess.close();

        DataStorage newNewDataStorage = newDataStorage.subsequence(1, 1);

        arrayAccess = newNewDataStorage.getArray(DataStorage.READ_WRITE, 0, 1);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("subsub[0]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("subsubsequence array length", 1, arrayAccess.getLength());
    }

    public void testSubsequenceSubArray()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(8);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 8);

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 4] = (double) 5;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 5] = (double) 6;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 6] = (double) 7;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 7] = (double) 8;
        arrayAccess.close();

        DataStorage newDataStorage = dataStorage.subsequence(1, 6);

        arrayAccess = newDataStorage.getArray(DataStorage.READ_WRITE, 1, 4);
        ArrayAccess newArrayAccess = arrayAccess.subsequence(1, 2);

        assertEquals("sub[0]", 4, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset()]);
        assertEquals("sub[1]", 5, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 1]);

        newArrayAccess.getDoubleData()[newArrayAccess.getOffset()] = (double) -1;
        newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 1] = (double) -2;
        newArrayAccess.close();
        arrayAccess.close();

        newArrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 6);

        assertEquals("[0]", 2, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 1]);
        assertEquals("[2]", -1, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 2]);
        assertEquals("[3]", -2, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 3]);
        assertEquals("[4]", 6, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 4]);
        assertEquals("[5]", 7, (int) newArrayAccess.getDoubleData()[newArrayAccess.getOffset() + 5]);
    }

    public void testReadOnly()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);
        assertEquals("data length", 4, dataStorage.getSize());

        DataStorage newDataStorage = dataStorage.subsequence(1, 2);

        assertEquals("base data length", 4, dataStorage.getSize());
        assertEquals("subsequence data length", 2, newDataStorage.getSize());

        assertEquals("base writable", false, dataStorage.isReadOnly());
        assertEquals("subsequence writable", false, newDataStorage.isReadOnly());

        newDataStorage.setReadOnly();

        assertTrue("1 base read-only", dataStorage.isReadOnly());
        assertTrue("1 subsequence read-only", newDataStorage.isReadOnly());

        assertEquals("1 read-only base data length", 4, dataStorage.getSize());
        assertEquals("1 read-only subsequence data length", 2, newDataStorage.getSize());

        dataStorage = createDataStorage();
        dataStorage.setSize(4);
        newDataStorage = dataStorage.subsequence(1, 2);
        dataStorage.setReadOnly();

        assertTrue("2 base read-only", dataStorage.isReadOnly());
        assertTrue("2 subsequence read-only", newDataStorage.isReadOnly());

        assertEquals("2 read-only base data length", 4, dataStorage.getSize());
        assertEquals("2 read-only subsequence data length", 2, newDataStorage.getSize());
    }

    public void testCopyFrom()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 4);

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.close();

        DataStorage newDataStorage = createDataStorage();

        newDataStorage.copyFrom(dataStorage, 2);

        assertEquals("data length 1", 2, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("array length 1", 2, arrayAccess.getLength());
        arrayAccess.close();

        newDataStorage.copyFrom(dataStorage);

        assertEquals("data length 2", 4, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[3]", 4, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3]);
        assertEquals("array length 2", 4, arrayAccess.getLength());
        arrayAccess.close();

        newDataStorage.copyFrom(dataStorage, 6);

        assertEquals("data length 3", 6, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 6);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[3]", 4, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3]);
        assertEquals("[4]", 0, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 4]);
        assertEquals("[5]", 0, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 5]);
        assertEquals("array length 3", 6, arrayAccess.getLength());
        arrayAccess.close();

        dataStorage = dataStorage.subsequence(2, 2);
        newDataStorage.copyFrom(dataStorage, 2);

        assertEquals("data length 4", 2, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 4, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("array length 4", 2, arrayAccess.getLength());
        arrayAccess.close();

        newDataStorage.copyFrom(dataStorage, 4);

        assertEquals("data length 5", 4, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 3, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 4, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 0, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 0, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3]);
        assertEquals("array length 5", 4, arrayAccess.getLength());
        arrayAccess.close();
    }

    public void testCopyFromSelf()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 4);

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.close();

        dataStorage.copyFrom(dataStorage, 2);

        assertEquals("data length 2", 2, dataStorage.getSize());

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, 2);

        assertTrue("class", arrayAccess.getData() instanceof double[]);
        assertEquals("[0]", 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1]);
        assertEquals("array length 1", 2, arrayAccess.getLength());
        arrayAccess.close();
    }

    public void testCopyFromBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int size = ctx.getBlockSize() / 8 * 7 / 2;
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getDoubleData()[arrayAccess.getOffset() + i] = (double) (i + 1);
        }
        arrayAccess.close();

        size = ctx.getBlockSize() / 8 * 5 / 2;
        DataStorage newDataStorage = createDataStorage();
        newDataStorage.copyFrom(dataStorage, size);

        assertEquals("data length", size, newDataStorage.getSize());

        arrayAccess = newDataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < size; i++)
        {
            assertEquals("[" + i + "]", i + 1, (int) arrayAccess.getDoubleData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }

    public void testIterator()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(4);

        DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 4);

        try
        {
            iterator.getDouble();
            fail("Write iterator can be read");
        }
        catch (IllegalStateException ise)
        {
            // OK; no read access
        }

        iterator.setDouble((double) 1);
        iterator.next();
        iterator.setDouble((double) 2);
        iterator.next();
        iterator.setDouble((double) 3);
        iterator.next();
        iterator.setDouble((double) 4);
        iterator.next();

        assertEquals("write iterator end", false, iterator.hasNext());

        iterator = dataStorage.iterator(DataStorage.READ, 1, 3);

        try
        {
            iterator.setDouble((double) 5);
            fail("Read iterator can be written");
        }
        catch (IllegalStateException ise)
        {
            // OK; no write access
        }

        assertEquals("1st forward read", 2, (int) iterator.getDouble());
        iterator.next();
        assertEquals("2nd forward read", 3, (int) iterator.getDouble());
        iterator.next();

        assertEquals("1st forward read iterator end", false, iterator.hasNext());

        iterator = dataStorage.iterator(DataStorage.READ, 3, 1);

        assertEquals("1st reverse read", 3, (int) iterator.getDouble());
        iterator.next();
        assertEquals("2nd reverse read", 2, (int) iterator.getDouble());
        iterator.next();

        assertEquals("1st reverse read iterator end", false, iterator.hasNext());

        iterator = dataStorage.iterator(DataStorage.READ, 4, 0);

        assertEquals("1st full reverse read", 4, (int) iterator.getDouble());
        iterator.next();
        assertEquals("2nd full reverse read", 3, (int) iterator.getDouble());
        iterator.next();
        assertEquals("3rd full reverse read", 2, (int) iterator.getDouble());
        iterator.next();
        assertEquals("4th full reverse read", 1, (int) iterator.getDouble());
        iterator.next();

        assertEquals("2nd reverse read iterator end", false, iterator.hasNext());

        try
        {
            iterator.next();
            fail("Iterate past end");
        }
        catch (IllegalStateException ise)
        {
            // OK; at end
        }

        try
        {
            dataStorage.iterator(0, 0, 4);
            fail("No-access iterator accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; no access mode
        }
    }

    public void testIteratorBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int size = ctx.getBlockSize() / 8 * 7 / 2;
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(size);

        DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            iterator.setDouble((double) i + 1);
            iterator.next();
        }
        assertEquals("write iterator end", false, iterator.hasNext());

        size = ctx.getBlockSize() / 8 * 2;

        iterator = dataStorage.iterator(DataStorage.READ, 0, size);
        for (int i = 0; i < size; i++)
        {
            assertEquals("[" + i + "]", i + 1, (int) iterator.getDouble());
            iterator.next();
        }
        assertEquals("read iterator end", false, iterator.hasNext());
    }

    public void testSubsequenceIterator()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(6);
        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, 6);

        arrayAccess.getDoubleData()[arrayAccess.getOffset()] = (double) 1;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 1] = (double) 2;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 2] = (double) 3;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 3] = (double) 4;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 4] = (double) 5;
        arrayAccess.getDoubleData()[arrayAccess.getOffset() + 5] = (double) 6;
        arrayAccess.close();

        dataStorage = dataStorage.subsequence(1, 4);

        DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 1, 3);

        assertEquals("1st forward read", 3, (int) iterator.getDouble());
        iterator.next();
        assertEquals("2nd forward read", 4, (int) iterator.getDouble());
        iterator.next();

        assertEquals("forward read iterator end", false, iterator.hasNext());

        iterator = dataStorage.iterator(DataStorage.READ, 4, 0);

        assertEquals("1st reverse read", 5, (int) iterator.getDouble());
        iterator.next();
        assertEquals("2nd reverse read", 4, (int) iterator.getDouble());
        iterator.next();
        assertEquals("3rd reverse read", 3, (int) iterator.getDouble());
        iterator.next();
        assertEquals("4th reverse read", 2, (int) iterator.getDouble());
        iterator.next();

        assertEquals("reverse read iterator end", false, iterator.hasNext());

        dataStorage = dataStorage.subsequence(1, 2);

        iterator = dataStorage.iterator(DataStorage.READ, 1, 2);

        assertEquals("1st subsub read", 4, (int) iterator.getDouble());
        iterator.next();

        assertEquals("subsub iterator end", false, iterator.hasNext());
    }

    public void testGenericIterator()
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(1);

        DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
        iterator.set(Double.TYPE, (double) 1);
        iterator.next();

        iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
        assertEquals("read", (double) 1, (double) iterator.get(Double.TYPE));
        iterator.next();
    }

    public void testUnsupportedIterator()
        throws Exception
    {
        DataStorage dataStorage = createDataStorage();
        dataStorage.setSize(1);

        Class<?>[] types = { Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
        for (Class<?> type : types)
        {
            if (!type.equals(Double.TYPE))
            {
                runUnsupportedIterator(type, dataStorage);
            }
        }

        if (!Integer.TYPE.equals(Double.TYPE))
        {
            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
                iterator.getInt();
                fail("Get as int allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }

            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
                iterator.setInt(1);
                fail("Set as int allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }
        }

        if (!Long.TYPE.equals(Double.TYPE))
        {
            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
                iterator.getLong();
                fail("Get as long allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }

            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
                iterator.setLong(1L);
                fail("Set as long allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }
        }

        if (!Float.TYPE.equals(Double.TYPE))
        {
            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
                iterator.getFloat();
                fail("Get as float allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }

            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
                iterator.setFloat(1.0f);
                fail("Set as float allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }
        }

        if (!Double.TYPE.equals(Double.TYPE))
        {
            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
                iterator.getDouble();
                fail("Get as double allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }

            try
            {
                DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
                iterator.setDouble(1.0);
                fail("Set as double allowed");
            }
            catch (UnsupportedOperationException uoe)
            {
                // OK
            }
        }
    }

    private <T> void runUnsupportedIterator(Class<T> type, DataStorage dataStorage)
        throws Exception
    {
        try
        {
            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);
            iterator.get(type);
            fail("Generic get as " + type.getName() + " allowed");
        }
        catch (UnsupportedOperationException uoe)
        {
            // OK
        }

        try
        {
            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
            @SuppressWarnings("unchecked")
            T value = (T) Double.class.getMethod("valueOf", String.class).invoke(null, "0");
            iterator.set(type, value);
            fail("Generic set as " + type.getName() + " allowed");
        }
        catch (UnsupportedOperationException uoe)
        {
            // OK
        }

        try
        {
            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.WRITE, 0, 1);
            @SuppressWarnings("unchecked")
            Class<T> correctType = (Class<T>) Double.TYPE;
            Object array = Array.newInstance(type, 1);
            @SuppressWarnings("unchecked")
            T value = (T) Array.get(array, 0);
            iterator.set(correctType, value);
            fail("Generic set with value type " + type.getName() + " allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK
        }
    }
}
