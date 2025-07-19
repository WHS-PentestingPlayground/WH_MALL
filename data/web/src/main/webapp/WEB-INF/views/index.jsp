<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>WH MALL</title>
    <link rel="stylesheet" href="/css/header.css" />
    <link rel="stylesheet" href="/css/index.css" />
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="hero-section">
        <h1 class="hero-title">대한민국이 선택한 최고의 쇼핑몰</h1>
        <p class="hero-subtitle">WH MALL에 오신 것을 환영합니다</p>

        <!-- 쿠폰 배너 시작 -->
        <a href="/coupon-issue" class="coupon-banner">
            <img src="/img/vip-coupon.png" alt="VIP 쿠폰 배너" class="coupon-banner-img" />
            <div class="coupon-banner-text">
                <strong>WH 쇼핑몰 VIP 쿠폰 특별 이벤트!</strong><br/>
                지금 바로 쿠폰을 발급받고 다양한 혜택을 누리세요.
            </div>
        </a>
        <!-- 쿠폰 배너 끝 -->

        <div class="cta-buttons">
            <a href="/board/posts" class="cta-button primary">
                <span class="button-icon">📝</span>
                가맹점 문의
            </a>
            <a href="/products" class="cta-button secondary">
                <span class="button-icon">🛍️</span>
                제품 소개
            </a>
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a href="/login" class="cta-button secondary">
                        <span class="button-icon">🔑</span>
                        로그인
                    </a>
                    <a href="/register" class="cta-button secondary">
                        <span class="button-icon">👤</span>
                        회원가입
                    </a>
                </c:when>
            </c:choose>
        </div>
    </div>

<%--    <div class="features-section">--%>
<%--        <div class="feature-card">--%>
<%--            <div class="feature-icon">🚀</div>--%>
<%--            <h3>빠른 개발</h3>--%>
<%--            <p>최신 기술을 활용한 빠른 개발 환경</p>--%>
<%--        </div>--%>
<%--        <div class="feature-card">--%>
<%--            <div class="feature-icon">🔒</div>--%>
<%--            <h3>안전한 보안</h3>--%>
<%--            <p>강력한 보안 시스템으로 안전한 서비스</p>--%>
<%--        </div>--%>
<%--        <div class="feature-card">--%>
<%--            <div class="feature-icon">💡</div>--%>
<%--            <h3>혁신적인 기능</h3>--%>
<%--            <p>사용자 중심의 혁신적인 기능 제공</p>--%>
<%--        </div>--%>
<%--    </div>--%>
</main>
</body>
</html>
