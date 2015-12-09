package io.polyglotted.pgmodel.util;

import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class DigestUtilTest extends DigestUtil {

    @Test
    public void testIndexKey() {
        String orig = keyFrom("abc", "a", "id101", 1234L).uniqueId();

        assertThat(orig, is("d6eaf177-6e2c-5d9a-aa76-a9884c0dc6db"));
        assertThat(orig, is(equalTo(keyFrom("abc", "a", "id101", 1234L).uniqueId())));
        assertThat(orig, is(not(equalTo(keyFrom("def", "a", "id101", 1234L).uniqueId()))));
        assertThat(orig, is(not(equalTo(keyFrom("abc", "a", "id102", 1234L).uniqueId()))));
        assertThat(orig, is(not(equalTo(keyFrom("abc", "a", "id101", 1245L).uniqueId()))));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testCreateMessageDigestFail() {
        createMessageDigest("abcd");
    }
}