package io.polyglotted.pgmodel.search.index;

import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.util.ModelUtil.serialize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HiddenFieldsTest extends HiddenFields {

    @Test
    public void testHiddenFields() {
        String serialize = serialize(hiddenFields());
        assertThat(serialize, serialize, is("[{\"field\":\"&ancestor\",\"type\":\"STRING\",\"indexed\":" +
           "\"NOT_ANALYZED\",\"docValues\":true,\"includeInAll\":false,\"argsMap\":{},\"properties\":{}}," +
           "{\"field\":\"&bytes\",\"type\":\"BINARY\",\"argsMap\":{},\"properties\":{}},{\"field\":\"&expiry\"," +
           "\"type\":\"DATE\",\"includeInAll\":false,\"argsMap\":{},\"properties\":{}},{\"field\":\"&status\"," +
           "\"type\":\"STRING\",\"indexed\":\"NOT_ANALYZED\",\"docValues\":true,\"includeInAll\":false," +
           "\"argsMap\":{},\"properties\":{}},{\"field\":\"&timestamp\",\"type\":\"DATE\",\"includeInAll\":false," +
           "\"argsMap\":{},\"properties\":{}},{\"field\":\"&updater\",\"type\":\"STRING\",\"indexed\":" +
           "\"NOT_ANALYZED\",\"includeInAll\":false,\"argsMap\":{},\"properties\":{}},{\"field\":\"&user\"," +
           "\"type\":\"STRING\",\"indexed\":\"NOT_ANALYZED\",\"docValues\":true,\"includeInAll\":false," +
           "\"argsMap\":{},\"properties\":{}}]"));
    }
}