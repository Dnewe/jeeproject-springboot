package com.project.jeeproject_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalide la session utilisateur
        }
        return "redirect:/login"; // Redirige vers la page de login
    }
}
