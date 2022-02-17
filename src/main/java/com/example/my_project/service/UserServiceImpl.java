package com.example.my_project.service;

import com.example.my_project.dto.SearchRequest;
import com.example.my_project.dto.UserDto;
import com.example.my_project.entity.User;
import com.example.my_project.entity.UserRole;
import com.example.my_project.enums.Roles;
import com.example.my_project.exceptions.BadRequestException;
import com.example.my_project.mappers.UserMapper;
import com.example.my_project.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends AbstractService implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public UserDto findUserByEmail(SearchRequest searchRequest) {

        if (Objects.isNull(searchRequest)) {
            throw new BadRequestException("search object cant be null");
        }

        if (Objects.isNull(searchRequest.getEmail()) || searchRequest.getEmail().length() < 1) {
            throw new BadRequestException("email cant be null or empty");
        }

        User byEmail = userRepository.findByEmail(searchRequest.getEmail());

        return UserMapper.toDto(byEmail);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("email already exists");
        }
        if (userDto.getAge() < 18) {
            throw new BadRequestException("age cant be <18");
        }
        User user = UserMapper.toEntity(userDto);
        user.setPassword(passwordEncoder().encode(userDto.getPassword()));
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(Roles.ROLE_USER, user));
        user.setRoles(userRoles);
        User save = userRepository.save(user);
        userDto = UserMapper.toDto(save);
        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toDto(userRepository.findById(id).
                orElseThrow(() -> new BadRequestException("User not found")));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDto getCurrentUser() {
        UserDto authUser = getAuthUser();
        return authUser;
    }
}
