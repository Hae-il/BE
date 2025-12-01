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
            events: {
                url: '/api/calendar/events',
                method: 'GET',
                failure: function() {
                    alert('일정을 불러오는 중 오류가 발생했습니다.');
                }
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
                        // 추후 사건 상세 페이지 구현 시 연결
                        // window.location.href = '/cases/' + id;
                    }
                }
            }
        });

        self.calendarObj.render();
    }

}

document.addEventListener('DOMContentLoaded', function (e) {
    new CalendarSchedule().init();
});
