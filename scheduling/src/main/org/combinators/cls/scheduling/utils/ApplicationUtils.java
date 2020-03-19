package org.combinators.cls.scheduling.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 Utils class on application level to show exceptions and warnings */
public class ApplicationUtils {
	
	/**
	 Flag to prevent application from opening to many dialogs at a time
	 */
	private static final int MAX_DIALOGS = 5;
	
	/**
	 Counter for open dialogs
	 */
	private static int openDialogs = 0;
	
	/**
	 Shows a dialog with exception details
	 @param title dialog title
	 @param content Dialog content
	 @param ex Exception to be shown in expander
	 */
	public static void showException(String title, String content, Exception ex) {
		Platform.runLater(() -> showExceptionDialog(title, content, ex));
	}
	
	/**
	 Shows a dialog with a warning details
	 @param title dialog title
	 @param content Dialog content
	 */
	public static void showWarning(String title, String content) {
		Platform.runLater(() -> showWarningDialog(title, content));
	}
	
	//region private methods
	
	/**
	 Shows a dialog with exception details
	 @param title dialog title
	 @param content Dialog content
	 @param ex Exception to be shown in expander
	 */
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
	
	/**
	 Shows a dialog with a warning details
	 @param title dialog title
	 @param content Dialog content
	 */
	private static void showWarningDialog(String title, String content) {
		if(++openDialogs >= MAX_DIALOGS)
			return;
		
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.showAndWait();
		
		openDialogs = 0;
	}
	//endregion
}
