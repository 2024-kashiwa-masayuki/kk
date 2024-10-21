package com.example.kk.controller.form;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

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
}
