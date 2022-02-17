package com.example.my_project.service;

import com.example.my_project.dto.SearchRequest;
import com.example.my_project.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserByEmail(SearchRequest searchRequest);

    UserDto createUser(UserDto userDto);

    List<UserDto> getAll();
    UserDto getById(Long id);
    void deleteUserById(Long id);

    UserDto getCurrentUser();
}
