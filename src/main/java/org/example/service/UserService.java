package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WrongPasswordException;
import org.example.mapper.UserMapper;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

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
                return true;
            }
        }
        return false;
    }

    public Optional<UserResponseDto> getUser(UserAuthorizationRequestDto userAuthorization) {
        try{
            if(userAuthorization == null){
                throw new UserNotFoundException("User not found with login " + userAuthorization.getLogin());
            }
            User user = findUserAndCheckPassword(userAuthorization);
            log.info("User found with id: {}", user.getId());
            return Optional.ofNullable(UserMapper.INSTANCE.toResponseDto(user));
        }catch (WrongPasswordException exception){
            throw exception;
        }
    }

    //TODO надо будет переделать этот метод. Он делает 2 дела.
    private User findUserAndCheckPassword(UserAuthorizationRequestDto userAuthorization) {
        String login = userAuthorization.getLogin();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!PasswordUtil.checkPassword(userAuthorization.getPassword(), user.getPassword())){
            throw  new WrongPasswordException("Wrong password");
        }

        return user;
    }

}
