package io.polyglotted.pgmodel.ac;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static io.polyglotted.pgmodel.util.ModelUtil.jsonEquals;

@RequiredArgsConstructor
public final class AccessContext {
    public final Subject subject;
    public final Environment environment;

    public int apiRef() {
        return environment.apiRef();
    }

    public String resource() {
        return environment.resource();
    }

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, environment);
    }
}
