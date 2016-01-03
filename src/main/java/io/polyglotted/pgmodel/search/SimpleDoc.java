package io.polyglotted.pgmodel.search;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Maps.filterKeys;
import static io.polyglotted.pgmodel.search.index.HiddenFields.BASEVERSION_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.BYTES_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.STATUS_FIELD;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class SimpleDoc {
    @Delegate(excludes = KeyExclude.class)
    public final IndexKey key;
    public final ImmutableMap<String, Object> source;

    @Override
    public boolean equals(Object o) { return jsonEquals(this, o); }

    @Override
    public int hashCode() { return Objects.hash(key, source); }

    public Map<String, Object> filteredCopy(boolean includeBase) {
        return filterKeys(source, includeBase ? SimpleDoc::baseVersionKey : SimpleDoc::validKey);
    }

    public IndexKey key() { return key; }

    public DocStatus status() { return DocStatus.fromStatus(strVal(STATUS_FIELD)); }

    public Long baseVersion() { return hasItem(BASEVERSION_FIELD) ? longStrVal(BASEVERSION_FIELD) : null; }

    public boolean hasItem(String property) { return source.containsKey(property); }

    public boolean boolVal(String property) { return fromNullable((Boolean) source.get(property)).or(false); }

    public int intVal(String property) { return fromNullable((Integer) source.get(property)).or(Integer.MIN_VALUE); }

    public long longVal(String property) { return fromNullable(longObjVal(property)).or(Long.MIN_VALUE); }

    public long longStrVal(String property) { return Long.parseLong(strVal(property)); }

    public Long longObjVal(String property) { return (Long) source.get(property); }

    public String strVal(String property) { return (String) source.get(property); }

    private static boolean validKey(String key) { return BYTES_FIELD.equals(key) || !key.startsWith("&"); }

    private static boolean baseVersionKey(String key) { return BASEVERSION_FIELD.equals(key) || validKey(key); }
}