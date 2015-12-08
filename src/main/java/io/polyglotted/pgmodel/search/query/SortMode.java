package io.polyglotted.pgmodel.search.query;

@SuppressWarnings("unused")
public enum SortMode {
    NONE, MIN, MAX, SUM, AVG;

    public String toMode() {
        return NONE == this ? null : name().toLowerCase();
    }
}
