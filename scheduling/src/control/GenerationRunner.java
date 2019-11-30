package control;

import model.Task;
import scala.Scheduler;
import utils.ClassificationUtils;
import utils.RunnerUtils;
import view.MainWindowAUI;

import java.util.List;

public class GenerationRunner {
	
	private MainWindowAUI mainWindowAUI;
	
	public GenerationRunner(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}
	
	public void run(Task task) {
		new Thread(() -> {
			ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
			mainWindowAUI.onClassificationFinished(classification);
			
			List<String> results = Scheduler.run(classification);
			mainWindowAUI.onGenerationFinished(results.size());
			
			int result = RunnerUtils.runResults(results);
			mainWindowAUI.onRunnerFinished(result);
		}).start();
	}
}
