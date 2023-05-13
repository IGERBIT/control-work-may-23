package com.example.controlwork;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

public class SingleSelectQuestion extends BaseQuestion {

    private String[] options;
    private int correctAnswer;
    private RadioButton[] radioButtons;
    
    private ToggleGroup toggleGroup = new ToggleGroup();
    
    protected SingleSelectQuestion(String questionText, String[] options, int correctAnswer) {
        super(questionText);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public void Setup(Node root) {
        super.Setup(root);
        
        var pane = (Pane) root;
        
        radioButtons = new RadioButton[options.length];
        for (int i = 0; i < options.length; i++) {
            var radioButton = new RadioButton(options[i]);
            radioButton.setToggleGroup(toggleGroup);
            radioButtons[i] = radioButton;
            pane.getChildren().add(radioButton);
        }
    }
    
    @Override
    public QuestionResult Resolve() {
        int answer = -1;
        for (int i = 0; i < radioButtons.length; i++) {
            var radioButton = radioButtons[i];
            if(radioButton.isSelected()) {
                answer = i;
            }
        }
        
        if(answer == -1) {
            return QuestionResult.UNANSWERED;
        }
        
        return answer == correctAnswer ? QuestionResult.CORRECT : QuestionResult.INCORRECT;
    }
}
