<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
    <%@ include file="header.jsp" %>
    <div class="login-section">
        <div class="login-container">
            <h2 class="form-title">로그인</h2>
            <div id="errorMessage" class="error-message" style="display:none;"></div>
            <form id="loginForm">
                <div class="form-group">
                    <label for="username" class="form-label">아이디</label>
                    <input type="text" id="username" name="username" class="form-input" required>
                </div>
                <div class="form-group">
                    <label for="password" class="form-label">비밀번호</label>
                    <input type="password" id="password" name="password" class="form-input" required>
                </div>
                <button type="submit" class="form-button">로그인</button>
            </form>
            <a href="/register" class="form-link">회원가입으로 이동</a>
        </div>
    </div>

    <script>
        document.getElementById('loginForm').addEventListener('submit', function(event) {
            event.preventDefault(); // 기본 폼 제출 방지

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const errorMessageDiv = document.getElementById('errorMessage');

            fetch('/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
            .then(response => {
                if (response.ok) {
                    const token = response.headers.get('Authorization');
                    if (token && token.startsWith('Bearer ')) {
                        localStorage.setItem('jwtToken', token.substring(7));
                        window.location.href = '/'; // 로그인 성공 후 메인 페이지로 이동
                    } else {
                        errorMessageDiv.textContent = '로그인 실패: 토큰이 발급되지 않았습니다.';
                        errorMessageDiv.style.display = 'block';
                    }
                } else if (response.status === 400) {
                    errorMessageDiv.textContent = '아이디 또는 비밀번호가 올바르지 않습니다.';
                    errorMessageDiv.style.display = 'block';
                } else {
                    errorMessageDiv.textContent = '로그인 중 오류가 발생했습니다.';
                    errorMessageDiv.style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                errorMessageDiv.textContent = '네트워크 오류가 발생했습니다.';
                errorMessageDiv.style.display = 'block';
            });
        });
    </script>
</body>
</html>