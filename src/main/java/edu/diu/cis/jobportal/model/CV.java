package edu.diu.cis.jobportal.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model class to represent a generated CV
 */
public class CV {
    
    private String contactInfo;
    private String professionalSummary;
    private String skills;
    private String workExperience;
    private String education;
    private String interests;
    private String fullCvContent;
    
    public CV() {
        this.contactInfo = "";
        this.professionalSummary = "";
        this.skills = "";
        this.workExperience = "";
        this.education = "";
        this.interests = "";
        this.fullCvContent = "";
    }
    
    public CV(String fullCvContent) {
        this.fullCvContent = fullCvContent;
        parseCV(fullCvContent);
    }
    
    /**
     * Parse the full CV text to extract sections
     * @param text The full CV text
     */
    private void parseCV(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        
        // Extract contact info section
        this.contactInfo = extractSection(text, 
            "(?i)(contact information|contact|contact details).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No contact information found");
            
        // Extract professional summary section
        this.professionalSummary = extractSection(text, 
            "(?i)(professional summary|summary|profile|about me).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No professional summary found");
            
        // Extract skills section
        this.skills = extractSection(text, 
            "(?i)(skills|technical skills|key skills).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No skills found");
            
        // Extract work experience section
        this.workExperience = extractSection(text, 
            "(?i)(work experience|experience|employment history|professional experience).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No work experience found");
            
        // Extract education section
        this.education = extractSection(text, 
            "(?i)(education|academic background|academic qualifications).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No education information found");
            
        // Extract interests section
        this.interests = extractSection(text, 
            "(?i)(interests|activities|hobbies|personal interests).*?(?=\\n\\s*\\n|\\n\\s*#|$)", 
            "No interests found");
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
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public String getProfessionalSummary() {
        return professionalSummary;
    }
    
    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }
    
    public String getSkills() {
        return skills;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public String getWorkExperience() {
        return workExperience;
    }
    
    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }
    
    public String getEducation() {
        return education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public String getFullCvContent() {
        return fullCvContent;
    }
    
    public void setFullCvContent(String fullCvContent) {
        this.fullCvContent = fullCvContent;
        parseCV(fullCvContent);
    }
}