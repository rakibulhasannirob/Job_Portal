package edu.diu.cis.jobportal.repository;

import edu.diu.cis.jobportal.model.JobApplication;
import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicant(User applicant);
    List<JobApplication> findByJobListing(JobListing jobListing);
    boolean existsByJobListingAndApplicant(JobListing jobListing, User applicant);
}