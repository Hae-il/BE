document.addEventListener('DOMContentLoaded', function() {
    // SSE 연결
    // 쿠키(Authorization)가 자동으로 전송되므로 별도 헤더 설정 불필요
    const eventSource = new EventSource('/api/v1/notifications/subscribe');

    eventSource.addEventListener('sse', function(event) {
        // 연결 확인용 더미 데이터 무시
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
        // console.error("SSE Error:", event);
        // 연결 끊김 시 브라우저가 자동 재연결 시도함
    };
});

function addNotification(data) {
    const list = document.getElementById('notification-list');
    const badge = document.getElementById('notification-badge');
    const countLabel = document.getElementById('notification-count');

    if (!list || !badge || !countLabel) return;

    // 뱃지 카운트 증가
    let count = parseInt(badge.innerText) || 0;
    count++;
    badge.innerText = count;
    badge.style.display = 'inline-block'; // 숨겨진 뱃지 표시
    countLabel.innerText = count + ' Alerts';

    // 시간 포맷팅 (간단하게)
    const timeString = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    // HTML 생성
    // 템플릿 디자인에 맞춘 마크업
    const itemHtml = `
        <div class="dropdown-item notification-item py-2 text-wrap">
            <span class="d-flex gap-2">
                <span class="avatar-md flex-shrink-0">
                    <span class="avatar-title bg-primary-subtle text-primary rounded fs-22">
                        <!-- 아이콘은 텍스트나 이미지로 대체하거나 lucide.createIcons() 호출 필요 -->
                        <i class="ti ti-bell fs-xl"></i>
                    </span>
                </span>
                <span class="flex-grow-1 text-muted">
                    <span class="fw-medium text-body">${data.content}</span>
                    <br>
                    <span class="fs-xs">${timeString}</span>
                </span>
                ${data.url ? `<a href="${data.url}" class="stretched-link"></a>` : ''}
            </span>
        </div>
    `;

    list.insertAdjacentHTML('afterbegin', itemHtml);
}

