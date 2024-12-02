package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Optional<Result> getResultById(int resultId) {
        return resultRepository.findById(resultId);
    }

    public Map<Course, List<Result>> getResultsByStudentIdGroupedByCourse(int studentId) {
        // Récupérer les résultats avec les cours associés
        List<Object[]> resultsWithCourses = resultRepository.findResultsWithCourseByStudentId(studentId);

        // Créer un Map pour stocker les résultats groupés par cours
        Map<Course, List<Result>> resultsByCourse = new HashMap<>();

        for (Object[] resultAndCourse : resultsWithCourses) {
            Result result = (Result) resultAndCourse[0];
            Course course = (Course) resultAndCourse[1];

            // Grouper les résultats par cours
            resultsByCourse.computeIfAbsent(course, k -> new ArrayList<>()).add(result);
        }

        return resultsByCourse;
    }

    public Map<Student, List<Result>> getResultsByCourseIdGroupedByStudent(int courseId) {
        // Récupérer les résultats avec les étudiants associés
        List<Object[]> resultsWithStudents = resultRepository.findResultsWithStudentByCourseId(courseId);

        // Créer un Map pour stocker les résultats groupés par étudiant
        Map<Student, List<Result>> resultsByStudent = new HashMap<>();

        for (Object[] resultAndStudent : resultsWithStudents) {
            Result result = (Result) resultAndStudent[0];
            Student student = (Student) resultAndStudent[1];

            // Grouper les résultats par étudiant
            resultsByStudent.computeIfAbsent(student, k -> new ArrayList<>()).add(result);
        }

        return resultsByStudent;
    }

    // Récupérer les résultats d'un étudiant pour un cours spécifique
    public List<Result> getResultsByStudentAndCourse(int studentId, int courseId) {
        return resultRepository.findByCourseIdAndStudentId(courseId, studentId);
    }

    // Ajouter un résultat
    public Result addResult(Result result) {
        return resultRepository.save(result);
    }

    // Mettre à jour un résultat
    public Result updateResult(Result result) {
        return resultRepository.save(result);
    }

    // Supprimer un résultat
    public void deleteResult(int resultId) {
        resultRepository.deleteById(resultId);
    }

}
