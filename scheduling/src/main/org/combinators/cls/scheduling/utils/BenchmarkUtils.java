package org.combinators.cls.scheduling.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.Machine;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.view.customcontrol.NumberTextField;

import java.util.*;
import java.util.stream.IntStream;

public class BenchmarkUtils {

	private static final int DEFAULT_MACHINES_COUNT = 5;
	private static final int DEFAULT_JOBS_COUNT = 9;
	private static final int DEFAULT_INSTANCES_COUNT = 10;

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
	
	public static int getOptimalFlowShopSchedule(Task task) {
		if (task.getJobs().size() >= 10) //Safety
			return -1;

		LinkedList<Job> jobs = new LinkedList<>(task.getJobs());

		int[] indexes = new int[jobs.size()];
		IntStream.range(0, indexes.length).forEach(i -> indexes[i] = 0);

		int i = 0, result = Integer.MAX_VALUE;
		while (i < indexes.length) {
			if (indexes[i] < i) {
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
	
	private static Task scheduleFlowShop(Task jobList) {
		Task localSchedule = jobList.cloned();
		Map<Machine, Integer> machineWorkingUntil = new HashMap<>();
		localSchedule.getJobs().getFirst().getMachines().forEach(machine -> machineWorkingUntil.put(machine, 0));
		
		for(Job jobToSchedule : localSchedule.getJobs()) {
			for(int machineIndex = 0; machineIndex < jobToSchedule.getScheduledRoute().getStages().size(); machineIndex++) {
				Machine machine = jobToSchedule.getScheduledRoute().getStages().get(machineIndex).getScheduledMachine();
				
				//finishtime of job
				int t1 = machineIndex == 0 ? 0 : jobToSchedule.getScheduledRoute().getStages().get(machineIndex - 1).getScheduledMachine().getFinishTime();
				
				//finishtime of machine
				int t2 = machineWorkingUntil.get(machine);
				int scheduleTime = Math.max(t1, t2);
				
				machine.setScheduledTime(scheduleTime);
				machineWorkingUntil.put(machine, machine.getFinishTime());
			}
		}
		localSchedule.setResult(localSchedule.getMakespan());
		return localSchedule;
	}
	
	public static class BenchmarkDialogResult {
		@Getter
		private int numMachines;
		@Getter
		private int numJobs;
		@Getter
		private int numInstances;
		
		public BenchmarkDialogResult(int numMachines, int numJobs, int numInstances) {
			this.numMachines = numMachines;
			this.numJobs = numJobs;
			this.numInstances = numInstances;
		}
	}
}
