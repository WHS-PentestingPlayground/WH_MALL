package com.whs.dev2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

@Controller
public class ApiProxyController {

    @Value("${API_SERVER_URL}")
    private String apiServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/api/**")
    public ResponseEntity<String> proxyApiRequest(HttpServletRequest request) throws URISyntaxException {
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = apiServerUrl + requestUri + (queryString != null ? "?" + queryString : "");

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, request.getHeader(headerName));
        }

        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(null, headers);

        // POST, PUT 요청의 경우 본문(body)을 복사
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
            try {
                // HttpServletRequest의 getReader()는 한 번만 읽을 수 있으므로 주의 필요
                // 여기서는 간단하게 String으로 변환하지만, 실제 대규모 앱에서는 Stream 복사가 필요
                StringBuilder requestBody = new StringBuilder();
                request.getReader().lines().forEach(line -> requestBody.append(line));
                httpEntity = new org.springframework.http.HttpEntity<>(requestBody.toString(), headers);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        }

        return restTemplate.exchange(new URI(fullUrl), httpMethod, httpEntity, String.class);
    }
} 