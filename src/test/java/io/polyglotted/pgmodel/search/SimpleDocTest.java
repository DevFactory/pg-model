package io.polyglotted.pgmodel.search;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.util.Map;

import static io.polyglotted.pgmodel.search.index.HiddenFields.ANCESTOR_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.BASEVERSION_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.BYTES_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.TIMESTAMP_FIELD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SimpleDocTest {

    @Test
    public void testFilteredCopy() throws Exception {
        Map<String, Object> filteredCopy = ImmutableMap.<String, Object>builder().put("foo", "foo").put("bar", 25)
           .put("baz", true).put(BYTES_FIELD, "bytes").build();
        Map<String, Object> baseVersionCopy = ImmutableMap.<String, Object>builder().put("foo", "foo").put("bar", 25)
           .put("baz", true).put(BYTES_FIELD, "bytes").put(BASEVERSION_FIELD, "32").build();

        ImmutableMap<String, Object> original = ImmutableMap.<String, Object>builder().put("foo", "foo").put("bar", 25)
           .put("baz", true).put(BYTES_FIELD, "bytes").put(TIMESTAMP_FIELD, "ts2")
           .put(ANCESTOR_FIELD, "ances").put(BASEVERSION_FIELD, "32").build();

        SimpleDoc simple = new SimpleDoc(IndexKey.keyWith("a", "b", "c"), original);
        assertThat(simple.filteredCopy(false), is(filteredCopy));
        assertThat(simple.filteredCopy(true), is(baseVersionCopy));
    }
}