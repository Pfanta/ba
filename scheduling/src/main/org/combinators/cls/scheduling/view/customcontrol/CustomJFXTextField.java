package org.combinators.cls.scheduling.view.customcontrol;

import com.jfoenix.controls.JFXTextField;

public class CustomJFXTextField extends JFXTextField {
	public CustomJFXTextField(String text, int layoutX, int layoutY, int prefWidth, int prefHeight) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
	}
}
