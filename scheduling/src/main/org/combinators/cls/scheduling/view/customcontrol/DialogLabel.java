package org.combinators.cls.scheduling.view.customcontrol;

import javafx.geometry.Pos;
import javafx.scene.control.Label;


public class DialogLabel extends Label {
	
	public DialogLabel(String text, int posX, int posY, Pos pos) {
		super(text);
		setup(posX, posY, pos);
	}
	
	
	public DialogLabel(int posX, int posY, Pos pos) {
		super();
		setup(posX, posY, pos);
	}
	
	private void setup(int posX, int posY, Pos pos) {
		this.setLayoutX(posX);
		this.setLayoutY(posY);
		this.setAlignment(pos);
		this.setPrefSize(150, 30);
	}
}
