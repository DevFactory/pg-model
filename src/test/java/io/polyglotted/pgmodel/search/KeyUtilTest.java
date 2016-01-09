package io.polyglotted.pgmodel.search;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;
import static io.polyglotted.pgmodel.search.IndexKey.keyWith;
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
        IndexKey expected = keyFrom("foo", "bar", "baz", 25L);
        IndexKey actual = expected.approvalKey().baseKey(expected.version);
        assertThat(serialize(actual), is(serialize(expected)));
    }

    @Test
    public void approvalKeyAndActualReturnTypesCorrectly() {
        String baseType = "bar";
        String approvalType = "bar$approval";
        String typeUrn = "foo:bar";
        IndexKey baseKey = keyWith("foo", "bar", "baz");
        IndexKey approvalKey = baseKey.approvalKey();
        assertThat(baseKey.approvalType(), is(approvalType));
        assertThat(approvalKey.approvalType(), is(approvalType));
        assertThat(baseKey.baseType(), is(baseType));
        assertThat(approvalKey.baseType(), is(baseType));
        assertThat(baseKey.typeUrn(), is(typeUrn));
        assertThat(approvalKey.typeUrn(), is(typeUrn));
        assertThat(keyWith("foo.live", "bar", "baz").typeUrn(), is(typeUrn));
        assertThat(keyWith("foo.live", "bar$approval", "baz").typeUrn(), is(typeUrn));
    }
}