window.onload = function(){
    today = new Date();
    var elements = document.getElementsByClassName("limit-date");
    var length = elements.length
    // 締切判定
    while (elements.length > 0) {
        // タスク期限の日付を取得
        var limitDate = new Date(elements[0].textContent);
        var limitYear = limitDate.getFullYear();
        var limitMonth = limitDate.getMonth() + 1;
        var limitDay = limitDate.getDate();
        // 今日の日付を取得
        var year = today.getFullYear();
        var month = today.getMonth() + 1;
        var day = today.getDate();
        // タスク期限が翌日以降なら黄色、当日以前なら赤色で表示
        if (year == limitYear) {
            if (month == limitMonth) {
                if (day < limitDay) {
                    elements[0].className = 'has-allowance';
                } else {
                    elements[0].className = 'deadline-up-close';
                }
            }
            else {
                if (month < limitMonth) {
                    elements[0].className = 'has-allowance';
                } else {
                    elements[0].className = 'deadline-up-close';
                }
            }
        } else {
            if (year < limitYear) {
                elements[0].className = 'has-allowance';
            } else {
                elements[0].className = 'deadline-up-close';
            }
        }
    }
}

function CheckDelete(){
    let result = window.confirm("タスクを削除してもよろしいですか？");
    if (!result) return false;
}

function ChangeStatus(){
    let result = window.confirm("ステータスを変更してもよろしいですか？");
    if (!result) {
        alert('ステータスは変更されませんでした')
        return false;
    }
}

$(function() {
    $('#taskNew').on("keyup", function(e){
        alert('key:'+e.key);
    });
});

$(function() {
    $('#nowDate').on("mousedown", function(e){
        alert('本日の日付です');
    });
});

document.addEventListener("DOMContentLoaded", () => {
    if (window.location.pathname === "/top") {
        let messages = [
                        "今日も一日頑張りましょう！",
                        "１つ１つ着実に完了させていきましょう",
                        "もう一息です",
                        ];
        let randomIndex = Math.floor(Math.random() * messages.length);
        alert(messages[randomIndex]);
    }
});