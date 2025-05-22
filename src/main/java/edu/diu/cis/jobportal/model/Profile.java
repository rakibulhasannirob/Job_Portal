package edu.diu.cis.jobportal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    
    @Column(length = 1000)
    private String education;
    
    @Column(length = 1000)
    private String interests;
    
    private String phoneNumber;
    
    @Column(length = 1000)
    private String skills;
    
    @Column(length = 2000)
    private String experience;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // Constructors
    public Profile() {}
    
    public Profile(String fullName, String education, String interests) {
        this.fullName = fullName;
        this.education = education;
        this.interests = interests;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}