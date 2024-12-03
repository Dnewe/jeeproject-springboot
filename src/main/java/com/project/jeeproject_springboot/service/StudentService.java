package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Student;
import com.project.jeeproject_springboot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Trouver les étudiants en fonction de la recherche et du cours
     * @param search Le terme de recherche pour le nom ou prénom de l'étudiant
     * @param courseId L'ID du cours pour filtrer les étudiants inscrits
     * @return Liste d'étudiants filtrés
     */
    public List<Student> findFilteredStudents(String search, int courseId) {
        if (search != null && !search.trim().isEmpty() && courseId != -1) {
            return studentRepository.findByCourseIdAndSearch(search.toLowerCase(), courseId);
        } else if (search != null && !search.trim().isEmpty()) {
            return studentRepository.findBySearch(search.toLowerCase());
        } else if (courseId != -1) {
            return studentRepository.findByCourseId(courseId);
        } else {
            return studentRepository.findAll(); // Si aucun filtre n'est appliqué, retourne tous les étudiants
        }
    }

    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        return studentRepository.findStudentsEnrolledInCourse(courseId);
    }

    public Optional<Student> findStudentByUserId(int userId) {
        return Optional.ofNullable(studentRepository.findByUser_Id(userId));
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudent(int studentId) {
        studentRepository.deleteById(studentId);
    }

    public Optional<Student> findStudentById(int studentId) {
        return studentRepository.findById(studentId);
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
}
