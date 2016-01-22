package io.polyglotted.pgmodel.search;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;
import static io.polyglotted.pgmodel.search.IndexKey.keyWith;
import static io.polyglotted.pgmodel.search.IndexKey.typeUrn;
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

    @DataProvider
    public static Object[][] typeUrnInputs() {
        return new Object[][]{
           {"foo:bar", "foo", "bar"},
           {"foo:*", "foo", "*"},
           {"*", "", "*"},
           {":*", "", "*"},
           {"", "", ""},
        };
    }

    @Test(dataProvider = "typeUrnInputs")
    public void typeOfIndexOf(String typeUrn, String index, String type) {
        assertThat(IndexKey.indexOf(typeUrn), is(index));
        assertThat(IndexKey.typeOf(typeUrn), is(type));
    }

    @DataProvider
    public static Object[][] indexKeyInputs() {
        return new Object[][]{
           {keyWith("foo_index", "Bar", "baz:001"), "foo_index/Bar/baz:001"},
           {keyWith("foo_index", "Bar", "baz:001").approvalKey(), "foo_index/Bar$approval/baz:001"},
           {keyWith("foo_index.live", "Bar", "baz:001"), "foo_index/Bar/baz:001"},
           {keyWith("foo_index.live", "Bar", "baz:001").approvalKey(), "foo_index/Bar$approval/baz:001"},
           {keyWith("foo_index.all", "Bar", "baz:001"), "foo_index/Bar/baz:001"},
           {keyWith("foo_index.all", "Bar", "baz:001").approvalKey(), "foo_index/Bar$approval/baz:001"},
        };
    }

    @Test(dataProvider = "indexKeyInputs")
    public void allUtilityFuncs(IndexKey key, String baseIndexId) {
        assertThat(key.baseType(), is("Bar"));
        assertThat(key.approvalType(), is("Bar$approval"));
        assertThat(key.baseIndex(), is("foo_index"));
        assertThat(key.liveAlias(), is("foo_index.live"));
        assertThat(key.allAlias(), is("foo_index.all"));
        assertThat(typeUrn(key.baseIndex(), key.baseType()), is("foo_index:Bar"));
        assertThat(typeUrn(key.liveAlias(), key.baseType()), is("foo_index.live:Bar"));
        assertThat(typeUrn(key.allAlias(), key.baseType()), is("foo_index.all:Bar"));
        assertThat(typeUrn(key.baseIndex(), "*"), is("foo_index:*"));
        assertThat(typeUrn(key.liveAlias(), "*"), is("foo_index.live:*"));
        assertThat(typeUrn(key.allAlias(), "*"), is("foo_index.all:*"));
        assertThat(key.baseIndexId(), is(baseIndexId));
    }
}