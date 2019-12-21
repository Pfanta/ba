package org.combinators.cls.scheduling.view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.VisualizationUtils;

public class ResultDialog extends Dialog<Boolean> {
	
	public ResultDialog(Task schedule) {
		super();
		this.setTitle("Scheduling result");
		this.setHeaderText("");
		
		WebView webView = new WebView();
		webView.getEngine().loadContent(VisualizationUtils.taskToHTMLGanttChart(schedule));
		
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		this.getDialogPane().setContent(new Pane(webView));
	}
}
