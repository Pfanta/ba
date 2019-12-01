package org.combinators.cls.scheduling.control;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.RunnerUtils;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.List;

public class GenerationRunner {
	
	private final MainWindowAUI mainWindowAUI;
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
		private final Task task;
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
