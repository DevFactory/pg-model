package io.polyglotted.pgmodel.search;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;
import static io.polyglotted.pgmodel.util.ModelUtil.serialize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class KeyUtilTest extends KeyUtil {

    @Test(expectedExceptions = RuntimeException.class)
    public void testToOutputStreamFail() {
        writeToStream(keyFrom("a", "b", "c", 1L), new OutputStream() {
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }

    @Test
    public void approvalKeyReversesBaseKey() {
        IndexKey expected = IndexKey.keyFrom("foo", "bar", "baz", 25L);
        IndexKey actual = expected.approvalKey().baseKey(expected.version);
        assertThat(serialize(actual), is(serialize(expected)));
    }

    @Test
    public void approvalKeyAndActualReturnTypesCorrectly() {
        String expectedType = "bar$approval";
        IndexKey original = IndexKey.keyWith("foo", "bar", "baz");
        assertThat(original.approvalType(), is(expectedType));
        assertThat(original.approvalKey().approvalType(), is(expectedType));
    }
}