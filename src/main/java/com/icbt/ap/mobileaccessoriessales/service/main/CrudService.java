package com.icbt.ap.mobileaccessoriessales.service.main;

import java.util.List;

public interface CrudService<K, T> {
    void add(T t);

    void update(T t);

    void delete(K id);

    T getById(K id);

    List<T> getAll();
}
