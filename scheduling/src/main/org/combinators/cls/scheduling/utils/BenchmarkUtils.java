package org.combinators.cls.scheduling.utils;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.view.customcontrol.NumberTextField;
import org.combinators.cls.scheduling.view.customdialogs.BenchmarkDialogResult;

import java.util.*;
import java.util.stream.IntStream;

/**
 Utils for the benchmarking progress */
public class BenchmarkUtils {
	/**
	 Default value for machine count
	 */
	private static final int DEFAULT_MACHINES_COUNT = 5;
	
	/**
	 Default value for job count
	 */
	private static final int DEFAULT_JOBS_COUNT = 9;
	
	/**
	 Default value for instance count
	 */
	private static final int DEFAULT_INSTANCES_COUNT = 10;
	
	/**
	 Shows benchmarking dialog
	 @return Container with dialog results
	 */
	public static Optional<BenchmarkDialogResult> showBenchmarkDialog() {
		Dialog<BenchmarkDialogResult> dialog = new Dialog<>();
		dialog.setTitle("Benchmark Dialog");
		dialog.setHeaderText("Generate random scheduling problems for benchmark");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));
		
		NumberTextField textField1 = new NumberTextField(DEFAULT_MACHINES_COUNT);
		NumberTextField textField2 = new NumberTextField(DEFAULT_JOBS_COUNT);
		NumberTextField textField3 = new NumberTextField(DEFAULT_INSTANCES_COUNT);
		
		//TODO: Add Benchmark support for Job Shops
		/*JFXComboBox<ShopClass> typeBox = new JFXComboBox<>();
		typeBox.getItems().addAll(ShopClass.FS, ShopClass.JS);
		typeBox.getSelectionModel().select(0);*/
		
		grid.add(new Label("#Maschinen:"), 0, 0);
		grid.add(textField1, 0, 1);
		grid.add(new Label("#Jobs:"), 1, 0);
		grid.add(textField2, 1, 1);
		grid.add(new Label("#Instanzen:"), 2, 0);
		grid.add(textField3, 2, 1);
		
		dialog.getDialogPane().setContent(grid);
		Platform.runLater(textField1::requestFocus);
		
		dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? new BenchmarkDialogResult(textField1.getNum(), textField2.getNum(), textField3.getNum()) : null);
		
		return dialog.showAndWait();
	}
	
	/**
	 Shows taillard benchmarking dialog
	 @return Vector of checked instances to benchmark
	 */
	public static Optional<Boolean[]> showTaillardBenchmarkDialog() {
		Dialog<Boolean[]> dialog = new Dialog<>();
		dialog.setTitle("Taillard Benchmark Dialog");
		dialog.setHeaderText("Run Taillard benchmark");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		List<JFXCheckBox> checkboxes = Arrays.asList(
				new JFXCheckBox("20x5"),
				new JFXCheckBox("20x10"),
				new JFXCheckBox("20x20"),
				new JFXCheckBox("50x5"),
				new JFXCheckBox("50x10"),
				new JFXCheckBox("50x20"),
				new JFXCheckBox("100x5"),
				new JFXCheckBox("100x10"),
				new JFXCheckBox("100x20"),
				new JFXCheckBox("200x10"),
				new JFXCheckBox("200x20"),
				new JFXCheckBox("500x20")
		);
		checkboxes.forEach(c -> c.setPadding(new Insets(10, 0, 0, 0)));
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20, 10, 10, 10));
		vbox.getChildren().addAll(checkboxes);
		dialog.getDialogPane().setContent(vbox);
		
		dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? checkboxes.stream().map(CheckBox::isSelected).toArray(Boolean[]::new) : null);
		
		return dialog.showAndWait();
	}
	
	/**
	 Calculates optimal schedule for permutation Flow Shop
	 @param task Task, must be Flow Shop in order to work
	 @return Optimal makespan for number of jobs < 10, -1 otherwise
	 */
	public static int getOptimalFlowShopSchedule(Task task) {
		if(task.getJobs().size() >= 10) //Safety
			return -1;
		
		LinkedList<Job> jobs = new LinkedList<>(task.getJobs());
		
		int[] indexes = new int[jobs.size()];
		IntStream.range(0, indexes.length).forEach(i -> indexes[i] = 0);
		
		int i = 0, result = scheduleFlowShop(new Task(jobs)).getResult();//Integer.MAX_VALUE;
		while(i < indexes.length) {
			if(indexes[i] < i) {
				Collections.swap(jobs, i % 2 == 0 ? 0 : indexes[i], i);
				
				Task schedule = scheduleFlowShop(new Task(jobs));
				
				int res = schedule.getResult();
				
				if(res < result)
					result = res;
				
				indexes[i]++;
				i = 0;
			} else {
				indexes[i] = 0;
				i++;
			}
		}
		System.out.println("OPT : " + result);
		return result;
	}
	
	/**
	 Schedules a permutation Flow Shop sequence
	 @param jobList Ordered sequence of jobs
	 @return Scheduled task
	 */
	private static Task scheduleFlowShop(Task jobList) {
		Task localSchedule = jobList.cloned();
		Map<Machine, Integer> machineWorkingUntil = new HashMap<>();
		localSchedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
		
		for(Job jobToSchedule : localSchedule.getJobs()) {
			for(int machineIndex = 0; machineIndex < jobToSchedule.getScheduledRoute().getStages().size(); machineIndex++) {
				Machine machine = jobToSchedule.getScheduledRoute().getStages().get(machineIndex).getScheduledMachine();
				
				//finish time of job
				int t1 = machineIndex == 0 ? 0 : jobToSchedule.getScheduledRoute().getStages().get(machineIndex - 1).getScheduledMachine().getFinishTime();
				
				//finish time of machine
				int t2 = machineWorkingUntil.get(machine);
				int scheduleTime = Math.max(t1, t2);
				
				machine.setScheduledTime(scheduleTime);
				machineWorkingUntil.put(machine, machine.getFinishTime());
			}
		}
		localSchedule.setResult(localSchedule.getMakespan());
		return localSchedule;
	}
}
