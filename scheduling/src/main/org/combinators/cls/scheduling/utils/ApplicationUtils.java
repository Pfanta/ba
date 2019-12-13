package org.combinators.cls.scheduling.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ApplicationUtils {
	public static void showException(String title, String content, Exception ex) {
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
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(content);
		alert.showAndWait();
	}
	
	/*J1|D-1|M1,5|M2,3|M3,3|M4,2|*/
	public static Job parse(String input) throws IllegalArgumentException {
		String[] split = input.split("\\|");
		if(split.length < 3)
			throw new IllegalArgumentException("Not a valid String: " + input);
		
		Job job = new Job(split[0], Integer.parseInt(split[1]));
		
		Set<Machine> allMachines = new HashSet<>();
		for(int i = 2; i < split.length; i++) {
			Stage stage = new Stage();
			String s = split[i];
			String[] machinesSplit = s.split(";");

			Arrays.stream(machinesSplit).forEach(t -> {
				String[] l = t.split(",");
				Machine machine = new Machine(l[0]);
				stage.addMachine(machine, Integer.parseInt(l[1]));
				
				if(!allMachines.add(machine))
					throw new IllegalArgumentException("Duplicate Machine :" + machine);
			});
			
			job.addStage(stage);
		}
		
		return job;
	}
}
