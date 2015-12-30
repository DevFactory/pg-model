package io.polyglotted.pgmodel.search.index;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static io.polyglotted.pgmodel.search.index.FieldMapping.notAnalyzedStringField;
import static io.polyglotted.pgmodel.search.index.FieldMapping.simpleField;

public abstract class HiddenFields {
    public static final String ANCESTOR_FIELD = "&ancestor";
    public static final String BYTES_FIELD = "&bytes";
    public static final String EXPIRY_FIELD = "&expiry";
    public static final String STATUS_FIELD = "&status";
    public static final String TIMESTAMP_FIELD = "&timestamp";
    public static final String USER_FIELD = "&user";
    public static final String UPDATER_FIELD = "&updater";
    public static final String COMMENT_FIELD = "&comment";
    public static final String UNIQUE_FIELD = "&uniqueid";

    public static List<FieldMapping> hiddenFields() {
        return ImmutableList.of(
           notAnalyzedStringField(ANCESTOR_FIELD).docValues(true).includeInAll(false).build(),
           simpleField(BYTES_FIELD, FieldType.BINARY).build(),
           simpleField(EXPIRY_FIELD, FieldType.DATE).includeInAll(false).build(),
           notAnalyzedStringField(STATUS_FIELD).docValues(true).includeInAll(false).build(),
           simpleField(TIMESTAMP_FIELD, FieldType.DATE).includeInAll(false).build(),
           notAnalyzedStringField(UPDATER_FIELD).docValues(null).includeInAll(false).build(),
           notAnalyzedStringField(USER_FIELD).docValues(true).includeInAll(false).build(),
           notAnalyzedStringField(COMMENT_FIELD).docValues(null).includeInAll(false).build(),
           notAnalyzedStringField(UNIQUE_FIELD).docValues(null).includeInAll(false).build());
    }
}
