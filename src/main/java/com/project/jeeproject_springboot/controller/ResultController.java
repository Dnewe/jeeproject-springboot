package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.*;
import com.project.jeeproject_springboot.service.*;
import com.project.jeeproject_springboot.util.EmailUtil;
import com.project.jeeproject_springboot.util.MathUtil;
import com.project.jeeproject_springboot.util.ServletUtil;
import com.project.jeeproject_springboot.util.TypeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public ResultController(ResultService resultService, StudentService studentService,
                            CourseService courseService, EnrollmentService enrollmentService) {
        this.resultService = resultService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public String handlePostRequest(@RequestParam("action") String action,
                                    @RequestParam(required = false) Integer studentId,
                                    @RequestParam(required = false) Integer courseId,
                                    @RequestParam(required = false) Integer resultId,
                                    @RequestParam(required = false) String assessmentName,
                                    @RequestParam(required = false) Integer maxScore,
                                    @RequestParam(required = false) Double weight,
                                    @RequestParam(required = false) Double grade,
                                    @RequestParam(required = false) String resultPage,
                                    @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                    @SessionAttribute("loggedProfessor") Optional<Professor> loggedProfessor,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes,
                                    Model model) {
        switch (action) {
            case "create":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return createResult( request, studentId, courseId, assessmentName, maxScore, weight, grade, resultPage, redirectAttributes);
            case "createMultiple":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return loggedProfessor.map(professor1 -> createResults(request, courseId, assessmentName, maxScore, weight, professor1, resultPage, redirectAttributes)).orElse("commonPages/unauthorized");
            case "update":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return loggedProfessor.map(value -> updateResult(resultId, assessmentName, maxScore, weight, grade, resultPage, model, redirectAttributes)).orElse("commonPages/unauthorized");
            case "delete":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return loggedProfessor.map(professor -> deleteResult(resultId, resultPage, model, redirectAttributes)).orElse("commonPages/unauthorized");
            default:
                model.addAttribute("errorMessage", "Action invalide");
                return "commonPages/error";
        }
    }

    @GetMapping
    public String handleGetRequest(@RequestParam("action") String action,
                                   @RequestParam(required = false) Integer resultId,
                                   @RequestParam(required = false) Integer studentId,
                                   @RequestParam(required = false) Integer courseId,
                                   @SessionAttribute("loggedUser") Optional<User> loggedUser,
                                   HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        switch (action) {
            case "studentDetails":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return viewStudentCourseResults(studentId, courseId, model, redirectAttributes);
            case "studentList":
                if (notStudent(loggedUser)) return "commonPages/unauthorized";
                return viewStudentResults(studentId, model, redirectAttributes);
            case "createForm":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return addCreateFormParams(studentId, courseId, model);
            case "createMultipleForm":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return addCreateMultipleFormParams(courseId, model);
            case "updateForm":
                if (notProfessor(loggedUser)) return "commonPages/unauthorized";
                return addUpdateFormParams(resultId, studentId, courseId, model, redirectAttributes);
            default:
                model.addAttribute("errorMessage", "Action invalide");
                return "commonPages/error";
        }
    }

    private String createResult(HttpServletRequest request,
                                Integer studentId,
                                Integer courseId,
                                String assessmentName,
                                Integer maxScore,
                                Double weight,
                                Double grade,
                                String resultPage,
                                RedirectAttributes redirectAttributes) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        LocalDate entryDate = LocalDate.now();

        if (!ServletUtil.validString(assessmentName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nom de l'évaluation invalide");
            return "redirect:/" + resultPage;
        }

        if (!validGrade(grade, maxScore, weight)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Note invalide");
            return "redirect:/" + resultPage;
        }

        if (enrollment.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Etudiant introuvable");
            return "redirect:/" + resultPage;
        }

        Result result = new Result();
        result.setEnrollment(enrollment.get());
        result.setGrade(grade);
        result.setMaxScore(maxScore);
        result.setWeight(weight);
        result.setAssessmentName(assessmentName);
        result.setEntryDate(entryDate);

        resultService.addResult(result);
        if (studentService.findStudentById(studentId).isEmpty() || courseService.getCourseById(courseId).isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cours et/ou Etudiant introuvable");
        }
        notifyStudentGradePublication(studentService.findStudentById(studentId).get(), courseService.getCourseById(courseId).get(), redirectAttributes);

        redirectAttributes.addFlashAttribute("successMessage", "Note enregistrée");
        return "redirect:/" + resultPage;
    }

    private String createResults(HttpServletRequest request,
                                 Integer courseId,
                                 String assessmentName,
                                 Integer maxScore,
                                 Double weight,
                                 Professor loggedProfessor,
                                 String resultPage,
                                 RedirectAttributes redirectAttributes) {
        LocalDate entryDate = LocalDate.now();

        if (!ServletUtil.validString(assessmentName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nom de l'évaluation invalide");
            return "redirect:/" + resultPage;
        }

        if (maxScore <= 0 || weight <= 0 || maxScore > 1000 || weight > 1000) {
            redirectAttributes.addFlashAttribute("errorMessage", "Valeurs invalides");
            return "redirect:/" + resultPage;
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("grades[")) {
                String studentIdStr = paramName.substring(7, paramName.length() - 1);
                int studentId = TypeUtil.getIntFromString(studentIdStr);
                String gradeStr = request.getParameter(paramName);

                if (ServletUtil.validString(gradeStr)) {
                    double grade = TypeUtil.getDoubleFromString(gradeStr);

                    if (validGrade(grade, maxScore, weight)) {
                        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
                        if (enrollment.isPresent()) {
                            Result result = new Result();
                            result.setEnrollment(enrollment.get());
                            result.setGrade(grade);
                            result.setMaxScore(maxScore);
                            result.setWeight(weight);
                            result.setAssessmentName(assessmentName);
                            result.setEntryDate(entryDate);

                            resultService.addResult(result);
                            if (studentService.findStudentById(studentId).isEmpty() || courseService.getCourseById(courseId).isEmpty()) {
                                redirectAttributes.addFlashAttribute("errorMessage", "Cours et/ou Etudiant introuvable");
                            }
                            notifyStudentGradePublication(studentService.findStudentById(studentId).get(), courseService.getCourseById(courseId).get(), redirectAttributes);
                        }
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "Notes enregistrées");
        return "redirect:/" + resultPage;
    }

    private String updateResult(Integer resultId,
                                String assessmentName,
                                Integer maxScore,
                                Double weight,
                                Double grade,
                                String resultPage,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        Optional<Result> optionalResult = resultService.getResultById(resultId);
        if (optionalResult.isEmpty()) {
            model.addAttribute("errorMessage", "Résultat introuvable");
            return "commonPages/error";
        }
        Result result = optionalResult.get();
        if (maxScore > 0 && maxScore < 1000) result.setMaxScore(maxScore);
        if (grade > 0 && grade < 1000 && grade < maxScore) result.setGrade(grade);
        if (weight > 0 && weight < 1000) result.setWeight(weight);
        if (ServletUtil.validString(assessmentName)) result.setAssessmentName(assessmentName);

        resultService.updateResult(result);
        redirectAttributes.addFlashAttribute("successMessage", "Résultat mis à jour");

        return "redirect:/" + resultPage;
    }

    private String deleteResult(Integer resultId, String resultPage, Model model, RedirectAttributes redirectAttributes) {
        Optional<Result> result = resultService.getResultById(resultId);

        if (result.isEmpty()) {
            model.addAttribute("errorMessage", "Resultat introuvable");
            return "commonPages/error";
        }

        resultService.deleteResult(resultId);
        redirectAttributes.addFlashAttribute("successMessage", "Note supprimée avec succès");

        return "redirect:/" + resultPage;
    }

    private String viewStudentResults(Integer studentId, Model model, RedirectAttributes redirectAttributes) {
        if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
            model.addAttribute("errorMessage", "Etudiant introuvable");
            return "commonPages/error";
        }

        Map<Course, List<Result>> resultsByCourse = resultService.getResultsByStudentIdGroupedByCourse(studentId);
        model.addAttribute("resultsByCourse", resultsByCourse);

        Map<Course, Double> averageByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));
        model.addAttribute("averageByCourse", averageByCourse);

        return "studentPages/results";
    }

    private String viewStudentCourseResults(Integer studentId, Integer courseId, Model model, RedirectAttributes redirectAttributes) {

        if (studentId == null || studentService.findStudentById(studentId).isEmpty()) {
            model.addAttribute("errorMessage", "Etudiant introuvable");
            return "commonPages/error";
        }

        if (courseId == null || courseService.getCourseById(courseId).isEmpty()) {
            model.addAttribute("errorMessage", "Cours introuvable");
            return "commonPages/error";
        }

        List<Result> results = resultService.getResultsByStudentAndCourse(studentId, courseId);
        model.addAttribute("results", results);
        model.addAttribute("course", courseService.getCourseById(courseId).get());
        model.addAttribute("student", studentService.findStudentById(studentId).get());
        model.addAttribute("average", MathUtil.calculateAverageFromResults(results));
        return "professorPages/studentDetails";
    }

    private String addCreateFormParams(Integer studentId, Integer courseId, Model model) {
        if (courseId==null || studentId==null || studentService.findStudentById(studentId).isEmpty() || courseService.getCourseById(courseId).isEmpty()) {
            model.addAttribute("errorMessage", "Cours et/ou Etudiant introuvable");
            return "commonPages/error";
        }
        model.addAttribute("student", studentService.findStudentById(studentId).get());
        model.addAttribute("course", courseService.getCourseById(courseId).get());
        return "professorPages/gradeStudent";
    }

    private String addCreateMultipleFormParams(Integer courseId, Model model) {
        if (courseId==null || courseService.getCourseById(courseId).isEmpty()) {
            model.addAttribute("errorMessage", "Cours introuvable");
            return "commonPages/error";
        }
        model.addAttribute("students", studentService.findStudentsEnrolledInCourse(courseId));
        model.addAttribute("course", courseService.getCourseById(courseId).get());
        return "professorPages/gradeStudents";
    }

    private String addUpdateFormParams(Integer resultId, Integer studentId, Integer courseId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Result> result = resultService.getResultById(resultId);
        if (result.isEmpty()) {
            model.addAttribute("errorMessage", "Resultat introuvable");
            return "commonPages/error";
        }
        if (courseId==null || studentId==null || studentService.findStudentById(studentId).isEmpty() || courseService.getCourseById(courseId).isEmpty()) {
            model.addAttribute("errorMessage", "Cours et/ou Etudiant introuvable");
            return "commonPages/error";
        }

        model.addAttribute("result", result.get());
        model.addAttribute("student", studentService.findStudentById(studentId).get());
        model.addAttribute("course", courseService.getCourseById(courseId).get());
        return "professorPages/updateResult";
    }

    private boolean validGrade(double grade, int maxScore, double weight) {
        return grade >= 0 && grade <= maxScore && weight > 0;
    }

    private void notifyStudentGradePublication(Student student, Course course, RedirectAttributes redirectAttributes) {
        String emailMessage = "Une note a été publiée pour le cours " + course.getName() + ".";
        try {
            EmailUtil.sendEmail(student.getUser().getEmail(), "Nouvelle note publiée", emailMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible d'envoyer email à" + student.getId());
        }
    }

    private boolean notProfessor(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("professor")).orElse(true);
    }
    private boolean notStudent(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("student")).orElse(true);
    }
}
