package com.example.controlwork;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

public class MultipleSelectQuestion extends BaseQuestion {

    private String[] options;
    private boolean[] correctAnswers;
    
    private CheckBox[] checkBoxes;

    public MultipleSelectQuestion(String text, String[] options, boolean[] correctAnswers) {
        super(text);
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public void Setup(Node root) {
        super.Setup(root);
        
        var pane = (Pane) root;
        
        checkBoxes = new CheckBox[options.length];
        for (int i = 0; i < options.length; i++) {
            var checkBox = new CheckBox(options[i]);
            checkBoxes[i] = checkBox;
            pane.getChildren().add(checkBox);
        }
    }
    

    @Override
    public QuestionResult Resolve() {
        for (int i = 0; i < checkBoxes.length; i++) {
            var checkBox = checkBoxes[i];
            if (checkBox.isSelected() != correctAnswers[i]) {
                return QuestionResult.INCORRECT;
            }
        }
        return QuestionResult.CORRECT;
    }
}
