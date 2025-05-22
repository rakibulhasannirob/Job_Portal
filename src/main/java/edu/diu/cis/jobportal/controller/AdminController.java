package edu.diu.cis.jobportal.controller;

import edu.diu.cis.jobportal.model.JobListing;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.service.JobApplicationService;
import edu.diu.cis.jobportal.service.JobListingService;
import edu.diu.cis.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JobListingService jobListingService;
    
    @Autowired
    private JobApplicationService jobApplicationService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long userCount = userService.getAllUsers().size();
        long jobCount = jobListingService.getAllJobListings().size();
        long applicationCount = jobApplicationService.getAllApplications().size();
        
        model.addAttribute("userCount", userCount);
        model.addAttribute("jobCount", jobCount);
        model.addAttribute("applicationCount", applicationCount);
        
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
    
    @GetMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        
        user.setActive(false);
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("successMessage", "User deactivated successfully!");
        return "redirect:/admin/users";
    }
    
    @GetMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        
        user.setActive(true);
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("successMessage", "User activated successfully!");
        return "redirect:/admin/users";
    }
    
    @GetMapping("/jobs")
    public String viewAllJobs(Model model) {
        List<JobListing> jobs = jobListingService.getAllJobListings();
        model.addAttribute("jobs", jobs);
        return "admin/jobs";
    }
    
    @GetMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobListingService.deleteJobListing(id);
        
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        return "redirect:/admin/jobs";
    }
}