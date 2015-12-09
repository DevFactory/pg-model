package io.polyglotted.pgmodel;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public interface EqualityChecker {
    @SafeVarargs
    static <T> void verifyEqualsHashCode(T obj, T copy, T... others) {
        assertNotNull(obj.toString());
        assertEquals(obj, obj);
        assertEquals(obj, copy);
        assertEquals(obj.hashCode(), copy.hashCode());
        assertFalse(obj.equals(null));
        assertFalse(obj.equals(""));
        for (T other : others) {
            assertNotEquals(obj, other);
        }
    }

    @SafeVarargs
    static <T extends Comparable<T>> void verifyComparable(T obj1, T... others) {
        assertEquals(obj1.compareTo(null), -1);
        assertEquals(obj1.compareTo(obj1), 0);
        for (T other : others) {
            assertTrue(obj1.compareTo(other) < 0);
        }
    }
}
