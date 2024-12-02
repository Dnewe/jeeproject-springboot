package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.UserService;
import com.project.jeeproject_springboot.util.EmailUtil;
import com.project.jeeproject_springboot.util.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String handleAction(@RequestParam(required = false) String action,
                               @RequestParam(required = false) Integer userId,
                               @RequestParam(required = false) String role,
                               Model model) {
        if (action == null) {
            return "/commonPages/error"; // Action invalid
        }

        switch (action) {
            case "list":
                if (!isAdmin()) return "unauthorized"; // Si l'utilisateur n'est pas admin
                Iterable<User> users = userService.getAllUsers();
                model.addAttribute("users", users);
                return "user/list"; // page qui liste les utilisateurs
            case "details":
                if (!isAdmin()) return "unauthorized";
                if (userId == null || userService.getUserById(userId).isEmpty()) {
                    model.addAttribute("errorMessage", "Utilisateur introuvable.");
                    return "user/list";
                }
                User user = userService.getUserById(userId).get();
                model.addAttribute("user", user);
                return "user/details"; // page des détails utilisateur
            case "createForm":
                if (!isAdmin()) return "unauthorized";
                model.addAttribute("selectedRole", role);
                return "user/register"; // Formulaire de création
            case "updateForm":
                if (!isAdmin()) return "unauthorized";
                if (userId == null || userService.getUserById(userId) == null) {
                    model.addAttribute("errorMessage", "Utilisateur introuvable.");
                    return "error";
                }
                model.addAttribute("user", userService.getUserById(userId));
                return "user/update"; // Formulaire de mise à jour
            default:
                return "error"; // Action inconnue
        }
    }

    @PostMapping
    public String handlePostAction(@RequestParam String action,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String role,
                                   @RequestParam(required = false) Integer userId,
                                   Model model) {

        switch (action) {
            case "update":
                if (!isAdmin()) return "unauthorized";
                updateUser(userId, email, password, model);
                return "redirect:/user?action=list"; // Rediriger après mise à jour
            case "delete":
                if (!isAdmin()) return "unauthorized";
                deleteUser(userId, model);
                return "redirect:/user?action=list"; // Rediriger après suppression
            default:
                return "error"; // Action inconnue
        }
    }

    private void updateUser(Integer userId, String email, String password, Model model) {
        if (userId == null || userService.getUserById(userId).isEmpty()) {
            model.addAttribute("errorMessage", "Utilisateur introuvable.");
            return;
        }

        User user = userService.getUserById(userId).get();
        if (EmailUtil.validEmail(email)) {
            user.setEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        userService.updateUser(user);
        model.addAttribute("user", user);
        model.addAttribute("successMessage", "Profil utilisateur modifié avec succès");
    }

    private void deleteUser(Integer userId, Model model) {
        if (userId == null || userService.getUserById(userId).isEmpty()) {
            model.addAttribute("errorMessage", "Utilisateur introuvable.");
            return;
        }
        userService.deleteUser(userId);

        model.addAttribute("successMessage", "Utilisateur supprimé avec succès");
    }

    private boolean isAdmin() {
        // Logique pour vérifier si l'utilisateur actuel est un admin
        return true; // Ceci est un exemple, à remplacer par votre propre logique d'authentification
    }
}
