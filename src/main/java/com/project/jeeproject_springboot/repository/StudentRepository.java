package com.project.jeeproject_springboot.repository;

import com.project.jeeproject_springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByUser_Id(int userId);

    @Query("SELECT DISTINCT s FROM Student s " +
            "JOIN Enrollment e ON s.id = e.student.id " +
            "WHERE e.course.id = :courseId")
    List<Student> findStudentsEnrolledInCourse(@Param("courseId") int courseId);

    // Recherche avec filtrage sur le nom et le prénom
    @Query("SELECT s " +
            "FROM Student s " +
            "WHERE (LOWER(s.lastName) LIKE :search OR LOWER(s.firstName) LIKE :search)")
    List<Student> findBySearch(@Param("search") String search);

    // Recherche avec filtrage sur le cours uniquement
    @Query("SELECT s " +
            "FROM Student s " +
            "JOIN Enrollment e ON e.course.id = s.id " +
            "WHERE e.course.id = :courseId ")
    List<Student> findByCourseId(@Param("courseId") int courseId);

    // Recherche avec filtrage sur le nom, prénom et le cours
    @Query("SELECT s " +
            "FROM Student s " +
            "JOIN Enrollment e ON e.student.id = s.id " +
            "WHERE (LOWER(s.lastName) LIKE :search OR LOWER(s.firstName) LIKE :search)" +
            "AND e.course.id = :courseId")
    List<Student> findByCourseIdAndSearch(
            @Param("search") String search, @Param("courseId") int courseId);
}