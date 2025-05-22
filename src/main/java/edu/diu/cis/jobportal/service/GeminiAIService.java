package edu.diu.cis.jobportal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-2.0-flash}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiAIService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Generate job recommendations based on user profile data
     * @param education User's education background
     * @param interests User's interests
     * @param skills User's skills
     * @param experience User's work experience
     * @return AI-generated recommendations
     */
    public String generateJobRecommendations(String education, String interests, String skills, String experience) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct the prompt for job recommendations
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("As a career advisor, provide job recommendations based on the following profile:\n\n");
            promptBuilder.append("Education: ").append(education != null ? education : "Not specified").append("\n");
            promptBuilder.append("Skills: ").append(skills != null ? skills : "Not specified").append("\n");
            promptBuilder.append("Interests: ").append(interests != null ? interests : "Not specified").append("\n");
            promptBuilder.append("Experience: ").append(experience != null ? experience : "Not specified").append("\n\n");
            promptBuilder.append("Please provide the following in your response:\n");
            promptBuilder.append("1. Top 3-5 recommended job roles with brief descriptions\n");
            promptBuilder.append("2. If the profile shows weak skills or academic background, suggest relevant courses to improve\n");
            promptBuilder.append("3. A brief career path recommendation\n\n");
            promptBuilder.append("Format the response with clear headings and bullet points.");

            // Construct the request body according to Gemini API format
            Map<String, Object> content = new HashMap<>();
            Map<String, String> textPart = new HashMap<>();
            textPart.put("text", promptBuilder.toString());
            
            Map<String, Object> part = new HashMap<>();
            part.put("parts", List.of(textPart));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(part));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // API endpoint for Gemini
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + 
                         ":generateContent?key=" + apiKey;
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Parse the response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            if (rootNode.has("candidates") && rootNode.get("candidates").size() > 0) {
                JsonNode candidate = rootNode.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts") && 
                    candidate.get("content").get("parts").size() > 0) {
                    return candidate.get("content").get("parts").get(0).get("text").asText();
                }
            }
            return "Unable to generate recommendations at this time.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating recommendations: " + e.getMessage();
        }
    }

    /**
     * Generate a CV based on user profile data
     * @param fullName User's full name
     * @param education User's education background
     * @param interests User's interests
     * @param skills User's skills
     * @param experience User's work experience
     * @return AI-generated CV in markdown format
     */
    public String generateCV(String fullName, String education, String interests, String skills, String experience) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct the prompt for CV generation
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("Generate a professional CV in markdown format for the following person:\n\n");
            promptBuilder.append("Full Name: ").append(fullName != null ? fullName : "Job Seeker").append("\n");
            promptBuilder.append("Education: ").append(education != null ? education : "Not specified").append("\n");
            promptBuilder.append("Skills: ").append(skills != null ? skills : "Not specified").append("\n");
            promptBuilder.append("Interests: ").append(interests != null ? interests : "Not specified").append("\n");
            promptBuilder.append("Experience: ").append(experience != null ? experience : "Not specified").append("\n\n");
            promptBuilder.append("The CV should include the following sections:\n");
            promptBuilder.append("1. Contact Information (use placeholder phone number and email)\n");
            promptBuilder.append("2. Professional Summary (based on their skills and experience)\n");
            promptBuilder.append("3. Skills (format as a list of key skills)\n");
            promptBuilder.append("4. Work Experience (format in a professional way)\n");
            promptBuilder.append("5. Education (format in a professional way)\n");
            promptBuilder.append("6. Interests/Activities (brief section)\n\n");
            promptBuilder.append("Use markdown formatting with appropriate headers, bullet points, etc. The CV should be professional, concise, and ready to use for job applications.");

            // Construct the request body according to Gemini API format
            Map<String, Object> content = new HashMap<>();
            Map<String, String> textPart = new HashMap<>();
            textPart.put("text", promptBuilder.toString());
            
            Map<String, Object> part = new HashMap<>();
            part.put("parts", List.of(textPart));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(part));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // API endpoint for Gemini
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + 
                         ":generateContent?key=" + apiKey;
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Parse the response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            if (rootNode.has("candidates") && rootNode.get("candidates").size() > 0) {
                JsonNode candidate = rootNode.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts") && 
                    candidate.get("content").get("parts").size() > 0) {
                    return candidate.get("content").get("parts").get(0).get("text").asText();
                }
            }
            return "Unable to generate CV at this time.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating CV: " + e.getMessage();
        }
    }
}