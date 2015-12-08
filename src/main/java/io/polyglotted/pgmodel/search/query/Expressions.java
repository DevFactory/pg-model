package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static io.polyglotted.pgmodel.search.index.HiddenFields.EXPIRY_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.STATUS_FIELD;
import static io.polyglotted.pgmodel.search.query.Expression.withArray;
import static io.polyglotted.pgmodel.search.query.Expression.withLabel;
import static io.polyglotted.pgmodel.search.query.Expression.withMap;
import static io.polyglotted.pgmodel.search.query.Expression.withOnlyChildren;
import static io.polyglotted.pgmodel.search.query.Expression.withValue;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public abstract class Expressions {

    public static Expression liveIndex() {
        return and(missing(STATUS_FIELD.name()), missing(EXPIRY_FIELD.name()));
    }

    public static Expression archiveIndex() {
        return and(exists(STATUS_FIELD.name()), exists(EXPIRY_FIELD.name()));
    }

    public static Expression all() {
        return withLabel(ExpressionType.All, "");
    }

    public static Expression ids(String... ids) {
        return ids(asList(ids));
    }

    public static Expression ids(Iterable<String> ids) {
        return withValue(ExpressionType.Ids, "", ImmutableList.copyOf(ids));
    }

    public static Expression equalsTo(String field, Object value) {
        return withValue(ExpressionType.Eq, field, value);
    }

    public static Expression greaterThanEquals(String field, Object value) {
        return withValue(ExpressionType.Gte, field, value);
    }

    public static Expression greaterThan(String field, Object value) {
        return withValue(ExpressionType.Gt, field, value);
    }

    public static Expression lessThanEquals(String field, Object value) {
        return withValue(ExpressionType.Lte, field, value);
    }

    public static Expression lessThan(String field, Object value) {
        return withValue(ExpressionType.Lt, field, value);
    }

    public static Expression prefix(String field, Object value) {
        return withValue(ExpressionType.Prefix, field, value);
    }

    public static Expression notEquals(String field, Object value) {
        return withValue(ExpressionType.Ne, field, value);
    }

    @SafeVarargs
    public static <E extends Comparable<E>> Expression in(String field, E... values) {
        return in(field, asList(values));
    }

    public static <E extends Comparable<E>> Expression in(String field, List<E> values) {
        return withArray(ExpressionType.In, field, ImmutableList.copyOf(values));
    }

    public static Expression between(String field, Object from, Object to) {
        return between(field, from, to, true, false);
    }

    public static Expression between(String field, Object from, Object to, boolean fromIncl, boolean toIncl) {
        return withMap(ExpressionType.Between.name(), field,
           ImmutableMap.of("from", from, "to", to, "fromIncl", fromIncl, "toIncl", toIncl));
    }

    public static Expression textAnywhere(Object value) {
        return textAnywhere("", value);
    }

    public static Expression textAnywhere(String field, Object value) {
        return withValue(ExpressionType.Text, field, value);
    }

    public static Expression regex(String field, String expr) {
        return withValue(ExpressionType.Regex, field, expr);
    }

    public static Expression exists(String field) {
        return withLabel(ExpressionType.Exists, field);
    }

    public static Expression missing(String field) {
        return withLabel(ExpressionType.Missing, field);
    }

    public static Expression type(String type) {
        return withLabel(ExpressionType.Type, type);
    }

    public static Expression json(String json) {
        return withValue(ExpressionType.Json, "", json);
    }

    public static Expression and(Expression... expressions) {
        return and(asList(expressions));
    }

    public static Expression and(Iterable<Expression> expressions) {
        return withOnlyChildren(ExpressionType.And, "", expressions);
    }

    public static Expression or(Expression... expressions) {
        return or(asList(expressions));
    }

    public static Expression or(Iterable<Expression> expressions) {
        return withOnlyChildren(ExpressionType.Or, "", expressions);
    }

    public static Expression not(Expression expression) {
        return withOnlyChildren(ExpressionType.Not, "", singletonList(expression));
    }

    public static Expression nested(String path, Expression expression) {
        return withOnlyChildren(ExpressionType.Nested, path, singletonList(expression));
    }

    public static Expression hasParent(String parentType, Expression expression) {
        return withOnlyChildren(ExpressionType.HasParent, parentType, singletonList(expression));
    }

    public static Expression hasChild(String childType, Expression expression) {
        return withOnlyChildren(ExpressionType.HasChild, childType, singletonList(expression));
    }
}