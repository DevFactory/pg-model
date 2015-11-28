package io.polyglotted.esmodel.api.query;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static io.polyglotted.esmodel.api.ModelUtil.jsonEquals;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class ResponseHeader {
    public final long tookInMillis;
    public final long totalHits;
    public final long returnedHits;
    public final String scrollId;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tookInMillis, totalHits, returnedHits, scrollId);
    }
}
