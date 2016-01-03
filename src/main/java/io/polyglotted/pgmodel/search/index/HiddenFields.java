package io.polyglotted.pgmodel.search.index;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.polyglotted.pgmodel.search.DocStatus;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static io.polyglotted.pgmodel.search.index.FieldMapping.notAnalyzedStringField;
import static io.polyglotted.pgmodel.search.index.FieldMapping.simpleField;
import static io.polyglotted.pgmodel.search.index.FieldType.LONG;
import static io.polyglotted.pgmodel.search.index.FieldType.STRING;

public abstract class HiddenFields {
    public static final String ANCESTOR_FIELD = "&ancestor";
    public static final String APPROVAL_ROLES_FIELD = "&approvalRoles";
    public static final String BASEKEY_FIELD = "&baseKey";
    public static final String BASEVERSION_FIELD = "&baseVersion";
    public static final String BYTES_FIELD = "&bytes";
    public static final String COMMENT_FIELD = "&comment";
    public static final String EXPIRY_FIELD = "&expiry";
    public static final String STATUS_FIELD = "&status";
    public static final String TIMESTAMP_FIELD = "&timestamp";
    public static final String UNIQUE_FIELD = "&uniqueId";
    public static final String UPDATER_FIELD = "&updater";
    public static final String USER_FIELD = "&user";

    public static List<FieldMapping> hiddenFields() {
        return ImmutableList.of(
           notAnalyzedStringField(ANCESTOR_FIELD).docValues(true).includeInAll(false).build(),
           notAnalyzedStringField(BASEKEY_FIELD).docValues(null).includeInAll(false).build(),
           simpleField(BYTES_FIELD, FieldType.BINARY).build(),
           simpleField(EXPIRY_FIELD, FieldType.DATE).includeInAll(false).build(),
           notAnalyzedStringField(STATUS_FIELD).docValues(true).includeInAll(false).build(),
           simpleField(TIMESTAMP_FIELD, FieldType.DATE).includeInAll(false).build(),
           notAnalyzedStringField(UPDATER_FIELD).docValues(null).includeInAll(false).build(),
           notAnalyzedStringField(USER_FIELD).docValues(true).includeInAll(false).build());
    }

    public static List<FieldMapping> hiddenFieldsForApproval() {
        return ImmutableList.of(
           simpleField(APPROVAL_ROLES_FIELD, STRING).indexed(Indexed.NO).includeInAll(false).build(),
           simpleField(BASEVERSION_FIELD, LONG).docValues(null).includeInAll(false).build(),
           simpleField(COMMENT_FIELD, STRING).indexed(Indexed.NO).includeInAll(false).build());
    }

    @VisibleForTesting
    public static ImmutableMap<String, Object> headerFrom(Map<String, ?> map) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        putVal(map, APPROVAL_ROLES_FIELD, builder);
        putVal(map, reqdProp(map, BASEKEY_FIELD), builder);
        putLongStr(map, BASEVERSION_FIELD, builder);
        putVal(map, COMMENT_FIELD, builder);
        putLongStr(map, EXPIRY_FIELD, builder);
        putStatus(map, STATUS_FIELD, builder);
        putLongStr(map, reqdProp(map, TIMESTAMP_FIELD), builder);
        putVal(map, UNIQUE_FIELD, builder);
        putVal(map, UPDATER_FIELD, builder);
        putVal(map, reqdProp(map, USER_FIELD), builder);
        return builder.build();
    }

    private static void putStatus(Map<String, ?> map, String property, ImmutableMap.Builder<String, Object> builder) {
        if (map.containsKey(property)) builder.put(property, DocStatus.fromStatus((String) map.get(property)));
    }

    private static void putLongStr(Map<String, ?> map, String property, ImmutableMap.Builder<String, Object> builder) {
        if (map.containsKey(property)) builder.put(property, Long.parseLong((String) map.get(property)));
    }

    private static void putVal(Map<String, ?> map, String property, ImmutableMap.Builder<String, Object> builder) {
        if (map.containsKey(property)) builder.put(property, map.get(property));
    }

    private static String reqdProp(Map<String, ?> map, String property) {
        checkArgument(map.containsKey(property), property + " is missing in the map");
        return property;
    }
}
