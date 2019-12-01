package org.combinators.cls.scheduling.view.customcontrol;

import com.jfoenix.controls.JFXTextField;

public class NumberTextField extends JFXTextField {
	
	public NumberTextField(int text) {
		setText(Integer.toString(text));
	}
	
	@Override
	public void replaceText(int start, int end, String text) {
		if(text.isEmpty() || text.matches("[0-9]"))
			super.replaceText(start, end, text);
	}
	
	@Override
	public void replaceSelection(String text) {
		if(text.isEmpty() || text.matches("[0-9]"))
			super.replaceSelection(text);
	}
	
	public int getNum() {
		return this.getText().length() == 0 ? 0 : Integer.parseInt(this.getText());
	}
}

