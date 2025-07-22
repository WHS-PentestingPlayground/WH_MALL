package com.whs.dev2.util;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class XssPreventionUtil {

    // HTML 태그 및 스크립트 패턴
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
        "<script[^>]*>.*?</script>", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile(
        "<[^>]*>", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile(
        "javascript:", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern ON_EVENT_PATTERN = Pattern.compile(
        "on\\w+\\s*=", 
        Pattern.CASE_INSENSITIVE
    );

    /**
     * HTML 인코딩 (가장 안전한 방법)
     * HTML 태그를 이스케이프하여 텍스트로만 표시
     */
    public String encodeForHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }

    /**
     * HTML 정제 (안전한 HTML 태그만 허용)
     * 기본적인 HTML 태그는 허용하되 스크립트는 제거
     */
    public String sanitizeHtml(String input) {
        if (input == null) {
            return "";
        }
        
        // 1단계: 스크립트 태그 제거
        String step1 = SCRIPT_PATTERN.matcher(input).replaceAll("");
        
        // 2단계: 이벤트 핸들러 제거
        String step2 = ON_EVENT_PATTERN.matcher(step1).replaceAll("");
        
        // 3단계: JavaScript 프로토콜 제거
        String step3 = JAVASCRIPT_PATTERN.matcher(step2).replaceAll("");
        
        // 4단계: 안전하지 않은 HTML 태그 제거 (선택적)
        // step3 = HTML_TAG_PATTERN.matcher(step3).replaceAll("");
        
        return step3;
    }

    /**
     * 정규식을 사용한 기본적인 XSS 필터링
     * 스크립트 태그와 이벤트 핸들러 제거
     */
    public String filterXssBasic(String input) {
        if (input == null) {
            return "";
        }
        
        String filtered = input;
        
        // 스크립트 태그 제거
        filtered = SCRIPT_PATTERN.matcher(filtered).replaceAll("");
        
        // JavaScript 프로토콜 제거
        filtered = JAVASCRIPT_PATTERN.matcher(filtered).replaceAll("");
        
        // 이벤트 핸들러 제거
        filtered = ON_EVENT_PATTERN.matcher(filtered).replaceAll("");
        
        return filtered;
    }

    /**
     * 게시글 제목용 XSS 방지 (HTML 인코딩만 적용)
     */
    public String sanitizeTitle(String title) {
        if (title == null) {
            return "";
        }
        return encodeForHtml(title.trim());
    }

    /**
     * 게시글 내용용 XSS 방지 (HTML 정제 적용)
     * 안전한 HTML 태그는 허용하되 스크립트는 제거
     */
    public String sanitizeContent(String content) {
        if (content == null) {
            return "";
        }
        
        // 1단계: 기본 XSS 필터링
        String step1 = filterXssBasic(content);
        
        // 2단계: HTML 정제 (안전한 태그만 허용)
        String step2 = sanitizeHtml(step1);
        
        return step2;
    }

    /**
     * 사용자 입력 검증 (XSS 패턴 감지)
     */
    public boolean containsXssPattern(String input) {
        if (input == null) {
            return false;
        }
        
        return SCRIPT_PATTERN.matcher(input).find() ||
               JAVASCRIPT_PATTERN.matcher(input).find() ||
               ON_EVENT_PATTERN.matcher(input).find();
    }

    /**
     * 게시글 데이터 전체 정제
     */
    public void sanitizePostData(com.whs.dev2.dto.PostRequestDto dto) {
        if (dto.getTitle() != null) {
            dto.setTitle(sanitizeTitle(dto.getTitle()));
        }
        if (dto.getContent() != null) {
            dto.setContent(sanitizeContent(dto.getContent()));
        }
    }
} 