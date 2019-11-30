package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import control.GenerationRunner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.Job;
import model.Stage;
import model.Task;
import utils.ApplicationUtils;
import utils.ClassificationUtils;
import utils.GenerationUtils;
import view.customcontrol.CustomJFXPlusButton;
import view.customcontrol.CustomJFXTextField;
import view.customcontrol.CustomLabel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class MainWindowViewController implements MainWindowAUI {
	
	/* control */
	GenerationRunner generationRunner;
	@FXML
	private Pane jobsPane;
	@FXML
	private JFXComboBox<String> comboBox;
	@FXML
	private JFXButton runBtn;
	private javafx.stage.Stage stage;
	/* model */
	private Task currentTask;
	
	void setup(javafx.stage.Stage stage) {
		this.stage = stage;
		this.currentTask = Task.empty();
		this.generationRunner = new GenerationRunner(this);
		comboBox.getSelectionModel().select(0);
		
		refreshJobsPane();
	}
	
	private void refreshJobsPane() {
		ObservableList<Node> nodes = jobsPane.getChildren();
		nodes.clear();
		
		for(int i = 0; i < currentTask.getJobs().size(); i++) {
			nodes.add(new CustomLabel("Job " + i, 10, 10 + i * 40, 50, 30));
			
			int y = 0;
			final Job currentJob = currentTask.getJobs().get(i);
			for(Stage stage : currentJob.getStages()) {
				nodes.add(new CustomJFXTextField(stage.toString(), 60 + y * 110, 10 + i * 40, 100, 30));//TODO: Display Stages
				y++;
			}
			nodes.add(new CustomJFXPlusButton(70 + y * 110, 10 + i * 40, 30, 30, event -> {
				currentJob.addStage(new Stage());
				refreshJobsPane();
			}));
		}
		nodes.add(new CustomJFXPlusButton(10, 10 + currentTask.getJobs().size() * 40, 30, 30, event -> {
			currentTask.add(Job.empty());
			refreshJobsPane();
		}));
	}
	
	private void refreshClassification() {
		ClassificationUtils.Classification classification = ClassificationUtils.classify(currentTask);
	}
	
	private void load(File file) throws IOException {
		currentTask = Task.empty();
		
		try {
			Files.lines(file.toPath()).forEach(s -> currentTask.add(ApplicationUtils.parse(s)));
		} catch(IllegalArgumentException ex) {
			ApplicationUtils.showException("Error", "Error while parsing File", ex);
		}
	}
	
	private void save(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("test");//TODO: Write actual data
		writer.close();
	}
	
	//region action Handler
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
		if(file != null) {
			try {
				load(file);
			} catch(IOException ex) {
				ApplicationUtils.showException("Error", "Error occured while loading", ex);
			}
		}
	}
	
	public void onSaveButtonClicked(ActionEvent event) {
		FileChooser fileDialog = new FileChooser();
		fileDialog.setTitle("Save Task File");
		File file = fileDialog.showSaveDialog(stage);
		if(file != null) {
			try {
				save(file);
			} catch(IOException ex) {
				ApplicationUtils.showException("Error", "Error occured while saving", ex);
			}
		}
	}
	
	public void onRunButtonClicked(ActionEvent event) {
		runBtn.setOpacity(.5);
		generationRunner.run(currentTask);
	}
	
	
	@Override
	public void onClassificationFinished(ClassificationUtils.Classification result) {
		System.out.println("classification");
	}
	
	@Override
	public void onGenerationFinished(int results) {
		System.out.println("generation");
	}
	
	@Override
	public void onRunnerFinished(int result) {
		runBtn.setOpacity(1);
		
		System.out.println("runner");
	}
	//endregion
}
