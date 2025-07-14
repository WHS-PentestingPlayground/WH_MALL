<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>새 글 작성 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/postForm.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Playfair+Display:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" />
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="form-container">
        <h2 class="form-title">새 글 작성</h2>
        <form id="postForm">
            <div class="form-group">
                <label for="title" class="form-label">제목</label>
                <input type="text" id="title" name="title" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="content" class="form-label">내용</label>
                <textarea id="content" name="content" class="form-input" required></textarea>
            </div>

            <div class="form-group">
                <div class="form-label">작성자: <b id="authorName"></b></div>
            </div>

            <button type="submit" class="form-button">등록</button>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('로그인 후 이용 가능합니다.');
        window.location.href = '/login';
        return;
    }
    try {
        const base64 = token.split('.')[1].replace(/-/g,'+').replace(/_/g,'/');
        const payload = JSON.parse(atob(base64));
        if (payload.role !== 'partner') {             // partner가 아니면
            alert('파트너 회원만 접근 가능합니다');
            return location.href = '/board/posts';  // 메인 or 목록 페이지
        }
    } catch (e) {
        console.warn('JWT 파싱 오류', e);
        alert('로그인 정보가 올바르지 않습니다.');
        return location.href = '/login';
    }

    // 사용자 정보 가져오기
    fetch('/api/users/me', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Unauthorized');
        }
        return response.json();
    })
    .then(user => {
        document.getElementById('authorName').textContent = user.username;
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
    });

    // 폼 제출 처리
    document.getElementById('postForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const postData = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        fetch('/api/posts/create', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create post');
            }
            return response.json();
        })
        .then(data => {
            alert('가맹점 문의가 등록되었습니다.');
            window.location.href = '/board/posts';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('가맹점 문의 등록에 실패했습니다.');
        });
    });
});
</script>
</body>
</html>
