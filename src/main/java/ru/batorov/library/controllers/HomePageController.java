package ru.batorov.library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    @GetMapping("/")
    public String home(){
        System.out.println("\n\n\nhome");
        return "home";
    }
}
