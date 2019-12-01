package org.combinators.cls.scheduling.utils;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.view.customcontrol.NumberTextField;

import java.util.Optional;

public class GenerationUtils {
	
	private static final int DEFAULT_MACHINES_COUNT = 4;
	private static final int DEFAULT_JOBS_COUNT = 4;
	
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
		typeBox.getItems().addAll(ShopClass.FS, ShopClass.FFS, ShopClass.JS);
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
		
		dialog.setResultConverter(dialogButton -> new GenerationDialogResult(textField1.getNum(), textField2.getNum(), deadlines.isSelected(), typeBox.getSelectionModel().getSelectedItem()));
		
		return dialog.showAndWait();
	}
	
	/**
	 @param result GenerationDialogResult
	 @return Task
	 */
	public static Task generateRandomTask(GenerationDialogResult result) {
		return generateRandomTask(result.getMachineCount(), result.getJobCount(), result.isDeadlines(), result.getShopClass());
	}
	
	/**
	 @param i number of machines
	 @param j number of jobs
	 @param deadlines whether deadlines should be generated
	 @param shopClass Class of the shop to be generated
	 @return Task
	 */
	private static Task generateRandomTask(int i, int j, boolean deadlines, ShopClass shopClass) {
		return Task.empty(); //TODO
	}
	
	public static class GenerationDialogResult {
		@Getter
		private int machineCount;
		@Getter
		private int jobCount;
		@Getter
		private boolean deadlines;
		@Getter
		private ShopClass shopClass;
		
		GenerationDialogResult(int machineCount, int jobCount, boolean deadlines, ShopClass shopClass) {
			this.machineCount = machineCount;
			this.jobCount = jobCount;
			this.deadlines = deadlines;
			this.shopClass = shopClass;
		}
	}
}
