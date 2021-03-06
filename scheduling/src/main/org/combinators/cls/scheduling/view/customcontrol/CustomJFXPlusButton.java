package org.combinators.cls.scheduling.view.customcontrol;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Wrapper for custom JFXPlusButton
 */
public class CustomJFXPlusButton extends JFXButton {
	
	/**
	 * Creates a new button
	 *
	 * @param layoutX Position x
	 * @param layoutY position y
	 * @param prefWidth Preferred width
	 * @param prefHeight Preferred height
	 * @param handler onClick event handler
	 */
	public CustomJFXPlusButton(int layoutX, int layoutY, int prefWidth, int prefHeight, EventHandler<ActionEvent> handler) {
		super("+");
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
		setOnAction(handler);
		setStyle("-fx-background-color:#008e00; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10;");
	}
}
