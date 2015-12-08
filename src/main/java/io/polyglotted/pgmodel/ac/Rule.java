package io.polyglotted.pgmodel.ac;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.transform;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;
import static java.util.Arrays.asList;

@ToString(includeFieldNames = false)
@RequiredArgsConstructor
public final class Rule {
    public final Effect effect;
    public final ImmutableList<Condition> conditions;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions, effect);
    }

    public static Builder ruleBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private Effect effect;
        private final List<Condition> conditions = new ArrayList<>();

        public Builder condition(Condition... conditions) {
            this.conditions.addAll(asList(conditions));
            return this;
        }

        public Builder condition(Condition.Builder... builders) {
            this.conditions.addAll(transform(asList(builders), Condition.Builder::build));
            return this;
        }

        public Rule build() {
            return new Rule(effect, ImmutableList.copyOf(conditions));
        }
    }
}
