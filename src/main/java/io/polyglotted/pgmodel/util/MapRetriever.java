package io.polyglotted.pgmodel.util;

import java.lang.reflect.Field;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static io.polyglotted.pgmodel.util.ReflectionUtil.declaredField;
import static io.polyglotted.pgmodel.util.ReflectionUtil.fieldValue;

public abstract class MapRetriever {

    @SuppressWarnings("unchecked")
    public static <T> T deepRetrieve(Object map, String property) {
        checkArgument(!property.startsWith("."), "property cannot begin with a dot");
        if(!property.contains(".")) return mapGetOrReflect(map, property);

        String[] parts = property.split("\\.");

        Object child = map;
        for (int i = 0; i < parts.length - 1; i++) {
            child = mapGetOrReflect(child, parts[i]);
            if(child == null) return null;
        }

        return (T) mapGetOrReflect(child, parts[parts.length - 1]);
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapGetOrReflect(Object object, String property) {
        if (object instanceof Map) return (T) ((Map) object).get(property);

        Field field = declaredField(object.getClass(), property);
        checkArgument(field != null, "path " + property + " does not refer to a map or object field");

        return (T) fieldValue(object, field);
    }
}
