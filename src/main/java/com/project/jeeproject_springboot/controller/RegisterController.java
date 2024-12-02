package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.UserService;
import com.project.jeeproject_springboot.util.EmailUtil;
import com.project.jeeproject_springboot.util.ServletUtil;
import com.project.jeeproject_springboot.util.TypeUtil;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    private String errorMessage;

    @PostMapping
    public String register(@Valid @ModelAttribute User user,
                           @RequestParam String role,
                           @RequestParam(required = false) String professorLastName,
                           @RequestParam(required = false) String professorFirstName,
                           @RequestParam(required = false) String studentLastName,
                           @RequestParam(required = false) String studentFirstName,
                           @RequestParam(required = false) LocalDate dateOfBirth,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        errorMessage = null;

        boolean valid = false;

        // Vérification des paramètres
        switch (role) {
            case "student":
                valid = validUserParameters(user, role) && validStudentParameters(studentFirstName, studentLastName, dateOfBirth);
                break;
            case "professor":
                valid = validUserParameters(user, role) && validProfessorParameters(professorFirstName, professorLastName);
                break;
            case "admin":
                valid = validUserParameters(user, role);
                break;
        }

        if (!valid) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/register";
        }
        // Création de l'utilisateur
        userService.addUser(user);

        // Création d'un étudiant ou professeur selon le rôle
        switch (role) {
            case "student":
                //LocalDate dateOfBirth = TypeUtil.getLocalDateFromString(studentDateOfBirth);
                Student student = createStudent(user, studentFirstName, studentLastName, dateOfBirth);
                studentService.addStudent(student);
                break;
            case "professor":
                Professor professor = createProfessor(user, professorFirstName, professorLastName);
                professorService.addProfessor(professor);
                break;
        }

        redirectAttributes.addFlashAttribute("successMessage", "Utilisateur créé avec succès");
        return "redirect:/login";  // Redirige vers la page de connexion après inscription
    }

    private boolean validUserParameters(User user, String role) {
        if (!role.equals("admin") && !role.equals("professor") && !role.equals("student")) {
            errorMessage = "Role invalide";
            return false;
        }
        if (!EmailUtil.validEmail(user.getEmail()) && userService.getUserByEmail(user.getEmail()).isPresent()) {
            errorMessage = "Email invalide ou déjà utilisé.";
            return false;
        }
        if (!ServletUtil.validString(user.getPassword())) {
            errorMessage = "Mot de passe invalide";
            return false;
        }
        if (user.getPassword().length() < 6) {
            errorMessage = "Le mot de passe doit faire au moins 6 caractères";
            return false;
        }
        return true;
    }

    private boolean validStudentParameters(String firstName, String lastName, LocalDate dateOfBirth) {
        //LocalDate dateOfBirth = TypeUtil.getLocalDateFromString(dateOfBirthStr);
        if (dateOfBirth == null) {
            errorMessage = "Date invalide. Utilisez le format yyyy-MM-dd.";
            return false;
        }
        return validCommonParameters(firstName, lastName);
    }

    private boolean validProfessorParameters(String firstName, String lastName) {
        return validCommonParameters(firstName, lastName);
    }

    private boolean validCommonParameters(String firstName, String lastName) {
        if (!ServletUtil.validString(lastName)) {
            errorMessage = "Nom invalide.";
            return false;
        }
        if (!ServletUtil.validString(firstName)) {
            errorMessage = "Prénom invalide.";
            return false;
        }
        return true;
    }

    private Student createStudent(User user, String firstName, String lastName, LocalDate dateOfBirth) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setDateOfBirth(dateOfBirth);
        student.setUser(user);
        return student;
    }

    private Professor createProfessor(User user, String firstName, String lastName) {
        Professor professor = new Professor();
        professor.setFirstName(firstName);
        professor.setLastName(lastName);
        professor.setUser(user);
        return professor;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("selectedRole", "student");
        return "/adminPages/user/register";  // Page de formulaire d'inscription
    }
}
