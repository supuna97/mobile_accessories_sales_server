package com.icbt.ap.mobileaccessoriessales.repository.main;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<K, T> {
    List<T> findAll();

    Optional<T> findById(K id);

    void save(T entity);

    void update(T entity);

    void delete(K id);

}
