package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.DatabaseException;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;


    public User create(User user) {

        try {
            Session session = sessionFactory.getCurrentSession();
            log.info("Saving user {}", user);
            session.persist(user);
            session.flush();
            return user;
        } catch (RuntimeException e) {
            log.error("Error while saving user {}", user, e);
            throw new DatabaseException("User creation failed");
        }
    }

}
