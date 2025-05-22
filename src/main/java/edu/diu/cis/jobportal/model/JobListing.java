package edu.diu.cis.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_listings")
public class JobListing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String companyName;
    
    @NotBlank
    private String location;
    
    @Column(length = 2000)
    private String description;
    
    @Column(length = 1000)
    private String requirements;
    
    private String salary;
    
    private LocalDateTime postedDate;
    
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;
    
    @OneToMany(mappedBy = "jobListing", cascade = CascadeType.ALL)
    private List<JobApplication> applications = new ArrayList<>();
    
    // Constructors
    public JobListing() {
        this.postedDate = LocalDateTime.now();
    }
    
    public JobListing(String title, String companyName, String location, String description) {
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.description = description;
        this.postedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<JobApplication> applications) {
        this.applications = applications;
    }
}