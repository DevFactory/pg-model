package io.polyglotted.pgmodel.util;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.search.index.FieldType;
import lombok.Data;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ReflectionUtilTest extends ReflectionUtil {

    @Test
    public void testSafeForName() throws Exception {
        assertEquals(safeForName(null), null);
        assertEquals(safeForName("io.pgmodel.DoesNotExist"), null);
        assertEquals(safeForName("io.polyglotted.pgmodel.util.ReflectionUtilTest$MyInner"), MyInner.class);
    }

    @Test
    public void testCreate() throws Exception {
        assertEquals(create(MyInner.class), new MyInner(null, 0));
        assertEquals(create(MyEmptyConstructor.class), new MyEmptyConstructor());
    }

    @Test
    public void testAsEnum() throws Exception {
        assertEquals(asEnum(FieldType.class, 1, "STRING"), FieldType.STRING);
        assertEquals(asEnum(MyInner.class, 1, "STRING"), "STRING");
    }

    @Test
    public void testIsEnum() throws Exception {
        assertFalse(isEnum(null));
        assertFalse(isEnum(MyInner.class));
        assertTrue(isEnum(Thread.State.class));
        assertTrue(isEnum(FieldType.class));
    }

    @Test
    public void testIsAssignable() throws Exception {
        assertFalse(isAssignable(MyInner.class, null));
        assertFalse(isAssignable(MyChild.class, MyInner.class));
        assertTrue(isAssignable(MyInner.class, MyChild.class));
    }

    @Test
    public void testDeclaredField() throws Exception {
        assertNotNull(declaredField(MyChild.class, "value"));
        assertNull(declaredField(MyChild.class, "foo"));
    }

    @Test
    public void testGetFieldValue() throws Exception {
        assertEquals(fieldValue(new MyInner("hi", 3), "value"), "hi");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetFieldValueFail() throws Exception {
        fieldValue(new MyInner("hi", 3), "boo");
    }

    @Test
    public void testSetFieldValue() throws Exception {
        assertEquals(fieldValue(new MyInner("hi", 3), "value", "hello"), new MyInner("hello", 3));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSetFieldValueFail() throws Exception {
        fieldValue(new MyInner("hi", 3), "boo", "boo");
    }

    @Test
    public void testFieldValues() throws Exception {
        assertEquals(fieldValues(new MyInner("hi", 3)), ImmutableMap.of("value", "hi"));
    }

    @Data
    public static class MyInner {
        private final static int STA = 5;
        private final String value;
        private final transient int none;
        private volatile boolean vol = false;
    }

    public static class MyChild extends MyInner {
        public MyChild(String value, int none) {
            super(value, none);
        }
    }

    @Data
    public static class MyEmptyConstructor {
    }
}