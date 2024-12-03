package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.ResultService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.TranscriptService;
import com.project.jeeproject_springboot.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transcript")
public class TranscriptController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ResultService resultService;

    @GetMapping
    public String createTranscriptForm(@RequestParam("studentId") Optional<Integer> studentId,
                                       @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        if (notAuthorized(loggedUser)) return "commonPages/unauthorized";

        if (studentId.isEmpty() || studentService.findStudentById(studentId.get()).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
            return "redirect:/adminDashboard";
        }

        Student student = studentService.findStudentById(studentId.get()).get();
        Map<Course, List<Result>> resultsByCourse = resultService.getResultsByStudentIdGroupedByCourse(studentId.get());
        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));

        model.addAttribute("student", student);
        model.addAttribute("resultsByCourse", resultsByCourse);
        model.addAttribute("averageByCourse", averageByCourse);

        return "adminPages/student/createTranscript"; // Vue pour afficher le formulaire de création du relevé
    }

    @PostMapping
    public String generateTranscript(@RequestParam("studentId") Optional<Integer> studentId,
                                   @RequestParam("comment") String comment,
                                     @RequestParam("resultPage") String resultPage,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   HttpServletResponse response,
                                   RedirectAttributes redirectAttributes) {
        if (notAuthorized(loggedUser)) {
            return "commonPages/unauthorized";
        }

        if (studentId.isEmpty() || studentService.findStudentById(studentId.get()).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
            return "redirect:/student?action=list";
        }

        if (comment == null || comment.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Appréciation invalide.");
            return "redirect:/student?action=list";
        }

        Student student = studentService.findStudentById(studentId.get()).get();
        Map<Course, List<Result>> resultsByCourse = resultService.getResultsByStudentIdGroupedByCourse(studentId.get());
        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));

        // Configurer la réponse pour le téléchargement du fichier PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"releve_notes_" + studentId.get() + ".pdf\"");

        try {
            TranscriptService.generate(response, student, resultsByCourse, averageByCourse, comment);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Création du PDF impossible.");
        }
        return "redirect:/" + resultPage;
    }

    private boolean notAuthorized(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("professor") && !user.getRole().equals("admin")).orElse(true);
    }
}
