package view.customcontrol;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class CustomJFXPlusButton extends JFXButton {
	public CustomJFXPlusButton(int layoutX, int layoutY, int prefWidth, int prefHeight, EventHandler<ActionEvent> handler) {
		super("+");
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		setPrefSize(prefWidth, prefHeight);
		setOnAction(handler);
		setStyle("-fx-background-color:#008e00; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10;");
	}
}
