package org.combinators.cls.scheduling.view.customdialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.util.Map;

/**
 * Dialog to show scheduling results
 */
public class BenchmarkResultDialog extends Dialog<Void> {
	/**
	 * Creates a new dialog and shows benchmark results
	 *
	 * @param benchmarkResults absolute values
	 * @param relValues relative values
	 */
	public BenchmarkResultDialog(Map<String, Double> benchmarkResults, Map<String, Double> relValues) {
		super();
		this.setTitle("Benchmark results");
		this.setHeaderText("");
		this.initModality(Modality.NONE);
		
		StringBuilder sb = new StringBuilder();
		
		for(Map.Entry<String, Double> entry : benchmarkResults.entrySet()) {
			sb.append(entry.getKey())
					.append(" : ")
					.append(entry.getValue())
					.append(" : ")
					.append(relValues.get(entry.getKey()))
					.append("%\n");
		}
		
		Label label = new Label(sb.toString());
		label.setWrapText(true);
		
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		this.getDialogPane().setContent(label);
	}
	
	/**
	 * Creates a new dialog and shows taillard benchmark results
	 *
	 * @param benchmarkResults results
	 */
	public BenchmarkResultDialog(Map<String, Double> benchmarkResults) {
		super();
		this.setTitle("Taillard Benchmark results");
		this.setHeaderText("");
		this.initModality(Modality.NONE);
		
		StringBuilder sb = new StringBuilder();
		
		for(Map.Entry<String, Double> entry : benchmarkResults.entrySet()) {
			sb.append(entry.getKey())
					.append(" : ")
					.append(entry.getValue())
					.append("%\n");
		}
		
		Label label = new Label(sb.toString());
		label.setWrapText(true);
		
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		this.getDialogPane().setContent(label);
	}
}
