package org.combinators.cls.scheduling.utils;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.combinators.cls.scheduling.model.*;
import org.combinators.cls.scheduling.view.customcontrol.NumberTextField;
import org.combinators.cls.scheduling.view.customdialogs.GenerationDialogResult;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

/**
 Utils for task generation */
public class GenerationUtils {
	/**
	 Default value for machine count
	 */
	private static final int DEFAULT_MACHINES_COUNT = 4;
	
	/**
	 Default value for job count
	 */
	private static final int DEFAULT_JOBS_COUNT = 4;
	
	/**
	 Lower bound for random machine times
	 */
	private static final int MIN_MACHINE_TIME = 1;
	
	/**
	 Upper bound for random machine times
	 */
	private static final int MAX_MACHINE_TIME = 6;
	
	/**
	 Maximum the deadline may exceed no-delay makespan
	 */
	private static final int MAX_DEADLINE_PLUS = 10;
	
	/**
	 Instance of random number generator
	 */
	private static final Random random = new Random();
	
	/**
	 Shows generation dialog
	 @return Container containing questioned values
	 */
	public static Optional<GenerationDialogResult> showGenerateDialog() {
		Dialog<GenerationDialogResult> dialog = new Dialog<>();
		dialog.setTitle("Generation Dialog");
		dialog.setHeaderText("Generate random scheduling problem");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));
		
		NumberTextField textField1 = new NumberTextField(DEFAULT_MACHINES_COUNT);
		NumberTextField textField2 = new NumberTextField(DEFAULT_JOBS_COUNT);
		JFXComboBox<ShopClass> typeBox = new JFXComboBox<>();
		typeBox.getItems().addAll(ShopClass.FS, ShopClass.JS);
		typeBox.getSelectionModel().select(0);
		JFXCheckBox deadlines = new JFXCheckBox("Deadlines");
		
		grid.add(new Label("#Maschinen:"), 0, 0);
		grid.add(textField1, 0, 1);
		grid.add(new Label("#Jobs:"), 1, 0);
		grid.add(textField2, 1, 1);
		grid.add(new Label("Type:"), 2, 0);
		grid.add(typeBox, 2, 1);
		grid.add(deadlines, 0, 2);
		
		dialog.getDialogPane().setContent(grid);
		Platform.runLater(textField1::requestFocus);
		
		dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? new GenerationDialogResult(textField1.getNum(), textField2.getNum(), deadlines.isSelected(), typeBox.getSelectionModel().getSelectedItem()) : null);
		
		return dialog.showAndWait();
	}
	
	/**
	 Generates a random task
	 @param result Generation dialog result
	 @return Task Generated task
	 */
	public static Task generateRandomTask(GenerationDialogResult result) {
		if(result.getNumJobs() <= 0 || result.getNumMachines() <= 0 || result.getShopClass().equals(ShopClass.NONE))
			throw new IllegalArgumentException();
		
		switch(result.getShopClass()) {
			case FS:
				return generateRandomFlowShop(result.getNumMachines(), result.getNumJobs(), result.isDeadlines());
			case JS:
				return generateRandomJobShop(result.getNumMachines(), result.getNumJobs(), result.isDeadlines());
			
			case FFS:
				return generateRandomFlexibleFlowShop(result.getNumMachines(), result.getNumJobs(), result.isDeadlines());
			
			case FJS:
				return generateRandomFlexibleJobShop(result.getNumMachines(), result.getNumJobs(), result.isDeadlines());
			
			default:
				return new Task();
		}
	}
	
	/**
	 Generates a random FlowShop
	 @param numMachines number of machines
	 @param numJobs number of jobs
	 @param deadlines whether deadlines should be generated
	 @return Task
	 */
	private static Task generateRandomFlowShop(int numMachines, int numJobs, boolean deadlines) {
		Task task = new Task();
		
		for(int i = 0; i < numJobs; i++) {
			Job job = new Job("J" + i);
			
			Route route = new Route();
			job.addRoute(route);
			
			int totalTime = 0;
			for(int j = 0; j < numMachines; j++) {
				int time = random.nextInt(MAX_MACHINE_TIME - MIN_MACHINE_TIME) + MIN_MACHINE_TIME;
				totalTime += time;
				
				Machine machine = new Machine("M" + j, time);
				Stage stage = new Stage(machine);
				route.addStage(stage);
			}
			if (deadlines)
				job.setDeadline(totalTime + random.nextInt(MAX_DEADLINE_PLUS) * numMachines);
			
			task.add(job);
		}
		
		return task;
	}
	
	/**
	 Generates a random JobShop
	 @param numMachines number of machines
	 @param numJobs number of jobs
	 @param deadlines whether deadlines should be generated
	 @return Task
	 */
	private static Task generateRandomJobShop(int numMachines, int numJobs, boolean deadlines) {
		Task task = generateRandomFlowShop(numMachines, numJobs, deadlines);
		
		do {
			task.getJobs().forEach(job -> Collections.shuffle(job.getScheduledRoute().getStages()));
		} while(!ClassificationUtils.classify(task).isStrictlyJobShop());
		
		return task;
	}
	
	/**
	 Generates a random FlexibleFlowShop
	 @param numMachines number of machines
	 @param numJobs number of jobs
	 @param deadlines whether deadlines should be generated
	 @return Task
	 */
	private static Task generateRandomFlexibleFlowShop(int numMachines, int numJobs, boolean deadlines) {
		return makeFlexible(generateRandomFlowShop(numMachines, numJobs, deadlines));
	}
	
	/**
	 Generates a random FlexibleJobShop
	 @param numMachines number of machines
	 @param numJobs number of jobs
	 @param deadlines whether deadlines should be generated
	 @return Task
	 */
	private static Task generateRandomFlexibleJobShop(int numMachines, int numJobs, boolean deadlines) {
		return makeFlexible(generateRandomJobShop(numMachines, numJobs, deadlines));
	}
	
	/**
	 Turns given Task into a flexible one
	 @param task Task
	 @return flexible Task
	 */
	private static Task makeFlexible(Task task) {
		//TODO
		task.getJobs().forEach(job -> job.getScheduledRoute().getStages().getFirst().getMachines().add(new Machine("FlexMachine", 1)));
		return task;
	}
}
