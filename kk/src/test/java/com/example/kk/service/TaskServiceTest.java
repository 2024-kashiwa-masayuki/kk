package com.example.kk.service;

import com.example.kk.controller.form.FilterConditionsForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    private final TaskService taskService = new TaskService();

    //②-1
    @Test
    public void testFindTaskTestWithValidDates() throws ParseException {

        //テストデータの準備(任意の開始日と終了日)
        FilterConditionsForm filterConditionsForm = new FilterConditionsForm();
        filterConditionsForm.setStart("2024-09-15");
        filterConditionsForm.setEnd("2024-09-29");

        //メソッドの呼び出し
        filterConditionsForm = taskService.findTaskTest(filterConditionsForm);

        //日付が期待通りに変換されているか確認
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //開始日の検証
        Date expectedStartDate = formatter.parse("2024-09-15 00:00:00");
        assertEquals(expectedStartDate, filterConditionsForm.getStartDate());

        //終了日の検証
        Date expectedEndDate = formatter.parse("2024-09-29 23:59:59");
        assertEquals(expectedEndDate, filterConditionsForm.getEndDate());
    }

    //②-2
    @Test
    public void testFindTaskTestWithDefaultDates() throws ParseException {

        //テストデータの準備 (開始日・終了日が空)
        FilterConditionsForm filterConditionsForm = new FilterConditionsForm();
        filterConditionsForm.setStart("");
        filterConditionsForm.setEnd("");

        //メソッドの呼び出し
        filterConditionsForm = taskService.findTaskTest(filterConditionsForm);

        //デフォルト値が設定されているか確認
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //開始日の検証
        Date expectedStartDate = formatter.parse("2020-01-01 00:00:00");
        assertEquals(expectedStartDate, filterConditionsForm.getStartDate());

        //終了日の検証
        Date expectedEndDate = formatter.parse("2100-12-31 23:59:59");
        assertEquals(expectedEndDate, filterConditionsForm.getEndDate());
    }
}