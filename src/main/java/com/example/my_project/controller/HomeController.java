package com.example.my_project.controller;

import com.example.my_project.dto.UserDto;
import com.example.my_project.exceptions.UserNotFoundException;
import com.example.my_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homePage(Model model){
        model.addAttribute("name", "aaaa");
        try {
            UserDto currentUser = userService.getCurrentUser();
            if (currentUser.getRole().equals("ROLE_ADMIN")) {
                model.addAttribute("allow", true);
            } else {
                model.addAttribute("allow", false);
            }

        } catch (UserNotFoundException e) {
            model.addAttribute("allow", false);
        }
        return "home";
    }

}
