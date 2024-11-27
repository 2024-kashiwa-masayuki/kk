package com.example.kk.service;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import org.dbunit.dataset.ITable;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    private IDatabaseTester databaseTester;
    private IDataSet originalDataSet;

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

    //DB設定メソッド
    public void setting() throws ClassNotFoundException {
        //PostgreSQL用のDBUnitテスターを初期化
        databaseTester = new JdbcDatabaseTester(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/kk",
                "kk",
                "kk"
        );
    }

    @BeforeEach
    void setUp() throws Exception {
    //元のデータをバックアップ
    originalDataSet = databaseTester.getConnection().createDataSet();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (originalDataSet != null) {
            // ここでデータ復元処理を行う
            databaseTester.setDataSet(originalDataSet);
            databaseTester.setTearDownOperation(DatabaseOperation.CLEAN_INSERT); // 元の状態に戻す
            databaseTester.onTearDown();
            databaseTester.getConnection().close();
        }
    }

    @Order(1)
    @Test
    void testFindAllTask() throws Exception {
        //DBの設定メソッドを呼び出し
        setting();

        //XMLデータセットを読み込む。
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(getClass().getClassLoader().getResourceAsStream("data/task-dataset.xml"));

        //DBUnitでデータを挿入
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();

        //Serviceクラスのメソッドを呼び出し
        List<TaskForm> taskForms = taskService.findAllTask();

        //結果を検証
        assertNotNull(taskForms);
        assertEquals(2, taskForms.size());

        //個別のデータを検証
        assertEquals("testTask1", taskForms.get(0).getContent());
        assertEquals("testTask2", taskForms.get(1).getContent());
    }

    @Test
    void testDeleteTask() throws Exception {
        //DBの設定メソッドを呼び出し
        setting();

        //XMLデータセットを読み込む。
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(getClass().getClassLoader().getResourceAsStream("data/task-dataset.xml"));

        //DBUnitでデータを挿入
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();

        //Serviceのメソッドを呼び出し、今回はid:1を削除
        Integer deleteId = 1;
        taskService.deleteTask(deleteId);

        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("tasks");

        boolean idExists = false;
        for (int i = 0; i < actualTable.getRowCount(); i++) {
            Integer id = (Integer) actualTable.getValue(i, "id");
            if (id.equals(deleteId)) {
                idExists = true;
                break;
            }
        }

        //検証
        assertFalse(idExists, "The task with ID " + deleteId + " should be deleted from the database.");

    }

    @Test
    void testSaveTask() throws Exception {
        //DBの設定メソッドを呼び出し
        setting();

        //XMLデータセットを読み込む。
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(getClass().getClassLoader().getResourceAsStream("data/task-dataset.xml"));

        //DBUnitでデータを挿入
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();

        //新規タスクの準備
        TaskForm newTask = new TaskForm();
        newTask.setContent("testTask3");
        newTask.setStatus((byte) 1);
        newTask.setLimitDate(LocalDate.parse("2024-12-23"));

        //Serviceのメソッドの呼び出し
        taskService.saveTask(newTask);

        //DBUnitを使って結果を検証
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("tasks");

        //保存されたデータを確認
        boolean taskFound = false;
        for (int i = 0; i < actualTable.getRowCount(); i++) {
            String content = (String) actualTable.getValue(i, "content");
            if ("testTask3".equals(content)) {
                taskFound = true;
                break;
            }
        }
        //検証
        assertTrue(taskFound, "The new task should be saved in the database.");
    }

    @Test
    void testEditTask() throws Exception {
        //DBの設定メソッドを呼び出し
        setting();

        //XMLデータセットを読み込む。
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(getClass().getClassLoader().getResourceAsStream("data/task-dataset.xml"));

        //DBUnitでデータを挿入
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();

        //Serviceクラスのメソッドを呼び出し
        Integer selectId = 2;
        TaskForm taskForm = taskService.editTask(selectId);

        //結果を検証
        assertNotNull(taskForm);

        //検証
        assertEquals("testTask2", taskForm.getContent());
    }
}