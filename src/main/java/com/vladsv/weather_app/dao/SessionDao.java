package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.sql.POJOObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
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

            log.info("Found session belonging to user {}", user);
            return session;
        } catch (PersistenceException e) {
            log.error("Error occurred during search for session by user: {} ", e.getMessage());
            throw new POJOObtainingException(e.getMessage());
        }
    }
}
