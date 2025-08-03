package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.SessionLogoutException;
import org.example.exceptions.WrongPasswordException;
import org.example.mapper.UserMapper;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtil;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;


    public UserResponseDto create(UserRegistrationRequestDto userRegistrationRequestDto) {
        User user = UserMapper.INSTANCE.toEntity(userRegistrationRequestDto);

        String encodedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.create(user);
        log.info("User created with id: {}", savedUser.getId());
        return UserMapper.INSTANCE.toResponseDto(savedUser);
    }

    public boolean existsByLogin(String login) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        return true;
    }

    public String createSession(UserAuthorizationRequestDto userAuthorization) {
        User user = findUserAndCheckPassword(userAuthorization);

        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusDays(1));
        log.info("Session created with id: {}", session.getId());
        return sessionRepository.save(session).getId().toString();
    }

    public void logout(String sessionId) {
        try{
            sessionRepository.deleteById(UUID.fromString(sessionId));
            log.info("Session deleted with id: {}", sessionId);
        } catch (RuntimeException exception){
            throw  new SessionLogoutException("Failed to logout with id " + sessionId);
        }
    }

    public Optional<UserResponseDto> getUser(UserAuthorizationRequestDto userAuthorization) {
        try{
            User user = findUserAndCheckPassword(userAuthorization);
            log.info("User found with id: {}", user.getId());
            return Optional.ofNullable(UserMapper.INSTANCE.toResponseDto(user));
        }catch (RuntimeException e){
            log.info("User not found");
            return Optional.empty();
        }
    }

    public Optional<UserResponseDto> getUserBySession(String sessionId) {
        if(sessionId == null || sessionId.isBlank()){
            return Optional.empty();
        }

        try {
            UUID uuid = UUID.fromString(sessionId);

            Optional<Session> session = sessionRepository.findValidById(uuid);
            User user = session.isPresent() ? session.get().getUser() : null;
            UserResponseDto userResponseDto = UserMapper.INSTANCE.toResponseDto(user);
            log.info("User found with id: {}", userResponseDto.getId());
            return Optional.of(userResponseDto);

        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private User findUserAndCheckPassword(UserAuthorizationRequestDto userAuthorization) {
        String login = userAuthorization.getLogin();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new WrongPasswordException("User not found"));


        if(!PasswordUtil.checkPassword(userAuthorization.getPassword(), user.getPassword())){
            throw  new WrongPasswordException("Wrong password");
        }

        return user;
    }

}
