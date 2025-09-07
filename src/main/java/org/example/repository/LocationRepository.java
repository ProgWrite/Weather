package org.example.repository;

import lombok.AllArgsConstructor;
import org.example.model.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
@AllArgsConstructor
public class LocationRepository {

    private final SessionFactory sessionFactory;

    public Location save(Location location) {
        sessionFactory.getCurrentSession().persist(location);
        return location;
    }

    public List<Location> findAllByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Location l where l.user.id=:userid";

        return session.createQuery(hql, Location.class)
                .setParameter("userid", userId)
                .getResultList();
    }


}
