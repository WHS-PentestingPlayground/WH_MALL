<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>에러</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/error.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
</head>
<body>
<%@ include file="header.jsp" %>
<div class="error-section">
    <div class="error-container">
        <div class="error-title">오류가 발생했습니다</div>
        <div class="error-code">에러 코드: ${statusCode}</div>
        <div class="error-message">${errorMessage}</div>
        <a href="/" class="form-link">메인으로 돌아가기</a>
    </div>
</div>
</body>
</html> 