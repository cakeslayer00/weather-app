package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.UserEntity;
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
public class UserDao implements Dao<UserEntity> {

    private final SessionFactory sessionFactory;

    @Override
    public void persist(UserEntity entity) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            throw new POJOPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserEntity> findAll() {
        return List.of();
    }

    @Override
    public void delete(UserEntity entity) {

    }
}
