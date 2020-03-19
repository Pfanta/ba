package org.combinators.cls.scheduling.view.customcontrol;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 Wrapper for dialog labels */
public class DialogLabel extends Label {
	/**
	 Creates a new label
	 @param text Text content
	 @param posX Position x
	 @param posY Position y
	 @param pos Text alignment position
	 */
	public DialogLabel(String text, int posX, int posY, Pos pos) {
		super(text);
		setup(posX, posY, pos);
	}
	
	/**
	 Creates a new empty label
	 @param posX Position x
	 @param posY Position y
	 @param pos Text alignment position
	 */
	public DialogLabel(int posX, int posY, Pos pos) {
		super();
		setup(posX, posY, pos);
	}
	
	/**
	 Sets up the label
	 @param posX Position x
	 @param posY Position y
	 @param pos Text alignment position
	 */
	private void setup(int posX, int posY, Pos pos) {
		this.setLayoutX(posX);
		this.setLayoutY(posY);
		this.setAlignment(pos);
		this.setPrefSize(150, 30);
	}
}
