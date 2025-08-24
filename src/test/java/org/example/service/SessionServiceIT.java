package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.config.TestAppConfig;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.model.Session;
import org.example.repository.SessionRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SessionServiceIT {

    private final SessionService sessionService;

    private final SessionRepository sessionRepository;
    private final SessionFactory sessionFactory;

    @Test
    public void shouldCreateSession(){
        UserAuthorizationRequestDto user = new UserAuthorizationRequestDto("Димка", "123");

        Session session =  sessionService.create(user);

        assertNotNull(session);
    }

    @Test
    public void logoutShouldRemoveSession(){
        UserAuthorizationRequestDto user = new UserAuthorizationRequestDto("Димка", "123");
        Session session = sessionService.create(user);
        assertNotNull(session);

        sessionService.logout(session.getId().toString());

        Optional<Session> validSession = sessionRepository.findValidById(session.getId());
        assertFalse(validSession.isPresent());

    }

    @Test
    @SneakyThrows
    void shouldRemoveSessionAfterDurationTime(){
        UserAuthorizationRequestDto user = new UserAuthorizationRequestDto("Димка", "123");
        sessionService.setSessionDuration(Duration.ofSeconds(2));
        Session session = sessionService.create(user);

        Thread.sleep(2001);
        sessionService.deleteIfSessionExpired(session.getId().toString());

        Optional<Session> expiredSession = sessionRepository.findValidById(session.getId());
        assertTrue(expiredSession.isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldContinueSession(){
        UserAuthorizationRequestDto user = new UserAuthorizationRequestDto("Димка", "123");
        sessionService.setSessionDuration(Duration.ofSeconds(2));
        Session session = sessionService.create(user);

        Thread.sleep(1500);
        sessionService.deleteIfSessionExpired(session.getId().toString());

        Optional<Session> unexpiredSession = sessionRepository.findValidById(session.getId());
        assertTrue(unexpiredSession.isPresent());
    }

}
