package org.ultimatechat2.util.function;

public interface Function<E> {

    void ifPresent(E e, Object[] args);

    void ifAbsent(E e, Object[] args);

}
