package io.polyglotted.esmodel.api.query;

@SuppressWarnings("unused")
public enum SearchOptions {
    IGNORE_UNAVAILABLE((byte) 1), ALLOW_NO_INDICES((byte) 2), EXPAND_WILDCARDS_OPEN((byte) 4),
    EXPAND_WILDCARDS_CLOSED((byte) 8), FORBID_ALIASES_TO_MULTIPLE_INDICES((byte) 16),
    FORBID_CLOSED_INDICES((byte) 32), STRICT_EXPAND_OPEN((byte) 6), LENIENT_EXPAND_OPEN((byte) 7),
    STRICT_EXPAND_OPEN_CLOSED((byte) 14), STRICT_EXPAND_OPEN_FORBID_CLOSED((byte) 38),
    STRICT_SINGLE_INDEX_NO_EXPAND_FORBID_CLOSED((byte) 48);

    public final byte[] bytes;

    SearchOptions(byte value) {
        this.bytes = new byte[]{value};
    }
}
