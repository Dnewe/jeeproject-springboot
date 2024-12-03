package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.CourseService;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String handleAction(@RequestParam(required = false) String action,
                               @RequestParam(required = false) Integer professorId,
                               @SessionAttribute("loggedUser") Optional<User> loggedUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (action == null) {
            return "commonPages/error"; // Action inconnue
        }

        switch (action) {
            case "list":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Vérifier rôle
                List<Professor> professors = professorService.getAllProfessors();
                model.addAttribute("professors", professors);
                return "adminPages/professor/professors"; // Liste des professeurs

            case "details":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Vérifier rôle
                if (professorId == null || professorService.getProfessorById(professorId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Professeur introuvable.");
                    return "redirect:/professor?action=list"; // Professeur introuvable
                }
                Professor professor = professorService.getProfessorById(professorId).get();
                List<Course> assignedCourses = courseService.getCoursesByProfessorId(professorId);
                List<Course> availableCourses = courseService.getAllCourses();
                availableCourses.removeAll(assignedCourses);
                model.addAttribute("professor", professor);
                model.addAttribute("assignedCourses", assignedCourses);
                model.addAttribute("availableCourses", availableCourses);
                return "adminPages/professor/professorDetails"; // Détails du professeur

            case "updateForm":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Vérifier rôle
                if (professorId == null || professorService.getProfessorById(professorId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Professeur introuvable.");
                    return "redirect:/professor?action=list"; // Professeur introuvable
                }
                model.addAttribute("professor", professorService.getProfessorById(professorId).get());
                return "adminPages/professor/updateProfessor"; // Formulaire de mise à jour

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    @PostMapping
    public String handlePostAction(@RequestParam String action,
                                   @RequestParam(required = false) Integer professorId,
                                   @RequestParam(required = false) String firstName,
                                   @RequestParam(required = false) String lastName,
                                   @RequestParam(required = false) String resultPage,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   RedirectAttributes redirectAttributes) {

        switch (action) {
            case "update":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Vérifier rôle
                return updateProfessor(professorId, firstName, lastName, resultPage, redirectAttributes);

            case "delete":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Vérifier rôle
                return deleteProfessor(professorId, redirectAttributes);

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    private String updateProfessor(Integer professorId, String firstName, String lastName, String resultPage, RedirectAttributes redirectAttributes) {
        if (professorId == null || professorService.getProfessorById(professorId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Professeur introuvable.");
            return "redirect:/professor?action=list";
        }
        Professor professor = professorService.getProfessorById(professorId).get();
        if (firstName != null && !firstName.isEmpty()) professor.setFirstName(firstName);
        if (lastName != null && !lastName.isEmpty()) professor.setLastName(lastName);
        professorService.updateProfessor(professor);
        redirectAttributes.addFlashAttribute("successMessage", "Professeur mis à jour avec succès.");
        return "redirect:/" + resultPage;
    }

    private String deleteProfessor(Integer professorId, RedirectAttributes redirectAttributes) {
        if (professorId == null || professorService.getProfessorById(professorId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Professeur introuvable.");
            return "redirect:/professor?action=list";
        }
        professorService.deleteProfessor(professorId);
        redirectAttributes.addFlashAttribute("successMessage", "Professeur supprimé avec succès.");
        return "redirect:/professor?action=list";
    }

    private boolean notAdmin(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("admin")).orElse(true);
    }
}
