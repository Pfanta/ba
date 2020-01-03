package org.combinators.cls.scheduling.view.customcontrol;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class CustomLabel extends Label {
	
	public CustomLabel(String text, int layoutX, int layoutY, int prefWidth, int prefHeight, int fontSize) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
		setFont(new Font("Arial", fontSize));
	}
	
	public CustomLabel(String text, int layoutX, int layoutY, int prefWidth, int prefHeight) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
	}
	
	public CustomLabel(String text, int layoutX, int layoutY) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
	}
}
