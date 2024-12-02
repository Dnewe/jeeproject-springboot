package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.service.CourseService;
import com.project.jeeproject_springboot.service.ProfessorService;
import com.project.jeeproject_springboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private String errorMessage;

    @GetMapping("/list")
    public String viewCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "adminPages/course/courses";
    }

    @GetMapping("/details")
    public String viewCourse(@RequestParam("course-id") int courseId, Model model) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }

        List<Student> enrolledStudents = studentService.findStudentsEnrolledInCourse(courseId);
        List<Student> availableStudents = studentService.findAllStudents();
        availableStudents.removeAll(enrolledStudents);

        List<Professor> availableProfessors = professorService.getAllProfessors();

        model.addAttribute("course", course);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("availableStudents", availableStudents);
        model.addAttribute("availableProfessors", availableProfessors);

        return "adminPages/course/courseDetails";
    }

    @GetMapping("/createForm")
    public String showCreateCourseForm() {
        return "adminPages/course/createCourse";
    }

    @PostMapping("/create")
    public String createCourse(@RequestParam String name, @RequestParam String description, Model model) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            errorMessage = "Le nom ou la description du cours est invalide.";
            model.addAttribute("errorMessage", errorMessage);
            return "adminPages/course/createCourse";
        }
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setProfessor(null);
        courseService.addCourse(course);

        model.addAttribute("successMessage", "Cours créé avec succès");
        return "redirect:/course/list";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("course-id") int courseId, Model model) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        model.addAttribute("course", course);
        return "adminPages/course/updateCourse";
    }

    @PostMapping("/update")
    public String updateCourse(@RequestParam("course-id") int courseId, @RequestParam String name,
                               @RequestParam String description, Model model) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        if (optionalCourse.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        Course course = optionalCourse.get();

        if (name != null && !name.isEmpty()) course.setName(name);
        if (description != null && !description.isEmpty()) course.setDescription(description);

        courseService.updateCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("successMessage", "Cours modifié avec succès");

        return "redirect:/course/list";
    }

    @PostMapping("/delete")
    public String deleteCourse(@RequestParam("course-id") int courseId, Model model) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        courseService.deleteCourse(courseId);
        model.addAttribute("successMessage", "Cours supprimé avec succès");
        return "redirect:/course/list";
    }

    @PostMapping("/assignProfessor")
    public String assignProfessorToCourse(@RequestParam("course-id") int courseId, @RequestParam("professor-id") int professorId, Model model) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        Optional<Professor> optionalProfessor = professorService.getProfessorById(professorId);

        if (optionalCourse.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        if (optionalProfessor.isEmpty()) {
            errorMessage = "Professeur introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        Course course = optionalCourse.get();
        Professor professor = optionalProfessor.get();

        course.setProfessor(professor);
        courseService.updateCourse(course);
        model.addAttribute("successMessage", "Professeur assigné au cours avec succès");
        return "redirect:/course/details?course-id=" + courseId;
    }

    @PostMapping("/removeProfessor")
    public String removeProfessorFromCourse(@RequestParam("course-id") int courseId, Model model) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        if (optionalCourse.isEmpty()) {
            errorMessage = "Cours introuvable.";
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/course/list";
        }
        Course course = optionalCourse.get();

        course.setProfessor(null);
        courseService.updateCourse(course);
        model.addAttribute("successMessage", "Professeur retiré du cours avec succès");
        return "redirect:/course/details?course-id=" + courseId;
    }
}
