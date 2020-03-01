package org.combinators.cls.scheduling.control;

import lombok.Getter;
import lombok.Setter;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.RunnerUtils;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GenerationRunner {
	
	private final MainWindowAUI mainWindowAUI;
	private Worker worker;
	
	@Getter
	@Setter
	private volatile Tuple<String, Task> result;
	
	@Getter
	@Setter
	private volatile List<Tuple<String, Task>> results;
	
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
		private final GenerationRunner generationRunner;
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
			
			if(!running) return;
			
			mainWindowAUI.onClassificationFinished(classification);
			Map<String, String> runnerResults = Scheduler.run(classification);
			
			if(!running) return;
			
			mainWindowAUI.onGenerationFinished(runnerResults.size());
			results = RunnerUtils.runResults(classification, runnerResults, mainWindowAUI);
			
			if(!running) return;
			
			mainWindowAUI.onRunnerFinished();
			results.sort(Comparator.comparingInt(t -> t.getSecond().getResult()));
			
			if(!running) return;
			
			generationRunner.setResult(results.get(0));
			mainWindowAUI.onEvaluationResult(result.getSecond().getResult());
		}
	}
}
