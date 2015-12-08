package io.polyglotted.pgmodel.search.query;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

import static io.polyglotted.pgmodel.util.ModelUtil.serialize;

@RequiredArgsConstructor
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class ResponseHeader {
    public final long tookInMillis;
    public final long totalHits;
    public final long returnedHits;
    public final String scrollId;

    @Override
    public boolean equals(Object o) {
        return this == o || (!(o == null || getClass() != o.getClass()) &&
           equalizer().equals(((ResponseHeader) o).equalizer()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(tookInMillis, totalHits, returnedHits, scrollId);
    }

    private String equalizer() {
        return serialize(ImmutableList.of(totalHits, returnedHits, scrollId == null ? "" : scrollId));
    }
}
