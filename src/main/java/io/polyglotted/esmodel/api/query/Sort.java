package io.polyglotted.esmodel.api.query;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.polyglotted.esmodel.api.ModelUtil.jsonEquals;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false, doNotUseGetters = true)
public final class Sort {
    public final String field;
    public final SortOrder order;
    public final SortMode mode;
    public final String unmapped;
    public final Object missing;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, order, mode, unmapped, missing);
    }

    public static Sort sortAsc(String field) {
        return sortBuilder().field(field).build();
    }

    public static Sort sortDesc(String field) {
        return sortBuilder().field(field).order(SortOrder.DESC).build();
    }

    public static Builder sortBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String field;
        private SortOrder order = SortOrder.ASC;
        private SortMode mode = SortMode.NONE;
        private String unmappedType;
        private Object missing;

        public Sort build() {
            return new Sort(checkNotNull(field, "field should not be null"), checkNotNull(order),
               checkNotNull(mode), unmappedType, missing);
        }
    }
}
