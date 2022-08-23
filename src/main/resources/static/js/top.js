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

    //モーダルで詳細追加時
    $(document).on("click","#amount_details_link",function(){
        $("#amount_details_area").append("<div class=item_1 amount_details_input_area>" +
                "<input placeholder='金額を入力'>" +
                "<select class='amount_details_select'>" +
                "</select>" +
                "</div>");

        $("#amount_details_area").append("<div class=item_2 amount_details_input_area>" +
                "<input placeholder='金額を入力'>" +
                "<select class='amount_details_select'>" +
                "</select>" +
                "</div>");

        var optionCnt = 1;
        //edit_payerの値を取得
        $('#edit_payer option').each(function(index, element){
            $(".item_" + optionCnt + " select").append($('<option>').text(element.text).val(element.value));
            optionCnt++;
        });

        $("#amount_details_link").hide();
        $("#amount_details_area").append("<a href='#' id=amount_close_link>閉じる</a>");
    });

    $(document).on("click","#amount_close_link",function(){
        $("#amount_details_area").children().remove();
        $("#amount_details_area").append("<a href='#' id=amount_details_link>詳細を記載する</a>");
    });

    //レコード追加ボタン押下
    $('#modal_add_btn').on("click", function(){
        //csrf対策
        csrfMeasures();

        var EventInfo = {
            money: $("#edit_money").val(),
            categoryId: $("#edit_category").val(),
            date: $("#edit_date").val(),
            payerId: $("#edit_payer").val(),
            remarks: $("#edit_remarks").val()
        }

        validationCheck(EventInfo);
        const {money, categoryId, date, payerId, remarks} = EventInfo;

        $.ajax({
              url: "/seppan/top/api/addEvent",  // リクエストを送信するURLを指定（action属性のurlを抽出）
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

    //レコード削除ボタン押下
    $('#modal_delete_btn').on("click", function(){
        //csrf対策
        csrfMeasures();

        const recordId = $("#recordId").val();
        $.ajax({
                url: "/seppan/top/api/deleteEvent",
                type: "POST",
                contentType: "application/json",
                data: recordId
        })
        .done(function(data) {
            alert("削除しました");
            renderCalendar();
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.status);
            console.log(textStatus);
            console.log(errorThrown);
        })
    });

    //レコード更新ボタン押下
    $('#modal_update_btn').on("click", function(){
        //csrf対策
        csrfMeasures();

        var EventInfo = {
            recordId: $("#recordId").val(),
            money: $("#edit_money").val(),
            categoryId: $("#edit_category").val(),
            date: $("#edit_date").val(),
            payerId: $("#edit_payer").val(),
            remarks: $("#edit_remarks").val()
        }

        validationCheck(EventInfo);
        const {money, categoryId, date, payerId, remarks} = EventInfo;

        $.ajax({
            url: "/seppan/top/api/updateEvent",  // リクエストを送信するURLを指定（action属性のurlを抽出）
            type: "POST",  // HTTPメソッドを指定（デフォルトはGET）
            contentType: "application/json",
            data: JSON.stringify(EventInfo)
        }).done(function(data) {
            alert("編集しました");
            renderCalendar();
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.status);
            console.log(textStatus);
            console.log(errorThrown);
        })
    })
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
                $('#modal_delete_btn').hide();
                $('#modal_update_btn').hide();
                $('#modal_add_btn').show();

                //内容リセット
                $("#edit_money").val("");
                $('#edit_category').prop("selectedIndex", 0);

                var dateStr = date.format();
                console.log(dateStr);
                document.getElementById("edit_date").value = dateStr;
                MicroModal.show('modal-1');
            },
            eventClick: function(calEvent) {
                $('#modal_delete_btn').show();
                $('#modal_update_btn').show();
                $('#modal_add_btn').hide();

                //日付は記入不可
                $('#edit_date').attr('readonly',true);

                //idの指定
                var recordId = calEvent.recordId;
                document.getElementById("recordId").value = recordId;

                //日付の指定
                var dateStr = calEvent.start._i;
                document.getElementById("edit_date").value = dateStr;

                //金額の指定
                var price = calEvent.price;
                document.getElementById("edit_money").value = price;

                //カテゴリの指定
                var categoryId = calEvent.categoryId;
                //forでcategoryIdとvalueの値が同じものを探す
                $('#edit_category option').each(function(index, element){
                    if(element.value == categoryId){
                        const val = "edit_category option[value='" + categoryId + "']";
                        $("#" + val).prop('selected', true);
                    }
                });

                //支払い者の指定
                var payerId = calEvent.payerId;
                $('#edit_payer option').each(function(index, element){
                    if(element.value == payerId){
                        const val = "edit_payer option[value='" + payerId + "']";
                        $("#" + val).prop('selected', true);
                    }
                })

                //備考の指定
                var remarks = calEvent.remarks;
                document.getElementById("edit_remarks").value = remarks;
                MicroModal.show('modal-1');
            }
        });
    });
}

function　csrfMeasures(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

function validationCheck(eventInfo){
    const {money, categoryId, date, payerId, remarks} = eventInfo;
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
        return message;
    }
}
