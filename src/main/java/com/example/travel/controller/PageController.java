package com.example.travel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
    @GetMapping("/")
    public String index(){
        return "index page";
    }

    @GetMapping("/login")
    public String login(){
        return "login page";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "signUp page";
    }

    @GetMapping("/board")
    public String board() {
        return "board";
    }
}
