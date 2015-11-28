package io.polyglotted.esmodel.api;

import com.google.common.base.Function;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.transform;
import static io.polyglotted.esmodel.api.ModelUtil.jsonEquals;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class Sleeve<T> {
    public final IndexKey key;
    public final T source;
    public final String ancestor;
    public final boolean stored;

    public static <T> Sleeve<T> create(IndexKey key, T source) {
        return new Sleeve<>(key, source, null, true);
    }

    public static <T> List<Sleeve<T>> createSleeves(List<T> objects, Function<T, Sleeve<T>> newSleeveFunction) {
        return transform(objects, newSleeveFunction);
    }

    public static <T> Sleeve<T> newSleeve(T object, Function<T, IndexKey> keyFunction) {
        return create(keyFunction.apply(object), object);
    }

    public static <T> Sleeve<T> newSleeveNotStored(T object, Function<T, IndexKey> keyFunction) {
        return new Sleeve<>(keyFunction.apply(object), object, null, false);
    }

    public static <T> List<Sleeve<T>> deleteSleeves(List<IndexKey> keys) {
        return transform(keys, Sleeve::deleteSleeve);
    }

    public static <T> Sleeve<T> deleteSleeve(IndexKey key) {
        return new Sleeve<>(key.delete(), null, key.uniqueId(), true);
    }

    public IndexKey key() {
        return key;
    }

    public String id() {
        return key.id;
    }

    public String index() {
        return key.index;
    }

    public T source() {
        return source;
    }

    public boolean isNew() {
        return key.version <= 0;
    }

    public boolean isDelete() {
        return key.delete;
    }

    public Sleeve<T> delete() {
        return new Sleeve<>(key.delete(), null, key.uniqueId(), stored);
    }

    public Sleeve<T> update(T update) {
        return new Sleeve<>(key, update, key.uniqueId(), stored);
    }

    public Sleeve<T> update(Function<T, T> updateFunction) {
        return update(updateFunction.apply(source));
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, source);
    }
}
