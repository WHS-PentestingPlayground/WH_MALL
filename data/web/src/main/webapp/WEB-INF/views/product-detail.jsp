<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${product.name} - WHS 쇼핑몰</title>
    <link rel="stylesheet" href="/css/header.css" />
    <link rel="stylesheet" href="/css/main.css" />
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
</head>
<body>
<%@ include file="header.jsp" %>
<main class="main-content">
    <div class="product-detail-container">
        <div class="product-detail-header">
            <a href="/products" class="back-link">← 제품 목록으로 돌아가기</a>
        </div>
        
        <div class="product-detail-content">
            <div class="product-detail-image-section">
                <div class="product-detail-image">${product.icon}</div>
            </div>
            
            <div class="product-detail-info">
                <h1 class="product-detail-title">${product.name}</h1>
                <div class="product-detail-price">₩${product.price}</div>
                <div class="product-detail-description">${product.description}</div>
                
                <div class="product-detail-specs">
                    <h3>제품 사양</h3>
                    <ul class="specs-list">
                        <c:forEach items="${product.specs}" var="spec">
                            <li>${spec}</li>
                        </c:forEach>
                    </ul>
                </div>
                
                <div class="product-detail-features">
                    <h3>주요 기능</h3>
                    <ul class="features-list">
                        <c:forEach items="${product.features}" var="feature">
                            <li>${feature}</li>
                        </c:forEach>
                    </ul>
                </div>
                
                <div class="product-detail-actions">
                    <button class="btn-secondary" onclick="handlePurchase()">즉시 구매</button>
                </div>
            </div>
        </div>
        
        <div class="product-detail-tabs">
            <div class="tab-buttons">
                <button class="tab-button active" data-tab="description">상품 설명</button>
                <button class="tab-button" data-tab="specs">상세 사양</button>
                <button class="tab-button" data-tab="reviews">리뷰</button>
            </div>
            
            <div class="tab-content">
                <div class="tab-panel active" id="description">
                    <h3>상품 상세 설명</h3>
                    <p>${product.detailDescription}</p>
                </div>
                
                <div class="tab-panel" id="specs">
                    <h3>상세 사양</h3>
                    <table class="specs-table">
                        <c:forEach items="${product.detailedSpecs}" var="spec">
                            <tr>
                                <td class="spec-label">${spec.key}</td>
                                <td class="spec-value">${spec.value}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                
                <div class="tab-panel" id="reviews">
                    <h3>고객 리뷰</h3>
                    <div class="reviews-section">
                        <p>아직 리뷰가 없습니다. 첫 번째 리뷰를 작성해보세요!</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabPanels = document.querySelectorAll('.tab-panel');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetTab = button.getAttribute('data-tab');
            
            // 모든 탭 버튼에서 active 클래스 제거
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabPanels.forEach(panel => panel.classList.remove('active'));
            
            // 클릭된 탭 버튼과 해당 패널에 active 클래스 추가
            button.classList.add('active');
            document.getElementById(targetTab).classList.add('active');
        });
    });
});

function handlePurchase() {
    // 로그인 상태 확인
    const token = localStorage.getItem('jwtToken');
    
    if (!token) {
        // 로그인하지 않은 경우
        alert('구매를 위해서는 로그인이 필요합니다.');
        // 현재 페이지 URL을 저장하여 로그인 후 돌아올 수 있도록 함
        localStorage.setItem('returnUrl', window.location.href);
        // 로그인 페이지로 이동
        window.location.href = '/login';
        return;
    }
    
    // 로그인된 경우 구매 로직 실행
    alert('잔액이 부족합니다.');
}
</script>
</body>
</html> 