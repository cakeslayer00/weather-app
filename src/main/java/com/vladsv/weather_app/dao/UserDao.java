package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJOObtainingException;
import com.vladsv.weather_app.exception.POJOPersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao implements Dao<Long, User> {

    private final EntityManagerFactory emf;

    @Override
    public void persist(User entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (HibernateException e) {
            throw new POJOPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    public Optional<User> findByLogin(String login) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            String query = "select u from User u where u.login = :login";
            Optional<User> user = em.createQuery(query, User.class)
                    .setParameter("login", login)
                    .getResultList().stream().findAny();

            em.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(User entity) {

    }
}
