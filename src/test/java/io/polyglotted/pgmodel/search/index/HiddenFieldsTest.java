package io.polyglotted.pgmodel.search.index;

import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.util.ModelUtil.serialize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class HiddenFieldsTest extends HiddenFields {

    @Test
    public void testHiddenFields() {
        String serialize = serialize(hiddenFields());
        assertThat(serialize, serialize, is(notNullValue()));
    }
}