package org.combinators.cls.scheduling.view.customcontrol;

import com.jfoenix.controls.JFXTextField;

/**
 Wrapper to restrict text field input to numbers */
public class NumberTextField extends JFXTextField {
	/**
	 Creates a new control
	 @param text Initial number
	 */
	public NumberTextField(int text) {
		setText(Integer.toString(text));
	}
	
	/**
	 Override Method from TextField
	 @param start StartIndex
	 @param end EndIndex
	 @param text Text
	 */
	@Override
	public void replaceText(int start, int end, String text) {
		if(text.isEmpty() || text.matches("[0-9]"))
			super.replaceText(start, end, text);
	}
	
	/**
	 Override Method from TextField
	 @param text Text
	 */
	@Override
	public void replaceSelection(String text) {
		if(text.isEmpty() || text.matches("[0-9]"))
			super.replaceSelection(text);
	}
	
	/**
	 Returns the number entered
	 @return The number entered
	 */
	public int getNum() {
		return this.getText().length() == 0 ? 0 : Integer.parseInt(this.getText());
	}
}

