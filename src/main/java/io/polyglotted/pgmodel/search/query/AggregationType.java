package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AggregationType {
    Max(false, false),
    Min(false, false),
    Sum(false, false),
    Avg(false, false),
    Count(false, false),
    Term(true, true) {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
            return (T) ImmutableList.copyOf(buckets);
        }
    },
    Statistics(false, true) {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
            return (T) ImmutableMap.copyOf(valueMap);
        }
    },
    DateHistogram(true, true) {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
            return (T) ImmutableList.copyOf(buckets);
        }
    },
    Filter(true, true) {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
            return (T) ImmutableList.copyOf(buckets);
        }
    },
    Children(true, true) {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
            return (T) ImmutableList.copyOf(buckets);
        }
    };

    public final boolean hasBuckets;
    public final boolean isMultiValue;

    @SuppressWarnings("unchecked")
    public <T> T valueFrom(Map<String, Object> valueMap, Iterable<Bucket> buckets) {
        return (T) valueMap.get(name());
    }
}
