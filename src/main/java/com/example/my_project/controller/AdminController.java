package com.example.my_project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/adminPage")
    public String goToAdminPage() {
        return "adminPage";
    }
}
