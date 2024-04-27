package kg.hackathon.inai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class OpenApiService {
    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;
public String getResponse(String prompt) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    // Build the request payload using a more structured approach
    String requestBody = buildRequestBody(prompt);
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    try {
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        return extractContentFromResponse(response.getBody());
    } catch (HttpClientErrorException e) {
        e.printStackTrace();
        return "Ошибка: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
    }
}

    private String buildRequestBody(String prompt) {
        String escapedPrompt = escapeJson(prompt);
        return String.format("{\"model\": \"gpt-4\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", escapedPrompt);
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String extractContentFromResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode messageNode = rootNode.path("choices").get(0).path("message").path("content");
            return messageNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при обработке ответа";
        }
    }

}
