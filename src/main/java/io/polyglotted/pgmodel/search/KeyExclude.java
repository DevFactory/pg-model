package io.polyglotted.pgmodel.search;

public interface KeyExclude {
    int compareTo(IndexKey other);

    IndexKey delete();

    Boolean store();

    IndexKey newVersion(long version);
}
