package io.polyglotted.pgmodel.search;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.util.Map;

import static io.polyglotted.pgmodel.search.index.HiddenFields.ANCESTOR_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.BYTES_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.TIMESTAMP_FIELD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SimpleDocTest {

    @Test
    public void testFilteredCopy() throws Exception {
        Map<String, Object> expected = ImmutableMap.<String, Object>builder().put("foo", "foo").put("bar", 25)
           .put("baz", true).put(BYTES_FIELD, "bytes").build();
        ImmutableMap<String, Object> original = ImmutableMap.<String, Object>builder().put("foo", "foo").put("bar", 25)
           .put("baz", true).put(BYTES_FIELD, "bytes").put(TIMESTAMP_FIELD, "ts2").put(ANCESTOR_FIELD, "ances").build();

        Map<String, Object> actual = new SimpleDoc(IndexKey.keyWith("a", "b", "c"), original).filteredCopy();

        assertThat(actual, is(expected));
    }
}