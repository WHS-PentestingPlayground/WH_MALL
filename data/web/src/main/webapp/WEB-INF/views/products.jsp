<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>제품 소개 - WHS 쇼핑몰</title>
    <link rel="stylesheet" href="/css/header.css" />
    <link rel="stylesheet" href="/css/main.css" />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="products-container">
        <h1 class="products-title">WHS 쇼핑몰 제품 소개</h1>
        <p class="products-subtitle">최고의 품질과 혁신적인 기술로 만든 프리미엄 제품들을 만나보세요</p>
        
        <div class="product-cards">
            <a href="/products/smartphone-pro" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">📱</div>
                    <div class="product-name">스마트폰 Pro</div>
                    <div class="product-description">
                        최신 기술이 적용된 프리미엄 스마트폰입니다. 고성능 카메라와 긴 배터리 수명으로 
                        일상생활을 더욱 편리하게 만들어드립니다.
                    </div>
                    <ul class="product-features">
                        <li>6.7인치 OLED 디스플레이</li>
                        <li>48MP 트리플 카메라</li>
                        <li>5000mAh 대용량 배터리</li>
                        <li>5G 네트워크 지원</li>
                    </ul>
                </div>
            </a>
            
            <a href="/products/laptop-ultra" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">💻</div>
                    <div class="product-name">노트북 Ultra</div>
                    <div class="product-description">
                        업무와 엔터테인먼트를 위한 고성능 노트북입니다. 빠른 처리 속도와 
                        선명한 화질로 모든 작업을 효율적으로 처리할 수 있습니다.
                    </div>
                    <ul class="product-features">
                        <li>15.6인치 4K 디스플레이</li>
                        <li>Intel i7 프로세서</li>
                        <li>16GB RAM + 512GB SSD</li>
                        <li>NVIDIA RTX 그래픽</li>
                    </ul>
                </div>
            </a>
            
            <a href="/products/wireless-earbuds" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">🎧</div>
                    <div class="product-name">무선 이어폰</div>
                    <div class="product-description">
                        프리미엄 사운드 품질을 제공하는 무선 이어폰입니다. 노이즈 캔슬링 기능과 
                        편안한 착용감으로 완벽한 음악 감상을 경험하세요.
                    </div>
                    <ul class="product-features">
                        <li>액티브 노이즈 캔슬링</li>
                        <li>24시간 재생 시간</li>
                        <li>방수 IPX4 등급</li>
                        <li>터치 컨트롤</li>
                    </ul>
                </div>
            </a>
            
            <a href="/products/smartwatch" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">⌚</div>
                    <div class="product-name">스마트워치</div>
                    <div class="product-description">
                        건강 관리와 일상 편의 기능을 모두 갖춘 스마트워치입니다. 
                        심박수 모니터링과 GPS 기능으로 건강한 라이프스타일을 지원합니다.
                    </div>
                    <ul class="product-features">
                        <li>1.4인치 AMOLED 터치스크린</li>
                        <li>심박수 모니터링</li>
                        <li>GPS 내비게이션</li>
                        <li>7일 배터리 수명</li>
                    </ul>
                </div>
            </a>
            
            <a href="/products/mirrorless-camera" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">📷</div>
                    <div class="product-name">미러리스 카메라</div>
                    <div class="product-description">
                        전문가급 사진과 영상을 촬영할 수 있는 미러리스 카메라입니다. 
                        고해상도 센서와 다양한 렌즈 옵션으로 창의적인 촬영이 가능합니다.
                    </div>
                    <ul class="product-features">
                        <li>24.1MP 풀프레임 센서</li>
                        <li>4K 영상 촬영</li>
                        <li>5축 손떨림 보정</li>
                        <li>Wi-Fi 연결</li>
                    </ul>
                </div>
            </a>
            
            <a href="/products/gaming-console" class="product-card-link">
                <div class="product-card">
                    <div class="product-image">🎮</div>
                    <div class="product-name">게이밍 콘솔</div>
                    <div class="product-description">
                        최신 게임을 최고의 품질로 즐길 수 있는 게이밍 콘솔입니다. 
                        빠른 로딩 속도와 부드러운 그래픽으로 몰입감 있는 게임 경험을 제공합니다.
                    </div>
                    <ul class="product-features">
                        <li>4K 게이밍 지원</li>
                        <li>1TB SSD 저장공간</li>
                        <li>Ray Tracing 기술</li>
                        <li>듀얼센스 컨트롤러</li>
                    </ul>
                </div>
            </a>
        </div>
        
        <div style="text-align: center;">
            <a href="/" class="back-button">홈으로 돌아가기</a>
        </div>
    </div>
</main>
</body>
</html> 