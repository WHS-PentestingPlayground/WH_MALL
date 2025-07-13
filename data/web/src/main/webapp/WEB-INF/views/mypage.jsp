<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 - WH MALL</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/mypage.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="mypage-container">
        <div class="mypage-content">
            
            <div class="user-info-section">
                <div class="user-profile">
                    <div class="profile-avatar">
                        <span class="avatar-text" id="avatarText">U</span>
                    </div>
                    <div class="profile-info">
                        <h2 class="username" id="username">사용자명</h2>
                        <div class="user-rank">
                            <span class="rank-badge" id="rankBadge">NORMAL</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="point-section">
                <h3 class="section-title">포인트 현황</h3>
                <div class="point-info">
                    <div class="current-point">
                        <span class="point-label">현재 포인트:</span>
                        <span class="point-value" id="currentPoint">0</span>
                        <span class="point-unit">P</span>
                    </div>
                    
                    <div class="vip-progress">
                        <div class="progress-info">
                            <span class="progress-label">VIP 등급까지</span>
                            <span class="progress-text" id="progressText">100,000P 남음</span>
                        </div>
                        <div class="progress-bar-container">
                            <div class="progress-bar" id="progressBar">
                                <div class="progress-fill" id="progressFill"></div>
                            </div>
                            <div class="progress-percentage" id="progressPercentage">0%</div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="rank-benefits">
                <h3 class="section-title">등급 혜택</h3>
                <div class="benefits-grid">
                    <div class="benefit-card normal-benefit">
                        <h4>NORMAL</h4>
                        <ul>
                            <li>기본 쇼핑 서비스</li>
                            <li>포인트 적립</li>
                        </ul>
                    </div>
                    <div class="benefit-card vip-benefit">
                        <h4>VIP</h4>
                        <ul>
                            <li>전용 쿠폰 제공</li>
                            <li>우선 배송</li>
                            <li>전용 고객센터</li>
                            <li>특별 할인 혜택</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <script src="/js/mypage.js"></script>
</body>
</html> 