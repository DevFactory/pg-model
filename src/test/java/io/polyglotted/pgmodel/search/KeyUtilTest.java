package io.polyglotted.pgmodel.search;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;

import static io.polyglotted.pgmodel.search.IndexKey.keyFrom;

public class KeyUtilTest extends KeyUtil {

    @Test(expectedExceptions = RuntimeException.class)
    public void testToOutputStreamFail() {
        writeToStream(keyFrom("a", "b", "c", 1L), new OutputStream() {
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }
}