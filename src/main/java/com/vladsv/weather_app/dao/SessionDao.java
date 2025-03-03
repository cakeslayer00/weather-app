package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.SessionEntity;
import com.vladsv.weather_app.exception.POJOPersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionDao implements Dao<SessionEntity> {

    private final SessionFactory sessionFactory;

    @Override
    public void persist(SessionEntity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new POJOPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<SessionEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<SessionEntity> findAll() {
        return List.of();
    }

    @Override
    public void update(SessionEntity entity) {

    }

    @Override
    public void delete(SessionEntity entity) {

    }
}
