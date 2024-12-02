package com.project.jeeproject_springboot.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    // Affichage de la page de login
    @GetMapping("/login")
    public String showLoginPage() {
        return "/commonPages/loginPage";  // Retourne le nom du template (login.html ou loginPage.jsp si vous utilisez JSP)
    }

    // Traitement du login
    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<User> OptionalUser = userService.getUserByEmailAndPassword(email, password);  // Utilisation de UserService pour authentifier l'utilisateur
        if (OptionalUser.isPresent()) {
            User user = OptionalUser.get();
            session.setAttribute("loggedUser", user);  // Mise en session de l'utilisateur
            switch (user.getRole()) {
                case "admin":
                    return "/adminPages/adminDashboard";  // Redirection vers le tableau de bord admin
                case "student":
                    session.setAttribute("loggedStudent", studentService.findStudentByUserId(user.getId()));
                    return "/studentPages/studentDashboard";  // Redirection vers le tableau de bord étudiant
                case "professor":
                    session.setAttribute("loggedProfessor", professorService.getProfessorByUserId(user.getId()));
                    return "/professorPages/professorDashboard";  // Redirection vers le tableau de bord professeur
                default:
                    return "/login";  // Si le rôle est inconnu
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email et/ou mot de passe invalide(s).");
            return "/login";  // Redirection en cas d'échec de l'authentification
        }
    }
}
