package com.example.kk.controller;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@WebMvcTest(controllers = KkController.class)
class KkControllerTest {

    private final KkController kkController = new KkController();

    //①-1
    @Test
    void validateTask_FullWidthSpace() {
        //全角スペースのみの場合
        TaskForm form = new TaskForm();
        form.setContent("　"); //全角スペース

        List<String> errors = kkController.validateTaskTest(form);

        //エラーが発生することを確認
        assertEquals(1, errors.size());
        assertEquals("タスクを入力してください", errors.get(0));
    }

    //①-2
    @Test
    void testValidateTask_EmptyString() {
        //空文字の場合
        TaskForm form = new TaskForm();
        form.setContent(""); //空文字

        List<String> errors = kkController.validateTaskTest(form);

        //エラーが発生することを確認
        assertEquals(1, errors.size());
        assertEquals("タスクを入力してください", errors.get(0));
    }

    //①-3
    @Test
    void testValidateTask_Null() {
        //nullの場合
        TaskForm form = new TaskForm();
        form.setContent(null); //null

        List<String> errors = kkController.validateTaskTest(form);

        //エラーが発生することを確認
        assertEquals(1, errors.size());
        assertEquals("タスクを入力してください", errors.get(0));
    }

    //①-4
    @Test
    void testValidateTask_Normal() {
        //正常な入力
        TaskForm form = new TaskForm();
        form.setContent("タスク内容");

        List<String> errors = kkController.validateTaskTest(form);

        //エラーが発生しないことを確認
        assertTrue(errors.isEmpty());
    }

    //addContent()全体のテスト
    @Autowired
    private MockMvc mockMvc;

    //モック化したTaskService
    @MockBean
    private TaskService taskService;

    @Test
    void testAddContent_validationError() throws Exception {
        //バリデーションエラーの場合
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "")//空のタスク
                )
                .andExpect(status().isOk()) //HTTP200でレスポンスされる
                .andExpect(view().name("/new")) //新規作成画面に戻る
                .andExpect(model().attributeExists("validationError")) //エラーメッセージが存在する
                .andExpect(model().attribute("validationError",
                        org.hamcrest.Matchers.hasItem("タスクを入力してください"))); //エラーメッセージの内容を検証
    }

    @Test
    void testAddContent_validInput() throws Exception {
        //タスクが正常に追加する場合
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", "ValidTask") //正しいタスク内容
                        .param("limitDate", "2024/11/26") //UserFormの他のバリデーションで引っかからないように値をセット
                )
                .andExpect(status().is3xxRedirection()) //リダイレクトが発生
                .andExpect(redirectedUrl("/top")); //リダイレクト先のURLを検証

        //タスクが保存されていることを確認
        verify(taskService, times(1)).saveTask(any(TaskForm.class));
    }

    //topDate()全体のテスト
    @Test
    void testTopDate() throws Exception {
        // モックのレスポンスを設定
        FilterConditionsForm mockFilterConditionsForm = new FilterConditionsForm();
        TaskForm mockTask = new TaskForm();
        mockTask.setContent("testTask");

        when(taskService.findTask(any(FilterConditionsForm.class)))
                .thenReturn(Arrays.asList(mockTask)); //モックの戻り値を設定

        //今日の日付のフォーマット
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        String today = sdFormat.format(new Date());

        //HTTP GETリクエストを実行
        mockMvc.perform(get("/2023-01-01-2023-12-31-1-testContent")
                        .param("start", "2023-01-01")
                        .param("end", "2023-12-31")
                )
                .andExpect(status().isOk()) //ステータスコード200を期待
                .andExpect(view().name("/top")) //画面遷移先を検証
                .andExpect(model().attributeExists("tasks")) //モデルにtasks属性が存在することを検証
                .andExpect(model().attributeExists("filterConditionsForm")) //モデルにfilterConditionsFormが存在することを検証
                .andExpect(model().attribute("today", today)) //今日の日付を検証
                .andExpect(model().attribute("tasks", org.hamcrest.Matchers.hasSize(1))) //タスクが1件あることを確認
                .andExpect(model().attribute("tasks", org.hamcrest.Matchers.hasItem(
                        org.hamcrest.Matchers.hasProperty("content", org.hamcrest.Matchers.is("testTask"))
                ))); //タスクの内容を確認

        //モックの呼び出しを確認
        verify(taskService, times(1)).findTask(any(FilterConditionsForm.class));
    }
}