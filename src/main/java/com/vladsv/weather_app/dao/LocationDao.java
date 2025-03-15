package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.POJOObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationDao extends BaseDao<Long, Location> {

    public LocationDao(EntityManagerFactory emf) {
        super(emf, Location.class);
    }

    public List<Location> findAllByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<Location> locations = em.createQuery("select l from Location l where l.user = :user", Location.class)
                    .setParameter("user", user)
                    .getResultList();

            em.getTransaction().commit();
            return locations;
        } catch (PersistenceException e) {
            throw new POJOObtainingException(e.getMessage());
        }
    }
}
