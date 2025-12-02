document.addEventListener('DOMContentLoaded', function() {
    // 1. 지난 알림 목록 가져오기
    fetchNotifications();

    // 2. SSE 연결
    const eventSource = new EventSource('/api/v1/notifications/subscribe');

    eventSource.addEventListener('sse', function(event) {
        if (event.data.includes("EventStream Created")) {
            console.log("SSE Connected");
            return;
        }

        try {
            const data = JSON.parse(event.data);
            addNotification(data);
        } catch (e) {
            console.error("Failed to parse notification:", e);
        }
    });

    eventSource.onerror = function(event) {
        // 연결 끊김 시 자동 재연결
    };

    // 3. View All Alerts 버튼 이벤트
    const viewAllBtn = document.getElementById('view-all-alerts');
    if (viewAllBtn) {
        viewAllBtn.addEventListener('click', function(e) {
            e.preventDefault();
            readAllNotifications();
        });
    }
});

// 알림 목록 조회
function fetchNotifications() {
    fetch('/api/v1/notifications')
        .then(response => response.json())
        .then(data => {
            if (data.isSuccess && Array.isArray(data.results)) {
                renderInitialNotifications(data.results);
            }
        })
        .catch(error => console.error("Failed to fetch notifications:", error));
}

// 알림 전체 읽음 처리
function readAllNotifications() {
    fetch('/api/v1/notifications/read-all', {
        method: 'PATCH'
    })
    .then(response => response.json())
    .then(data => {
        if (data.isSuccess) {
            const list = document.getElementById('notification-list');
            list.innerHTML = ''; // 목록 비우기
            updateBadge(0); // 뱃지 초기화
        }
    })
    .catch(error => console.error("Failed to read all notifications:", error));
}

// 개별 알림 삭제 (읽음 처리)
window.deleteNotification = function(id, event) {
    // 부모의 click 이벤트(페이지 이동 등) 방지
    if (event) {
        event.preventDefault();
        event.stopPropagation();
    }

    fetch(`/api/v1/notifications/${id}`, {
        method: 'PATCH'
    })
    .then(response => response.json())
    .then(data => {
        if (data.isSuccess) {
            const item = document.getElementById(`notification-${id}`);
            if (item) {
                item.remove();
                // 현재 뱃지 숫자에서 1 감소
                const badge = document.getElementById('notification-badge');
                let currentCount = parseInt(badge.innerText) || 0;
                updateBadge(Math.max(0, currentCount - 1));
            }
        }
    })
    .catch(error => console.error("Failed to delete notification:", error));
};

function renderInitialNotifications(notifications) {
    const list = document.getElementById('notification-list');
    if (!list) return;

    list.innerHTML = '';
    updateBadge(notifications.length);

    notifications.forEach(notification => {
        const html = createNotificationHtml(notification);
        list.insertAdjacentHTML('beforeend', html);
    });
    
    if (window.lucide) window.lucide.createIcons();
}

function addNotification(data) {
    const list = document.getElementById('notification-list');
    if (!list) return;

    const badge = document.getElementById('notification-badge');
    let count = parseInt(badge.innerText) || 0;
    updateBadge(count + 1);

    const itemHtml = createNotificationHtml(data);
    list.insertAdjacentHTML('afterbegin', itemHtml);
    
    if (window.lucide) window.lucide.createIcons();
}

function updateBadge(count) {
    const badge = document.getElementById('notification-badge');
    const countLabel = document.getElementById('notification-count');

    if (!badge || !countLabel) return;

    if (count > 0) {
        badge.innerText = count;
        badge.classList.remove('d-none');
        countLabel.innerText = count + ' Alerts';
    } else {
        badge.innerText = 0;
        badge.classList.add('d-none');
        countLabel.innerText = '0 Alerts';
    }
}

function formatTimeAgo(dateString) {
    if (!dateString) return '방금 전';
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date; 
    
    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(diff / 3600000);
    const days = Math.floor(diff / 86400000);

    if (minutes < 1) return '방금 전';
    if (minutes < 60) return `${minutes}분 전`;
    if (hours < 24 && date.getDate() === now.getDate()) {
        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }
    if (days < 1) return '어제'; 
    if (days < 7) return `${days}일 전`;
    return date.toLocaleDateString();
}

function createNotificationHtml(data) {
    const timeString = formatTimeAgo(data.createdAt);

    return `
        <div class="dropdown-item notification-item py-2 text-wrap" id="notification-${data.id}">
            <span class="d-flex gap-2 align-items-start position-relative">
                <span class="avatar-md flex-shrink-0">
                    <span class="avatar-title bg-primary-subtle text-primary rounded fs-22">
                        <i class="ti ti-bell fs-xl"></i>
                    </span>
                </span>
                <span class="flex-grow-1 text-muted">
                    <span class="fw-medium text-body">${data.content}</span>
                    <br>
                    <span class="fs-xs">${timeString}</span>
                </span>
                
                <!-- Close Button -->
                <button type="button" class="btn btn-link text-muted p-0 fs-lg z-2 position-relative" 
                        onclick="deleteNotification(${data.id}, event)" 
                        title="Dismiss">
                    <i class="ti ti-x"></i>
                </button>

                ${data.url ? `<a href="${data.url}" class="stretched-link z-1"></a>` : ''}
            </span>
        </div>
    `;
}
