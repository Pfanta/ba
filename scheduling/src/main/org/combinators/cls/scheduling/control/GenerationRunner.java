package org.combinators.cls.scheduling.control;

import lombok.Getter;
import lombok.Setter;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.RunnerUtils;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.List;

public class GenerationRunner {
	
	private final MainWindowAUI mainWindowAUI;
	private Worker worker;
	
	@Getter
	@Setter
	private volatile Task result;
	
	public GenerationRunner(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}
	
	public void run(Task task) {
		worker = new Worker(task, this);
		worker.start();
	}
	
	public void cancel() {
		worker.cancel();
	}
	
	class Worker extends Thread {
		private final Task task;
		private GenerationRunner generationRunner;
		private volatile boolean running;
		
		Worker(Task task, GenerationRunner generationRunner) {
			this.task = task;
			this.generationRunner = generationRunner;
		}
		
		@Override
		public void run() {
			this.running = true;
			work();
			mainWindowAUI.onFinishedOrCanceled();
		}
		
		void cancel() {
			running = false;
		}
		
		private void work() {
            ClassificationUtils.Classification classification = ClassificationUtils.classify(task);

            if (!running) return;
			
			mainWindowAUI.onClassificationFinished(classification);
			List<String> results = Scheduler.run(classification);
			
			if(!running) return;
			
			mainWindowAUI.onGenerationFinished(results.size());
			Task bestSchedule = RunnerUtils.runResults(classification, results);
			
			if(!running) return;
			
			generationRunner.setResult(bestSchedule);
			mainWindowAUI.onRunnerResult(bestSchedule.getResult());
		}
	}
}
