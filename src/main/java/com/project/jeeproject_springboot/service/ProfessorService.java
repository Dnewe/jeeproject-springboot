package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Professor;
import com.project.jeeproject_springboot.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Optional<Professor> getProfessorById(int professorId) {
        return professorRepository.findById(professorId);
    }

    // Récupérer un professeur par son ID utilisateur
    public Optional<Professor> getProfessorByUserId(int userId) {
        return Optional.ofNullable(professorRepository.findByUserId(userId));
    }

    // Ajouter un nouveau professeur
    public Professor addProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    // Supprimer un professeur par son ID
    public void deleteProfessor(int professorId) {
        professorRepository.deleteById(professorId);
    }

    // Récupérer tous les professeurs
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    // Mettre à jour les informations d'un professeur
    public Professor updateProfessor(Professor professor) {
        return professorRepository.save(professor);
    }
}
