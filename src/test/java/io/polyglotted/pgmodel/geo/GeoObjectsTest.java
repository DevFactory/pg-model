package io.polyglotted.pgmodel.geo;

import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.EqualityChecker.verifyEqualsHashCode;
import static io.polyglotted.pgmodel.geo.GeoShape.shapeBuilder;
import static io.polyglotted.pgmodel.util.ModelUtil.doubleEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GeoObjectsTest {

    @Test
    public void geoPointEqHash() {
        GeoPoint orig = new GeoPoint(25.65, 32.78);
        GeoPoint copy = new GeoPoint(25.65, 32.78);
        GeoPoint other = GeoPoint.geoPointFromString("[97.41,20.95]");
        verifyEqualsHashCode(orig, copy, other);
        assertTrue(doubleEquals(orig.lat(), 25.65));
        assertTrue(doubleEquals(orig.lon(), 32.78));
    }

    @Test
    public void geoHashEqHash() {
        GeoHash orig = GeoHash.decodeGeoHash("ek89yj6q4ds3");
        GeoHash copy = GeoHash.decodeGeoHash(new GeoPoint(25.65, -32.78).geoHash());
        GeoHash other = GeoHash.decodeGeoHash("uryzuxzrfrup");
        verifyEqualsHashCode(orig, copy, other);
        assertNotNull(orig.point());
    }

    @Test
    public void geoShapeEqHash() {
        GeoShape orig = shapeBuilder().type(GeoType.POINT).coordinates("[-77.03653, 38.897676]").build();
        GeoShape copy = shapeBuilder().type(GeoType.POINT).coordinates("[-77.03653, 38.897676]").build();
        GeoShape other1 = shapeBuilder().type(GeoType.MULTIPOINT).coordinates("[-77.03653, 38.897676]").build();
        GeoShape other2 = shapeBuilder().type(GeoType.valueOf("CIRCLE")).coordinates("[-90.0, 45.0]").radius("2.5").build();
        verifyEqualsHashCode(orig, copy, other1, other2);
    }
}