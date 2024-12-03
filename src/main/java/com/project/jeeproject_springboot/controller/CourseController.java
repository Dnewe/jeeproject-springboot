package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.CourseService;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String handleAction(@RequestParam(required = false) String action,
                               @RequestParam(required = false) Integer courseId,
                               @RequestParam(required = false) Integer studentId,
                               @RequestParam(required = false) Integer professorId,
                               @SessionAttribute("loggedUser") Optional<User> loggedUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (action == null) {
            return "commonPages/error"; // Action inconnue
        }

        switch (action) {
            case "list":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                model.addAttribute("courses", courseService.getAllCourses());
                return "adminPages/course/courses";

            case "studentList":
                if (notStudent(loggedUser)) return "commonPages/unauthorized";
                if (studentId==null || studentService.findStudentById(studentId).isEmpty()) {return "commonPages/error";}
                model.addAttribute("courses", courseService.getCoursesByStudentId(studentId));
                return "studentPages/courses";

            case "professorList":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                if (professorId==null || professorService.getProfessorById(professorId).isEmpty()) {return "commonPages/error";}
                model.addAttribute("courses", courseService.getCoursesByProfessorId(professorId));
                return "professorPages/courses";

            case "details":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return handleCourseDetails(courseId, model, redirectAttributes);

            case "createForm":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return "adminPages/course/createCourse";

            case "updateForm":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return handleUpdateForm(courseId, model, redirectAttributes);

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    @PostMapping
    public String handlePostAction(@RequestParam String action,
                                   @RequestParam(required = false) Integer courseId,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) Integer professorId,
                                   @RequestParam(required = false) String resultPage,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   RedirectAttributes redirectAttributes) {

        switch (action) {
            case "create":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return createCourse(name, description, redirectAttributes);

            case "update":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return updateCourse(courseId, name, description, redirectAttributes);

            case "delete":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return deleteCourse(courseId, redirectAttributes);

            case "assignProfessor":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return assignProfessorToCourse(courseId, professorId, resultPage, redirectAttributes);

            case "removeProfessor":
                if (notAdmin(loggedUser)) return "commonPages/unauthorized";
                return removeProfessorFromCourse(courseId, resultPage, redirectAttributes);

            default:
                return "commonPages/error"; // Action inconnue
        }
    }

    private String handleCourseDetails(Integer courseId, Model model, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }

        Course course = courseService.getCourseById(courseId).get();
        List<Student> enrolledStudents = studentService.findStudentsEnrolledInCourse(courseId);
        List<Student> availableStudents = studentService.findAllStudents();
        availableStudents.removeAll(enrolledStudents);

        List<Professor> availableProfessors = professorService.getAllProfessors();
        if (course.getProfessor()!=null) {availableProfessors.remove(course.getProfessor());}

        model.addAttribute("course", course);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("availableStudents", availableStudents);
        model.addAttribute("availableProfessors", availableProfessors);

        return "adminPages/course/courseDetails";
    }

    private String handleUpdateForm(Integer courseId, Model model, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }
        model.addAttribute("course", courseService.getCourseById(courseId).get());
        return "adminPages/course/updateCourse";
    }

    private String createCourse(String name, String description, RedirectAttributes redirectAttributes) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom ou la description du cours est invalide.");
            return "redirect:/course?action=createForm";
        }

        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        courseService.addCourse(course);

        redirectAttributes.addFlashAttribute("successMessage", "Cours créé avec succès.");
        return "redirect:/course?action=list";
    }

    private String updateCourse(Integer courseId, String name, String description, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }

        Course course = courseService.getCourseById(courseId).get();
        if (name != null && !name.isEmpty()) course.setName(name);
        if (description != null && !description.isEmpty()) course.setDescription(description);

        courseService.updateCourse(course);

        redirectAttributes.addFlashAttribute("successMessage", "Cours modifié avec succès.");
        return "redirect:/course?action=list";
    }

    private String deleteCourse(Integer courseId, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }

        courseService.deleteCourse(courseId);
        redirectAttributes.addFlashAttribute("successMessage", "Cours supprimé avec succès.");
        return "redirect:/course?action=list";
    }

    private String assignProfessorToCourse(Integer courseId, Integer professorId, String resultPage, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }
        if (professorId == null || professorService.getProfessorById(professorId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Professeur introuvable.");
            return "redirect:/course?action=list";
        }

        Course course = courseService.getCourseById(courseId).get();
        Professor professor = professorService.getProfessorById(professorId).get();
        course.setProfessor(professor);

        courseService.updateCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Professeur assigné avec succès.");
        return "redirect:/" + resultPage;
    }

    private String removeProfessorFromCourse(Integer courseId, String resultPage, RedirectAttributes redirectAttributes) {
        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable.");
            return "redirect:/course?action=list";
        }

        Course course = courseService.getCourseById(courseId).get();
        course.setProfessor(null);

        courseService.updateCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Professeur retiré avec succès.");
        return "redirect:/" + resultPage;
    }

    private boolean notAdmin(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("admin")).orElse(true);
    }
    private boolean notProfessor(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("professor")).orElse(true);
    }
    private boolean notStudent(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("student")).orElse(true);
    }
}
