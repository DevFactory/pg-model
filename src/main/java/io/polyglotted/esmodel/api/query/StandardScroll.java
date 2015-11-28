package io.polyglotted.esmodel.api.query;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static io.polyglotted.esmodel.api.ModelUtil.jsonEquals;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class StandardScroll {
    public final String scrollId;
    public final long scrollTimeInMillis;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scrollId, scrollTimeInMillis);
    }

    public static StandardScroll fromScrollId(String scrollId) {
        return new StandardScroll(scrollId, MINUTES.toMillis(5));
    }
}
