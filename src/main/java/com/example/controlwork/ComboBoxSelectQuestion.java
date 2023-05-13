package com.example.controlwork;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

public class ComboBoxSelectQuestion extends BaseQuestion {

    private String[] options;
    private final int correctAnswers;
    
    private ComboBox<String> comboBox;

    public ComboBoxSelectQuestion(String text, String[] options, int correctAnswers) {
        super(text);
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public void Setup(Node root) {
        super.Setup(root);
        
        var pane = (Pane) root;
        
        comboBox = new ComboBox<String>();
        comboBox.getItems().addAll(options);
        pane.getChildren().add(comboBox);
    }
    

    @Override
    public QuestionResult Resolve() {
        int answer = comboBox.getSelectionModel().getSelectedIndex();
        
        if(answer == -1) {
            return QuestionResult.UNANSWERED;
        }
        
        return answer == correctAnswers ? QuestionResult.CORRECT : QuestionResult.INCORRECT;
    }
}
