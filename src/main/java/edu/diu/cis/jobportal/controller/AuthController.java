package edu.diu.cis.jobportal.controller;

import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showRegisterOptions() {
        return "register/options";
    }
    
    @GetMapping("/jobseeker")
    public String showJobSeekerRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register/jobseeker-form";
    }
    
    @PostMapping("/jobseeker")
    public String registerJobSeeker(@Valid @ModelAttribute("user") User user, 
                                   BindingResult result, 
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register/jobseeker-form";
        }
        
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username is already taken");
            return "register/jobseeker-form";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email is already in use");
            return "register/jobseeker-form";
        }
        
        userService.registerJobSeeker(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
        return "redirect:/login";
    }
    
    @GetMapping("/employer")
    public String showEmployerRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register/employer-form";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login"; // This should return the name of your login template file without the .html extension
    }
    
    @PostMapping("/employer")
    public String registerEmployer(@Valid @ModelAttribute("user") User user, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register/employer-form";
        }
        
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username is already taken");
            return "register/employer-form";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email is already in use");
            return "register/employer-form";
        }
        
        userService.registerEmployer(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
        return "redirect:/login";
    }
}