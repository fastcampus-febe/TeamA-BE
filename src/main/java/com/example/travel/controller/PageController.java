package com.example.travel.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "redirect:/login";
    }
}
