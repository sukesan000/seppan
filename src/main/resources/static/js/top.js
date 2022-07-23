$(document).ready(function() {
        MicroModal.init();
        renderCalendar();

        //ログインユーザをセッション保持
        const username = $('#username').val();
        sessionStorage.setItem('userName', username);

        //ユーザ名表示
        $('#username_display').val("ようこそ　" + username + "さん");
})

$(function(){
    $('#modal_btn').on("click", function(){
        var EventInfo = {
            money: $("#edit_money").val(),
            category: $("#edit_category").val(),
            date: $("#edit_date").val(),
            payer: $("#edit_payer").val(),
            remarks: $("#edit_remarks").val()
        }

        $.ajax({
              url: "/seppan/top/api/editEvent",  // リクエストを送信するURLを指定（action属性のurlを抽出）
              type: "POST",  // HTTPメソッドを指定（デフォルトはGET）
              contentType: "application/json",
              data: JSON.stringify(EventInfo),
              dataType: "json" // レスポンスデータをjson形式と指定する
            })
            .done(function(data) {
                renderCalendar();
                console.log("success!!");
              })
            .fail(function() {
              alert("error!");  // 通信に失敗した場合の処理
            })
    });
});

function renderCalendar(){
    $(function() {
        $('#calendar').fullCalendar({
            initialView: 'dayGridMonth',
            eventSources: [
                {
                    url: '/seppan/top/api/all',
                    type: 'GET',
                }
            ],
            locale: 'ja',
            aspectRatio: 2.5,
            dayClick: function(date) {
                var dateStr = date.format();
                console.log(dateStr);
                document.getElementById("edit_date").value = dateStr;
                MicroModal.show('modal-1');
            }
        });
    });
}
