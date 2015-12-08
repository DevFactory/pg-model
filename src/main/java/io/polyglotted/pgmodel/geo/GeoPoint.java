package io.polyglotted.pgmodel.geo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static io.polyglotted.pgmodel.util.ModelUtil.doubleEquals;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class GeoPoint {
    private static final Pattern GEO_JSON = Pattern.compile("([-+]?[0-9]*\\.?[0-9]+)");
    public final double lat;
    public final double lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoPoint geoPoint = (GeoPoint) o;
        return doubleEquals(lat, geoPoint.lat) && doubleEquals(lon, geoPoint.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return "[" + lon + "," + lat + "]";
    }

    public String geoHash() {
        return GeoHash.encode(lat, lon);
    }

    public static GeoPoint geoPointFromString(String value) {
        Matcher matcher = GEO_JSON.matcher(value);
        checkArgument(matcher.find(), "cannot find longitude");
        Double longitude = Double.parseDouble(matcher.group());
        checkArgument(matcher.find(), "cannot find longitude");
        Double latitude = Double.parseDouble(matcher.group());
        checkArgument(!matcher.find());

        return new GeoPoint(latitude, longitude);
    }
}

