package io.polyglotted.pgmodel.ac;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Maps.filterKeys;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.ACCESS_TOKEN;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.CREDENTIAL;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.DISPLAY_NAME;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.GROUPS;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.ROLES;
import static io.polyglotted.pgmodel.util.MapRetriever.deepRetrieve;
import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;

@ToString(includeFieldNames = false)
@RequiredArgsConstructor
public final class Subject {
    public final String principal;
    public final ImmutableMap<String, Object> attributes;

    public final <T> T attribute(String path) { return deepRetrieve(attributes, path); }

    public final <T> T attribute(SubjectAttribute attribute) { return attribute(attribute.name()); }

    public String credential() { return attribute(CREDENTIAL); }

    public String token() { return attribute(ACCESS_TOKEN); }

    public String user() {
        String displayName = attribute(DISPLAY_NAME);
        return isNullOrEmpty(displayName) ? principal : displayName;
    }

    public List<String> groups() {
        return attributes.containsKey(GROUPS.name()) ? attribute(GROUPS) : ImmutableList.of();
    }

    public Set<String> roles() {
        return attributes.containsKey(ROLES.name()) ? attribute(ROLES) : ImmutableSet.of();
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal, attributes);
    }

    public static Builder subjectBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String principal;
        private final Map<String, Object> attributes = new LinkedHashMap<>();

        public Builder attribute(SubjectAttribute attr, Object value) {
            return attribute(attr.name(), value);
        }

        public Builder attribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public Builder attributes(Map<String, Object> map) {
            this.attributes.putAll(map);
            return this;
        }

        public Subject build() {
            return new Subject(checkNotNull(principal, "principal is required"),
               ImmutableMap.copyOf(filterKeys(attributes, not("principal"::equals))));
        }
    }
}
