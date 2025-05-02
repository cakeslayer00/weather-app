
package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.exception.sql.EntityAlreadyExistsException;
import com.vladsv.weather_app.exception.sql.EntityObtainingException;
import com.vladsv.weather_app.exception.sql.EntityPersistenceException;
import com.vladsv.weather_app.exception.sql.EntityUpdatingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class BaseDao<I extends Serializable, T> implements CrudDao<I, T> {

    protected final EntityManagerFactory emf;
    private final Class<T> clazz;

    @Override
    public void persist(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();

            log.info("Persisted entity: {}", entity);
        } catch (PersistenceException e) {
            log.error("Error persisting entity: {}", entity, e);
            if (e.getMessage().contains("already exists")) {
                throw new EntityAlreadyExistsException(e.getMessage());
            }
            throw new EntityPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<T> findById(I id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Optional<T> entity = Optional.ofNullable(em.find(clazz, id));
            em.getTransaction().commit();

            log.info("Retrieved entity: {}", entity);
            return entity;
        } catch (PersistenceException e) {
            log.info("Error during obtaining entity with id: {}", id, e);
            throw new EntityObtainingException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();

            log.info("Updated entity: {}", entity);
        } catch (PersistenceException e) {
            log.error("Error updating entity: {}", entity, e);
            throw new EntityUpdatingException(e.getMessage());
        }
    }

}
