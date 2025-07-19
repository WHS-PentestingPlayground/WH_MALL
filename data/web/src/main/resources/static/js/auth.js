document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatusAndUpdateHeader();
});

function checkLoginStatusAndUpdateHeader() {
    const token = localStorage.getItem('jwtToken');
    const userMenu = document.getElementById('userMenu');

    if (!userMenu) {
        // userMenu가 없는 페이지(예: 로그인 페이지의 헤더)에서는 아무것도 하지 않음
        return;
    }

    if (token) {
        fetch('/api/users/me', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                localStorage.removeItem('jwtToken');
                throw new Error('인증 토큰이 유효하지 않습니다.');
            }
        })
        .then(data => {
            if (data && data.username) {
                updateUserMenu(data.username);
            } else {
                 updateUserMenu(null);
            }
        })
        .catch(error => {
            console.error('사용자 상태 확인 중 오류 발생:', error);
            updateUserMenu(null);
        });
    } else {
        updateUserMenu(null);
    }
}

function updateUserMenu(username) {
    const userMenu = document.getElementById('userMenu');
    if (!userMenu) return;

    if (username) {
        userMenu.innerHTML = `
            <a href="/mypage" class="header-username">${username}님</a>
            <button type="button" class="header-logout-btn" onclick="logout()">로그아웃</button>
        `;
    } else {
        userMenu.innerHTML = `
            <a href="/login" class="header-user-link">로그인</a>
            <a href="/register" class="header-user-link">회원가입</a>
        `;
    }
}

function logout() {
    localStorage.removeItem('jwtToken');
    // 서버에 로그아웃 요청을 보내 세션을 무효화 할 수도 있습니다.
    fetch('/api/users/logout', { method: 'POST' })
        .finally(() => {
            window.location.href = '/';
        });
} 