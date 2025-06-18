<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/postDetail.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="post-detail-container">
        <div class="post-header">
            <h2 class="post-title" id="postTitle"></h2>
            <div class="post-meta">
                <span class="post-author" id="postAuthor"></span>
                <span class="post-date" id="postDate"></span>
                <span class="post-views">조회수: <span id="postViews">0</span></span>
            </div>
        </div>

        <div class="post-content" id="postContent"></div>

        <div class="post-attachment" id="postAttachment" style="display: none;">
            <h3>첨부파일</h3>
            <a href="#" id="attachmentLink" class="attachment-link"></a>
        </div>

        <div class="post-actions" id="postActions" style="display: none;">
            <button type="button" class="edit-button" onclick="editPost()">수정</button>
            <button type="button" class="delete-button" onclick="deletePost()">삭제</button>
        </div>

        <div class="post-navigation">
            <a href="/board/posts" class="back-button">목록으로</a>
        </div>
    </div>
</div>

<script>
let currentPostId = null;
let currentUserId = null;

document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
        return;
    }

    // URL에서 게시글 ID 가져오기
    const pathParts = window.location.pathname.split('/');
    currentPostId = pathParts[pathParts.length - 1];

    // 사용자 정보 가져오기
    fetch('/api/users/me', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => response.json())
    .then(user => {
        currentUserId = user.id;
        loadPostDetail();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('사용자 정보를 가져오는데 실패했습니다.');
    });
});

function loadPostDetail() {
    fetch(`/api/posts/${currentPostId}`)
        .then(response => response.json())
        .then(post => {
            document.getElementById('postTitle').textContent = post.title;
            document.getElementById('postAuthor').textContent = post.author;
            document.getElementById('postDate').textContent = new Date(post.createdAt).toLocaleString();
            document.getElementById('postViews').textContent = post.viewCount || 0;
            document.getElementById('postContent').textContent = post.content;

            // 첨부파일이 있는 경우
            if (post.fileName) {
                const attachmentDiv = document.getElementById('postAttachment');
                const attachmentLink = document.getElementById('attachmentLink');
                attachmentLink.href = `/api/post/${currentPostId}/file`;
                attachmentLink.textContent = post.fileName;
                attachmentDiv.style.display = 'block';
            }

            // 작성자인 경우에만 수정/삭제 버튼 표시
            if (post.user && post.user.id === currentUserId) {
                document.getElementById('postActions').style.display = 'block';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('게시글을 불러오는데 실패했습니다.');
        });
}

function editPost() {
    window.location.href = `/board/editPost?id=${currentPostId}`;
}

function deletePost() {
    if (!confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
        return;
    }

    const token = localStorage.getItem('jwtToken');
    fetch(`/api/posts/${currentPostId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to delete post');
        }
        alert('게시글이 삭제되었습니다.');
        window.location.href = '/board/posts';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('게시글 삭제에 실패했습니다.');
    });
}
</script>
</body>
</html>
