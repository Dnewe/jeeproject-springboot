package com.project.jeeproject_springboot.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "result")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assessment_name", nullable = false)
    private String assessmentName;

    @Column(name = "grade", nullable = false)
    private double grade;

    @Column(name = "max_score", nullable = false)
    private int maxScore;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) {this.enrollment = enrollment; }

    public String getAssessmentName() { return assessmentName; }
    public void setAssessmentName(String assessmentName) {this.assessmentName = assessmentName;}
}
