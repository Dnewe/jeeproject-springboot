package com.project.jeeproject_springboot.repository;

import com.project.jeeproject_springboot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c " +
            "FROM Course c " +
            "JOIN Enrollment e ON e.course.id = c.id " +
            "WHERE e.student.id = :studentId")
    List<Course> findByStudentId(@Param("studentId") int studentId);

    List<Course> findByProfessor_Id(int professorId);
}
