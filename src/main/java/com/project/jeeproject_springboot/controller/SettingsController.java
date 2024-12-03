package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showSettingsForm(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            model.addAttribute("errorMessage", "Vous n'êtes pas connecté.");
            return "redirect:/login";
        }
        return "commonPages/settings";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            Model model) {

        // Vérification de l'utilisateur connecté
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            model.addAttribute("errorMessage", "Vous n'êtes pas connecté.");
            return "commonPages/settings";
        }

        // Vérification de l'ancien mot de passe
        if (!loggedUser.getPassword().equals(oldPassword)) {
            model.addAttribute("errorMessage", "L'ancien mot de passe est incorrect.");
            return "commonPages/settings";
        }

        // Validation du nouveau mot de passe
        if (newPassword.length() < 6) {
            model.addAttribute("errorMessage", "Le nouveau mot de passe doit faire au moins 6 caractères.");
            return "commonPages/settings";
        }

        // Modification du mot de passe
        loggedUser.setPassword(newPassword);
        userService.updateUser(loggedUser);
        model.addAttribute("successMessage", "Mot de passe modifié avec succès.");

        return "commonPages/settings";
    }
}
