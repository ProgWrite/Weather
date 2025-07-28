package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public UserResponseDto create(UserRequestDto userRequestDto) {
       User user = UserMapper.INSTANCE.toEntity(userRequestDto);
       User savedUser = userRepository.create(user);
       log.info("User created with id: {}", savedUser.getId());

       return UserMapper.INSTANCE.toResponseDto(savedUser);
    }

}
