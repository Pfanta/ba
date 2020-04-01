package org.combinators.cls.scheduling.view.customcontrol;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * Wrapper for custom labels
 */
public class CustomLabel extends Label {
	/**
	 * Creates a new label
	 *
	 * @param text Text content
	 * @param layoutX Position x
	 * @param layoutY Position y
	 * @param prefWidth Preferred width
	 * @param prefHeight Preferred height
	 * @param fontSize Font size
	 */
	public CustomLabel(String text, int layoutX, int layoutY, int prefWidth, int prefHeight, int fontSize) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
		setFont(new Font("Arial", fontSize));
	}
	
	/**
	 * Creates a new label
	 *
	 * @param text Text content
	 * @param layoutX Position x
	 * @param layoutY Position y
	 * @param prefWidth Preferred width
	 * @param prefHeight Preferred height
	 */
	public CustomLabel(String text, int layoutX, int layoutY, int prefWidth, int prefHeight) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
	}
	
	/**
	 * Creates a new label
	 *
	 * @param text Text content
	 * @param layoutX Position x
	 * @param layoutY Position y
	 */
	public CustomLabel(String text, int layoutX, int layoutY) {
		super(text);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
	}
}
