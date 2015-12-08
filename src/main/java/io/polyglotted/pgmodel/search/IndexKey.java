package io.polyglotted.pgmodel.search;

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
import static io.polyglotted.pgmodel.search.KeyUtil.generateUuid;
import static io.polyglotted.pgmodel.util.ModelUtil.equalsAll;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class IndexKey implements Comparable<IndexKey> {
    public final String index;
    public final String type;
    public final String id;
    public final String parent;
    public final long version;
    public final boolean delete;
    public final boolean store;

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
        return keyFrom(index, type, id, parent, -1);
    }

    public static IndexKey keyFrom(String index, String type, String id, long version) {
        return keyFrom(index, type, id, null, version);
    }

    public static IndexKey keyFrom(String index, String type, String id, String parent, long version) {
        return new IndexKey(index, type, id, parent, version, false, true);
    }

    public String uniqueId() {
        return generateUuid(writeToStream(this, new ByteArrayOutputStream()).toByteArray()).toString();
    }

    public IndexKey delete() {
        return new IndexKey(index, type, id, parent, version, true, store);
    }

    public IndexKey newVersion(long version) {
        return new IndexKey(index, type, id, parent, version, delete, store);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexKey that = (IndexKey) o;
        return equalsAll(index, that.index, id, that.id, parent, that.parent, version, that.version, delete, that.delete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, id, parent, version, delete);
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
