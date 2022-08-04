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

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
          xhr.setRequestHeader(header, token);
        });

        var EventInfo = {
            money: $("#edit_money").val(),
            categoryId: $("#edit_category").val(),
            date: $("#edit_date").val(),
            payerId: $("#edit_payer").val(),
            remarks: $("#edit_remarks").val()
        }

        const {money, categoryId, date, payerId, remarks} = EventInfo;
        console.log(date.match(/\d{4}-\d{2}-\d{2}/));
        let message = [];
        if(money == ""){
            message.push("金額が未入力です");
        }else if(isNaN(money)){
            message.push("数値を入力してください");
        }
        if(date == ""){
            message.push("日付が未入力です");
        }else if(!date.match(/\d{4}-\d{2}-\d{2}/)){
            message.push("有効な日付で入力してください");
        }
        if(remarks.length >= 21){
            message.push("備考は20文字以内で記入してください");
        }

        if(message.length > 0){
            alert(message);
            return;
        }

        $.ajax({
              url: "/seppan/top/api/editEvent",  // リクエストを送信するURLを指定（action属性のurlを抽出）
              type: "POST",  // HTTPメソッドを指定（デフォルトはGET）
              contentType: "application/json",
              data: JSON.stringify(EventInfo)
            })
            .done(function(data) {
                alert("登録しました");
                renderCalendar();
              })
            .fail(function(jqXHR, textStatus, errorThrown) {
              console.log(jqXHR.status);
              console.log(textStatus);
              console.log(errorThrown);
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
