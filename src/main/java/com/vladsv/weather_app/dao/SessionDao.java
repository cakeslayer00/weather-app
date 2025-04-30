package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.sql.EntityObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
            log.error("Error occurred during search for session by user: {} ", user, e);
            throw new EntityObtainingException(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 3600, timeUnit = TimeUnit.SECONDS)
    public void deleteSessionAfterExpired() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("delete from Session s where s.expiryAt < :expiryAt")
                    .setParameter("expiryAt", LocalDateTime.now())
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Error occurred during deletion of expired sessions: ", e);
            throw new EntityObtainingException(e.getMessage());
        }
    }
}
