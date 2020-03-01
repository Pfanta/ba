package org.combinators.cls.scheduling.view.customDialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.utils.VisualizationUtils;

public class ResultDialog extends Dialog<Boolean> {
	public ResultDialog(Tuple<String, Task> schedule) {
		super();
		this.setTitle("Scheduling result: " + schedule.getFirst());
		this.setHeaderText("");
		this.initModality(Modality.NONE);
		
		WebView webView = new WebView();
		webView.getEngine().loadContent(VisualizationUtils.taskToHTMLGanttChart(schedule.getSecond()));
		
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		this.getDialogPane().setContent(new Pane(webView));
	}
}
