package io.polyglotted.pgmodel.search;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.polyglotted.pgmodel.search.index.*;
import io.polyglotted.pgmodel.search.query.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.time.*;
import java.util.*;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ExtnGenerator {

    private static final Map<Class<?>, Integer> CoreRefs = ImmutableMap.<Class<?>, Integer>builder()
       .put(Boolean.class, -2).put(Integer.class, -3).put(Short.class, -3).put(Byte.class, -3)
       .put(Long.class, -4).put(BigInteger.class, -4).put(Float.class, -5).put(Double.class, -6)
       .put(String.class, -7).put(byte[].class, -8).put(LocalDate.class, -9).put(LocalTime.class, -10)
       .put(OffsetTime.class, -10).put(ZonedDateTime.class, -11).put(LocalDateTime.class, -11)
       .put(OffsetDateTime.class, -11).put(Date.class, -11).put(UUID.class, -12).put(BigDecimal.class, -13)
       .put(Inet4Address.class, -20).put(Inet6Address.class, -20).build();
    private static final int TplArrayRef = -14;
    private static final int TplSetRef = -15;
    private static final int TplMapRef = -16;
    private static final int VarArrayRef = -17;
    private static final int VarRef = -19;
    private static final int SEARCH_START = -38;

    private static final ImmutableMap<Class<?>, String> SearchClasses = ImmutableMap.<Class<?>, String>builder()
       .put(Expression.class, "search expression")
       .put(Alias.class, "index alias")
       .put(Script.class, "transform or search script")
       .put(IndexKey.class, "index key")
       .put(Sleeve.class, "object sleeve")
       .put(SortOrder.class, "sort order constants")
       .put(SortMode.class, "sort mode constants")
       .put(Sort.class, "simple sort")
       .put(SearchOptions.class, "search options constants")
       .put(SearchType.class, "search type constants")
       .put(QueryHints.class, "query hints")
       .put(Aggregation.class, "search aggregation")
       .put(Bucket.class, "aggregation buckets")
       .put(ResponseHeader.class, "search response header")
       .put(SimpleDoc.class, "simple search document")
       .put(Flattened.class, "flattened search aggregation")
       .put(StandardQuery.class, "standard search query")
       .put(StandardScroll.class, "standard scroll query")
       .put(QueryResponse.class, "search query response")
       .put(Indexed.class, "field mapping indexed")
       .put(FieldType.class, "field mapping type")
       .put(FieldMapping.class, "field mapping")
       .build();

    private static final ImmutableMap<Class<?>, List<String>> Requireds = ImmutableMap.<Class<?>, List<String>>builder()
       .put(Expression.class, asList("operation", "label"))
       .put(Alias.class, asList("alias", "remove"))
       .put(Script.class, singletonList("script"))
       .put(IndexKey.class, asList("index", "type", "id", "version", "delete"))
       .put(Sleeve.class, asList("key", "stored"))
       .put(Sort.class, asList("field", "order", "mode"))
       .put(QueryHints.class, asList("searchOptions", "searchType", "timeoutInSeconds", "fetchSource"))
       .put(Aggregation.class, asList("label", "type", "value"))
       .put(Bucket.class, asList("key", "value", "count", "errors"))
       .put(ResponseHeader.class, asList("tookInMillis", "totalHits", "returnedHits"))
       .put(SimpleDoc.class, singletonList("key"))
       .put(StandardQuery.class, asList("queryHints", "offset", "size"))
       .put(StandardScroll.class, asList("scrollId", "scrollTimeInMillis"))
       .put(QueryResponse.class, singletonList("header"))
       .put(FieldMapping.class, asList("field", "type"))
       .build();

    private static final ImmutableMap<Class<?>, Map<String, String>> Defaulteds = ImmutableMap.<Class<?>, Map<String, String>>builder()
       .put(Alias.class, of("remove", "false"))
       .put(IndexKey.class, of("index", "", "delete", "false"))
       .put(Sleeve.class, of("stored", "true"))
       .put(Sort.class, of("order", "ASC", "mode", "NONE"))
       .put(QueryHints.class, of("searchOptions", "LENIENT_EXPAND_OPEN", "searchType", "QUERY_THEN_FETCH",
          "timeoutInSeconds", "10", "fetchSource", "true"))
       .put(Bucket.class, of("count", "-1", "errors", "0"))
       .put(StandardQuery.class, of("offset", "0", "size", "10"))
       .put(StandardScroll.class, of("scrollTimeInMillis", "5000"))
       .put(FieldMapping.class, of("type", "STRING"))
       .build();

    private static final ImmutableMap<Class<?>, List<String>> StoreTransients = ImmutableMap.<Class<?>, List<String>>builder()
       .put(FieldMapping.class, asList("indexed", "analyzer", "copyTo", "docValues", "includeInAll", "hasFields",
          "stored", "argsMap", "properties"))
       .build();

    private static Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static void main(String ar[]) {

        int traitRefStart = SEARCH_START;
        Map<Class<?>, Integer> localTraits = new LinkedHashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Class<?>, String> entry : SearchClasses.entrySet()) {
            Class<?> clazz = entry.getKey();
            localTraits.put(clazz, traitRefStart);
            boolean isConst = isEnum(clazz);

            Map<String, Object> inner = new TreeMap<>();
            inner.put("_type", isConst ? "constant" : "definition");
            inner.put("desc", entry.getValue());
            inner.put("fqn", toTraitFqn(clazz.getSimpleName()));
            if (!isConst) inner.put("properties", props(clazz, localTraits));
            inner.put("tags", tagsFrom(clazz));
            inner.put("traitRef", traitRefStart--);
            inner.put("user", "System");
            if (isConst) inner.put("values", enumValues(clazz));

            result.add(inner);
        }
        System.out.println(GSON.toJson(result));
    }

    private static List<Map<String, Object>> props(Class<?> clazz, Map<Class<?>, Integer> localTraits) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;

            Map<String, Object> fieldMap = new TreeMap<>();
            fieldMap.put("id", field.getName());

            Map<String, String> defaulteds = Defaulteds.get(clazz);
            if (defaulteds != null && defaulteds.containsKey(field.getName()))
                fieldMap.put("defaultValue", defaulteds.get(field.getName()));

            List<String> requireds = Requireds.get(clazz);
            if (requireds != null && requireds.contains(field.getName())) fieldMap.put("required", true);

            List<String> transients = StoreTransients.get(clazz);
            if (transients != null && transients.contains(field.getName()))
                fieldMap.put("tags", ImmutableMap.of("objectstore.transient", true));

            Class<?> fieldClass = Primitives.wrap(field.getType());
            int traitRef;
            if ((traitRef = getBasicOrLocal(fieldClass, localTraits)) > -128)
                fieldMap.put("traitRef", traitRef);
            else if (ImmutableList.of("array", "creates", "results").contains(field.getName()))  //TODO better?
                fieldMap.put("traitRef", VarArrayRef);
            else
                handleTemplated(fieldMap, fieldClass, field.getGenericType(), localTraits);

            result.add(fieldMap);
        }
        return result;
    }

    private static int getBasicOrLocal(Class<?> fieldClass, Map<Class<?>, Integer> localTraits) {

        if (fieldClass == Object.class) {
            return VarRef;
        } else if (CoreRefs.containsKey(fieldClass)) {
            return CoreRefs.get(fieldClass);
        } else if (localTraits.containsKey(fieldClass)) {
            return localTraits.get(fieldClass);
        }
        return -128;
    }

    private static void handleTemplated(Map<String, Object> fieldMap, Class<?> fieldClass, Type
       genericType, Map<Class<?>, Integer> localTraits) {

        Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
        Class<?> keyClass = getTypeArg(typeArgs[0]);

        if (Map.class.isAssignableFrom(fieldClass)) {
            fieldMap.put("traitRef", TplMapRef);
            fieldMap.put("keyRef", getBasicOrLocal(keyClass, localTraits));
            fieldMap.put("valueRef", getBasicOrLocal(getTypeArg(typeArgs[1]), localTraits));
        } else if (List.class.isAssignableFrom(fieldClass)) {
            fieldMap.put("traitRef", TplArrayRef);
            fieldMap.put("keyRef", getBasicOrLocal(keyClass, localTraits));
        } else if (Set.class.isAssignableFrom(fieldClass)) {
            fieldMap.put("traitRef", TplSetRef);
            fieldMap.put("keyRef", getBasicOrLocal(keyClass, localTraits));
        }
    }

    private static Class<?> getTypeArg(Type typeArg) {
        return (typeArg instanceof Class<?>) ? (Class<?>) typeArg : Object.class;
    }

    private static List<String> enumValues(Class<?> clazz) {
        return newArrayList(transform(transform(asList(clazz.getEnumConstants()), Object::toString), String::toUpperCase));
    }

    private static boolean isEnum(Class<?> clazz) {
        return clazz != null && (clazz.isEnum() || Enum.class.isAssignableFrom(clazz));
    }

    private static Map<String, Object> tagsFrom(Class<?> clazz) {
        Map<String, Object> tags = new LinkedHashMap<>();
        tags.put("javaClass", clazz.getName());
        return tags;
    }

    private static String toTraitFqn(String className) {
        return "search/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
    }
}
