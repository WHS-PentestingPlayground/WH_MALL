document.addEventListener('DOMContentLoaded', function() {
    loadUserData();
});

function loadUserData() {
    const token = localStorage.getItem('jwtToken');
    
    if (!token) {
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        window.location.href = '/login';
        return;
    }

    // 사용자 정보 로드
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
            window.location.href = '/login';
            throw new Error('인증 토큰이 유효하지 않습니다.');
        }
    })
    .then(userData => {
        updateUserInfo(userData.username);
        loadPointData();
    })
    .catch(error => {
        console.error('사용자 정보 로드 중 오류:', error);
        window.location.href = '/login';
    });
}

function updateUserInfo(username) {
    // 사용자명 업데이트
    document.getElementById('username').textContent = username;
    
    // 아바타 텍스트 업데이트 (사용자명의 첫 글자)
    const avatarText = document.getElementById('avatarText');
    avatarText.textContent = username.charAt(0).toUpperCase();
}

function loadPointData() {
    const token = localStorage.getItem('jwtToken');
    
    fetch('/api/users/point', {
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
            throw new Error('포인트 정보를 가져올 수 없습니다.');
        }
    })
    .then(pointData => {
        updatePointDisplay(pointData.point, pointData.rank);
    })
    .catch(error => {
        console.error('포인트 정보 로드 중 오류:', error);
        // 기본값으로 표시
        updatePointDisplay(0, 'normal');
    });
}

function updatePointDisplay(point, rank) {
    const VIP_THRESHOLD = 100000;
    
    // 포인트 값 업데이트
    document.getElementById('currentPoint').textContent = point.toLocaleString();
    
    // 등급 배지 업데이트
    const rankBadge = document.getElementById('rankBadge');
    rankBadge.textContent = rank.toUpperCase();
    rankBadge.className = 'rank-badge ' + rank.toLowerCase();
    
    // VIP 진행률 계산 및 업데이트
    const progressPercentage = Math.min((point / VIP_THRESHOLD) * 100, 100);
    const remainingPoints = Math.max(VIP_THRESHOLD - point, 0);
    
    // 진행률 바 업데이트
    const progressFill = document.getElementById('progressFill');
    progressFill.style.width = progressPercentage + '%';
    
    // 진행률 퍼센트 업데이트
    document.getElementById('progressPercentage').textContent = Math.round(progressPercentage) + '%';
    
    // 남은 포인트 텍스트 업데이트
    const progressText = document.getElementById('progressText');
    if (rank === 'vip') {
        progressText.textContent = 'VIP 등급 달성!';
        progressText.style.color = '#8b4513';
        progressFill.style.background = 'linear-gradient(90deg, #ffd700 0%, #ffb300 100%)';
    } else {
        progressText.textContent = remainingPoints.toLocaleString() + 'P 남음';
        progressText.style.color = '#1976d2';
    }
    
    // VIP 혜택 카드 활성화 상태 업데이트
    const vipBenefitCard = document.querySelector('.vip-benefit');
    if (rank === 'vip') {
        vipBenefitCard.classList.add('active');
    } else {
        vipBenefitCard.classList.remove('active');
    }
    
    // 진행률 바 색상 업데이트
    if (progressPercentage >= 100) {
        progressFill.style.background = 'linear-gradient(90deg, #ffd700 0%, #ffb300 100%)';
    } else if (progressPercentage >= 80) {
        progressFill.style.background = 'linear-gradient(90deg, #ff9800 0%, #ff5722 100%)';
    } else if (progressPercentage >= 50) {
        progressFill.style.background = 'linear-gradient(90deg, #ffc107 0%, #ff9800 100%)';
    } else {
        progressFill.style.background = 'linear-gradient(90deg, #4caf50 0%, #8bc34a 100%)';
    }
}

