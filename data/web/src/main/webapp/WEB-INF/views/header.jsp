<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>

<header class="header">
    <nav class="header-nav">
        <!-- 왼쪽: 로고 -->
        <div class="header-logo">
            <a href="/" class="header-logo-link">
                <span class="header-logo-emoji">🤖</span>
                <span class="header-logo-text">WH 화햇로보틱스</span>
            </a>
        </div>

        <!-- 오른쪽: 유저 메뉴 -->
        <div class="header-user-menu" id="userMenu">
            <!-- 이 부분은 auth.js에 의해 동적으로 채워집니다. -->
        </div>
    </nav>
</header>

<!-- 모든 페이지에서 인증 상태를 관리하기 위한 중앙 스크립트 -->
<script src="/js/auth.js"></script>
