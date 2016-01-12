package io.polyglotted.pgmodel;

public interface ObjectRef<T> {

    KeyRef key();

    T source();
}
