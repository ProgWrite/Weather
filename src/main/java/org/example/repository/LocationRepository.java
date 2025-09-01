package org.example.repository;

import lombok.AllArgsConstructor;
import org.example.model.Location;
import org.example.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;


//TODO код методов дублируется. Можно сделать интерфейс
@Repository
@AllArgsConstructor
public class LocationRepository {

    private final SessionFactory sessionFactory;

    public Location save(Location location) {
        sessionFactory.getCurrentSession().persist(location);
        return location;
    }
}
