package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJODeletionException;
import com.vladsv.weather_app.exception.POJOObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationDao extends BaseDao<Long, Location> {

    public static final String SELECT_LOCATION_WITH_GIVEN_USER = "select l from Location l where l.user = :user";

    public LocationDao(EntityManagerFactory emf) {
        super(emf, Location.class);
    }

    public List<Location> findAllByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<Location> locations = em.createQuery(SELECT_LOCATION_WITH_GIVEN_USER, Location.class)
                    .setParameter("user", user)
                    .getResultList();

            em.getTransaction().commit();
            return locations;
        } catch (PersistenceException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }

    public void delete(Long locationId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from Location l where l.id = :id").setParameter("id", locationId);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new POJODeletionException(e.getMessage());
        }
    }
}
