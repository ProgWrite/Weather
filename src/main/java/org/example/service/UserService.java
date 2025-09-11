package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WrongPasswordException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    public void checkPassword(UserAuthorizationRequestDto userAuthorization) {
        if (userAuthorization == null) {
            throw new UserNotFoundException("User not found");
        }
        String login = userAuthorization.getLogin();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!PasswordUtil.checkPassword(userAuthorization.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
    }
}
