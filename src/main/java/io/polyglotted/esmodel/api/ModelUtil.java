package io.polyglotted.esmodel.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ModelUtil {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
       .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer<>(ImmutableList::copyOf))
       .registerTypeAdapter(ImmutableMap.class, new ImmutableMapDeserializer())
       .registerTypeAdapter(Double.class, new DoubleSerializer())
       .create();

    public static String serialize(Object o) {
        return GSON.toJson(o);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static boolean jsonEquals(Object c, Object o) {
        return c == o || !(o == null || c.getClass() != o.getClass()) && serialize(c).equals(serialize(o));
    }

    @RequiredArgsConstructor
    public static final class ImmutableListDeserializer<E> implements JsonDeserializer<E> {
        private final Function<List<?>, E> function;

        @Override
        public E deserialize(final JsonElement json, final Type
           type, final JsonDeserializationContext context) throws JsonParseException {

            final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            final Type parameterizedType = listOf(typeArguments[0]).getType();

            return function.apply(context.deserialize(json, parameterizedType));
        }
    }

    @SuppressWarnings("unchecked")
    private static <E> TypeToken<List<E>> listOf(final Type arg) {
        return new TypeToken<List<E>>() {}
           .where(new TypeParameter<E>() {}, (TypeToken<E>) TypeToken.of(arg));
    }

    public static final class ImmutableMapDeserializer implements JsonDeserializer<ImmutableMap<?, ?>> {
        @Override
        public ImmutableMap<?, ?> deserialize(final JsonElement json, final Type
           type, final JsonDeserializationContext context) throws JsonParseException {
            return ImmutableMap.copyOf(context.deserialize(json, mapTypeOf(type)));
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Type mapTypeOf(final Type type) {
        final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        return new TypeToken<Map<K, V>>() {}
           .where(new TypeParameter<K>() {}, (TypeToken<K>) TypeToken.of(typeArguments[0]))
           .where(new TypeParameter<V>() {}, (TypeToken<V>) TypeToken.of(typeArguments[1]))
           .getType();
    }

    private static class DoubleSerializer implements JsonSerializer<Double> {
        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            if(src == src.longValue())
                return new JsonPrimitive(src.longValue());
            return new JsonPrimitive(src);
        }
    }
}
