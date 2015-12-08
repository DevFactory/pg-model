package io.polyglotted.pgmodel.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModelUtilTest extends ModelUtil {

    @DataProvider
    public static Object[][] equalInputs() {
        return new Object[][]{
           {true, new Object[]{null, null}},
           {false, new Object[]{null, "hello"}},
           {false, new Object[]{"hello", null}},
           {false, new Object[]{"hello", 32}},
           {true, new Object[]{"hello", "hello", 32, 32}},
        };
    }

    @Test(dataProvider = "equalInputs")
    public void testEqualsAll(boolean expected, Object[] objects) throws Exception {
        assertThat(equalsAll(objects), is(expected));
    }

    @Test
    public void testEqualsEmpty() throws Exception {
        assertThat(equalsAll(), is(true));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEqualsFail() throws Exception {
        equalsAll("hello");
    }
}