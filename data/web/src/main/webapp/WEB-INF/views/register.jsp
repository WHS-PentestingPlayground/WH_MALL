<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WH MALL - 회원가입</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/register.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
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
                <div class="username-requirements" id="usernameHelp">
                    영문 대소문자, 숫자만 사용 가능 (6~20자)
                </div>
            </div>
            <div class="form-group">
                <label for="password" class="form-label">비밀번호</label>
                <input type="password" id="password" name="password" class="form-input" required>
            </div>
            <div class="form-group">
                <label for="authCode" class="form-label">인증코드</label>
                <input type="password" id="authCode" name="authCode" class="form-input" required>
            </div>
            <button type="submit" class="form-button">회원가입</button>
        </form>
        <a href="/login" class="form-link">로그인으로 돌아가기</a>
    </div>
</div>

<script>

    const a = "";
    const b = "";
    const c = "";

    // 아이디 실시간 검증
    document.getElementById('username').addEventListener('input', function() {
        const username = this.value;
        const helpElement = document.getElementById('usernameHelp');
        const usernameRegex = /^[A-Za-z0-9]{6,20}$/;
        
        if (username.length === 0) {
            helpElement.textContent = '영문 대소문자, 숫자만 사용 가능 (6~20자)';
            helpElement.className = 'username-requirements';
        } else if (!usernameRegex.test(username)) {
            if (username.length < 6) {
                helpElement.textContent = '최소 6글자 이상 입력해주세요';
            } else if (username.length > 20) {
                helpElement.textContent = '최대 20글자까지 입력 가능합니다';
            } else if (!/^[A-Za-z0-9]+$/.test(username)) {
                helpElement.textContent = '영문 대소문자와 숫자만 사용 가능합니다';
            }
            helpElement.className = 'username-requirements error';
        } else {
            helpElement.textContent = '사용 가능한 아이디입니다';
            helpElement.className = 'username-requirements success';
        }
    });



    document.getElementById('registerForm').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const authCode = document.getElementById('authCode').value;
        const errorMessageDiv = document.getElementById('errorMessage')
        const correctAuthCode = window.atob(a) + window.atob(b) + window.atob(c);

        // 아이디 길이 검증
        if (username.length < 6) {
            errorMessageDiv.textContent = '아이디는 6글자 이상이어야 합니다.';
            errorMessageDiv.style.display = 'block';
            return;
        }

        // 아이디 정규 표현식 검증 (영문 대소문자, 숫자만 허용, 6-20자)
        const usernameRegex = /^[A-Za-z0-9]{6,20}$/;
        if (!usernameRegex.test(username)) {
            errorMessageDiv.textContent = '아이디는 영문 대소문자와 숫자만 사용하여 6~20자로 입력해주세요.';
            errorMessageDiv.style.display = 'block';
            return;
        }

        if (authCode !== correctAuthCode) {
            errorMessageDiv.textContent = '인증번호가 올바르지 않습니다.';
            errorMessageDiv.style.display = 'block';
            return;
        }

        fetch('/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, auth: authCode })
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
