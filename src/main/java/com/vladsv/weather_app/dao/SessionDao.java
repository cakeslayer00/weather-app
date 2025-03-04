package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Session;
import com.vladsv.weather_app.entity.User;
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
        return Optional.empty();
    }

    public Optional<Session> findByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            String query = "select s from Session s where s.user = :user";
            Optional<Session> session = em.createQuery(query, Session.class)
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
    public void update(Session entity) {

    }

    @Override
    public void delete(Session entity) {

    }

}
