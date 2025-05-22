package edu.diu.cis.jobportal.service;

import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobListingService {
    
    @Autowired
    private JobListingRepository jobListingRepository;
    
    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }
    
    public List<JobListing> getJobListingsByEmployer(User employer) {
        return jobListingRepository.findByEmployer(employer);
    }
    
    public Optional<JobListing> getJobListingById(Long id) {
        return jobListingRepository.findById(id);
    }
    
    public JobListing createJobListing(JobListing jobListing, User employer) {
        jobListing.setEmployer(employer);
        jobListing.setPostedDate(LocalDateTime.now());
        return jobListingRepository.save(jobListing);
    }
    
    public JobListing updateJobListing(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }
    
    public void deleteJobListing(Long id) {
        jobListingRepository.deleteById(id);
    }
    
    public List<JobListing> searchJobsByTitle(String title) {
        return jobListingRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<JobListing> searchJobsByLocation(String location) {
        return jobListingRepository.findByLocationContainingIgnoreCase(location);
    }
}