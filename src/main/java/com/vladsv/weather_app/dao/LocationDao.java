package com.vladsv.weather_app.dao;

import com.vladsv.weather_app.entity.Location;
import com.vladsv.weather_app.entity.User;
import com.vladsv.weather_app.exception.sql.EntityDeletionException;
import com.vladsv.weather_app.exception.sql.EntityObtainingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
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

            log.info("Found {} locations with user {}", locations.size(), user);
            return locations;
        } catch (PersistenceException e) {
            log.error("Error occurred during search for all locations by user: {} ", e.getMessage());
            throw new EntityObtainingException(e.getMessage());
        }
    }

    public void delete(Long locationId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from Location l where l.id = :id").setParameter("id", locationId).executeUpdate();
            em.getTransaction().commit();

            log.info("Deleted location with id {}", locationId);
        } catch (PersistenceException e) {
            log.error("Error occurred during deletion of location: {} ", e.getMessage());
            throw new EntityDeletionException(e.getMessage());
        }
    }
}
