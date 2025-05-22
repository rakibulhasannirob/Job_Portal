package edu.diu.cis.jobportal.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model class to represent career recommendations from AI
 */
public class CareerRecommendations {
    
    private String jobRoles;
    private String courses;
    private String careerPath;
    private String fullRecommendation;
    
    public CareerRecommendations() {
        this.jobRoles = "";
        this.courses = "";
        this.careerPath = "";
        this.fullRecommendation = "";
    }
    
    public CareerRecommendations(String fullRecommendation) {
        this.fullRecommendation = fullRecommendation;
        parseRecommendation(fullRecommendation);
    }
    
    /**
     * Parse the full recommendation text to extract sections
     * @param text The full recommendation text
     */
    private void parseRecommendation(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        
        // Extract job roles section
        this.jobRoles = extractSection(text, 
            "(?i)(recommended job roles|top job roles|job recommendations).*?(?=\\n\\s*\\n|$)", 
            "No job roles found");
            
        // Extract courses section
        this.courses = extractSection(text, 
            "(?i)(recommended courses|courses|suggested courses).*?(?=\\n\\s*\\n|$)", 
            "No courses recommended");
            
        // Extract career path section
        this.careerPath = extractSection(text, 
            "(?i)(career path|career progression|career development).*?(?=\\n\\s*\\n|$)", 
            "No career path provided");
    }
    
    /**
     * Helper method to extract a section from the text using regex
     */
    private String extractSection(String text, String regex, String defaultValue) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).trim();
        }
        return defaultValue;
    }
    
    // Getters and Setters
    
    public String getJobRoles() {
        return jobRoles;
    }
    
    public void setJobRoles(String jobRoles) {
        this.jobRoles = jobRoles;
    }
    
    public String getCourses() {
        return courses;
    }
    
    public void setCourses(String courses) {
        this.courses = courses;
    }
    
    public String getCareerPath() {
        return careerPath;
    }
    
    public void setCareerPath(String careerPath) {
        this.careerPath = careerPath;
    }
    
    public String getFullRecommendation() {
        return fullRecommendation;
    }
    
    public void setFullRecommendation(String fullRecommendation) {
        this.fullRecommendation = fullRecommendation;
        parseRecommendation(fullRecommendation);
    }
}