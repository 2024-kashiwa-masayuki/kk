package com.example.kk.controller.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;

@Getter
@Setter
public class TaskForm {
    
    private int id;

    @NotBlank(message = "タスクを入力してください")
    @Size(max = 140, message = "タスクは140文字以内で入力してください")
    private String content;

    private byte status;

    @NotNull(message = "期限を設定してください")
    @FutureOrPresent(message = "無効な日付です")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate limitDate;

    private Date createdDate;

    private Date updatedDate;

    private String statusLabel;

    // topで呼び出すかも
    private Map<Integer, String> statusMap = Map.ofEntries(
            entry(1, "未着手"),
            entry(2, "実行中"),
            entry(3, "ステイ中"),
            entry(4, "完了")
    );
    /*
     * ステートのvalueを取得
     */
    public String getStatusLabel() {
        return switch (this.status) {
            case 1 -> "未着手";
            case 2 -> "実行中";
            case 3 -> "ステイ中";
            case 4 -> "完了";
            default -> "";
        };
    }
    /*
     * ステートのkeyを取得
     */
    public Integer translationToState(String status) {
        for (Map.Entry<Integer, String> entry : this.statusMap.entrySet()) {
            if (Objects.equals(status, entry.getValue())) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
