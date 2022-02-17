package com.example.my_project.service;

import com.example.my_project.dto.UserDto;
import com.example.my_project.entity.User;
import com.example.my_project.exceptions.UserNotFoundException;
import com.example.my_project.mappers.UserMapper;
import com.example.my_project.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AbstractService {

    private final UserRepository userRepository;

    public AbstractService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected UserDto getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        User user = userRepository.findByEmail(login);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found");
        }
        return UserMapper.toDto(user);
    }

}
