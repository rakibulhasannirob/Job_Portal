package edu.diu.cis.jobportal.controller.api;

import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
import edu.diu.cis.jobportal.model.CV;
import edu.diu.cis.jobportal.service.GeminiAIService;
import edu.diu.cis.jobportal.service.ProfileService;
import edu.diu.cis.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cv")
public class CVApiController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private GeminiAIService geminiAIService;
    
    private User getCurrentUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    /**
     * API endpoint to generate CV
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateCV(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        if (profile == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Profile not found. Please complete your profile first.");
            return ResponseEntity.badRequest().body(response);
        }
        
        String cvContent = geminiAIService.generateCV(
            profile.getFullName(),
            profile.getEducation(),
            profile.getInterests(),
            profile.getSkills(),
            profile.getExperience()
        );
        
        // Create a CV object to parse the content into sections
        CV cv = new CV(cvContent);
        
        Map<String, Object> response = new HashMap<>();
        response.put("fullContent", cv.getFullCvContent());
        response.put("contactInfo", cv.getContactInfo());
        response.put("professionalSummary", cv.getProfessionalSummary());
        response.put("skills", cv.getSkills());
        response.put("workExperience", cv.getWorkExperience());
        response.put("education", cv.getEducation());
        response.put("interests", cv.getInterests());
        
        return ResponseEntity.ok(response);
    }
}