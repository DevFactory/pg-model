package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Iterables.transform;
import static io.polyglotted.pgmodel.search.DocStatus.PENDING;
import static io.polyglotted.pgmodel.search.DocStatus.PENDING_DELETE;
import static io.polyglotted.pgmodel.search.DocStatus.REJECTED;
import static io.polyglotted.pgmodel.search.index.HiddenFields.EXPIRY_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.STATUS_FIELD;
import static io.polyglotted.pgmodel.search.index.HiddenFields.TIMESTAMP_FIELD;
import static io.polyglotted.pgmodel.search.query.Expression.ValueKey;
import static io.polyglotted.pgmodel.search.query.Expression.withArray;
import static io.polyglotted.pgmodel.search.query.Expression.withLabel;
import static io.polyglotted.pgmodel.search.query.Expression.withMap;
import static io.polyglotted.pgmodel.search.query.Expression.withOnlyChildren;
import static io.polyglotted.pgmodel.search.query.Expression.withValue;
import static io.polyglotted.pgmodel.search.query.ExpressionType.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public abstract class Expressions {

    public static Expression liveIndex() {
        return and(missing(STATUS_FIELD), missing(EXPIRY_FIELD), exists(TIMESTAMP_FIELD));
    }

    public static Expression archiveIndex() { return and(exists(STATUS_FIELD), exists(EXPIRY_FIELD)); }

    public static Expression allIndex() { return and(exists(TIMESTAMP_FIELD)); }

    public static Expression pendingApproval() {
        return and(in(STATUS_FIELD, PENDING.name(), PENDING_DELETE.name()), missing(EXPIRY_FIELD));
    }

    public static Expression approvalRejected() {
        return and(equalsTo(STATUS_FIELD, REJECTED.name()), missing(EXPIRY_FIELD));
    }

    public static Expression all() { return withLabel(ExpressionType.All, ""); }

    public static Expression ids(String... ids) { return ids(asList(ids)); }

    public static Expression ids(Iterable<String> ids) { return ids(ImmutableList.of(), ids); }

    public static Expression ids(Iterable<String> types, Iterable<String> ids) {
        return withMap(Ids.name(), "", of(ValueKey, copyOf(ids), "types", copyOf(types)));
    }

    public static Expression equalsTo(String field, Object value) {
        return value == null ? missing(field) : withValue(Eq, field, value);
    }

    public static Expression greaterThanEquals(String field, Object value) { return withValue(Gte, field, value); }

    public static Expression greaterThan(String field, Object value) { return withValue(Gt, field, value); }

    public static Expression lessThanEquals(String field, Object value) { return withValue(Lte, field, value); }

    public static Expression lessThan(String field, Object value) { return withValue(Lt, field, value); }

    public static Expression prefix(String field, Object value) { return withValue(Prefix, field, value); }

    public static Expression notEquals(String field, Object value) {
        return value == null ? exists(field) : withValue(Ne, field, value);
    }

    @SafeVarargs
    public static <E extends Comparable<E>> Expression in(String field, E... vals) { return in(field, asList(vals)); }

    public static <E extends Comparable<E>> Expression in(String field, List<E> values) {
        return withArray(In, field, copyOf(values));
    }

    public static Expression between(String field, Object from, Object to) {
        return between(field, from, to, true, false);
    }

    public static Expression between(String field, Object from, Object to, boolean fromIncl, boolean toIncl) {
        return withMap(Between.name(), field, of("from", from, "to", to, "fromIncl", fromIncl, "toIncl", toIncl));
    }

    public static Expression textAnywhere(Object value) { return textAnywhere("", value); }

    public static Expression textAnywhere(String field, Object value) { return withValue(Text, field, value); }

    public static Expression regex(String field, String expr) {
        return withValue(Regex, field, expr);
    }

    public static Expression exists(String field) { return withLabel(Exists, field); }

    public static Expression missing(String field) { return withLabel(Missing, field); }

    public static Expression types(String... types) { return types.length == 1 ? type(types[0]) : types(asList(types)); }

    public static Expression types(Iterable<String> types) { return or(transform(types, Expressions::type)); }

    public static Expression type(String type) { return withLabel(Type, type); }

    public static Expression json(String json) { return withValue(Json, "", json); }

    public static Expression and(Expression... expressions) { return and(asList(expressions)); }

    public static Expression and(Iterable<Expression> expressions) { return withOnlyChildren(And, "", expressions); }

    public static Expression or(Expression... expressions) { return or(asList(expressions)); }

    public static Expression or(Iterable<Expression> expressions) { return withOnlyChildren(Or, "", expressions); }

    public static Expression not(Expression expression) { return withOnlyChildren(Not, "", singletonList(expression)); }

    public static Expression nested(String path, Expression expression) {
        return withOnlyChildren(Nested, path, singletonList(expression));
    }

    public static Expression hasParent(String parentType, Expression expression) {
        return withOnlyChildren(HasParent, parentType, singletonList(expression));
    }

    public static Expression hasChild(String childType, Expression expression) {
        return withOnlyChildren(HasChild, childType, singletonList(expression));
    }
}
