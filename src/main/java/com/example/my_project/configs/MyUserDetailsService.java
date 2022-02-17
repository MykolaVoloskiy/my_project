package com.example.my_project.configs;

import com.example.my_project.entity.User;
import com.example.my_project.entity.UserRole;
import com.example.my_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Set;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName);
        return getByUser(user);
    }

    private Principle getByUser(User user) {
        Set<UserRole> roles = user.getRoles();
        return new Principle(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), roles);
    }
}
