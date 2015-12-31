package io.polyglotted.pgmodel.search;

@SuppressWarnings("unused")
public interface KeyExclude {
    int compareTo(IndexKey other);

    IndexKey delete();

    Boolean store();

    IndexKey newVersion(long version);

    IndexKey baseKey(Long baseVersion);
}
