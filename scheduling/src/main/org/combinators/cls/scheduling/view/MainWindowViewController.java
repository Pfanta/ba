package org.combinators.cls.scheduling.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.combinators.cls.scheduling.control.GenerationRunner;
import org.combinators.cls.scheduling.model.Job;
import org.combinators.cls.scheduling.model.JobMachineTuple;
import org.combinators.cls.scheduling.model.Stage;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ApplicationUtils;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.GenerationUtils;
import org.combinators.cls.scheduling.utils.IOUtils;
import org.combinators.cls.scheduling.view.customDialogs.ProgressDialog;
import org.combinators.cls.scheduling.view.customDialogs.ResultDialog;
import org.combinators.cls.scheduling.view.customcontrol.CustomJFXPlusButton;
import org.combinators.cls.scheduling.view.customcontrol.CustomJFXTextField;
import org.combinators.cls.scheduling.view.customcontrol.CustomLabel;

import java.io.File;
import java.io.IOException;

public class MainWindowViewController implements MainWindowAUI {
	private static final int MAX_JOBS_SHOWN = 20;
	private static final int MAX_MACHINES_SHOWN = 15;
	
	@FXML
	private Pane jobsPane;
	@FXML
	private JFXComboBox<String> comboBox;
	@FXML
	private JFXButton runBtn;
	
	private javafx.stage.Stage stage;
	private ProgressDialog progressDialog;
	
	/* org.combinators.cls.scheduling.model */
	private Task currentTask;
	
	/* org.combinators.cls.scheduling.control */
	private GenerationRunner generationRunner;
	
	void setup(javafx.stage.Stage stage) {
		this.stage = stage;
		this.currentTask = new Task();
		this.generationRunner = new GenerationRunner(this);
		comboBox.getSelectionModel().select(0);

		refreshJobsPane();
	}
	
	private void refreshJobsPane() {
		ObservableList<Node> nodes = jobsPane.getChildren();
		nodes.clear();
		
		//Catch too large tasks
		JobMachineTuple tuple = ClassificationUtils.getTaskDimensions(currentTask);
		if(tuple.getJobCount() > MAX_JOBS_SHOWN || tuple.getMachineCount() > MAX_MACHINES_SHOWN) {
			nodes.add(new CustomLabel("Dimensions of Task too large to visualize.", 50, 50));
			return;
		}
		
		for(int i = 0; i < currentTask.getJobs().size(); i++) {
			nodes.add(new CustomLabel("Job " + i, 10, 10 + i * 40, 50, 30));
			
			int y = 0;
			final Job currentJob = currentTask.getJobs().get(i);
			for(Stage stage : currentJob.getStages()) {
				nodes.add(new CustomJFXTextField(stage.toString(), 60 + y * 110, 10 + i * 40, 100, 30));//TODO: Display Stages
				y++;
			}
			nodes.add(new CustomJFXPlusButton(70 + y * 110, 10 + i * 40, 30, 30, event -> {
				currentJob.addStage(new Stage(null, -1, -1));//FIXME
				refreshJobsPane();
			}));
		}
		nodes.add(new CustomJFXPlusButton(10, 10 + currentTask.getJobs().size() * 40, 30, 30, event -> {
			currentTask.add(new Job());
			refreshJobsPane();
		}));
	}
	
	//region action Handler
	public void onDragOver(DragEvent event) {
		if(event.getDragboard().hasFiles())
			event.acceptTransferModes(TransferMode.MOVE);
		event.consume();
	}
	
	public void onFileDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if(dragboard.hasFiles() && dragboard.getFiles().size() == 1 && dragboard.getFiles().get(0).getName().endsWith(".task"))
			tryLoadFile(dragboard.getFiles().get(0));
		
		event.setDropCompleted(true);
		event.consume();
	}
	
	public void onFileDroppedOnButton(DragEvent event) {
		onFileDropped(event);
		onRunButtonClicked(null);
	}
	
	public void onGenerateButtonClicked(ActionEvent event) {
		GenerationUtils.showGenerateDialog().ifPresent(result -> {
			this.currentTask = GenerationUtils.generateRandomTask(result);
			refreshJobsPane();
		});
	}
	
	public void onLoadButtonClicked(ActionEvent event) {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle("Open Task File");
		fileDialog.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Task Files", "*.task"),
				new FileChooser.ExtensionFilter("All Files", "*.*")
		);
		
		File file = fileDialog.showOpenDialog(stage);
		if(file != null)
			tryLoadFile(file);
	}
	
	public void onSaveButtonClicked(ActionEvent event) {
		if(!ClassificationUtils.validate(currentTask)) {
			ApplicationUtils.showWarning("Invalid Task", "Current Task is not valid.");
			return;
		}
		
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle("Save Task File");
		File file = fileDialog.showSaveDialog(stage);
		if(file != null) {
			try {
				IOUtils.saveTask(file, currentTask);
			} catch(IOException ex) {
				ApplicationUtils.showException("Error", "Error occurred while saving", ex);
			}
		}
	}
	
	public void onRunButtonClicked(ActionEvent event) {
		runBtn.setOpacity(.5);
		generationRunner.run(currentTask);
		
		progressDialog = new ProgressDialog(comboBox.getSelectionModel().getSelectedItem()); // TODO: Use value for generation
		progressDialog.showAndWait().ifPresent(b -> {
			if(b == ButtonType.CANCEL)
				generationRunner.cancel();
			
			if(b == ButtonType.APPLY)
				new ResultDialog(generationRunner.getResult()).show();
		});
	}
	
	private void tryLoadFile(File file) {
		try {
			currentTask = IOUtils.loadTask(file);
			refreshJobsPane();
		} catch(IOException ex) {
			ApplicationUtils.showException("Error", "Error occurred while loading", ex);
		} catch(IllegalArgumentException ex) {
			ApplicationUtils.showException("Error", "Error while parsing File", ex);
		}
	}
	//endregion
	
	//region AUI refreshes
	@Override
	public void onClassificationFinished(ClassificationUtils.Classification classification) {
		Platform.runLater(() -> progressDialog.setClassificationResult(classification));
	}
	
	@Override
	public void onGenerationFinished(int result) {
		Platform.runLater(() -> progressDialog.setGenerationResult(result));
	}
	
	@Override
	public void onRunnerProgress(float progress) {
		Platform.runLater(() -> progressDialog.setRunProgress(progress));
	}
	
	@Override
	public void onRunnerFinished() {
		Platform.runLater(() -> progressDialog.setRunFinished());
	}
	
	@Override
	public void onEvaluationResult(int result) {
		Platform.runLater(() -> progressDialog.setEvaluationResult(result));
	}
	
	@Override
	public void onFinishedOrCanceled() {
		runBtn.setOpacity(1);
	}
	//endregion
}
