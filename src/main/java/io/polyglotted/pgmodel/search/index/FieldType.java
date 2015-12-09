package io.polyglotted.pgmodel.search.index;

import com.google.gson.JsonObject;

import static com.google.common.base.Preconditions.checkArgument;
import static io.polyglotted.pgmodel.search.index.Indexed.ANALYZED;
import static io.polyglotted.pgmodel.search.index.Indexed.NO;

@SuppressWarnings("unused")
public enum FieldType {
    BOOLEAN {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, false);
        }
    },
    STRING {
        @Override
        public void validate(FieldMapping mapping) {
            if (mapping.docValues != null && (mapping.indexed == null || mapping.indexed == ANALYZED))
                throw new IllegalArgumentException("field " + mapping.field
                   + " of type STRING cannot contain docValues if the indexed is not NOT_ANALYZED");
        }
    },
    FLOAT {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    DOUBLE {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    BYTE {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    SHORT {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    INTEGER {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    LONG {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    DATE {
        @Override
        public void extra(JsonObject object) {
            object.addProperty("format", "dateOptionalTime");
        }

        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    BINARY {
        @Override
        public void validate(FieldMapping mapping) {
            nullOnAll(mapping);
        }
    },
    NESTED {
        @Override
        public void validate(FieldMapping mapping) {
            nullOnAll(mapping);
        }
    },
    IP {
        @Override
        public void validate(FieldMapping mapping) {
            noOrNull(mapping, true);
        }
    },
    GEO_POINT {
        @Override
        public void validate(FieldMapping mapping) {
            nullOnAll(mapping);
        }
    },
    GEO_SHAPE {
        @Override
        public void extra(JsonObject object) {
            object.addProperty("tree", "quadtree");
        }

        @Override
        public void validate(FieldMapping mapping) {
            nullOnAll(mapping);
        }
    },
    OBJECT {
        @Override
        public void validate(FieldMapping mapping) {
            nullOnAll(mapping);
        }
    };

    public void extra(JsonObject object) {}

    public abstract void validate(FieldMapping mapping);

    private static void nullOnAll(FieldMapping mapping) {
        checkArgument(mapping.indexed == null && mapping.analyzer == null && mapping.docValues == null,
           "field " + mapping.field + " with type " + mapping.type + " cannot contain indexed, analyzer or docValues");
    }

    private static void noOrNull(FieldMapping mapping, boolean docValues) {
        checkArgument(mapping.indexed == null || mapping.indexed == NO,
           "field " + mapping.field + " with type " + mapping.type + " can be only indexed as No");
        checkArgument(mapping.analyzer == null,
           "field " + mapping.field + " with type " + mapping.type + " cannot contain analyzer");
        checkArgument(docValues || mapping.docValues == null,
           "field " + mapping.field + " with type " + mapping.type + " cannot contain docValues");
    }
}
