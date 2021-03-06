package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.transform;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class Flattened {
    public final ImmutableList<Object> array;

    public static Flattened flattened(Object... objects) {
        return new Flattened(ImmutableList.copyOf(objects));
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return 37 * array.hashCode();
    }

    public static Stream<Flattened> flatten(Aggregation aggs) {
        return flatten(new String[0], aggs);
    }

    private static Stream<Flattened> flatten(final String[] strings, Aggregation aggs) {
        if (!aggs.hasBuckets()) {
            return Stream.of(build(strings, aggs.valueIterable()));
        }
        return aggs.buckets().stream().flatMap(bucket -> {
            final String[] inner = makeArray(strings, bucket.key);
            return !bucket.hasAggregations() ? Stream.of(build(inner, bucket.count)) :
               bucket.aggregations.stream().flatMap(child -> flatten(inner, child));
        });
    }

    private static String[] makeArray(String[] strings, String key) {
        String[] result = new String[strings.length + 1];
        System.arraycopy(strings, 0, result, 0, strings.length);
        result[strings.length] = key;
        return result;
    }

    private static Flattened build(String[] strings, long docCount) {
        final List<Object> values = Lists.newArrayList((Object[]) strings);
        values.add(docCount);
        return Flattened.flattened(values.toArray());
    }

    private static Flattened build(String[] strings, Iterable<Entry<String, Object>> aggs) {
        final List<Object> values = Lists.newArrayList((Object[]) strings);
        Iterables.addAll(values, transform(aggs, Entry::getValue));
        return new Flattened(ImmutableList.copyOf(values));
    }
}
