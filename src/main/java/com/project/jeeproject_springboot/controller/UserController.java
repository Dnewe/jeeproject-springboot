package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.UserService;
import com.project.jeeproject_springboot.util.EmailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                               @RequestParam(required = false) Optional<String> role,
                               @SessionAttribute("loggedUser") Optional<User> loggedUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (action == null) {
            return "/commonPages/error"; // Action invalid
        }

        switch (action) {
            case "list":
                if (notAdmin(loggedUser)) return "/commonPages/unauthorized"; // Si l'utilisateur n'est pas admin
                Iterable<User> users = userService.getAllUsers();
                model.addAttribute("users", users);
                return "adminPages/user/users"; // page qui liste les utilisateurs
            case "details":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                if (userId == null || userService.getUserById(userId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable.");
                    return "redirect:/user?action=list";
                }
                User user = userService.getUserById(userId).get();
                switch (user.getRole()) {
                    case "professor":
                        Optional<Professor> professor = professorService.getProfessorByUserId(userId);
                        if (professor.isPresent()) {
                            model.addAttribute("firstName", professor.get().getFirstName());
                            model.addAttribute("lastName", professor.get().getLastName());
                        }
                        break;
                    case "student":
                        Optional<Student> student = studentService.findStudentByUserId(userId);
                        if (student.isPresent()) {
                            model.addAttribute("firstName", student.get().getFirstName());
                            model.addAttribute("lastName", student.get().getLastName());
                        }
                        break;
                    default:
                        model.addAttribute("firstName", "N/A");
                        model.addAttribute("lastName", "N/A");
                }
                model.addAttribute("user", user);
                return "adminPages/user/userDetails"; // page des détails utilisateur
            case "createForm":
                if (notAdmin(loggedUser)) return "/commonPages/unauthorized";
                model.addAttribute("selectedRole", role.isPresent()? role.get() : "admin");
                return "adminPages/user/register"; // Formulaire de création
            case "updateForm":
                if (notAdmin(loggedUser)) return "/commonPages/unauthorized";
                if (userId == null || userService.getUserById(userId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable.");
                    return "redirect:/user?action=list";
                }
                model.addAttribute("user", userService.getUserById(userId).get());
                return "adminPages/user/updateUser"; // Formulaire de mise à jour
            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    @PostMapping
    public String handlePostAction(@RequestParam String action,
                                   @RequestParam Optional<String> email,
                                   @RequestParam Optional<String> password,
                                   @RequestParam(required = false) Integer userId,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   RedirectAttributes redirectAttributes) {

        switch (action) {
            case "update":
                if (notAdmin(loggedUser)) return "/commonPages/unauthorized";
                if (email.isEmpty() || password.isEmpty())  {
                    redirectAttributes.addFlashAttribute("errorMessage", "Email ou mot de passe absent.");
                    return "redirect:/user?action=list";
                }
                if (userService.getUserByEmail(email.get()).isPresent()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Email  déjà utilisé.");
                    return "redirect:/user?action=list";
                }
                updateUser(userId, email.get(), password.get(), redirectAttributes);
                return "redirect:/user?action=list"; // Rediriger après mise à jour
            case "delete":
                if (notAdmin(loggedUser)) return "/commonPages/unauthorized";
                deleteUser(userId, redirectAttributes);
                return "redirect:/user?action=list" ; // Rediriger après suppression
            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    private void updateUser(Integer userId, String email, String password, RedirectAttributes redirectAttributes) {
        if (userId == null || userService.getUserById(userId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable.");
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
        redirectAttributes.addFlashAttribute("successMessage", "Profil utilisateur modifié avec succès");
    }

    private void deleteUser(Integer userId, RedirectAttributes redirectAttributes) {
        if (userId == null || userService.getUserById(userId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable.");
            return;
        }
        userService.deleteUser(userId);

        redirectAttributes.addFlashAttribute("successMessage", "Utilisateur supprimé avec succès");
    }

    private boolean notAdmin(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("admin")).orElse(true);
    }
}
