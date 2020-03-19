package org.combinators.cls.scheduling.control;

import lombok.Getter;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.RunnerUtils;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 Worker class for algorithm generation and task execution */
class Worker extends AbstractWorker {
	
	/**
	 Task to be executed
	 */
	private final Task task;
	
	/**
	 Scheduling results
	 Tuple is constructed from chosen heuristic (first) and schedule (second)
	 */
	@Getter
	protected volatile List<Tuple<String, Task>> results;
	
	/**
	 Creates a new worker with given callback AUI and task to be executed
	 @param callback GUI callback AUI
	 @param task Task to be executed
	 */
	Worker(MainWindowAUI callback, Task task) {
		super(callback);
		this.task = task;
	}
	
	/**
	 Override method from AbstractWorker, that is invoked upon Thread.run() is called
	 Needs to generate algorithms from CLS, run task and evaluate results
	 */
	@Override
	void work() {
		ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
		
		if(!running) return;
		
		callback.onClassificationFinished(classification);
		Map<String, String> runnerResults = Scheduler.run(classification);
		
		if(!running) return;
		
		callback.onGenerationFinished(runnerResults.size());
		results = RunnerUtils.runResults(classification, runnerResults, callback);
		
		if(!running) return;
		
		callback.onRunnerFinished();
		results.sort(Comparator.comparingInt(t -> t.getSecond().getResult()));
		
		if(!running) return;
		
		callback.onEvaluationResult(results.get(0).getSecond().getResult());
	}
}
