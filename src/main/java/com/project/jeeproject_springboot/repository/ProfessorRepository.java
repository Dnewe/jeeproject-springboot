package com.project.jeeproject_springboot.repository;

import com.project.jeeproject_springboot.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {

    Professor findByUserId(int userId);
}
