package org.example.repository;

import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.DatabaseException;
import org.example.exceptions.UserExistsException;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
            throw new UserExistsException("User already exists");
        }
    }

    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User", User.class)
                .getResultList();
    }

    public Optional<User> findByLogin(String login) {
       try{
           Session session = sessionFactory.getCurrentSession();
           User user = session.createQuery("FROM User WHERE login = :login", User.class)
                   .setParameter("login", login)
                   .uniqueResult();
           log.info("Found user with login{}", login);
           return Optional.of(user);
       }catch (RuntimeException e){
           log.error("Error while finding user with login {}", login, e);
           throw new DatabaseException("User not found");
       }
    }

}
