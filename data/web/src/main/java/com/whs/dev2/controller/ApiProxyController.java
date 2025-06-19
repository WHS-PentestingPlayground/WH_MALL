package com.whs.dev2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;

@Controller
public class ApiProxyController {

    @Value("${API_SERVER_URL}")
    private String apiServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws URISyntaxException, IOException {
        String uri = apiServerUrl + request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        if (method == null) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Unsupported HTTP method".getBytes());
        }

        // 헤더 복사 (Content-Type은 제외)
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("content-length")) {
                headers.put(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        // 바디 읽기
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(new URI(uri), method, entity, byte[].class);
    }
}
