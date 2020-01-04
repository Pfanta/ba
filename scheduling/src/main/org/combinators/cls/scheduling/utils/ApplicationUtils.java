package org.combinators.cls.scheduling.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApplicationUtils {
	
	private static final int MAX_DIALOGS = 5;
	private static int openDialogs = 0;
	
	public static void showException(String title, String content, Exception ex) {
		Platform.runLater(() -> showExceptionDialog(title, content, ex));
	}
	
	private static void showExceptionDialog(String title, String content, Exception ex) {
		if(++openDialogs >= MAX_DIALOGS)
			return;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(content);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		TextArea textArea = new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
		
		openDialogs = 0;
	}
	
	public static void showWarning(String title, String content) {
		Platform.runLater(() -> showWarningDialog(title, content));
	}
	
	private static void showWarningDialog(String title, String content) {
		if(++openDialogs >= MAX_DIALOGS)
			return;
		
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.showAndWait();
		
		openDialogs = 0;
	}
}
