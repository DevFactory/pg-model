package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;
import static java.util.Arrays.asList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class QueryHints {
    public final SearchOptions searchOptions;
    public final SearchType searchType;
    public final int timeoutInSeconds;
    public final ImmutableList<String> routing;
    public final String preference;
    public final boolean fetchSource;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchOptions, searchType, timeoutInSeconds, routing, preference, fetchSource);
    }

    public static Builder hintsBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private SearchOptions indicesOptions = SearchOptions.LENIENT_EXPAND_OPEN;
        private SearchType searchType = SearchType.QUERY_THEN_FETCH;
        private int timeoutInSeconds = 10;
        private final List<String> routing = new ArrayList<>();
        private String preference = null;
        private boolean fetchSource = true;

        public Builder routing(String... routing) {
            this.routing.addAll(asList(routing));
            return this;
        }

        public QueryHints build() {
            return new QueryHints(checkNotNull(indicesOptions), checkNotNull(searchType), timeoutInSeconds,
               ImmutableList.copyOf(routing), preference, fetchSource);
        }
    }
}
