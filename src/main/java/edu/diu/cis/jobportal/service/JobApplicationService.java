package edu.diu.cis.jobportal.service;

import edu.diu.cis.jobportal.model.JobApplication;
import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {
    
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    
    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }
    
    public List<JobApplication> getApplicationsByApplicant(User applicant) {
        return jobApplicationRepository.findByApplicant(applicant);
    }
    
    public List<JobApplication> getApplicationsByJobListing(JobListing jobListing) {
        return jobApplicationRepository.findByJobListing(jobListing);
    }
    
    public Optional<JobApplication> getApplicationById(Long id) {
        return jobApplicationRepository.findById(id);
    }
    
    public JobApplication createApplication(JobApplication application, User applicant, JobListing jobListing) {
        application.setApplicant(applicant);
        application.setJobListing(jobListing);
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("PENDING");
        return jobApplicationRepository.save(application);
    }
    /**
     * Delete an application by its ID
     */
    public void deleteApplication(Long id) {
        jobApplicationRepository.deleteById(id);
    }
    
    public JobApplication updateApplicationStatus(JobApplication application, String status) {
        application.setStatus(status);
        return jobApplicationRepository.save(application);
    }
    
    public boolean hasApplied(User applicant, JobListing jobListing) {
        return jobApplicationRepository.existsByJobListingAndApplicant(jobListing, applicant);
    }
}