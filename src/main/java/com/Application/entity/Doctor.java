package com.Application.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String specialization;
    
    @ElementCollection
    @CollectionTable(name = "doctor_schedule", joinColumns = @JoinColumn(name = "doctor_id"))
    private List<DoctorSchedule> weeklySchedule;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorLeave> leaves;

    // Other doctor details, getters, setters, and constructors...

    // Empty constructor required by JPA
    public Doctor() {
    }

    // Example constructor for Doctor entity
    public Doctor(String name, String email, String specialization,List<DoctorSchedule> weeklySchedule,List<DoctorLeave> leaves) {
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.weeklySchedule=weeklySchedule;
        this.leaves=leaves;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public List<DoctorSchedule> getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(List<DoctorSchedule> weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }
    public List<DoctorLeave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<DoctorLeave> leaves) {
        this.leaves = leaves;
    }

    // You can add other methods as needed...

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
