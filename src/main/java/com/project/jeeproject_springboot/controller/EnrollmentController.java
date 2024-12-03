package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Enrollment;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.CourseService;
import com.project.jeeproject_springboot.service.EnrollmentService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public String handlePost(@RequestParam("action") String action,
                             @RequestParam(value = "studentId", required = false) Integer studentId,
                             @RequestParam(value = "courseId", required = false) Integer courseId,
                             @RequestParam(value = "resultPage", required = false) String resultPage,
                             @RequestParam(value = "enrollmentDate", required = false) LocalDate enrollmentDate,
                             @SessionAttribute("loggedUser") Optional<User> loggedUser,
                             HttpSession session, RedirectAttributes redirectAttributes) {
        if (notAdmin(loggedUser)) return "commonPages/unauthorized";

        switch (action) {
            case "create":
                return createEnrollment(studentId, courseId, enrollmentDate, resultPage, redirectAttributes);
            case "delete":
                return deleteEnrollment(studentId, courseId, resultPage, redirectAttributes);
            default:
                redirectAttributes.addFlashAttribute("errorMessage", "Action invalide.");
                return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }
    }

    private String createEnrollment(Integer studentId, Integer courseId, LocalDate enrollmentDate, String resultPage, RedirectAttributes redirectAttributes) {
        if (studentId == null || courseId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Identifiants manquants pour l'étudiant ou le cours.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        LocalDate date = enrollmentDate != null ? enrollmentDate : LocalDate.now();

        if (courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        if (studentService.findStudentById(studentId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        if (enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Inscription déjà existante.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(date);
        enrollment.setCourse(courseService.getCourseById(courseId).get());
        enrollment.setStudent(studentService.findStudentById(studentId).get());

        enrollmentService.addEnrollment(enrollment);

        redirectAttributes.addFlashAttribute("successMessage", "Inscription créée avec succès.");
        notifyStudentEnrollmentChange(studentService.findStudentById(studentId).get(),
                "Inscription au cours " + courseService.getCourseById(courseId).get().getName(), redirectAttributes);
        return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
    }

    private String deleteEnrollment(Integer studentId, Integer courseId, String resultPage, RedirectAttributes redirectAttributes) {
        if (studentId == null || courseId == null || studentService.findStudentById(studentId).isEmpty() || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Etudiant et/ou cours introuvable.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        if (enrollment.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Inscription introuvable.");
            return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
        }

        enrollmentService.deleteEnrollment(studentId, courseId);

        redirectAttributes.addFlashAttribute("successMessage", "Inscription supprimée avec succès.");
        notifyStudentEnrollmentChange(studentService.findStudentById(studentId).get(),
                "Désinscription du cours " + courseService.getCourseById(courseId).get().getName(), redirectAttributes);
        return !resultPage.isEmpty()? "redirect:/" + resultPage : "commonPages/error";
    }

    private void notifyStudentEnrollmentChange(Student student, String changeType, RedirectAttributes redirectAttributes) {
        String subject = "Changement dans votre inscription";
        String message = String.format("Bonjour %s,\n\nNous vous informons qu'un changement a été effectué dans votre inscription : %s.\n\nCordialement,\nL'équipe de gestion",
                student.getFirstName(), changeType);

        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), subject, message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage","Echec d'envoi d'email pour " + student.getId());
        }
    }

    private boolean notAdmin(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("admin")).orElse(true);
    }
}
