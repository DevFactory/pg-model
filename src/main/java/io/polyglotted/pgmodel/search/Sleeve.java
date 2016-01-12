package io.polyglotted.pgmodel.search;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.ObjectRef;
import io.polyglotted.pgmodel.search.index.HiddenFields;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.transform;
import static io.polyglotted.pgmodel.search.KeyUtil.longToCompare;
import static io.polyglotted.pgmodel.util.ModelUtil.equalsAll;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class Sleeve<T> implements ObjectRef<T> {
    @Delegate(excludes = KeyExclude.class)
    public final IndexKey key;
    public final T source;
    public final String ancestor;
    public final ImmutableMap<String, Object> header;

    public static <T> Sleeve<T> create(IndexKey key, T source) {
        return new Sleeve<>(checkNotNull(key), source, null, ImmutableMap.of());
    }

    public static <T> Sleeve<T> delete(IndexKey key) {
        return new Sleeve<>(checkNotNull(key).delete(), null, key.uniqueId(), ImmutableMap.of());
    }

    public static <T> List<Sleeve<T>> createSleeves(List<T> objects, Function<T, Sleeve<T>> newSleeveFunction) {
        return transform(objects, newSleeveFunction);
    }

    public static <T> Sleeve<T> newSleeve(T object, Function<T, IndexKey> keyFunction) {
        return create(keyFunction.apply(object), object);
    }

    public IndexKey key() { return key; }

    public T source() { return source; }

    public boolean isNew() { return longToCompare(key.version) <= 0; }

    public boolean shouldDelete() { return Boolean.TRUE.equals(key.delete); }

    public boolean shouldStore() { return !Boolean.FALSE.equals(key.store); }

    public DocStatus status() { return (DocStatus) header.get(HiddenFields.STATUS_FIELD); }

    public Sleeve<T> delete() { return new Sleeve<>(key.delete(), null, key.uniqueId(), ImmutableMap.of()); }

    public Sleeve<T> update(T update) { return new Sleeve<>(key, update, key.uniqueId(), ImmutableMap.of()); }

    public Sleeve<T> update(Function<T, T> updateFunction) { return update(updateFunction.apply(source)); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sleeve that = (Sleeve) o;
        return equalsAll(key, that.key(), source, that.source(), ancestor, that.ancestor);
    }

    @Override
    public int hashCode() { return Objects.hash(key, source, ancestor); }
}
