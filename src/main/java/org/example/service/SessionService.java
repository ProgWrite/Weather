package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.exceptions.SessionLogoutException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session create(UserAuthorizationRequestDto userAuthorization) {
        User user = userRepository.findByLogin(userAuthorization.getLogin())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        Session session = new Session();
        session.setUser(user);
        //TODO сделай 24 часа здесь
        session.setExpiresAt(LocalDateTime.now().plusHours(24));
        log.info("Session created with id: {}", session.getId());
        return sessionRepository.save(session);
    }

    public void logout(String sessionId) {
        try{
            sessionRepository.deleteById(UUID.fromString(sessionId));
            log.info("Session deleted with id: {}", sessionId);
        } catch (RuntimeException exception){
            throw  new SessionLogoutException("Failed to logout with id " + sessionId);
        }
    }

    public void deleteExpiredSessions(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        Optional<Session> session =  sessionRepository.findValidById(uuid);
        if (session.isEmpty()) {
            sessionRepository.deleteById(uuid);
        }
    }

}
