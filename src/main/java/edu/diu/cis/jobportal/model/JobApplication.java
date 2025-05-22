package edu.diu.cis.jobportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Added version field for optimistic locking
    @Version
    private Long version = 0L;
    
    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobListing jobListing;
    
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;
    
    private LocalDateTime applicationDate;
    
    @Column(length = 1000)
    private String coverLetter;
    
    private String status = "PENDING"; // PENDING, REVIEWED, ACCEPTED, REJECTED
    
    // Constructors
    public JobApplication() {
        this.applicationDate = LocalDateTime.now();
    }
    
    public JobApplication(JobListing jobListing, User applicant) {
        this.jobListing = jobListing;
        this.applicant = applicant;
        this.applicationDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobListing getJobListing() {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing) {
        this.jobListing = jobListing;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // Added getter and setter for version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}