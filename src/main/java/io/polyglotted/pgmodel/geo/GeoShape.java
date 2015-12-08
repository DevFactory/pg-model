package io.polyglotted.pgmodel.geo;

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
public final class GeoShape {
    public final GeoType type;
    public final String coordinates;
    public final String radius;

    @Override
    public boolean equals(Object o) {
        return jsonEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, coordinates, radius);
    }

    public static Builder shapeBuilder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private GeoType type;
        private String coordinates;
        private String radius;

        public GeoShape build() {
            return new GeoShape(checkNotNull(type, "type is required"), checkNotNull(coordinates,
               "coordinates is required"), radius);
        }
    }
}
