package org.combinators.cls.scheduling.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApplicationUtils {
	
	public static void showException(String title, String content, Exception ex) {
		Platform.runLater(() -> showExceptionDialog(title, content, ex));
	}
	
	private static void showExceptionDialog(String title, String content, Exception ex) {
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
	}
	
	public static void showWarning(String title, String content) {
		Platform.runLater(() -> showWarningDialog(title, content));
	}
	
	private static void showWarningDialog(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.showAndWait();
	}
	
	/*J1|-1|M1,1,5|M2,1,3|M3,1,3|M4,1,2|*/
	public static Job parse(String input) throws IllegalArgumentException {
		String[] split = input.split("\\|");
		if(split.length < 3)
			throw new IllegalArgumentException("Not a valid String: " + input);
		
		Job job = new Job(split[0], Integer.parseInt(split[1]));
		
		for(int i = 2; i < split.length; i++) {
			String s = split[i];
			String[] machinesSplit = s.split(",");
			
			Stage stage = new Stage(new Machine(machinesSplit[0]), Integer.parseInt(machinesSplit[1]), Integer.parseInt(machinesSplit[2]));
			job.addStage(stage);
		}
		
		return job;
	}
}
