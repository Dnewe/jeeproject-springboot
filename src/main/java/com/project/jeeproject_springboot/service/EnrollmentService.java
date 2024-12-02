package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Enrollment;
import com.project.jeeproject_springboot.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    // Récupérer une inscription par l'ID de l'étudiant et l'ID du cours
    public Optional<Enrollment> getEnrollmentByStudentIdAndCourseId(int studentId, int courseId) {
        return Optional.ofNullable(enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId));
    }

    // Ajouter une nouvelle inscription
    public Enrollment addEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // Supprimer une inscription par l'ID de l'étudiant et l'ID du cours
    public void deleteEnrollment(int studentId, int courseId) {
        Optional<Enrollment> enrollment = getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        enrollment.ifPresent(enrollmentRepository::delete);
    }
}
