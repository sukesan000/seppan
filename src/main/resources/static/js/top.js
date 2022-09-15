//MODE
const ADD = 1;
const UPDATE = 2;

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
        setDetail(ADD);
    });

    //閉じるボタン押下時
    $(document).on("click","#amount_close_link",function(){
        $("#amount_details_area").children().remove();
        $("#amount_details_area").append("<a href='#' id=amount_details_link>詳細を記載する</a>");
    });

    //レコード追加ボタン押下
    $('#modal_add_btn').on("click", function(){
        //csrf対策
        csrfMeasures();
        //エラー文
        let message = [];

        let EventInfo = getEventInfo();
        let {money, ownPayment, partnerPayment, categoryId, date, payerId, remarks} = EventInfo;

        //バリデーション確認
        const valMsg = validationCheck(EventInfo);
        if(valMsg){
            message.push(valMsg);
        }

        //空文字であれば0を入れる
        if(ownPayment　== ""){
            EventInfo.ownPayment = "0";
        }
        if(partnerPayment == ""){
            EventInfo.partnerPayment = "0";
        }

        //計算があっているかチェック
        if((ownPayment && partnerPayment) && (ownPayment > 0 || partnerPayment > 0)){
            const calcMsg = calcCheck(money,ownPayment,partnerPayment)
            if(calcMsg){
                message.push(calcMsg);
            }
        }

        //詳細が閉じていればownPaymentとpartnerPaymentには0を入れる
        if($('.amount_details_input_area').length == 0){
            EventInfo.ownPayment = "0";
            EventInfo.partnerPayment = "0";
        }

        if(message.length){
            alert(message);
        }else{
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
        }
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
        //エラー文
        let message = [];

        const recordId = $("input[name='recordId']").val();

        let EventInfo = getEventInfo(recordId);
        let {money, ownPayment, partnerPayment, categoryId, date, payerId, remarks} = EventInfo;

        //空文字であれば0を入れる
        if(ownPayment　== ""){
            EventInfo.ownPayment = "0";
        }
        if(partnerPayment == ""){
            EventInfo.partnerPayment = "0";
        }

        //バリデーション確認
        const valMsg = validationCheck(EventInfo);
        if(valMsg){
            message.push(valMsg);
        }

        //計算があっているかチェック
        if((ownPayment && partnerPayment) && (ownPayment > 0 || partnerPayment > 0)){
            const calcMsg = calcCheck(money,ownPayment,partnerPayment)
            if(calcMsg){
                message.push(calcMsg);
            }
        }

        if(message.length){
            alert(message);
        }else{
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
        }
    })

    //精算決定ボタン押下
    $('#modal_calc_btn').on("click", function(){
        //csrf対策
        csrfMeasures();

        let DatePeriod =　{
            dateFrom: $("#edit_date_from").val(),
            dateTo: $("#edit_date_to").val()
        }
        let {dateFrom,dateTo} = DatePeriod;
        //日付チェック
        const date_from_replace = dateFrom.replaceAll('-','');
        const date_to_replace = dateTo.replaceAll('-','');
        const res = Math.sign(date_to_replace - date_from_replace);
        if(res != 1){
            alert('期間指定が正常ではありません');
            return;
        }

        $.ajax({
                url: "/seppan/top/api/adjustment",  // リクエストを送信するURLを指定（action属性のurlを抽出）
                type: "GET",  // HTTPメソッドを指定（デフォルトはGET）
                data: {
                    dateFrom: dateFrom,
                    dateTo: dateTo
                }
            }).done(function(data) {
                alert(data);
            }).fail(function(jqXHR, textStatus, errorThrown) {
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
            customButtons:{
                eventListButton:{
                    text: '精算',
                    click: function() {
                        MicroModal.show('modal-2');
                    }
                }
            },
            header: {
                right: 'eventListButton,prev,next'
            },
            dayClick: function(date) {
                $('#modal_delete_btn').hide();
                $('#modal_update_btn').hide();
                $('#modal_add_btn').show();

                //詳細リセット
                $("#amount_details_area").children().remove();
                $("#amount_details_area").append("<a href='#' id=amount_details_link>詳細を記載する</a>");

                //内容リセット
                $("#edit_money").val("");
                $('.item_1 .one_side_payment').val("");
                $('.item_2 .one_side_payment').val("");
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
                const recordId = calEvent.recordId;
                document.getElementById("recordId").value = recordId;

                //日付の指定
                const dateStr = calEvent.start._i;
                document.getElementById("edit_date").value = dateStr;

                //金額の指定
                const price = calEvent.price;
                document.getElementById("edit_money").value = price;

                //ownPaymentとpartnerPaymentはまだhtmlが作成されていない
                //自分が支払う金額
                const ownPayment =calEvent.ownPayment;
                document.getElementById("ownPayment").value = ownPayment;

                //相手が支払う金額
                const partnerPayment = calEvent.partnerPayment;
                document.getElementById("partnerPayment").value = partnerPayment;

                //詳細
                setDetail(UPDATE);
                $("#amount_close_link").hide();

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

function calcCheck(money,moneyA,moneyB){
    let message = [];
    if(isNaN(money) || isNaN(moneyA) || isNaN(moneyB)){
        message.push("数字を入力してください");
        return message;
   }

    if(Number(money) < (Number(moneyA) + Number(moneyB))){
        message.push("計算が間違っています");
        return message;
    }
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
        return message;
    }
}

function getEventInfo(recordId){
    const EventInfo = {
        recordId: recordId,
        money: $("#edit_money").val(),
        ownPayment: $('.item_1 .one_side_payment').val(),
        partnerPayment: $('.item_2 .one_side_payment').val(),
        categoryId: $("#edit_category").val(),
        date: $("#edit_date").val(),
        payerId: $("#edit_payer").val(),
        remarks: $("#edit_remarks").val()
    }
    return EventInfo;
}

function setDetail(mode){

    if($('.item_1').length == 0){
        $("#amount_details_area").append("<br><div class=item_1>" +
                    "<input id='item_1' class='one_side_payment' value=0>" +
                    "<select class='amount_details_select'>" +
                    "</select>" +
                    "</div>");

        $("#amount_details_area").append("<div class=item_2>" +
                    "<input id='item_2' class='one_side_payment' value=0>" +
                    "<select class='amount_details_select'>" +
                    "</select>" +
                    "</div>");

        $('.item_1').addClass('amount_details_input_area');
        $('.item_2').addClass('amount_details_input_area');

        $("#item_1").val($("#ownPayment").val());
        $("#item_2").val($("#partnerPayment").val());

    }
    if(mode == 1){
        $('.item_1 .one_side_payment').val("");
        $('.item_2 .one_side_payment').val("");
        $("#amount_details_area").append("<a href='#' id=amount_close_link>閉じる</a>");
    }else if(mode == 2){
        $("#item_1").val($("#ownPayment").val());
        $("#item_2").val($("#partnerPayment").val());

    }

    var optionCnt = 1;
    //edit_payerの値を取得
    $('#edit_payer option').each(function(index, element){
            $(".item_" + optionCnt + " select").append($('<option>').text(element.text).val(element.value));
            optionCnt++;
    });

    $("#amount_details_link").hide();
}
