package com.project.jeeproject_springboot.controller;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.service.PerformanceReportService;
import com.project.jeeproject_springboot.service.ResultService;
import com.project.jeeproject_springboot.service.StudentService;
import com.project.jeeproject_springboot.service.TranscriptService;
import com.project.jeeproject_springboot.util.MathUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/performanceReport")
public class PerformanceReportController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ResultService resultService;

    @PostMapping
    public String generateTranscript(@RequestParam("studentId") Optional<Integer> studentId,
                                     @RequestParam(value = "resultPage", required = false) String resultPage,
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

        // get students results by course
        Map<Course, List<Result>> resultsByCourse = resultService.getResultsByStudentIdGroupedByCourse(studentId.get());

        Student student = studentService.findStudentById(studentId.get()).get();
        Map<Course, Double> studentAveragesByCourse = resultsByCourse.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> MathUtil.calculateAverageFromResults(entry.getValue())
                ));

        // get courses average by course
        Map<Course, Double> courseAverages = resultsByCourse.keySet().stream()
                .collect(Collectors.toMap(
                        course -> course,
                        course -> {
                            Map<Student, List<Result>> resultsGroupedByStudent = resultService.getResultsByCourseIdGroupedByStudent(course.getId());
                            // get student averages
                            List<Double> studentAverages = resultsGroupedByStudent.values().stream()
                                    .map(MathUtil::calculateAverageFromResults)
                                    .toList();
                            // compute course average
                            return studentAverages.stream()
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElse(0.0);
                        }
                ));

        // get student ranks by course
        Map<Course, Integer> studentRanksByCourse = calculateRankByCourse(student.getId(), resultsByCourse.keySet());

        Map<Course, Integer> studentCountByCourse = resultsByCourse.keySet().stream()
                .collect(Collectors.toMap(
                        course -> course,
                        course -> {
                            List<Student> students = studentService.findStudentsEnrolledInCourse(course.getId());
                            return students.size();
                        }
                ));

        // student general average
        double studentGeneralAverage = MathUtil.calculateAverageFromDouble(studentAveragesByCourse.values());

        // Configurer la réponse pour le téléchargement du fichier PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"rapport_performance_" + studentId.get() + ".pdf\"");

        try {
            PerformanceReportService.generate(response, student, resultsByCourse, studentAveragesByCourse, courseAverages, studentRanksByCourse, studentCountByCourse, studentGeneralAverage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Création du PDF impossible.");
        }
        return "redirect:/" + resultPage;
    }

    public Map<Course, Integer> calculateRankByCourse(int studentId, Set<Course> courses) {
        Map<Course, Integer> rankByCourse = new HashMap<>();

        for (Course course : courses) {
            // get students enrolled in course
            List<Student> students = studentService.findStudentsEnrolledInCourse(course.getId());
            // get average per student
            Map<Student, Double> studentAverages = new HashMap<>();
            for (Student student : students) {
                List<Result> results = resultService.getResultsByCourseIdGroupedByStudent(course.getId()).getOrDefault(student, List.of());
                double average = MathUtil.calculateAverageFromResults(results);
                studentAverages.put(student, average);
            }
            // sort students average
            List<Map.Entry<Student, Double>> sortedAverages = new ArrayList<>(studentAverages.entrySet());
            sortedAverages.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            // get rank
            int rank = 1;
            boolean found = false;
            for (Map.Entry<Student, Double> entry : sortedAverages) {
                if (entry.getKey().getId() == studentId) {
                    rankByCourse.put(course, rank);
                    found = true;
                    break;
                }
                rank++;
            }
            // if student not found
            if (!found) {
                rankByCourse.put(course, -1);
            }
        }

        return rankByCourse;
    }

    private boolean notAuthorized(Optional<User> loggedUser) {
        return loggedUser.map(user -> !user.getRole().equals("professor") && !user.getRole().equals("admin")).orElse(true);
    }
}
