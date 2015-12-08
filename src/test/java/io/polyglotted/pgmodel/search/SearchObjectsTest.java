package io.polyglotted.pgmodel.search;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.search.index.*;
import io.polyglotted.pgmodel.search.query.*;
import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.EqualityChecker.verifyComparable;
import static io.polyglotted.pgmodel.EqualityChecker.verifyEqualsHashCode;
import static io.polyglotted.pgmodel.search.IndexKey.keyWith;
import static io.polyglotted.pgmodel.search.IndexKey.keyWithParent;
import static io.polyglotted.pgmodel.search.index.Alias.aliasBuilder;
import static io.polyglotted.pgmodel.search.index.FieldMapping.notAnalyzedStringField;
import static io.polyglotted.pgmodel.search.index.Script.scriptBuilder;
import static io.polyglotted.pgmodel.search.query.Aggregation.aggregationBuilder;
import static io.polyglotted.pgmodel.search.query.Bucket.bucketBuilder;
import static io.polyglotted.pgmodel.search.query.Flattened.flattened;
import static io.polyglotted.pgmodel.search.query.QueryHints.hintsBuilder;
import static io.polyglotted.pgmodel.search.query.QueryResponse.responseBuilder;
import static io.polyglotted.pgmodel.search.query.StandardQuery.queryBuilder;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

public class SearchObjectsTest {

    @Test
    public void fieldTypeValues() {
        asList(FieldType.values()).contains(FieldType.valueOf("BINARY"));
    }

    @Test
    public void indexedValues() {
        asList(Indexed.values()).contains(Indexed.valueOf("NOT_ANALYZED"));
    }

    @Test
    public void hintOptionValues() {
        asList(SearchOptions.values()).contains(SearchOptions.valueOf("STRICT_EXPAND_OPEN"));
    }

    @Test
    public void hintTypeValues() {
        asList(SearchType.values()).contains(SearchType.valueOf("QUERY_AND_FETCH"));
    }

    @Test
    public void sortModeValues() {
        asList(SortMode.values()).contains(SortMode.valueOf("SUM"));
        assertEquals(SortMode.AVG.toMode(), "avg");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void aliasWithNoAliases() {
        aliasBuilder().build();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void aliasWithNoIndices() {
        aliasBuilder().alias("hello").build();
    }

    @Test
    public void aliasEqHash() {
        Alias orig = aliasBuilder().alias("a").index("a").build();
        Alias copy = aliasBuilder().alias("a").index("a").build();
        Alias other = aliasBuilder().alias("b").index("a").build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void scriptEqHash() {
        Script orig = scriptBuilder().script("a=b").build();
        Script copy = scriptBuilder().script("a=b").build();
        Script other = scriptBuilder().script("c=d").build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void sleeveEqHash() {
        Sleeve<String> orig = Sleeve.create(keyWith("a", "a"), "a");
        Sleeve<String> copy = Sleeve.create(keyWith("a", "a"), "a");
        Sleeve<String> other = orig.delete();
        Sleeve<String> other2 = orig.update("b");
        verifyEqualsHashCode(orig, copy, other, other2);
    }

    @Test
    public void fieldMappingEqHash() {
        FieldMapping orig = notAnalyzedStringField("a").build();
        FieldMapping copy = notAnalyzedStringField("a").build();
        FieldMapping other = notAnalyzedStringField("b").build();
        verifyEqualsHashCode(orig, copy, other);
        verifyComparable(orig, other);
    }

    @Test
    public void indexKeyEqHash() {
        IndexKey orig = keyWith("a", "b", "c");
        IndexKey copy = keyWith("a", "b", "c");
        IndexKey other1 = keyWithParent("a", "b", "c", "d");
        IndexKey other2 = orig.delete();
        IndexKey other3 = orig.newVersion(123L);
        verifyEqualsHashCode(orig, copy, other1, other2, other3);
        verifyComparable(orig, other3);
    }

    @Test
    public void simpleDocEqHash() {
        SimpleDoc orig = new SimpleDoc(keyWith("a", "b", "c"), ImmutableMap.of("a", "aa"));
        SimpleDoc copy = new SimpleDoc(keyWith("a", "b", "c"), ImmutableMap.of("a", "aa"));
        SimpleDoc other1 = new SimpleDoc(keyWith("a", "b", "d"), ImmutableMap.of("a", "aa"));
        SimpleDoc other2 = new SimpleDoc(keyWith("a", "b", "c"), ImmutableMap.of("a", "bb"));
        verifyEqualsHashCode(orig, copy, other1, other2);
    }

    @Test
    public void flattenedEqHash() {
        Flattened orig = flattened("a", "b", 1, 0L);
        Flattened copy = flattened("a", "b", 1, 0L);
        Flattened other1 = flattened("a", "b", 1, 1L);
        verifyEqualsHashCode(orig, copy, other1);
    }

    @Test
    public void expressionSimpleEqHash() {
        Expression orig = Expressions.equalsTo("product", "Coffee");
        Expression copy = Expressions.equalsTo("product", "Coffee");
        Expression other1 = Expressions.greaterThan("product", "Coffee");
        Expression other2 = Expressions.equalsTo("beverage", "Coffee");
        Expression other3 = Expressions.equalsTo("product", "Tea");
        verifyEqualsHashCode(orig, copy, other1, other2, other3);
    }

    @Test
    public void expressionCompoundEqHash() {
        Expression orig = Expressions.not(Expressions.in("bets", "a", "b"));
        Expression copy = Expressions.not(Expressions.in("bets", "a", "b"));
        Expression other1 = Expressions.not(Expressions.in("cups", "a", "b"));
        Expression other2 = Expressions.or(Expressions.in("bets", "a", "b"));
        Expression other3 = Expressions.in("product", "Coffee", "Tea");
        verifyEqualsHashCode(orig, copy, other1, other2, other3);
    }

    @Test
    public void responseHeaderEqHash() {
        ResponseHeader orig = new ResponseHeader(0, 20, 10, "hello");
        ResponseHeader copy = new ResponseHeader(0, 20, 10, "hello");
        ResponseHeader nil = new ResponseHeader(0, 20, 10, null);
        ResponseHeader nilCopy = new ResponseHeader(0, 20, 10, null);
        ResponseHeader other1 = new ResponseHeader(0, 10, 10, "hello");
        ResponseHeader other2 = new ResponseHeader(0, 20, 20, "hello");
        ResponseHeader other3 = new ResponseHeader(0, 20, 10, "notsame");
        verifyEqualsHashCode(orig, copy, nil, other1, other2, other3);
        verifyEqualsHashCode(nil, nilCopy, other3);
    }

    @Test
    public void standardQueryEqHash() {
        StandardQuery orig = queryBuilder().build();
        StandardQuery copy = queryBuilder().build();
        StandardQuery other = queryBuilder().index("hello").build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void standardScrollEqHash() {
        StandardScroll orig = new StandardScroll("a", 1L);
        StandardScroll copy = new StandardScroll("a", 1L);
        StandardScroll other1 = new StandardScroll("b", 1L);
        StandardScroll other2 = new StandardScroll("a", 2L);
        verifyEqualsHashCode(orig, copy, other1, other2);
    }

    @Test
    public void queryResponseEqHash() {
        QueryResponse orig = responseBuilder().header(new ResponseHeader(0, 20, 10, "orig")).build();
        QueryResponse copy = responseBuilder().header(new ResponseHeader(0, 20, 10, "orig")).build();
        QueryResponse other = responseBuilder().header(new ResponseHeader(0, 20, 10, "other")).build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void aggregationEqHash() {
        Aggregation orig = aggregationBuilder().label("a").type(AggregationType.Avg).value("Avg", 25).build();
        Aggregation copy = aggregationBuilder().label("a").type(AggregationType.Avg).value("Avg", 25).build();
        Aggregation other = aggregationBuilder().label("b").type(AggregationType.Avg).value("Avg", 25).build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void bucketEqHash() {
        Bucket orig = bucketBuilder().key("a").keyValue(1).docCount(1L).build();
        Bucket copy = bucketBuilder().key("a").keyValue(1).docCount(1L).build();
        Bucket other = bucketBuilder().key("b").keyValue(1).docCount(1L).build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void hintsEqHash() {
        QueryHints orig = hintsBuilder().build();
        QueryHints copy = hintsBuilder().build();
        QueryHints other = hintsBuilder().searchType(SearchType.SCAN).build();
        verifyEqualsHashCode(orig, copy, other);
    }

    @Test
    public void sortEqHash() {
        Sort orig = Sort.sortAsc("a");
        Sort copy = Sort.sortAsc("a");
        Sort other = Sort.sortDesc("a");
        verifyEqualsHashCode(orig, copy, other);
    }

}