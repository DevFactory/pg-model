package io.polyglotted.pgmodel.search.index;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.search.DocStatus;
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
        Map<String, Object> expected = ImmutableMap.<String, Object>of(STATUS_FIELD, DocStatus.EXPIRED,
           COMMENT_FIELD, "my comment", BASEKEY_FIELD, "base", TIMESTAMP_FIELD, 25L, USER_FIELD, "user");

        Map<String, Object> input = ImmutableMap.<String, Object>builder()
           .put(BASEKEY_FIELD, "base").put("bar", true).put(STATUS_FIELD, "expired").put("baz", 25)
           .put(COMMENT_FIELD, "my comment").put(BYTES_FIELD, "foobarbazqux").put(TIMESTAMP_FIELD, "25")
           .put(USER_FIELD, "user").build();

        assertThat(headerFrom(input), is(expected));
    }
}