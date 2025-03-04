package com.vladsv.weather_app.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<I extends Serializable, T> {

    void persist(T entity);

    Optional<T> findById(I id);

    List<T> findAll();

    void update(T entity);

    void delete(T entity);
}
