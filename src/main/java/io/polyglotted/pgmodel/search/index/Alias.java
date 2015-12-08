package io.polyglotted.pgmodel.search.index;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.polyglotted.pgmodel.search.query.Expression;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;
import static java.util.Arrays.asList;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class Alias {
    public final String alias;
    public final ImmutableList<String> indices;
    public final Expression filter;
    public final boolean remove;

    public List<String> indices() {
        return indices;
    }

    public boolean containsIndex(String index) {
        return indices.contains(index);
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, indices, filter, remove);
    }

    public static Builder aliasBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String alias;
        private final Set<String> indices = new TreeSet<>();
        private Expression filter;
        private boolean remove = false;

        public Builder index(String... indices) {
            return index(asList(indices));
        }

        public Builder index(Iterable<String> indices) {
            Iterables.addAll(this.indices, indices);
            return this;
        }

        public Builder remove() {
            this.remove(true);
            return this;
        }

        public Alias build() {
            checkArgument(!indices.isEmpty(), "atleast one index must be added");
            return new Alias(checkNotNull(alias, "alias required"), ImmutableList.copyOf(indices), filter, remove);
        }
    }
}
