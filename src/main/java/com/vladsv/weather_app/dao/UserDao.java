package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJOObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDao extends BaseDao<Long, User> {

    private static final String SELECT_USER_BY_LOGIN = "select u from User u where u.username = :username";

    public UserDao(EntityManagerFactory emf) {
        super(emf, User.class);
    }

    public Optional<User> findByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Optional<User> user = em.createQuery(SELECT_USER_BY_LOGIN, User.class)
                    .setParameter("username", username)
                    .getResultList().stream().findAny();

            em.getTransaction().commit();
            return user;
        } catch (PersistenceException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }
}
