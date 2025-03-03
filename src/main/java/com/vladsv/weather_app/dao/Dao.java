package com.vladsv.weather_app.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    void persist(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void update(T entity);

    void delete(T entity);
}
