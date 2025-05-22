package edu.diu.cis.jobportal.controller.api;

import edu.diu.cis.jobportal.model.Profile;
import edu.diu.cis.jobportal.model.User;
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
@RequestMapping("/api/career")
public class CareerApiController {

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
     * API endpoint to generate initial recommendations
     */
    @GetMapping("/recommendations/generate")
    public ResponseEntity<?> generateRecommendations(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        if (profile == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Profile not found. Please complete your profile first.");
            return ResponseEntity.badRequest().body(response);
        }
        
        String recommendations = geminiAIService.generateJobRecommendations(
            profile.getEducation(),
            profile.getInterests(),
            profile.getSkills(),
            profile.getExperience()
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("recommendations", recommendations);
        return ResponseEntity.ok(response);
    }
    
    /**
     * API endpoint to refresh existing recommendations
     */
    @GetMapping("/recommendations/refresh")
    public ResponseEntity<?> refreshRecommendations(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Profile profile = profileService.getProfileByUser(currentUser);
        
        if (profile == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Profile not found. Please complete your profile first.");
            return ResponseEntity.badRequest().body(response);
        }
        
        String recommendations = geminiAIService.generateJobRecommendations(
            profile.getEducation(),
            profile.getInterests(),
            profile.getSkills(),
            profile.getExperience()
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("recommendations", recommendations);
        return ResponseEntity.ok(response);
    }
}