package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJODeletionException;
import com.vladsv.weather_app.exception.POJOObtainingException;
import com.vladsv.weather_app.exception.POJOPersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionDao implements Dao<UUID, Session> {

    private static final String SELECT_SESSION_BY_USER_QUERY = "select s from Session s where s.user = :user";
    private static final String SELECT_SESSION_BY_ID = "select s from Session s where s.id = :id";

    private final EntityManagerFactory emf;

    @Override
    public void persist(Session entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (HibernateException e) {
            throw new POJOPersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<Session> findById(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Optional<Session> session = em.createQuery(SELECT_SESSION_BY_ID, Session.class)
                    .setParameter("id", id)
                    .getResultList().stream().findAny();

            em.getTransaction().commit();
            return session;
        } catch (HibernateException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }

    public Optional<Session> findByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Optional<Session> session = em.createQuery(SELECT_SESSION_BY_USER_QUERY, Session.class)
                    .setParameter("user", user)
                    .getResultList().stream().findAny();

            em.getTransaction().commit();
            return session;
        } catch (HibernateException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }

    @Override
    public List<Session> findAll() {
        return List.of();
    }

    @Override
    public void update(Session session) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(session);
            em.getTransaction().commit();
        } catch (HibernateException e) {
            throw new POJODeletionException(e.getMessage());
        }
    }

    @Override
    public void delete(Session session) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(session);
            em.getTransaction().commit();
        } catch (HibernateException e) {
            throw new POJODeletionException(e.getMessage());
        }
    }

}
