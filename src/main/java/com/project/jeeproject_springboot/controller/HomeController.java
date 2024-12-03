package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String homePage(HttpSession session, RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur connecté
        User user = (User) session.getAttribute("loggedUser");

        // Si aucun utilisateur n'est connecté
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous n'êtes pas connecté.");
            return "redirect:/login"; // Vue de connexion
        }

        // Redirection basée sur le rôle de l'utilisateur
        String role = user.getRole();
        switch (role) {
            case "admin":
                return "/adminPages/adminDashboard"; // Vue pour l'administrateur
            case "student":
                return "/studentPages/studentDashboard"; // Vue pour les étudiants
            case "professor":
                return "/professorPages/professorDashboard"; // Vue pour les professeurs
            default:
                redirectAttributes.addFlashAttribute("errorMessage", "Rôle inconnu. Veuillez vous reconnecter.");
                return "redirect:/login"; // Vue de connexion
        }
    }
}
