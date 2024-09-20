package com.firatmelih.privote.dev.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ResponseUtility {
    public ResponseEntity<Object>
    createResponse(
            String message, HttpStatus status) {
        HashMap<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }

    public ResponseEntity<Object>
    createResponseWithObject(
            String message,
            String customKey,
            Object customEntity,
            HttpStatus status) {
        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put(customKey, customEntity);
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }


}
