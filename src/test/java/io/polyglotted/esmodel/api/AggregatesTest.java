package io.polyglotted.esmodel.api;

import io.polyglotted.esmodel.api.query.Aggregates;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.google.common.collect.ImmutableMap.of;
import static io.polyglotted.esmodel.api.Expression.withMap;
import static io.polyglotted.esmodel.api.Expressions.equalsTo;
import static io.polyglotted.esmodel.api.ModelUtil.deserialize;
import static io.polyglotted.esmodel.api.ModelUtil.serialize;
import static io.polyglotted.esmodel.api.query.AggregationType.Term;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AggregatesTest extends Aggregates {

    @DataProvider
    public static Object[][] aggregationInputs() {
        return new Object[][]{
           {max("hello", "world")},
           {min("hello", "world")},
           {sum("hello", "world")},
           {avg("hello", "world")},
           {count("hello", "world")},
           {term("hello", "world")},
           {term("hello", "world", 5)},
           {term("hello", "world", 5, "count", true)},
           {term("hello", "world", 5, "TERM", false)},
           {withMap(Term.name(), "hello", of("field", "world", "size", 5, "order", "count"))},
           {term("hello", "world", 5, "foo.bar", true)},
           {stats("hello", "world")},
           {filterAggBuilder("hello", equalsTo("a", "b")).add(sumBuilder("x", "y")).build()},
           {childrenAggBuilder("hello", "world").add(sumBuilder("x", "y")).build()},
           {complexTerm()},
        };
    }

    @Test(dataProvider = "aggregationInputs")
    public void aggregateValid(Expression aggs) throws Exception {
        String serialize = serialize(aggs);
        Expression actual = deserialize(serialize, Expression.class);
        assertThat(actual, is(aggs));
    }

    private static Expression complexTerm() {
        Builder root = termBuilder("sources", "source");
        Builder entities = root.addAndGet(termBuilder("entities", "entity"));
        Builder traders = entities.addAndGet(termBuilder("traders", "trader"));
        traders.add(statsBuilder("traderPriceStats", "price"));
        Builder types = traders.addAndGet(dateHistogramBuilder("dates", "trade_date", "month"));
        types.add(statsBuilder("quantityStats", "quantity"));
        return root.build();
    }
}
