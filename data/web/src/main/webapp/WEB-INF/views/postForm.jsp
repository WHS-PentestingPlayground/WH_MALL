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
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="form-container">
        <h2 class="form-title">새 글 작성</h2>
        <form id="postForm" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title" class="form-label">제목</label>
                <input type="text" id="title" name="title" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="content" class="form-label">내용</label>
                <textarea id="content" name="content" class="form-input" required></textarea>
            </div>

            <div class="form-group">
                <label for="file" class="form-label">파일 첨부</label>
                <input type="file" id="file" name="file" class="form-input">
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

        const formData = new FormData();
        formData.append('title', document.getElementById('title').value);
        formData.append('content', document.getElementById('content').value);
        const fileInput = document.getElementById('file');
        if (fileInput.files.length > 0) {
            formData.append('file', fileInput.files[0]);
        }

        fetch('/api/post', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create post');
            }
            return response.json();
        })
        .then(data => {
            alert('게시글이 등록되었습니다.');
            window.location.href = '/board/posts';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('게시글 등록에 실패했습니다.');
        });
    });
});
</script>
</body>
</html>
