package com.example.controlwork;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public abstract class BaseQuestion {

    private String questionText;

    protected BaseQuestion(String questionText) {
        this.questionText = questionText;
    }

    public void Setup(Node root) {
        var pane = (Pane) root;
        
        var caption = new Label(questionText);
        caption.setWrapText(true);
        

        pane.getChildren().add(caption);
    }
    public abstract QuestionResult Resolve();
    
}
