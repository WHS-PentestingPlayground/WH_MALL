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
            alert('로그인 후 이용 가능합니다.');
            window.location.href = '/login';
            return;
        }

        const pathParts = window.location.pathname.split('/');
        const cleanedPathParts = pathParts.filter(part => part !== '');

        if (cleanedPathParts.length > 0) {
            currentPostId = cleanedPathParts[cleanedPathParts.length - 1];
            if (isNaN(currentPostId)) {
                console.error("URL에 유효한 게시글 ID가 없습니다:", currentPostId);
                alert('잘못된 접근입니다. 게시글 ID가 필요합니다.');
                window.location.href = '/board/posts';
                return;
            }
        } else {
            console.error("URL에 게시글 ID가 없습니다. 올바른 형식: /board/posts/{id}");
            alert('잘못된 접근입니다. 게시글 ID가 필요합니다.');
            window.location.href = '/board/posts';
            return;
        }

        // 사용자 정보 가져오기
        fetch('/api/users/me', {
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 401) {
                        localStorage.removeItem('jwtToken');
                        alert('세션이 만료되었거나 로그인이 필요합니다.');
                        window.location.href = '/login';
                    }
                    throw new new Error(`사용자 정보를 가져오는데 실패했습니다: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(user => {
                currentUserId = user.id;
                if (currentPostId) {
                    console.log("loadPostDetail 함수 호출 직전! currentPostId:", currentPostId); //  추가 로그 1
                    loadPostDetail();
                }
            })
            .catch(error => {
                console.error('사용자 정보 로드 오류:', error);
                alert('사용자 정보를 가져오는데 실패했습니다. 로그인이 필요할 수 있습니다.');
                window.location.href = '/login';
            });
    });

    function loadPostDetail() {
        // 템플릿 리터럴 대신 문자열 연결 사용
        const apiUrl = '/api/posts/' + currentPostId;
        console.log("loadPostDetail 함수 내부. API 호출 URL:", apiUrl);

        fetch(apiUrl) // 수정된 apiUrl 변수 사용
            .then(response => {
                console.log("API 응답 수신:", response);
                if (!response.ok) {
                    if (response.status === 404) {
                        alert('요청하신 게시글을 찾을 수 없습니다.');
                        window.location.href = '/board/posts';
                        return;
                    }
                    throw new Error(`게시글을 불러오는데 실패했습니다: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(post => {
                //게시글 데이터가 배열로 오고 있으므로 첫 번째 요소 사용
                // 만약 서버에서 특정 ID에 대한 응답이 배열이 아닌 단일 객체여야 한다면,
                // 서버 API를 수정해야 합니다. 여기서는 일단 받은 배열의 첫 요소를 사용합니다.
                const actualPost = Array.isArray(post) ? post[0] : post;

                if (!actualPost) {
                    alert('게시글 데이터를 불러오는데 실패했습니다. 데이터가 비어있거나 올바른 형식이 아닙니다.');
                    window.location.href = '/board/posts';
                    return;
                }

                console.log("실제 표시할 게시글 데이터:", actualPost);

                document.getElementById('postTitle').textContent = actualPost.title;
                document.getElementById('postAuthor').textContent = actualPost.author;
                document.getElementById('postDate').textContent = new Date(actualPost.createdAt).toLocaleString();
                document.getElementById('postViews').textContent = actualPost.viewCount || 0;
                document.getElementById('postContent').textContent = actualPost.content;

                if (actualPost.fileName) {
                    const attachmentDiv = document.getElementById('postAttachment');
                    const attachmentLink = document.getElementById('attachmentLink');
                    attachmentLink.href = `/api/posts/${currentPostId}/file`;
                    attachmentLink.textContent = actualPost.fileName;
                    attachmentDiv.style.display = 'block';
                } else {
                    document.getElementById('postAttachment').style.display = 'none';
                }

                if (actualPost.user && actualPost.user.id === currentUserId) {
                    document.getElementById('postActions').style.display = 'block';
                } else {
                    document.getElementById('postActions').style.display = 'none';
                }
            })
            .catch(error => {
                console.error('게시글 로드 오류:', error);
            });
    }

    // editPost와 deletePost 함수는 변경 없음
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
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 403) { alert('게시글을 삭제할 권한이 없습니다.'); }
                    else if (response.status === 404) { alert('삭제할 게시글을 찾을 수 없습니다.'); }
                    else if (response.status === 401) {
                        localStorage.removeItem('jwtToken');
                        alert('세션이 만료되어 삭제할 수 없습니다. 다시 로그인해주세요.');
                        window.location.href = '/login';
                    }
                    throw new Error(`게시글 삭제 실패: ${response.status} ${response.statusText}`);
                }
                alert('게시글이 삭제되었습니다.');
                window.location.href = '/board/posts';
            })
            .catch(error => {
                console.error('게시글 삭제 오류:', error);
                alert('게시글 삭제에 실패했습니다.');
            });
    }
</script>
</body>
</html>
