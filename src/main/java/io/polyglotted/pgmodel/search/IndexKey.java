package io.polyglotted.pgmodel.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ComparisonChain.start;
import static io.polyglotted.pgmodel.search.KeyUtil.checkNotEmpty;
import static io.polyglotted.pgmodel.search.KeyUtil.longToCompare;
import static io.polyglotted.pgmodel.search.KeyUtil.writeToStream;
import static io.polyglotted.pgmodel.util.DigestUtil.generateUuid;
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
    public final Long version;
    public final Boolean delete;
    public final Boolean store;

    public static IndexKey keyWith(String index, String type, String id) {
        return keyWithParent(index, type, id, null);
    }

    public static IndexKey keyWithParent(String index, String type, String parent) {
        return keyWithParent(index, type, null, parent);
    }

    public static IndexKey keyWithParent(String index, String type, String id, String parent) {
        return keyFrom(index, type, id, parent, null);
    }

    public static IndexKey keyFrom(String index, String type, String id, Long version) {
        return keyFrom(index, type, id, null, version);
    }

    public static IndexKey keyFrom(String index, String type, String id, String parent, Long version) {
        return new IndexKey(checkNotEmpty(index), checkNotEmpty(type), id, parent, version, null, null);
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
        return equalsAll(index, that.index, type, that.type, id, that.id,
           parent, that.parent, version, that.version, delete, that.delete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, type, id, parent, version, delete);
    }

    @Override
    public int compareTo(IndexKey other) {
        return other == null ? -1 : start().compare(index, other.index).compare(type, other.type).compare(nullToEmpty
           (id), nullToEmpty(other.id)).compare(longToCompare(version), longToCompare(other.version)).result();
    }
}
