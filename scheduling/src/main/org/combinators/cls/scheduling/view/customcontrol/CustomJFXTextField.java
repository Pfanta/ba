package org.combinators.cls.scheduling.view.customcontrol;

import com.jfoenix.controls.JFXTextField;

/**
 wrapper for custon JFXTextField */
public class CustomJFXTextField extends JFXTextField {
	
	/**
	 Creates a new TextField
	 @param text text
	 @param layoutX Position x
	 @param layoutY Position y
	 @param prefWidth Preferred width
	 @param prefHeight Preferred height
	 */
	public CustomJFXTextField(String text, int layoutX, int layoutY, int prefWidth, int prefHeight) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
		focusedProperty().addListener((observable, oldValue, newValue) -> selectAll());
	}
	
	/**
	 Creates a new TextField
	 @param text text
	 @param layoutX Position x
	 @param layoutY Position y
	 @param prefWidth Preferred width
	 @param prefHeight Preferred height
	 */
	public CustomJFXTextField(int text, int layoutX, int layoutY, int prefWidth, int prefHeight) {
		this(Integer.toString(text), layoutX, layoutY, prefWidth, prefHeight);
	}
}
