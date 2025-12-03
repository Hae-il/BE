/**
 * Template Name: INSPINIA - Multipurpose Admin & Dashboard Template
 * By (Author): WebAppLayers
 * Module/App (File Name): Apps Calendar
 * Version: 4.2.0
 */

class CalendarSchedule {

    constructor() {
        this.calendar = document.getElementById('calendar');
        this.calendarObj = null;
        this.allEvents = []; // 로드된 전체 이벤트 저장용
    }

    init() {
        /*  Initialize the calendar  */
        const today = new Date();
        const self = this;

        // cal - init
        self.calendarObj = new FullCalendar.Calendar(self.calendar, {
            plugins: [],
            slotDuration: '00:30:00', /* If we want to split day time each 15minutes */
            slotMinTime: '07:00:00',
            slotMaxTime: '19:00:00',
            themeSystem: 'bootstrap',
            bootstrapFontAwesome: false,
            buttonText: {
                today: 'Today',
                month: 'Month',
                week: 'Week',
                day: 'Day',
                list: 'List',
                prev: 'Prev',
                next: 'Next'
            },
            initialView: 'dayGridMonth',
            handleWindowResize: true,
            height: window.innerHeight - 240,
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
            },
            events: function(info, successCallback, failureCallback) {
                // API 호출
                fetch(`/api/calendar/events?start=${info.startStr}&end=${info.endStr}`)
                    .then(response => response.json())
                    .then(data => {
                        self.allEvents = data; // 원본 데이터 저장
                        const filteredEvents = self.filterEvents(data);
                        successCallback(filteredEvents);
                    })
                    .catch(error => {
                        console.error('Error fetching events:', error);
                        failureCallback(error);
                    });
            },
            editable: false,
            droppable: false,
            selectable: false,
            dateClick: null,
            eventClick: function (info) {
                const eventId = info.event.id; // "consultation_1" or "case_3"
                if (eventId) {
                    const [type, id] = eventId.split('_');
                    
                    if (type === 'consultation') {
                        window.location.href = '/consultations/' + id;
                    } else if (type === 'case') {
                        // CaseEventAdapter에서 extendedProps에 caseId와 status를 담아줌
                        const props = info.event.extendedProps;
                        if (props && props.caseId) {
                             let urlPath = 'ongoing'; // 기본값
                             if (props.status === 'COMPLETED') {
                                 urlPath = 'completed';
                             } else if (props.status === 'UNASSIGNED') {
                                 urlPath = 'unassigned';
                             }
                             // 그 외(ONGOING 등)는 ongoing으로 이동
                             
                             window.location.href = '/cases/' + urlPath + '/' + props.caseId;
                        }
                    } else if (type === 'contract') {
                        const props = info.event.extendedProps;
                        if (props && props.contractId) {
                            // 계약 상세 페이지 경로 (예시)
                            // window.location.href = '/contracts/' + props.contractId;
                            // 현재는 계약 목록으로 이동하되, 추후 상세 구현 시 변경
                             window.location.href = '/contracts';
                        }
                    } else if (type === 'settlement') {
                        const props = info.event.extendedProps;
                        if (props && props.settlementId) {
                             window.location.href = '/settlements/' + props.settlementId;
                        }
                    }
                }
            }
        });

        self.calendarObj.render();

        // 필터 체크박스 이벤트 리스너 등록
        const filterCheckboxes = document.querySelectorAll('.filter-checkbox');
        filterCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', () => {
                // 현재 뷰에 있는 이벤트들을 메모리 상에서 다시 필터링하여 렌더링
                // refetchEvents를 호출하면 events 함수가 다시 실행되어 API를 호출하게 됨.
                // 여기서는 API 재호출 없이 필터링만 하고 싶다면 다른 방식을 써야 하지만,
                // FullCalendar 구조상 refetch가 가장 깔끔함 (events 함수 내에서 캐싱 로직 구현 가능하나 생략)
                self.calendarObj.refetchEvents();
            });
        });
    }
    
    // 이벤트 필터링 헬퍼 함수
    filterEvents(events) {
        const activeFilters = Array.from(document.querySelectorAll('.filter-checkbox:checked'))
                                   .map(cb => cb.value); // ['CONSULTATION', 'CASE_EVENT', ...]
        
        return events.filter(event => {
            // event.type은 CalendarEvent DTO의 type 필드 (CONSULTATION, CASE_EVENT, CONTRACT, SETTLEMENT)
            // FullCalendar event object에서는 extendedProps로 접근해야 할 수 있음.
            // 하지만 API 응답 JSON 자체(events)를 필터링하는 것이므로 DTO 구조를 따름.
            return activeFilters.includes(event.type);
        });
    }

}

document.addEventListener('DOMContentLoaded', function (e) {
    new CalendarSchedule().init();
});
