package edu.diu.cis.jobportal.repository;

import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    List<JobListing> findByEmployer(User employer);
    List<JobListing> findByTitleContainingIgnoreCase(String title);
    List<JobListing> findByLocationContainingIgnoreCase(String location);
}