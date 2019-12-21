package org.combinators.cls.scheduling.view;

import com.jfoenix.controls.JFXProgressBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.view.customcontrol.DialogLabel;

public class ProgressDialog extends Dialog<ButtonType> {
	
	private final JFXProgressBar progressBar;
	private final Label labelNumMachines, labelNumJobs, labelClassification, labelNumResults, labelResult, labelProgress;
	private boolean finished;
	
	public ProgressDialog(String function) {
		super();
		this.setTitle("Running...");
		this.setHeaderText("");

		Pane pane = new Pane();
		pane.setPadding(new Insets(20, 10, 10, 10));

		labelNumMachines = new DialogLabel(150, 0, Pos.CENTER_RIGHT);
		labelNumJobs = new DialogLabel(150, 30, Pos.CENTER_RIGHT);
		labelClassification = new DialogLabel(150, 60, Pos.CENTER_RIGHT);
		labelNumResults = new DialogLabel(150, 90, Pos.CENTER_RIGHT);
		labelResult = new DialogLabel(150, 120, Pos.CENTER_RIGHT);

		progressBar = new JFXProgressBar(0);
		progressBar.setPrefWidth(300);
		progressBar.setLayoutY(160);

		labelProgress = new DialogLabel("1/3 Classification...", 0, 160, Pos.CENTER);
		labelProgress.setPrefWidth(300);
		
		pane.getChildren().addAll(
				new DialogLabel("#Machines:", 0, 0, Pos.CENTER_LEFT),
				new DialogLabel("#Jobs:", 0, 30, Pos.CENTER_LEFT),
				new DialogLabel("Classification:", 0, 60, Pos.CENTER_LEFT),
				new DialogLabel("#Generated Runners:", 0, 90, Pos.CENTER_LEFT),
				new DialogLabel("Result (" + function + "):", 0, 120, Pos.CENTER_LEFT),
				labelNumMachines,
				labelNumJobs,
				labelClassification,
				labelNumResults,
				labelResult,
				progressBar,
				labelProgress);
		
		this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		this.getDialogPane().setContent(pane);
	}
	
	public void setClassificationResult(ClassificationUtils.Classification classification) {
		progressBar.setProgress(1F / 3F);
		labelNumMachines.setText("" + classification.getMachineCount());
		labelNumJobs.setText("" + classification.getJobCount());
		labelClassification.setText("" + classification.getShopClass());
		labelProgress.setText("2/3 Generating...");
	}
	
	public void setGenerationResult(int result) {
		labelNumResults.setText("" + result);
		progressBar.setProgress(2F / 3F);
		labelProgress.setText("2/3 Running...");
	}
	
	public void setRunResult(int result) {
		labelResult.setText("" + result);
		progressBar.setProgress(1);
		labelProgress.setText("Finished.");
		
		getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
		getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		getDialogPane().getButtonTypes().add(ButtonType.APPLY);
		
		((Button) getDialogPane().lookupButton(ButtonType.APPLY)).setText("Show Results");
		finished = true;
	}
}
