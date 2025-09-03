package org.example.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.SessionLogoutException;
import org.example.exceptions.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
//    private Duration sessionDuration = Duration.ofHours(24);
    private Duration sessionDuration = Duration.ofSeconds(15);

    public Session create(UserAuthorizationRequestDto userAuthorization) {
        User user = userRepository.findByLogin(userAuthorization.getLogin())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plus(sessionDuration));
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

    public Optional<UserResponseDto> getUserBySession(String sessionId) {
        if(sessionId == null || sessionId.isBlank()){
            return Optional.empty();
        }

        try {
            UUID uuid = UUID.fromString(sessionId);

            Optional<Session> session = sessionRepository.findValidById(uuid);
            User user = session.get().getUser();
            if(user == null){
                return Optional.empty();
            }

            UserResponseDto userResponseDto = UserMapper.INSTANCE.toResponseDto(user);
            return Optional.ofNullable(userResponseDto);

        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    public void deleteIfSessionExpired(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        Optional<Session> session = sessionRepository.findValidById(uuid);
        if (session.isEmpty()) {
            sessionRepository.deleteById(uuid);
            log.info("Session deleted with id: {}", uuid);
        }
    }

}