package com.example.controlwork;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    private final BaseQuestion[] questions = new BaseQuestion[] {
            new MultipleSelectQuestion("Выберите все города, являющиеся столицами государств:\n", new String[] {"Шанхай", "Марсель", "Рим", "Прага", "Осло"}, new boolean[] {false, false, true, true, true}), 
            new SingleSelectQuestion("Вопрос 3", new String[] {"3,33м", "3,73м", " 2.89м", "2.05м"}, 1), 
            new ComboBoxSelectQuestion("Продолжите фразу: \"citius altius ...\"", new String[] {"ergo", "est", "fortius", "cogito"}, 2)
    };
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private TabPane tabPane;
    private int[] mistakes;
    private boolean isEnd = false;
    private final boolean isSecondEnd = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().clear();

        mistakes = new int[questions.length];

        createTabs();

        select(0);
    }

    public void showModal(Scene rootScene, String title, String text) {
        var stage = new Stage();
        var pane = new VBox();
        var label = new Text(text);
        var btn = new Button("Закрыть");
        label.setFont(new Font(16));
        pane.setSpacing(10);
        pane.getChildren().add(label);
        pane.getChildren().add(btn);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10, 10, 10, 10));
        stage.setTitle(title);
        stage.setScene(new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootScene.getWindow());
        btn.setOnAction(e -> stage.close());
        stage.showAndWait();
    }

    private void createTabs() {
        for (int i = 0; i < questions.length; i++) {
            BaseQuestion q = questions[i];
            var pane = new VBox();
            pane.setSpacing(10);
            pane.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
            Tab tab = new Tab();
            tab.setDisable(true);
            tab.setText("" + (i + 1));
            tab.setContent(pane);
            q.Setup(pane);
            tabPane.getTabs().add(tab);
        }
    }

    private void setupEndPane(VBox pane) {
        var title = new Label("Тест завершен");
        title.setFont(new Font(20));
        pane.getChildren().add(title);

        pane.getChildren().add(new Label("Кол-во ошибок:"));
        for (int i = 0; i < questions.length; i++) {
            var mistake = mistakes[i];
            var mistakeText = new Text("Вопрос " + (i + 1) + ": " + mistake);
            pane.getChildren().add(mistakeText);
        }

        var questionsWithNoMistakes = 0;
        var questionsWithOneOrMoreMistakes = 0;
        for (int i = 0; i < mistakes.length; i++) {
            var mistake = mistakes[i];
            if (mistake == 0) {
                questionsWithNoMistakes++;
            }
            if (mistake > 0) {
                questionsWithOneOrMoreMistakes++;
            }
        }

        pane.getChildren().add(new Label("Вопросы без ошибок: " + questionsWithNoMistakes));
        pane.getChildren().add(new Label("Вопросы с ошибками: " + questionsWithOneOrMoreMistakes));
    }

    private void destroyEndTab() {
        var tab = tabPane.getTabs().get(tabPane.getTabs().size() - 1);
        tabPane.getTabs().remove(tab);
        isEnd = false;
        rightButton.setText("Далее");
    }

    private void makeEndTab() {
        var tab = new Tab("Результат");
        var pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setSpacing(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        tab.setContent(pane);
        setupEndPane(pane);
        tabPane.getTabs().add(tab);
        var currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            currentTab.setDisable(true);
        }
        tab.setDisable(false);
        isEnd = true;
        rightButton.setText("Выйти");
        leftButton.setVisible(true);
        leftButton.setText("Доп. вопросы");
        tabPane.getSelectionModel().select(tab);
    }

    private void select(int index) {
        var currentTab = tabPane.getSelectionModel().getSelectedItem();
        var newTab = tabPane.getTabs().get(index);
        if (currentTab == newTab) {
            return;
        }
        if (currentTab != null) {
            currentTab.setDisable(true);
        }
        newTab.setDisable(false);
        tabPane.getSelectionModel().select(index);

        leftButton.setVisible(false);
        if (index == (questions.length - 1)) {
            rightButton.setText("Завершить");
        } else {
            rightButton.setText("Далее");
        }
    }

    private void moveOn() {
        if (isEnd) {
            var stage = (Stage) tabPane.getScene().getWindow();
            stage.close();
            return;
        }
        var currentTabInd = tabPane.getSelectionModel().getSelectedIndex();
        if (currentTabInd < (questions.length - 1)) {
            select(currentTabInd + 1);
        } else {
            makeEndTab();
        }
    }

    @FXML
    protected void onRightButtonClick() {
        var currentTabInd = tabPane.getSelectionModel().getSelectedIndex();
        if (currentTabInd < questions.length) {
            var currentTab = tabPane.getSelectionModel().getSelectedItem();
            var question = questions[currentTabInd];
            var result = question.Resolve();
            if (result == QuestionResult.UNANSWERED) {
                showModal(currentTab.getContent().getScene(), "Внимание!", "Необходимо ответить на вопрос!");
                return;
            }
            if (result == QuestionResult.INCORRECT) {
                showModal(currentTab.getContent().getScene(), "Ошибка", "Неверный ответ!");
                mistakes[currentTabInd]++;
                return;
            }

            currentTab.setStyle("-fx-background-color: green");
            moveOn();
        }
    }

    @FXML
    protected void onLeftButtonClick() {
        if (isEnd) {
            showModal(tabPane.getScene(), "Увы", "Доп. вопросы отсутствуют");
        }
    }
}