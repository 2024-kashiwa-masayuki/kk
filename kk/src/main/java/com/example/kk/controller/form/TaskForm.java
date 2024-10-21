package com.example.kk.controller.form;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

@Getter
@Setter
public class TaskForm {

    private int id;

    @NotEmpty(message = "タスクを入力してください")
    @Length(min = 1, max = 140, message = "タスクは140文字以内で入力してください")
    private String content;

    private byte status;

    @NotNull(message = "期限を設定してください")
    @FutureOrPresent(message = "無効な日付です")
//    a@Pattern(regexp = "", message = "不正なパラメータです")
    private Date limitDate;

    private Date createdDate;

    private Date updatedDate;

    private String statusLabel;

    public String getStatusOption() {
        return switch (this.status) {
            case 1 -> "未着手";
            case 2 -> "実行中";
            case 3 -> "ステイ中";
            case 4 -> "完了";
            default -> "";
        };
    }
}
