package io.polyglotted.esmodel.api;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.google.common.collect.ComparisonChain.start;
import static io.polyglotted.esmodel.api.KeyUtil.generateUuid;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class IndexKey implements Comparable<IndexKey> {
    public final String index;
    public final String type;
    public final String id;
    public final long version;
    public final boolean delete;
    public final String parent;

    public IndexKey(String index, String type, String id, long version) {
        this(index, type, id, version, false, null);
    }

    public String uniqueId() {
        return generateUuid(writeToStream(this, new ByteArrayOutputStream()).toByteArray()).toString();
    }

    public IndexKey delete() {
        return new IndexKey(index, type, id, version, true, parent);
    }

    public IndexKey version(long version) {
        return new IndexKey(index, type, id, version, false, parent);
    }

    public static IndexKey keyWith(String type, String id) {
        return keyWithParent("", type, id, null);
    }

    public static IndexKey keyWith(String index, String type, String id) {
        return keyWithParent(index, type, id, null);
    }

    public static IndexKey keyWithParent(String index, String type, String parent) {
        return keyWithParent(index, type, "", parent);
    }

    public static IndexKey keyWithParent(String index, String type, String id, String parent) {
        return new IndexKey(index, type, id, -1, false, parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexKey that = (IndexKey) o;
        return index.equals(that.index) && id.equals(that.id) && (version == that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, id, version);
    }

    @Override
    public int compareTo(IndexKey other) {
        return other == null ? -1 : start().compare(index, other.index)
           .compare(id, other.id).compare(version, other.version).result();
    }

    @VisibleForTesting
    static <OS extends OutputStream> OS writeToStream(IndexKey indexKey, OS output) {
        try {
            DataOutputStream stream = new DataOutputStream(output);
            stream.writeBytes(indexKey.index);
            stream.writeBytes(indexKey.id);
            stream.writeLong(indexKey.version);
            stream.close();
        } catch (Exception ex) {
            throw new RuntimeException("failed to writeToStream");
        }
        return output;
    }
}
