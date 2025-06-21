package com.whs.dev2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Controller
public class ApiProxyController {

    @Value("${API_SERVER_URL}")
    private String apiServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws URISyntaxException, IOException {
        String uri = apiServerUrl + request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        log.info("Proxying request to URI: {}", uri);

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        log.info("Request Method: {}", method);
        if (method == null) {
            log.error("Unsupported HTTP method: {}", request.getMethod());
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Unsupported HTTP method".getBytes());
        }

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("content-length")) {
                headers.put(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        // Add JWT token from session if it exists and there's no Authorization header from the client
        HttpSession session = request.getSession(false); // Don't create a new session if one doesn't exist
        if (session != null && session.getAttribute("jwt_token") != null && !headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            String token = (String) session.getAttribute("jwt_token");
            headers.set(HttpHeaders.AUTHORIZATION, token);
            log.info("Added JWT token from session to forwarded headers for user: {}", session.getAttribute("user"));
        }

        log.info("Forwarding headers: {}", headers);

        HttpEntity<?> entity;

        String contentType = request.getContentType();
        log.info("Request Content-Type: {}", contentType);

        if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
            log.info("Detected multipart/form-data request. Processing as multipart.");
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Add text parts
            for (Map.Entry<String, String[]> entry : multipartRequest.getParameterMap().entrySet()) {
                String paramName = entry.getKey();
                for (String value : entry.getValue()) {
                    log.info("Adding text part -> {}: {}", paramName, value);
                    body.add(paramName, value);
                }
            }

            // Add file parts
            for (Map.Entry<String, MultipartFile> entry : multipartRequest.getFileMap().entrySet()) {
                String partName = entry.getKey();
                MultipartFile file = entry.getValue();
                if (file != null && !file.isEmpty()) {
                    log.info("Adding file part -> {}: {} ({} bytes)", partName, file.getOriginalFilename(), file.getSize());
                    ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add(partName, resource);
                } else {
                    log.warn("File part '{}' is null or empty.", partName);
                }
            }

            entity = new HttpEntity<>(body, headers);
            log.info("Constructed multipart HttpEntity.");
        } else {
            log.info("Processing as a standard request.");
            byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
            entity = new HttpEntity<>(body, headers);
        }

        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(new URI(uri), method, entity, byte[].class);
            log.info("Received response with status: {}", responseEntity.getStatusCode());

            String requestUri = request.getRequestURI();
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                if (requestUri.equals("/api/users/login")) {
                    try {
                        log.info("Processing successful login response.");
                        Map<String, Object> responseBody = objectMapper.readValue(responseEntity.getBody(), Map.class);
                        String username = (String) responseBody.get("username");
                        String authHeader = responseEntity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                        if (username != null && authHeader != null) {
                            HttpSession newSession = request.getSession(true); // Create a new session
                            newSession.setAttribute("user", username);
                            newSession.setAttribute("jwt_token", authHeader);
                            log.info("User '{}' and JWT token set in session.", username);
                        } else {
                            log.warn("Username or Auth Header not found in login response body.");
                        }
                    } catch (IOException e) {
                        log.error("Failed to parse login response body", e);
                    }
                } else if (requestUri.equals("/api/users/logout")) {
                    log.info("Processing logout response.");
                    request.getSession().invalidate();
                    log.info("Session invalidated.");
                }
            }
            return responseEntity;
        } catch (HttpClientErrorException e) {
            log.error("Error from API server: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            log.error("An unexpected error occurred during proxying", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Proxying failed".getBytes());
        }
    }
}
