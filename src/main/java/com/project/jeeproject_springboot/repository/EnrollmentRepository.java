package com.project.jeeproject_springboot.repository;

import com.project.jeeproject_springboot.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    Enrollment findByStudent_IdAndCourse_Id(int studentId, int courseId);
}
