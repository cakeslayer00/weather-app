
package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.exception.POJOObtainingException;
import com.vladsv.weather_app.exception.POJOPersistenceException;
import com.vladsv.weather_app.exception.POJOUpdatingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public abstract class BaseDao<I extends Serializable, T> implements CrudDao<I, T> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    @Override
    public void persist(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();

        } catch (PersistenceException e) {
            throw new POJOPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<T> findById(I id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Optional<T> entity = Optional.ofNullable(em.find(entityClass, id));
            em.getTransaction().commit();
            return entity;
        } catch (PersistenceException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new POJOUpdatingException(e.getMessage());
        }
    }

}
