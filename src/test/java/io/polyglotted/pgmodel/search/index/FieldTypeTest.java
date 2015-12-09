package io.polyglotted.pgmodel.search.index;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.search.index.FieldMapping.notAnalyzedStringField;
import static io.polyglotted.pgmodel.search.index.FieldMapping.simpleField;
import static io.polyglotted.pgmodel.search.index.FieldType.*;
import static io.polyglotted.pgmodel.search.index.Indexed.ANALYZED;
import static io.polyglotted.pgmodel.search.index.Indexed.NO;
import static io.polyglotted.pgmodel.search.index.Indexed.NOT_ANALYZED;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class FieldTypeTest {

    @DataProvider
    public static Object[][] mappingFailures() {
        return new Object[][]{
           {simpleField("a", STRING).docValues(true).build(), "STRING cannot contain docValues"},
           {simpleField("a", STRING).docValues(true).indexed(ANALYZED).build(), "STRING cannot contain docValues"},
           {simpleField("a", STRING).docValues(true).indexed(ANALYZED).build(), "STRING cannot contain docValues"},
           {simpleField("a", FLOAT).indexed(ANALYZED).build(), "can be only indexed as No"},
           {simpleField("a", DOUBLE).indexed(NOT_ANALYZED).build(), "can be only indexed as No"},
           {simpleField("a", BYTE).indexed(ANALYZED).build(), "can be only indexed as No"},
           {simpleField("a", SHORT).indexed(NOT_ANALYZED).build(), "can be only indexed as No"},
           {simpleField("a", INTEGER).analyzer("hi").build(), "cannot contain analyzer"},
           {simpleField("a", LONG).analyzer("hi").build(), "cannot contain analyzer"},
           {simpleField("a", BOOLEAN).docValues(true).build(), "cannot contain docValues"},
           {simpleField("a", BINARY).analyzer("hi").build(), "cannot contain indexed, analyzer or docValues"},
           {simpleField("a", GEO_POINT).indexed(ANALYZED).build(), "cannot contain indexed, analyzer or docValues"},
           {simpleField("a", GEO_SHAPE).docValues(true).build(), "cannot contain indexed, analyzer or docValues"},
           {simpleField("a", OBJECT).analyzer("hi").build(), "cannot contain indexed, analyzer or docValues"},
           {simpleField("a", NESTED).indexed(NOT_ANALYZED).build(), "cannot contain indexed, analyzer or docValues"},
           {simpleField("a", OBJECT).indexed(NO).build(), "cannot contain indexed, analyzer or docValues"},
        };
    }

    @Test(dataProvider = "mappingFailures")
    public void validateFail(FieldMapping mapping, String message) {
        try {
            mapping.type.validate(mapping);
            fail("cannot validate success");

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            assertTrue(ex.getMessage().contains(message));
        }
    }

    @DataProvider
    public static Object[][] mappingSuccess() {
        return new Object[][]{
           {simpleField("a", STRING).indexed(NO).docValues(true).build()},
           {notAnalyzedStringField("a").build()},
           {simpleField("a", DATE).docValues(true).build()},
           {simpleField("a", IP).docValues(true).build()},
        };
    }

    @Test(dataProvider = "mappingSuccess")
    public void validateSuccess(FieldMapping mapping) {
        mapping.type.validate(mapping);
    }
}