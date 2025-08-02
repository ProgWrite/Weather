package org.example.repository;


import lombok.AllArgsConstructor;
import org.example.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class SessionRepository {

    private final SessionFactory sessionFactory;


    public Session save(Session session) {
        sessionFactory.getCurrentSession().persist(session);
        return session;
    }

    public void deleteById(UUID id) {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM Session WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

}
