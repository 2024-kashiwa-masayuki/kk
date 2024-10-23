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