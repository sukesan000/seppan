$(document).ready(function() {
        //ログインユーザをセッション保持
        const username = sessionStorage.getItem('userName');
        console.log(username);
        //ユーザ名表示
        const name = $('#username_display').text("ようこそ " + username + "さん");
        console.log(name);
})
