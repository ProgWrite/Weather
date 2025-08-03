package org.example.repository;


import lombok.AllArgsConstructor;
import org.example.model.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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

    public Optional<Session> findValidById(UUID id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession()
                        .createQuery("""
                    FROM Session s 
                    JOIN FETCH s.user 
                    WHERE s.id = :id 
                    AND s.expiresAt > CURRENT_TIMESTAMP
                    """, Session.class)
                        .setParameter("id", id)
                        .uniqueResult()
        );
    }



}
