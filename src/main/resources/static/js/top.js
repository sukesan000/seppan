document.addEventListener('DOMContentLoaded', function() {
        MicroModal.init();
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
          initialView: 'dayGridMonth',
          events: [
                    {
                        id: '1',
                        title: 'event1',
                        start: '2022-06-01',
                        url: '#'
                    },
                    {
                        id: '2',
                        title: 'event2',
                        start: '2022-05-05',
                        url: '#'
                    },
                    {
                        id: '3',
                        title: 'event3',
                        start: '2021-01-07',
                        end: '2021-01-11', // 2021-01-10 23:59:59で終了
                        url: '#'
                    }
                ],
                locale: 'ja',
                aspectRatio: 2.5,
                dateClick: function(info) {
                    console.log(info.date);
                    console.log(info.dateStr);
                    document.getElementById("edit_date").value = info.dateStr;
                    MicroModal.show('modal-1');
                }
        });
        calendar.render();
});
