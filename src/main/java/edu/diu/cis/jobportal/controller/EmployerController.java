package edu.diu.cis.jobportal.controller;

import edu.diu.cis.jobportal.model.JobApplication;
import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.service.JobApplicationService;
import edu.diu.cis.jobportal.service.JobListingService;
import edu.diu.cis.jobportal.service.ProfileService;
import edu.diu.cis.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private JobListingService jobListingService;
    
    @Autowired
    private JobApplicationService jobApplicationService;
    
    private User getCurrentUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<JobListing> jobs = jobListingService.getJobListingsByEmployer(currentUser);
        
        model.addAttribute("user", currentUser);
        model.addAttribute("jobs", jobs);
        
        return "employer/dashboard";
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        model.addAttribute("profile", profile);
        return "employer/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Profile profile, 
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        Profile existingProfile = profileService.getProfileByUser(currentUser);
        
        existingProfile.setFullName(profile.getFullName());
        existingProfile.setPhoneNumber(profile.getPhoneNumber());
        
        profileService.updateProfile(existingProfile);
        
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/employer/profile";
    }
    
    @GetMapping("/jobs/new")
    public String showCreateJobForm(Model model) {
        model.addAttribute("jobListing", new JobListing());
        return "employer/job-form";
    }
    
    @PostMapping("/jobs/new")
    public String createJob(@Valid @ModelAttribute JobListing jobListing,
                           BindingResult result,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "employer/job-form";
        }
        
        User currentUser = getCurrentUser(authentication);
        jobListingService.createJobListing(jobListing, currentUser);
        
        redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully!");
        return "redirect:/employer/jobs";
    }
    
    @GetMapping("/jobs")
    public String viewPostedJobs(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<JobListing> jobs = jobListingService.getJobListingsByEmployer(currentUser);
        
        model.addAttribute("jobs", jobs);
        return "employer/jobs";
    }
    
    @GetMapping("/jobs/{id}")
    public String viewJobDetails(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        JobListing job = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        // Check if the job belongs to the current employer
        if (!job.getEmployer().getId().equals(currentUser.getId())) {
            return "redirect:/employer/jobs";
        }
        
        List<JobApplication> applications = jobApplicationService.getApplicationsByJobListing(job);
        
        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        model.addAttribute("isEmployer", true);
        
        return "employer/job-details";
    }
    
    @GetMapping("/jobs/{id}/edit")
    public String showEditJobForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        JobListing job = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        // Check if the job belongs to the current employer
        if (!job.getEmployer().getId().equals(currentUser.getId())) {
            return "redirect:/employer/jobs";
        }
        
        model.addAttribute("jobListing", job);
        return "employer/job-form";
    }
    
    @PostMapping("/jobs/{id}/edit")
    public String updateJob(@PathVariable Long id,
                          @Valid @ModelAttribute JobListing jobListing,
                          BindingResult result,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "employer/job-form";
        }
        
        User currentUser = getCurrentUser(authentication);
        JobListing existingJob = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        // Check if the job belongs to the current employer
        if (!existingJob.getEmployer().getId().equals(currentUser.getId())) {
            return "redirect:/employer/jobs";
        }
        
        existingJob.setTitle(jobListing.getTitle());
        existingJob.setCompanyName(jobListing.getCompanyName());
        existingJob.setLocation(jobListing.getLocation());
        existingJob.setDescription(jobListing.getDescription());
        existingJob.setRequirements(jobListing.getRequirements());
        existingJob.setSalary(jobListing.getSalary());
        
        jobListingService.updateJobListing(existingJob);
        
        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully!");
        return "redirect:/employer/jobs";
    }
    
    @GetMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, 
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        JobListing job = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        // Check if the job belongs to the current employer
        if (!job.getEmployer().getId().equals(currentUser.getId())) {
            return "redirect:/employer/jobs";
        }
        
        jobListingService.deleteJobListing(id);
        
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        return "redirect:/employer/jobs";
    }
    
    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id,
                                        @RequestParam String status,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        JobApplication application = jobApplicationService.getApplicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        
        // Check if the job belongs to the current employer
        if (!application.getJobListing().getEmployer().getId().equals(currentUser.getId())) {
            return "redirect:/employer/jobs";
        }
        
        jobApplicationService.updateApplicationStatus(application, status);
        
        redirectAttributes.addFlashAttribute("successMessage", "Application status updated!");
        return "redirect:/employer/jobs/" + application.getJobListing().getId();
    }
}