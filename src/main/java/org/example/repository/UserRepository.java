package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    // TODO мб возвращать тип лонг это не очень
    public User create(User user) {
        Session session = sessionFactory.getCurrentSession();
        log.info("Saving user {}", user);
        session.persist(user);
        session.flush();
        return user;
    }

}
