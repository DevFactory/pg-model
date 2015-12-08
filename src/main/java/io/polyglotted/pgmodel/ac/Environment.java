package io.polyglotted.pgmodel.ac;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;
import static io.polyglotted.pgmodel.util.MapRetriever.deepRetrieve;

@ToString(includeFieldNames = false)
@RequiredArgsConstructor
public final class Environment {
    public final ImmutableMap<String, Object> properties;

    public final int apiRef() {
        return checkNotNull((Integer) property("API_REF"));
    }

    public final String resource() {
        return checkNotNull(property("RESOURCE"));
    }

    public final <T> T property(String path) {
        return deepRetrieve(properties, path);
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    public static Environment from(Map<String, Object> map, Clock clock) {
        return new Environment(ImmutableMap.<String, Object>builder().putAll(map).put("TIMESTAMP", clock.millis()).build());
    }
}
