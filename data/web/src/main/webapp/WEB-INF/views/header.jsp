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

        <!-- 가운데: 메인 메뉴 -->
        <div class="header-main-menu">
            <a href="/board/posts" class="header-menu-link">게시판</a>
        </div>

        <!-- 오른쪽: 유저 메뉴 -->
        <div class="header-user-menu" id="userMenu">
            <!-- 로그인/회원가입 또는 사용자 정보/로그아웃 버튼이 JavaScript에 의해 동적으로 로드됩니다. -->
        </div>
    </nav>
</header>

<script>
    const apiServerUrl = "${apiServerUrl}";

    document.addEventListener('DOMContentLoaded', function() {
        checkLoginStatus();
    });

    function checkLoginStatus() {
        const token = localStorage.getItem('jwtToken');
        const userMenu = document.getElementById('userMenu');

        if (token) {
            // 토큰이 있으면 사용자 정보를 가져옵니다.
            fetch(`${apiServerUrl}/api/users/me`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    // 토큰이 유효하지 않으면 (예: 만료) 토큰을 제거합니다.
                    localStorage.removeItem('jwtToken');
                    return Promise.reject('Unauthorized');
                }
            })
            .then(data => {
                userMenu.innerHTML = `
                    <span class="header-username">${data.username}님</span>
                    <button type="button" class="header-logout-btn" onclick="logout()">로그아웃</button>
                `;
            })
            .catch(error => {
                console.error('Failed to fetch user data:', error);
                userMenu.innerHTML = `
                    <a href="/login" class="header-user-link">로그인</a>
                    <a href="/register" class="header-user-link">회원가입</a>
                `;
            });
        } else {
            // 토큰이 없으면 로그인/회원가입 버튼을 표시합니다.
            userMenu.innerHTML = `
                <a href="/login" class="header-user-link">로그인</a>
                <a href="/register" class="header-user-link">회원가입</a>
            `;
        }
    }

    function logout() {
        fetch(`${apiServerUrl}/api/users/logout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
            }
        })
        .then(response => {
            if (response.ok) {
                localStorage.removeItem('jwtToken');
                window.location.href = '/'; // 메인 페이지로 리다이렉트
            } else {
                console.error('Logout failed');
            }
        })
        .catch(error => {
            console.error('Error during logout:', error);
        });
    }
</script>
