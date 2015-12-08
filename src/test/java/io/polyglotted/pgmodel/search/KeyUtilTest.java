package io.polyglotted.pgmodel.search;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class KeyUtilTest extends KeyUtil {

    @Test
    public void testIndexKey() {
        String orig = keyFrom("abc", "", "id101", 1234).uniqueId();

        assertThat(orig, is("0fbce132-d525-5bea-8bba-d4d21fdbb7b1"));
        assertThat(orig, is(equalTo(keyFrom("abc", "", "id101", 1234).uniqueId())));
        assertThat(orig, is(not(equalTo(keyFrom("def", "", "id101", 1234).uniqueId()))));
        assertThat(orig, is(not(equalTo(keyFrom("abc", "", "id102", 1234).uniqueId()))));
        assertThat(orig, is(not(equalTo(keyFrom("abc", "", "id101", 1245).uniqueId()))));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testCreateMessageDigestFail() {
        createMessageDigest("abcd");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testToOutputStreamFail() {
        IndexKey.writeToStream(keyFrom("a", "b", "c", 1), new OutputStream() {
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }
}