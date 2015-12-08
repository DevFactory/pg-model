package io.polyglotted.pgmodel.search.query;

@SuppressWarnings("unused")
public enum SearchType {
    DFS_QUERY_THEN_FETCH, DFS_QUERY_AND_FETCH, QUERY_THEN_FETCH, QUERY_AND_FETCH, SCAN, COUNT;

    public String toType() {
        return name().toLowerCase();
    }
}
