package com.project.jeeproject_springboot.service;

import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.User;
import com.project.jeeproject_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    // Récupérer un utilisateur par son email
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(userRepository.findByEmailAndPassword(email, password));
    }

    // Ajouter un utilisateur
    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    // Supprimer un utilisateur
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    // Récupérer tous les utilisateurs
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Vérifier si un utilisateur existe par email
    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
