package io.polyglotted.pgmodel.search.index;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.polyglotted.pgmodel.search.index.FieldMapping.notAnalyzedStringField;
import static io.polyglotted.pgmodel.search.index.FieldMapping.simpleField;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public enum HiddenFields {
    ANCESTOR_FIELD("&ancestor") {
        @Override
        public FieldMapping toMapping() {
            return notAnalyzedStringField(name()).docValues(true).includeInAll(false).build();
        }
    },
    BYTES_FIELD("&bytes") {
        @Override
        public FieldMapping toMapping() {
            return simpleField(name(), FieldType.BINARY).build();
        }
    },
    EXPIRY_FIELD("&expiry") {
        @Override
        public FieldMapping toMapping() {
            return simpleField(name(), FieldType.DATE).includeInAll(false).build();
        }
    },
    STATUS_FIELD("&status") {
        @Override
        public FieldMapping toMapping() {
            return notAnalyzedStringField(name()).docValues(true).includeInAll(false).build();
        }
    },
    TIMESTAMP_FIELD("&timestamp") {
        @Override
        public FieldMapping toMapping() {
            return simpleField(name(), FieldType.DATE).includeInAll(false).build();
        }
    },
    USER_FIELD("&user") {
        @Override
        public FieldMapping toMapping() {
            return notAnalyzedStringField(name()).docValues(true).includeInAll(false).build();
        }
    };

    public final String value;
    abstract FieldMapping toMapping();

    public static List<FieldMapping> hiddenMappings() {
        return Lists.transform(asList(values()), HiddenFields::toMapping);
    }
}
