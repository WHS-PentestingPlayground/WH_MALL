<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 - 화햇 로보틱스</title>
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/postList.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="post-list-container">
        <h2 class="post-list-title">게시판</h2>
        <div class="post-list-header">
            <div class="post-list-count">전체 <span id="totalCount">0</span>개</div>
            <a href="/board/newPost" class="new-post-button">새 글 작성</a>
        </div>
        <div class="post-list">
            <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                        <th>조회수</th>
                    </tr>
                </thead>
                <tbody id="postListBody">
                    <!-- 게시글 목록이 여기에 동적으로 로드됩니다 -->
                </tbody>
            </table>
        </div>
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
    loadPosts();
});

function loadPosts() {
    const token = localStorage.getItem('jwtToken');

    fetch('/api/posts', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    alert('인증이 만료되었거나 유효하지 않습니다. 다시 로그인해주세요.');
                    window.location.href = '/login';
                }
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(posts => {
            const tbody = document.getElementById('postListBody');
            document.getElementById('totalCount').textContent = posts.length;

            tbody.innerHTML = posts.map((post, index) => {
                const createdAt = new Date(post.createdAt).toLocaleDateString('ko-KR');
                return `
                    <tr>
                        <td>${posts.length - index}</td>
                        <td>
                            <a href="/board/posts/${post.id}" class="post-title-link">
                                ${post.title}
                                ${post.fileName ? '📎' : ''}
                            </a>
                        </td>
                        <td>${post.author}</td>
                        <td>${createdAt}</td>
                        <td>${post.viewCount != null ? post.viewCount : 0}</td>
                    </tr>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error loading posts:', error);
            alert('게시글을 불러오는데 실패했습니다.');
        });
}
</script>
</body>
</html>