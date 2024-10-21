$(function(){
	$('.deleteButton').on('click', function(){
		let result = window.confirm("タスクを削除してもよろしいですか？");
		if (!result) return false;
	});
});