package com.project.jeeproject_springboot.repository;

import com.project.jeeproject_springboot.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {

    // Méthode personnalisée pour trouver les résultats avec le cours en fonction de l'ID de l'étudiant
    @Query("SELECT r, c " +
            "FROM Result r " +
            "JOIN r.enrollment e " +
            "JOIN e.course c " +
            "JOIN e.student s " +
            "WHERE s.id = :studentId " +
            "ORDER BY r.entryDate ASC")
    List<Object[]> findResultsWithCourseByStudentId(@Param("studentId") int studentId);

    // Méthode personnalisée pour trouver les résultats avec l'étudiant en fonction de l'ID du cours
    @Query("SELECT r, s " +
            "FROM Result r " +
            "JOIN r.enrollment e " +
            "JOIN e.course c " +
            "JOIN e.student s " +
            "WHERE c.id = :courseId " +
            "ORDER BY r.entryDate ASC")
    List<Object[]> findResultsWithStudentByCourseId(@Param("courseId") int courseId);

    // Méthode personnalisée pour trouver les résultats par studentId et courseId
    @Query("SELECT r " +
            "FROM Result r " +
            "JOIN r.enrollment e " +
            "WHERE e.course.id = :courseId " +
            "AND e.student.id = :studentId " +
            "ORDER BY r.entryDate ASC")
    List<Result> findByCourseIdAndStudentId(@Param("courseId") int courseId, @Param("studentId") int studentId);
}
