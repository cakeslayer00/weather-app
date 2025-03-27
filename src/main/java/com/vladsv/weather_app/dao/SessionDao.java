package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJOObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SessionDao extends BaseDao<UUID, Session> {

    private static final String SELECT_SESSION_BY_USER_QUERY = "select s from Session s where s.user = :user";

    public SessionDao(EntityManagerFactory emf) {
        super(emf, Session.class);
    }

    public Optional<Session> findSessionByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Optional<Session> session = em.createQuery(SELECT_SESSION_BY_USER_QUERY, Session.class)
                    .setParameter("user", user)
                    .getResultList().stream().findAny();

            em.getTransaction().commit();
            return session;
        } catch (PersistenceException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }
}
