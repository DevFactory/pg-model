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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;
import static java.util.Arrays.asList;

@ToString(includeFieldNames = false)
@RequiredArgsConstructor
public final class Policy {
    public final String urn;
    public final ImmutableList<String> resources;
    public final ImmutableList<String> operations;
    public final ImmutableList<Rule> rules;
    public final Effect effect;
    public final boolean enabled;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urn, resources, operations, rules, effect);
    }

    public static Builder policyBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String urn;
        private final List<String> resources = new ArrayList<>();
        private final List<String> operations = new ArrayList<>();
        private final List<Rule> rules = new ArrayList<>();
        private Effect defaultEffect = Effect.DENY;
        private boolean enabled = true;

        public Builder resource(String... resources) {
            this.resources.addAll(asList(resources));
            return this;
        }

        public Builder operation(String... operations) {
            this.operations.addAll(asList(operations));
            return this;
        }

        public Builder rule(Rule... rules) {
            this.rules.addAll(asList(rules));
            return this;
        }

        public Builder rule(Rule.Builder... builders) {
            this.rules.addAll(transform(asList(builders), Rule.Builder::build));
            return this;
        }

        public Policy build() {
            return new Policy(checkNotNull(urn), copyOf(resources), copyOf(operations),
               copyOf(rules), checkNotNull(defaultEffect), enabled);
        }
    }
}
