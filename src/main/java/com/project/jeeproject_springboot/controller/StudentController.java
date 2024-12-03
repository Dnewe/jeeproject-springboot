package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.CourseService;
import com.project.jeeproject_springboot.service.ResultService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.UserService;
import com.project.jeeproject_springboot.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ResultService resultService;

    @GetMapping
    public String handleAction(@RequestParam(required = false) String action,
                               @RequestParam(required = false) Integer studentId,
                               @RequestParam(required = false) Integer courseId,
                               @RequestParam(required = false) String search,
                               @SessionAttribute("loggedUser") Optional<User> loggedUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (action == null) {
            return "commonPages/error"; // Action inconnue
        }

        switch (action) {
            case "list":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized"; // Si l'utilisateur n'est pas admin
                model.addAttribute("students", studentService.findFilteredStudents(search, courseId!=null? courseId:-1));
                model.addAttribute("courses", courseService.getAllCourses());
                if (courseId!=null && courseId!=-1 && courseService.getCourseById(courseId).isPresent()) {model.addAttribute("filteredCourse", courseService.getCourseById(courseId).get());}
                return "adminPages/student/students"; // Vue pour afficher la liste des étudiants

            case "courseList":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
                    return "commonPages/error";
                }
                // get average
                Map<Student, List<Result>> resultsByStudent = resultService.getResultsByCourseIdGroupedByStudent(courseId);
                Map<Student, Double> averageByStudent = resultsByStudent.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                        ));
                model.addAttribute("averageByStudent", averageByStudent);
                model.addAttribute("students", studentService.findFilteredStudents(search, courseId));
                model.addAttribute("course", courseService.getCourseById(courseId).get());
                return "professorPages/students";

            case "details":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
                    return "redirect:/student?action=list";
                }
                List<Course> enrolledCourses = courseService.getCoursesByStudentId(studentId);
                List<Course> avalaibleCourses = courseService.getAllCourses();
                avalaibleCourses.removeAll(enrolledCourses);
                model.addAttribute("enrolledCourses", enrolledCourses);
                model.addAttribute("availableCourses", avalaibleCourses);
                model.addAttribute("student", studentService.findStudentById(studentId).get());
                return "adminPages/student/studentDetails"; // Vue pour afficher les détails d'un étudiant

            case "createForm":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return "redirect:/user?action=createForm&role=student"; // Redirection vers le formulaire de création

            case "updateForm":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
                    return "redirect:/student?action=list";
                }
                model.addAttribute("student", studentService.findStudentById(studentId).get());
                return "adminPages/student/updateStudent"; // Formulaire de mise à jour

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    @PostMapping
    public String handlePostAction(@RequestParam String action,
                                   @RequestParam(required = false) Integer studentId,
                                   @RequestParam(required = false) String firstName,
                                   @RequestParam(required = false) String lastName,
                                   @RequestParam(required = false) String resultPage,
                                   @RequestParam(required = false) LocalDate dateOfBirth,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   RedirectAttributes redirectAttributes) {

        switch (action) {
            case "update":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                updateStudent(studentId, lastName, firstName, dateOfBirth, redirectAttributes);
                return "redirect:/" + resultPage; // Redirection après mise à jour

            case "delete":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                deleteStudent(studentId, redirectAttributes);
                return "redirect:/student?action=list"; // Redirection après suppression

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    private void updateStudent(Integer studentId, String lastName, String firstName, LocalDate dateOfBirth, RedirectAttributes redirectAttributes) {
        if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
            return;
        }
        Student student = studentService.findStudentById(studentId).get();
        student.setId(studentId); // Conserver l'ID existant
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setDateOfBirth(dateOfBirth);
        studentService.updateStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Étudiant mis à jour avec succès.");
    }

    private void deleteStudent(Integer studentId, RedirectAttributes redirectAttributes) {
        if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
            return;
        }
        studentService.deleteStudent(studentId);
        redirectAttributes.addFlashAttribute("successMessage", "Étudiant supprimé avec succès.");
    }

    private boolean notAdmin(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("admin")).orElse(true);
    }
    private boolean notProfessor(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("professor")).orElse(true);
    }
}
