package io.polyglotted.pgmodel.search.index;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.util.Map;

import static io.polyglotted.pgmodel.util.ModelUtil.serialize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class HiddenFieldsTest extends HiddenFields {

    @Test
    public void testHiddenFields() {
        String serialize = serialize(hiddenFields());
        assertThat(serialize, is(notNullValue()));
    }

    @Test
    public void headerMapFromHiddenFields() {
        Map<String, Object> expected = ImmutableMap.<String, Object>of(STATUS_FIELD, "expired",
           COMMENT_FIELD, "my comment");
        Map<String, Object> input = ImmutableMap.<String, Object>of("bar", true, STATUS_FIELD, "expired",
           "baz", 25, COMMENT_FIELD, "my comment", BYTES_FIELD, "foobarbazqux");

        assertThat(headerMapFrom(input), is(expected));
    }
}