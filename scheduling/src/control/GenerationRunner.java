package control;

import model.Task;
import scala.Scheduler;
import utils.ClassificationUtils;
import utils.RunnerUtils;
import view.MainWindowAUI;

import java.util.List;

public class GenerationRunner {
	
	private MainWindowAUI mainWindowAUI;
	private Worker worker;
	
	public GenerationRunner(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}
	
	public void run(Task task) {
		worker = new Worker(task);
		worker.start();
	}
	
	public void cancel() {
		worker.cancel();
	}
	
	class Worker extends Thread {
		private Task task;
		private volatile boolean running;
		
		Worker(Task task) {
			this.task = task;
		}
		
		@Override
		public void run() {
			this.running = true;
			
			ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
			
			if(running) {
				mainWindowAUI.onClassificationFinished(classification);
				List<String> results = Scheduler.run(classification);
				
				if(running) {
					mainWindowAUI.onGenerationFinished(results.size());
					int result = RunnerUtils.runResults(results);
					
					if(running) {
						mainWindowAUI.onRunnerResult(result);
					}
				}
			}
			
			mainWindowAUI.onFinished();
		}
		
		void cancel() {
			running = false;
		}
	}
}
