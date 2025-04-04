package com.vladsv.weather_app.dao;

import java.io.Serializable;
import java.util.Optional;

public interface CrudDao<I extends Serializable, T> {

    void persist(T entity);

    Optional<T> findById(I id);

    void update(T entity);

}
