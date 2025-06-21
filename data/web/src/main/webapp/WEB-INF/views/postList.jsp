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
                </tr>
                </thead>
                <tbody id="postListBody">
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
        const apiUrl = '/api/posts';

        console.log("게시글 목록 API 호출 URL:", apiUrl);

        fetch(apiUrl, {
            headers: {
                'Authorization': 'Bearer ' + token // 문자열 연결 방식 유지
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
                if (!Array.isArray(posts)) {
                    console.error('서버 응답이 배열이 아닙니다:', posts);
                    alert('잘못된 게시글 데이터 형식입니다.');
                    tbody.innerHTML = '<tr><td colspan="4">게시글을 불러올 수 없습니다.</td></tr>';
                    document.getElementById('totalCount').textContent = 0;
                    return;
                }

                document.getElementById('totalCount').textContent = posts.length;

                if (posts.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="4">게시글이 없습니다. 새 글을 작성해 보세요.</td></tr>';
                    return;
                }

                // ⭐ ⭐ ⭐ 이 부분에서 템플릿 리터럴을 문자열 연결로 변경합니다. ⭐ ⭐ ⭐
                // posts.map 대신 forEach 루프를 사용하여 각 행을 직접 추가합니다.
                // 이렇게 하면 템플릿 리터럴 파싱 문제를 완전히 우회할 수 있습니다.
                tbody.innerHTML = ''; // 기존 내용을 초기화
                posts.forEach((post, index) => {
                    const createdAt = new Date(post.createdAt).toLocaleDateString('ko-KR');

                    // 번호는 총 게시글 수에서 현재 인덱스를 빼는 방식으로 계산 (최신글이 1번)
                    const postNumber = posts.length - index;

                    // 파일 첨부 아이콘 추가 여부
                    const fileAttachmentIcon = post.fileName ? '📎' : '';

                    // HTML 문자열을 문자열 연결로 구성
                    const rowHtml = '<tr>' +
                        '<td>' + postNumber + '</td>' +
                        '<td>' +
                        '<a href="/board/posts/' + post.id + '" class="post-title-link">' +
                        post.title + ' ' + fileAttachmentIcon +
                        '</a>' +
                        '</td>' +
                        '<td>' + (post.author || '알 수 없음') + '</td>' + // author가 없을 경우 대비
                        '<td>' + createdAt + '</td>' +
                        '</tr>';

                    tbody.insertAdjacentHTML('beforeend', rowHtml); // tbody에 HTML 추가
                });
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                alert('게시글을 불러오는데 실패했습니다.');
                document.getElementById('postListBody').innerHTML = '<tr><td colspan="4">게시글을 불러오는 중 오류가 발생했습니다.</td></tr>';
                document.getElementById('totalCount').textContent = 0;
            });
    }
</script>
</body>
</html>