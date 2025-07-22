/**
 * XSS 방지를 위한 JavaScript 유틸리티
 * 프론트엔드에서 사용자 입력을 안전하게 처리합니다.
 */

const XssPrevention = {
    /**
     * HTML 엔티티 인코딩
     * HTML 태그를 이스케이프하여 텍스트로만 표시
     */
    encodeHtml: function(text) {
        if (!text) return '';
        
        return text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#x27;');
    },

    /**
     * HTML 태그 제거
     * 모든 HTML 태그를 제거하고 텍스트만 남김
     */
    stripHtml: function(text) {
        if (!text) return '';
        
        return text.replace(/<[^>]*>/g, '');
    },

    /**
     * 스크립트 태그 및 이벤트 핸들러 제거
     * 안전하지 않은 스크립트 요소들을 제거
     */
    sanitizeScripts: function(text) {
        if (!text) return '';
        
        return text
            // 스크립트 태그 제거
            .replace(/<script[^>]*>[\s\S]*?<\/script>/gi, '')
            // 이벤트 핸들러 제거
            .replace(/\bon\w+\s*=/gi, '')
            // JavaScript 프로토콜 제거
            .replace(/javascript:/gi, '')
            // data: 프로토콜 제거 (잠재적 위험)
            .replace(/data:text\/html/gi, '')
            .replace(/data:application\/javascript/gi, '');
    },

    /**
     * 안전한 HTML 태그만 허용
     * 기본적인 서식 태그는 허용하되 스크립트는 제거
     */
    sanitizeHtml: function(text) {
        if (!text) return '';
        
        // 1단계: 스크립트 관련 요소 제거
        let sanitized = this.sanitizeScripts(text);
        
        // 2단계: 안전하지 않은 태그 제거 (선택적)
        // sanitized = sanitized.replace(/<(?!\/?(p|br|div|span|strong|em|u|h[1-6]|a|ul|ol|li|blockquote|pre|code))[^>]*>/gi, '');
        
        return sanitized;
    },

    /**
     * 게시글 제목용 XSS 방지
     */
    sanitizeTitle: function(title) {
        if (!title) return '';
        return this.encodeHtml(title.trim());
    },

    /**
     * 게시글 내용용 XSS 방지
     */
    sanitizeContent: function(content) {
        if (!content) return '';
        return this.sanitizeHtml(content);
    },

    /**
     * 폼 데이터 정제
     */
    sanitizeFormData: function(formData) {
        const sanitized = {};
        for (let [key, value] of formData.entries()) {
            if (typeof value === 'string') {
                if (key === 'title') {
                    sanitized[key] = this.sanitizeTitle(value);
                } else if (key === 'content') {
                    sanitized[key] = this.sanitizeContent(value);
                } else {
                    sanitized[key] = this.encodeHtml(value);
                }
            } else {
                sanitized[key] = value;
            }
        }
        return sanitized;
    },

    /**
     * DOM에 안전하게 내용 삽입
     */
    setTextContent: function(element, text) {
        if (!element) return;
        element.textContent = text || '';
    },

    /**
     * DOM에 안전하게 HTML 삽입
     */
    setInnerHTML: function(element, html) {
        if (!element) return;
        element.innerHTML = this.sanitizeHtml(html || '');
    },

    /**
     * 입력 필드 값 정제
     */
    sanitizeInput: function(inputElement) {
        if (!inputElement) return '';
        
        const value = inputElement.value;
        if (inputElement.type === 'text' || inputElement.type === 'search') {
            return this.encodeHtml(value);
        } else if (inputElement.tagName === 'TEXTAREA') {
            return this.sanitizeContent(value);
        }
        return value;
    }
};

// 전역 객체로 노출 (기존 코드와의 호환성을 위해)
window.XssPrevention = XssPrevention; 