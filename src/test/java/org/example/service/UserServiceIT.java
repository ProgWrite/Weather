package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.config.TestAppConfig;
import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserServiceIT {

    private final static String LOGIN = "Павел";
    private final static String EXISTED_LOGIN = "Димка";
    private final static String NON_EXISTENT_LOGIN = "неверный логин";
    private final static String PASSWORD = "пароль";

    private final UserService userService;


    @Test
    public void shouldCreateUser() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(LOGIN, PASSWORD, PASSWORD);
        UserResponseDto result = userService.create(dto);

        assertNotNull(result.getId());
        assertEquals(LOGIN, result.getLogin());
    }



    @Test
    public void createUserWithExistingLoginShouldThrowDatabaseException() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(EXISTED_LOGIN, PASSWORD, PASSWORD);

        assertThrows(DatabaseException.class, () -> {
            userService.create(dto);
        });

    }

    @Test
    public void shouldExistUser(){
        boolean result = userService.existsByLogin(EXISTED_LOGIN);

        assertTrue(result);
    }

    @Test
    public void shouldNotExistUser(){
        boolean result = userService.existsByLogin(NON_EXISTENT_LOGIN);

        assertFalse(result);
    }


}

