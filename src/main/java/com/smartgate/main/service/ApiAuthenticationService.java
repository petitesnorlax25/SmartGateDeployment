package com.smartgate.main.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgate.main.entity.StudentsData;


@Service
public class ApiAuthenticationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiAuthenticationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StudentsData authenticate(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String responseBody = response.getBody();
                // Check for the specific error message in the response
                if (responseBody.contains("account not found") || responseBody.contains("TYPE_ERROR")) {
                    return null;
                }
                // Parse JSON response to StudentData
                return objectMapper.readValue(responseBody, StudentsData.class);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions properly in production code
        }
        return null;
    }
}

