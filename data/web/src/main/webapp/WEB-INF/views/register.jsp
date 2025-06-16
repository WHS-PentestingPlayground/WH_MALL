<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/register.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="login-section">
    <div class="login-container">
        <h2 class="form-title">회원가입</h2>
        <div id="errorMessage" class="error-message" style="display:none;"></div>
        <form id="registerForm">
            <div class="form-group">
                <label for="username" class="form-label">아이디</label>
                <input type="text" id="username" name="username" class="form-input" required>
            </div>
            <div class="form-group">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" id="password" name="password" class="form-input" required>
                <div class="password-requirements">
                    영문, 숫자, 특수문자를 포함한 8자 이상
                </div>
            </div>
            <div class="form-group">
                <label for="email" class="form-label">이메일</label>
                <input type="email" id="email" name="email" class="form-input" required>
            </div>
            <button type="submit" class="form-button">회원가입</button>
        </form>
        <a href="/login" class="form-link">로그인으로 돌아가기</a>
    </div>
</div>

<script>
    document.getElementById('registerForm').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const email = document.getElementById('email').value;
        const errorMessageDiv = document.getElementById('errorMessage');

        fetch('/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, email })
        })
        .then(response => {
            if (response.ok) {
                alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/login';
            } else {
                return response.json().then(errorData => {
                    let message = '회원가입 중 알 수 없는 오류가 발생했습니다.';
                    if (response.status === 400) {
                        if (errorData.message) {
                            message = `회원가입 실패: ${errorData.message}`;
                        } else {
                            message = '회원가입 실패: 입력 값을 확인하세요.';
                        }
                    } else if (response.status === 409) {
                        message = '회원가입 실패: 이미 존재하는 사용자명입니다.';
                    } else if (response.status >= 500) {
                        message = '서버 오류: 잠시 후 다시 시도해주세요.';
                    }
                    errorMessageDiv.textContent = message;
                    errorMessageDiv.style.display = 'block';
                }).catch(() => {
                    errorMessageDiv.textContent = '회원가입 실패: 서버 응답을 처리할 수 없습니다.';
                    errorMessageDiv.style.display = 'block';
                });
            }
        })
        .catch(error => {
            console.error('Fetch error:', error);
            errorMessageDiv.textContent = '네트워크 오류가 발생했습니다. 인터넷 연결을 확인하세요.';
            errorMessageDiv.style.display = 'block';
        });
    });
</script>
</body>
</html>
