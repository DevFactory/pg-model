package io.polyglotted.pgmodel.search;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum DocStatus {
    LIVE, EXPIRED, DELETED, PENDING, PENDING_DELETE, REJECTED;

    private static final Map<String, DocStatus> STATUS_MAP = buildStatusMap();

    public String toStatus() {
        return name().toLowerCase();
    }

    public static DocStatus fromStatus(String status) {
        return STATUS_MAP.get(status);
    }

    private static Map<String, DocStatus> buildStatusMap() {
        ImmutableMap.Builder<String, DocStatus> builder = ImmutableMap.builder();
        for (DocStatus status : values()) {
            builder.put(status.name(), status);
            builder.put(status.name().toLowerCase(), status);
        }
        return builder.build();
    }
}
