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
    <link rel="stylesheet" href="/css/postList.css"> </head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="board-container">
        <div class="board-header">
            <h2 class="board-title">게시판</h2>
            <div class="post-list-count">전체 <span id="totalCount">0</span>개</div> <a href="/board/newPost" class="new-post-btn">새 글 작성</a>
        </div>

        <div class="post-list">
            <table>
                <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                </tr>
                </thead>
                <tbody id="postListBody">
                </tbody>
            </table>
        </div>

        <div class="empty-message" id="emptyMessage" style="display: none;">
            아직 게시글이 없습니다. 첫 번째 글을 작성해 보세요!
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
        const apiUrl = '/api/posts';

        console.log("게시글 목록 API 호출 URL:", apiUrl);

        fetch(apiUrl, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                console.log('API 응답 수신:', response);
                if (!response.ok) {
                    if (response.status === 401 || response.status === 403) {
                        localStorage.removeItem('jwtToken');
                        alert('인증이 만료되었거나 유효하지 않습니다. 다시 로그인해주세요.');
                        window.location.href = '/login';
                    }
                    throw new Error('게시글을 불러오는데 실패했습니다: ' + response.status + ' ' + response.statusText);
                }
                return response.json();
            })
            .then(posts => {
                console.log('서버로부터 받은 게시글 데이터:', posts);

                const tbody = document.getElementById('postListBody');
                const totalCountElement = document.getElementById('totalCount'); // 전체 개수 엘리먼트
                const emptyMessage = document.getElementById('emptyMessage');

                if (!Array.isArray(posts)) {
                    console.error('서버 응답이 배열이 아닙니다:', posts);
                    alert('잘못된 게시글 데이터 형식입니다.');
                    tbody.innerHTML = '<tr><td colspan="4">게시글을 불러올 수 없습니다.</td></tr>';
                    totalCountElement.textContent = 0;
                    emptyMessage.style.display = 'none'; // 오류 시 빈 메시지는 숨김
                    return;
                }

                totalCountElement.textContent = posts.length; // 전체 개수 업데이트

                if (posts.length === 0) {
                    tbody.innerHTML = ''; // 테이블 내용 비움
                    emptyMessage.style.display = 'block'; // 빈 메시지 표시
                    return;
                } else {
                    emptyMessage.style.display = 'none'; // 게시글이 있으면 빈 메시지 숨김
                }

                tbody.innerHTML = ''; // 기존 내용 초기화

                posts.forEach((post, index) => {
                    const createdAt = new Date(post.createdAt).toLocaleDateString('ko-KR');
                    const postNumber = posts.length - index;
                    const fileAttachmentIcon = post.fileName ? '<span class="file-icon">📎</span>' : '';

                    // ⭐⭐⭐ HTML 문자열에 data-label 속성 추가 ⭐⭐⭐
                    const rowHtml = '<tr>' +
                        '<td data-label="번호">' + postNumber + '</td>' +
                        '<td data-label="제목">' +
                        '<a href="/board/posts/' + post.id + '" class="post-title-link">' +
                        post.title + fileAttachmentIcon +
                        '</a>' +
                        '</td>' +
                        '<td data-label="작성자">' + (post.author || '알 수 없음') + '</td>' +
                        '<td data-label="작성일">' + createdAt + '</td>' +
                        '</tr>';

                    tbody.insertAdjacentHTML('beforeend', rowHtml);
                });
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                alert('게시글을 불러오는데 실패했습니다.');
                document.getElementById('postListBody').innerHTML = '<tr><td colspan="4">게시글을 불러오는 중 오류가 발생했습니다.</td></tr>';
                document.getElementById('totalCount').textContent = 0;
                document.getElementById('emptyMessage').style.display = 'none'; // 오류 시 빈 메시지는 숨김
            });
    }
</script>
</body>
</html>