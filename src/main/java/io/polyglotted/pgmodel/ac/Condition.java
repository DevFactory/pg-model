package io.polyglotted.pgmodel.ac;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;

@ToString(includeFieldNames = false)
@RequiredArgsConstructor
public final class Condition {
    public final String attribute;
    public final Function function;
    public final Object value;
    public final boolean negate;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, function, value, negate);
    }

    public static Builder conditionBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String attribute;
        private Function function = Function.EQUALS;
        private Object value;
        private boolean negate = false;

        public Condition build() {
            return new Condition(checkNotNull(attribute, "attribute cannot be null"), function,
               checkNotNull(value, "value cannot be null"), negate);
        }
    }
}
