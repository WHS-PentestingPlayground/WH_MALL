<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>쿠폰 발급 - WHS 쇼핑몰</title>
    <link rel="stylesheet" href="/css/index.css" />
    <style>
        .coupon-issue-container {
            max-width: 480px;
            margin: 80px auto;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 16px rgba(26,42,108,0.07);
            padding: 2.5rem 2rem;
            text-align: center;
        }
        .coupon-issue-title {
            font-size: 1.7rem;
            color: #d72660;
            font-weight: 700;
            margin-bottom: 1.2rem;
        }
        .coupon-issue-desc {
            font-size: 1.1rem;
            color: #1a2a6c;
            margin-bottom: 2.2rem;
        }
        .home-btn {
            display: inline-block;
            background: #1a2a6c;
            color: #fff;
            padding: 0.8rem 2.2rem;
            border-radius: 10px;
            text-decoration: none;
            font-weight: 600;
            font-size: 1.1rem;
            transition: background 0.2s;
        }
        .home-btn:hover {
            background: #2a3a7c;
        }
    </style>
</head>
<body>
    <div class="coupon-issue-container">
        <img src="/img/vip-coupon.png" alt="VIP 쿠폰" style="width:220px; margin-bottom:1.5rem; border-radius:12px; box-shadow:0 2px 8px rgba(26,42,108,0.08);" />
        <div class="coupon-issue-title">VIP 쿠폰 발급 페이지</div>
        <div class="coupon-issue-desc">
            WHS 쇼핑몰 VIP 특별 이벤트 쿠폰을 지금 바로 발급받으세요!<br/>
            (※ 실제 쿠폰 발급 기능은 추후 구현 예정)
        </div>
        <a href="/" class="home-btn">홈으로 돌아가기</a>
    </div>
</body>
</html> 