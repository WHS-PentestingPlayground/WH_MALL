<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>글 수정 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/postForm.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="form-container">
        <h2 class="form-title">글 수정</h2>
        <form id="editPostForm" enctype="multipart/form-data">
            <input type="hidden" id="postId" value="${postId}" />

            <div class="form-group">
                <label for="title" class="form-label">제목</label>
                <input type="text" id="title" name="title" class="form-input" required />
            </div>

            <div class="form-group">
                <label for="content" class="form-label">내용</label>
                <textarea id="content" name="content" class="form-input" required></textarea>
            </div>

            <div class="form-group">
                <label for="file" class="form-label">파일 첨부 (선택)</label>
                <input type="file" id="file" name="file" class="form-input" />
                <p id="currentFile" class="mt-1" style="display: none;"></p>
            </div>

            <div class="form-group">
                <div class="form-label">작성자: <b id="authorName"></b></div>
            </div>

            <button type="submit" class="form-button">수정 완료</button>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }

    const postId = document.getElementById('postId').value;

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
        loadPostData(postId);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
    });
});

function loadPostData(postId) {
    const token = localStorage.getItem('jwtToken');
    fetch(`/api/posts/${postId}`, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => response.json())
    .then(post => {
        document.getElementById('title').value = post.title;
        document.getElementById('content').value = post.content;
        
        if (post.fileName) {
            const currentFile = document.getElementById('currentFile');
            currentFile.textContent = `현재 파일: ${post.fileName}`;
            currentFile.style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('게시글을 불러오는데 실패했습니다.');
    });
}

document.getElementById('editPostForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const token = localStorage.getItem('jwtToken');
    const postId = document.getElementById('postId').value;
    const formData = new FormData();
    formData.append('title', document.getElementById('title').value);
    formData.append('content', document.getElementById('content').value);

    const fileInput = document.getElementById('file');
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    fetch(`/api/posts/${postId}/edit`, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        body: formData
    })
    .then(response => {
        if (!response.ok) return response.text().then(msg => { throw new Error(msg); });
        return response.text();
    })
    .then(() => {
        alert('게시글이 수정되었습니다.');
        window.location.href = `/board/posts/${postId}`;
    })
    .catch(error => {
        console.error('Error:', error);
        alert('게시글 수정에 실패했습니다.\n' + error.message);
    });
});
</script>
</body>
</html>
