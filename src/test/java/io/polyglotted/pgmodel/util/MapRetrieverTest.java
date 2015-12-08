package io.polyglotted.pgmodel.util;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.ac.Environment;
import io.polyglotted.pgmodel.ac.Subject;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Map;

import static io.polyglotted.pgmodel.ac.Subject.subjectBuilder;
import static java.time.Instant.ofEpochMilli;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class MapRetrieverTest extends MapRetriever {

    @DataProvider
    public static Object[][] mapInputs() {
        return new Object[][]{
           {stringDoubleMap(), "z", 1.2},
           {simple(101, "bob"), "id", 101},
           {variable("tplMap", nativeStringInnerMap()), "value.a3.name", "dave"},
           {varMap(), "xv", true},
           {varMap(), "s2.name", "bill"},
           {varMap(), "notFound", null},
           {variable("tplMap", nativeStringInnerMap()), "value.notFound.item", null},
        };
    }

    @Test(dataProvider = "mapInputs")
    public void testRetrieve(Object map, String prop, Object expected) {
        assertThat(deepRetrieve(map, prop), is(expected));
    }

    @Test
    public void retrieveSpecials() {
        Subject subject = subjectBuilder().principal("tester").attribute("name", "unit tester")
           .attribute("varMap", varMap()).build();
        assertThat(subject.attribute("varMap.notFound"), is(nullValue()));
        assertThat(subject.attribute("varMap.xv"), is(true));
        assertThat(subject.attribute("varMap.s2.name"), is("bill"));

        Environment environment = Environment.from(ImmutableMap.of("API_REF", 25, "RESOURCE", "myResource",
           "b", true, "c", stringDoubleMap()), Clock.fixed(ofEpochMilli(1442955118895L), ZoneOffset.UTC));
        assertThat(environment.apiRef(), is(25));
        assertThat(environment.resource(), is("myResource"));
        assertThat(environment.property("c.y"), is(2.4));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
       expectedExceptionsMessageRegExp = "property cannot begin with a dot.*")
    public void testRetrieveIllegalProperty() {
        deepRetrieve(varMap(), ".value");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
       expectedExceptionsMessageRegExp = "path .* does not refer to a map or object field")
    public void testRetrieveNonMapProperty() {
        deepRetrieve(varMap(), "y.z");
    }

    static Map<String, Object> varMap() {
        return ImmutableMap.of("z", 10, "y", 2.4, "xv", true, "s2", simple(102, "bill"), "s3", simple(103, "dave"));
    }

    static ImmutableMap<String, Double> stringDoubleMap() {
        return ImmutableMap.of("z", 1.2, "y", 2.4);
    }

    static ImmutableMap<String, SimpleClass> nativeStringInnerMap() {
        return ImmutableMap.of("a3", simple(103, "dave"));
    }

    static SimpleClass simple(int id, String name) {
        return new SimpleClass(id, name);
    }

    static VariableClass variable(String key, Object value) {
        return new VariableClass(key, value);
    }

    @ToString
    @RequiredArgsConstructor
    @EqualsAndHashCode(doNotUseGetters = true)
    static class SimpleClass {
        public final int id;
        public final String name;
    }

    @ToString
    @RequiredArgsConstructor
    @EqualsAndHashCode(doNotUseGetters = true)
    static final class VariableClass {
        public final String key;
        public final Object value;
    }
}