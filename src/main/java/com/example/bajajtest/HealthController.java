package com.example.bajajtest;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bfhl")
public class HealthController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> processInput(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extract "data" and "file_b64" from the request body
            List<String> data = (List<String>) request.get("data");
            String fileBase64 = (String) request.get("file_b64");

            // Separate numbers and alphabets from the data array
            List<String> numbers = data.stream().filter(s -> s.matches("\\d+")).collect(Collectors.toList());
            List<String> alphabets = data.stream().filter(s -> s.matches("[a-zA-Z]")).collect(Collectors.toList());

            // Find the highest lowercase letter (last in alphabetical order)
            String highestLowercase = alphabets.stream()
                    .filter(s -> s.equals(s.toLowerCase()))
                    .max(String::compareTo)
                    .orElse("");

            // Check if there are any prime numbers in the numbers array
            boolean isPrimeFound = numbers.stream()
                    .mapToInt(Integer::parseInt)
                    .anyMatch(HealthController::isPrime);

            // File validation
            boolean fileValid = false;
            String mimeType = "";
            double fileSizeKB = 0.0;

            if (fileBase64 != null && !fileBase64.isEmpty()) {
                try {
                    byte[] decodedFile = Base64Utils.decodeFromString(fileBase64);
                    fileValid = true;
                    mimeType = "application/octet-stream"; // Replace with appropriate MIME type detection
                    fileSizeKB = decodedFile.length / 1024.0;
                } catch (Exception e) {
                    fileValid = false;
                }
            }

            // Build the response
            response.put("is_success", true);
            response.put("user_id", "satyam_dubey_21082000");
            response.put("email", "satyam.dubey@example.com");
            response.put("roll_number", "0827CI211175");
            response.put("numbers", numbers);
            response.put("alphabets", alphabets);
            response.put("highest_lowercase_alphabet", Collections.singletonList(highestLowercase));
            response.put("is_prime_found", isPrimeFound);
            response.put("file_valid", fileValid);
            response.put("file_mime_type", mimeType);
            response.put("file_size_kb", Math.round(fileSizeKB * 100.0) / 100.0);

        } catch (Exception e) {
            response.put("is_success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOperationCode() {
        Map<String, Object> response = new HashMap<>();
        response.put("operation_code", 1);
        return ResponseEntity.ok(response);
    }

    // Utility function to check if a number is prime
    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}

