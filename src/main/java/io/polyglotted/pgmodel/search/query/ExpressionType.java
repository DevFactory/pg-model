package io.polyglotted.pgmodel.search.query;

public enum ExpressionType {
    All, Ids, Eq, Gte, Gt, Lte, Lt, Prefix, Ne, In, Between, Text, Regex, Exists, Missing,
    Type, Json, And, Or, Not, Nested, HasParent, HasChild
}
