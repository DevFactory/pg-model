package io.polyglotted.pgmodel.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.lang.reflect.Modifier.isVolatile;

public abstract class ReflectionUtil {
    private static final Map<Class<?>, Object> JAVA_DEFAULTS = ImmutableMap.<Class<?>, Object>builder()
       .put(Boolean.TYPE, false).put(Character.TYPE, Character.MIN_VALUE)
       .put(Byte.TYPE, Byte.valueOf("0")).put(Short.TYPE, Short.valueOf("0"))
       .put(Integer.TYPE, 0).put(Long.TYPE, Long.valueOf("0"))
       .put(Float.TYPE, Float.valueOf("0.0")).put(Double.TYPE, 0.0).build();

    public static Class<?> safeForName(String className) {
        try {
            return className == null ? null : Class.forName(className);
        } catch (ClassNotFoundException cfe) {
            return null;
        }
    }

    @SneakyThrows
    public static Object create(Class<?> clazz) {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        return (constructor.getParameterCount() == 0) ?
           constructor.newInstance() : initWithArgs(constructor);
    }

    public static Object initWithArgs(Constructor<?> constructor) throws Exception {
        Class<?>[] params = constructor.getParameterTypes();
        Object[] initargs = new Object[params.length];
        for (int i = 0; i < params.length; i++)
            initargs[i] = JAVA_DEFAULTS.get(params[i]);
        return constructor.newInstance(initargs);
    }

    public static Object asEnum(Class<?> clazz, int index, Object defValue) {
        return isEnum(clazz) ? clazz.getEnumConstants()[index] : defValue;
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz != null && (clazz.isEnum() || Enum.class.isAssignableFrom(clazz));
    }

    public static boolean isAssignable(Class<?> from, Class<?> to) {
        return to != null && from.isAssignableFrom(to);
    }

    public static <T> T fieldValue(Object object, String fieldName) {
        return fieldValue(object, declaredField(object.getClass(), fieldName));
    }

    @SuppressWarnings("unchecked")
    public static <T> T fieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception e) {
            throw new IllegalStateException("unable to find field value for " + field, e);
        }
    }

    public static <T> T fieldValue(T object, String fieldName, Object value) {
        return fieldValue(object, declaredField(object.getClass(), fieldName), value);
    }

    public static <T> T fieldValue(T object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
            return object;
        } catch (Exception e) {
            throw new IllegalStateException("unable to set field value for " + field, e);
        }
    }

    public static Field declaredField(Class<?> clazz, String name) {
        Field result = null;
        while (clazz != Object.class) {
            try {
                result = clazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return result;
    }

    public static Map<String, Object> fieldValues(Object object) {
        ImmutableSortedMap.Builder<String, Object> builder = ImmutableSortedMap.naturalOrder();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (isFieldSerializable(field)) {
                Object value = fieldValue(object, field);
                if(value != null) builder.put(field.getName(), value);
            }
        }
        return builder.build();
    }

    public static boolean isFieldSerializable(Field field) {
        int modifiers = field.getModifiers();
        return !(isStatic(modifiers) || isTransient(modifiers) || isVolatile(modifiers));
    }
}
