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
import static io.polyglotted.pgmodel.util.ModelUtil.safeIndex;

@Accessors(fluent = true)
@RequiredArgsConstructor
@ToString(includeFieldNames = false, of = {"index", "type", "id", "parent", "version"}, doNotUseGetters = true)
public final class IndexKey implements Comparable<IndexKey> {
    @Getter
    public final String index;
    @Getter
    public final String type;
    @Getter
    public final String id;
    @Getter
    public final String parent;
    @Getter
    public final Long version;
    public final Boolean delete;
    public final Boolean store;
    private transient String _uniqueId;
    private transient String _typeUrn;

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

    public String approvalType() { return isApprovalType(type) ? type : approvalType(type); }

    public String baseIndex() { return isDotIndex(index) ? baseIndex(index) : index; }

    public String baseType() { return isApprovalType(type) ? baseType(type) : type; }

    public String typeUrn() { return _typeUrn == null ? (_typeUrn = typeUrn(baseIndex(), baseType())) : _typeUrn; }

    public String uniqueId() {
        return _uniqueId == null ? (_uniqueId = generateUuid(writeToStream(this, new ByteArrayOutputStream())
           .toByteArray()).toString()) : _uniqueId;
    }

    public IndexKey delete() { return new IndexKey(index, type, id, parent, version, true, store); }

    public IndexKey newVersion(long version) { return new IndexKey(index, type, id, parent, version, delete, store); }

    public IndexKey approvalKey() { return new IndexKey(index, approvalType(), id, parent, null, delete, store); }

    public IndexKey baseKey(Long baseVersion) {
        return new IndexKey(index, baseType(), id, parent, baseVersion, delete, store);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexKey that = (IndexKey) o;
        return equalsAll(index, that.index, type, that.type, id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, type, id);
    }

    @Override
    public int compareTo(IndexKey other) {
        return other == null ? -1 : start().compare(index, other.index).compare(type, other.type).compare(nullToEmpty
           (id), nullToEmpty(other.id)).compare(longToCompare(version), longToCompare(other.version)).result();
    }

    public static boolean isApprovalType(String type) { return type.indexOf("$approval") > 0; }

    public static String approvalType(String type) { return type + "$approval"; }

    public static String baseType(String type) { return type.substring(0, safeIndex(type.indexOf("$approval"))); }

    public static boolean isDotIndex(String index) { return index.indexOf(".") > 0; }

    public static String baseIndex(String index) { return index.substring(0, safeIndex(index.indexOf("."))); }

    public static String typeUrn(String index, String type) { return index + ":" + type; }
}
