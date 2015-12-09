package io.polyglotted.pgmodel.search;

import java.io.DataOutputStream;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

public abstract class KeyUtil {

    public static String checkNotEmpty(String value) {
        return checkNotNull(emptyToNull(value), "value cannot be empty or null");
    }

    public static long longToCompare(Long value) {
        return value == null ? -1 : value;
    }

    public static <OS extends OutputStream> OS writeToStream(IndexKey indexKey, OS output) {
        try {
            DataOutputStream stream = new DataOutputStream(output);
            stream.writeBytes(indexKey.index);
            stream.writeBytes(indexKey.type);
            stream.writeBytes(nullToEmpty(indexKey.id));
            stream.writeLong(longToCompare(indexKey.version));
            stream.close();
        } catch (Exception ex) {
            throw new RuntimeException("failed to writeToStream", ex);
        }
        return output;
    }
}
