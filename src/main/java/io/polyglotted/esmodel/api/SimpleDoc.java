package io.polyglotted.esmodel.api;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static com.google.common.base.Optional.fromNullable;
import static io.polyglotted.esmodel.api.ModelUtil.jsonEquals;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class SimpleDoc {
    public final IndexKey key;
    public final ImmutableMap<String, Object> source;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, source);
    }

    public IndexKey key() {
        return key;
    }

    public long version() {
        return key.version;
    }

    public boolean boolVal(String property) {
        return fromNullable((Boolean) source.get(property)).or(false);
    }

    public int intVal(String property) {
        return fromNullable((Integer) source.get(property)).or(Integer.MIN_VALUE);
    }

    public long longVal(String property) {
        return fromNullable((Long) source.get(property)).or(Long.MIN_VALUE);
    }

    public long longStrVal(String property) {
        return Long.parseLong(strVal(property));
    }

    public String strVal(String property) {
        return (String) source.get(property);
    }
}