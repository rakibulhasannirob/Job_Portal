package edu.diu.cis.jobportal.controller;

import edu.diu.cis.jobportal.model.CareerRecommendations;
import edu.diu.cis.jobportal.model.CV;
import edu.diu.cis.jobportal.model.JobApplication;
import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.service.GeminiAIService;
import edu.diu.cis.jobportal.service.JobApplicationService;
import edu.diu.cis.jobportal.service.JobListingService;
import edu.diu.cis.jobportal.service.ProfileService;
import edu.diu.cis.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/jobseeker")
public class JobSeekerController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private JobListingService jobListingService;
    
    @Autowired
    private JobApplicationService jobApplicationService;
    
    @Autowired
    private GeminiAIService geminiAIService;
    
    private User getCurrentUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        model.addAttribute("user", currentUser);
        
        List<JobApplication> applications = jobApplicationService.getApplicationsByApplicant(currentUser);
        model.addAttribute("applications", applications);
        
        // Check if the user has a profile to determine if career recommendations should be shown
        Profile profile = profileService.getProfileByUser(currentUser);
        boolean showCareerRecommendations = (profile != null && 
                                           (profile.getEducation() != null || 
                                            profile.getSkills() != null || 
                                            profile.getInterests() != null));
        model.addAttribute("showCareerRecommendations", showCareerRecommendations);
        
        // Also enable CV generator if there's enough profile data
        boolean profileComplete = (profile != null && profile.getFullName() != null &&
                                   (profile.getEducation() != null || 
                                    profile.getSkills() != null || 
                                    profile.getExperience() != null));
        model.addAttribute("profileComplete", profileComplete);
        
        return "jobseeker/dashboard";
    }
    @GetMapping("/career-recommendations")
    public String viewCareerRecommendations(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        if (profile == null) {
            // Redirect to profile creation if the user doesn't have a profile yet
            return "redirect:/jobseeker/profile?mode=edit";
        }
        
        // Don't automatically generate recommendations, just pass the profile to the view
        model.addAttribute("profile", profile);
        
        return "jobseeker/career-recommendations";
    }
    
    /**
     * CV generation page
     */
    @GetMapping("/cv")
    public String showCVPage(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        if (profile == null) {
            // Redirect to profile page if profile is not complete
            return "redirect:/jobseeker/profile?error=Please complete your profile first";
        }
        
        // Instead of generating CV right away, just pass the user to the view
        model.addAttribute("user", currentUser);
        model.addAttribute("showCvContent", false);
        
        return "cv";
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        model.addAttribute("profile", profile);
        return "jobseeker/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Profile profile, 
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        Profile existingProfile = profileService.getProfileByUser(currentUser);
        
        existingProfile.setFullName(profile.getFullName());
        existingProfile.setEducation(profile.getEducation());
        existingProfile.setInterests(profile.getInterests());
        existingProfile.setPhoneNumber(profile.getPhoneNumber());
        existingProfile.setSkills(profile.getSkills());
        existingProfile.setExperience(profile.getExperience());
        
        profileService.updateProfile(existingProfile);
        
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/jobseeker/profile";
    }
    
    @GetMapping("/jobs")
    public String viewAllJobs(Model model) {
        List<JobListing> jobs = jobListingService.getAllJobListings();
        model.addAttribute("jobs", jobs);
        return "jobseeker/job-list";
    }
    
    @GetMapping("/jobs/search")
    public String searchJobs(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String location,
                            Model model) {
        List<JobListing> jobs;
        
        if (title != null && !title.isEmpty()) {
            jobs = jobListingService.searchJobsByTitle(title);
        } else if (location != null && !location.isEmpty()) {
            jobs = jobListingService.searchJobsByLocation(location);
        } else {
            jobs = jobListingService.getAllJobListings();
        }
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("title", title);
        model.addAttribute("location", location);
        return "jobseeker/job-list";
    }
    
    @GetMapping("/jobs/{id}")
    public String viewJobDetails(@PathVariable Long id, Model model, Authentication authentication) {
        JobListing job = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        User currentUser = getCurrentUser(authentication);
        boolean hasApplied = jobApplicationService.hasApplied(currentUser, job);
        
        model.addAttribute("job", job);
        model.addAttribute("hasApplied", hasApplied);
        model.addAttribute("application", new JobApplication());
        model.addAttribute("isEmployer", false);
        
        return "jobseeker/job-details";
    }
    
    @PostMapping("/jobs/{id}/apply")
    public String applyForJob(@PathVariable Long id, 
                             @ModelAttribute JobApplication application,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        JobListing job = jobListingService.getJobListingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID"));
        
        User currentUser = getCurrentUser(authentication);
        
        if (jobApplicationService.hasApplied(currentUser, job)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already applied for this job");
            return "redirect:/jobseeker/jobs/" + id;
        }
        
        // Create a new application instead of using the one bound from the form
        JobApplication newApplication = new JobApplication();
        newApplication.setCoverLetter(application.getCoverLetter());
        
        jobApplicationService.createApplication(newApplication, currentUser, job);
        
        redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully!");
        return"redirect:/jobseeker/jobs/" + id;
    }
    
    @GetMapping("/applications")
    public String viewApplications(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<JobApplication> applications = jobApplicationService.getApplicationsByApplicant(currentUser);
        
        model.addAttribute("applications", applications);
        return "jobseeker/applications";
    }
    
    @GetMapping("/applications/{id}")
    public String viewApplication(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        JobApplication application = jobApplicationService.getApplicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        
        // Check if the application belongs to the current user
        if (!application.getApplicant().getId().equals(currentUser.getId())) {
            return "redirect:/jobseeker/applications";
        }
        
        model.addAttribute("application", application);
        return "jobseeker/application-detail";
    }

    @GetMapping("/applications/{id}/withdraw")
    public String withdrawApplication(@PathVariable Long id, 
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        JobApplication application = jobApplicationService.getApplicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        
        // Check if the application belongs to the current user
        if (!application.getApplicant().getId().equals(currentUser.getId())) {
            return "redirect:/jobseeker/applications";
        }
        
        // Check if the application is still in PENDING status
        if (!"PENDING".equals(application.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only withdraw pending applications");
            return "redirect:/jobseeker/applications";
        }
        
        jobApplicationService.deleteApplication(id);
        
        redirectAttributes.addFlashAttribute("successMessage", "Application withdrawn successfully!");
        return "redirect:/jobseeker/applications";
    }
}