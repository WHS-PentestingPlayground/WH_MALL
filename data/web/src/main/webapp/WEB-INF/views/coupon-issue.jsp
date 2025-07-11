<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>쿠폰 발급 - WHS 쇼핑몰</title>
    <link rel="stylesheet" href="/css/main.css" />
    <link rel="stylesheet" href="/css/header.css" />
    <link rel="stylesheet" href="/css/coupon-issue.css" />
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="main-content">
        <div class="coupon-issue-container">
            <div class="coupon-header">
                <img src="/img/vip-coupon.png" alt="VIP 쿠폰" class="coupon-issue-image" />
                <div class="coupon-issue-title">VIP 쿠폰 발급</div>
                <div class="coupon-issue-desc">
                    WHS 쇼핑몰 <strong>VIP 특별 이벤트 쿠폰</strong>을 지금 바로 발급받으세요!
                </div>
            </div>

            <div class="coupon-details">
                <div class="coupon-info">
                    <div class="coupon-benefit">
                        <span class="discount-amount">100%</span>
                        <span class="discount-text">할인</span>
                    </div>
                    <div class="coupon-conditions">
                        <p>• 사용기간: 2024.12.31까지</p>
                        <p>• VIP 등급만 발급 가능</p>
                    </div>
                </div>
            </div>

            <div class="coupon-actions">
                <button type="button" class="issue-btn" onclick="issueCoupon()">쿠폰 발급받기</button>
                <a href="/" class="home-btn">홈으로 돌아가기</a>
            </div>
        </div>
    </div>

    <script>
        function issueCoupon() {
            alert('VIP 등급 연동 기능이 구현 예정입니다.\n\n추후 VIP 등급 사용자만 쿠폰을 발급받을 수 있도록 구현될 예정입니다.');
        }
    </script>
</body>
</html> 