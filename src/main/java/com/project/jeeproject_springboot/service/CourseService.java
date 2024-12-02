package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Récupérer un cours par son ID
    public Optional<Course> getCourseById(int courseId) {
        return courseRepository.findById(courseId);
    }

    // Récupérer les cours auxquels un étudiant est inscrit
    public List<Course> getCoursesByStudentId(int studentId) {
        return courseRepository.findByStudentId(studentId);
    }

    // Récupérer les cours d'un professeur
    public List<Course> getCoursesByProfessorId(int professorId) {
        return courseRepository.findByProfessor_Id(professorId);
    }

    // Ajouter un nouveau cours
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    // Mettre à jour un cours existant
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    // Supprimer un cours par son ID
    public void deleteCourse(int courseId) {
        courseRepository.deleteById(courseId);
    }

    // Récupérer tous les cours
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
