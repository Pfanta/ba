package org.combinators.cls.scheduling.control;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 Benchmark worker class for benchmarking on taillard instances */
class TaillardBenchmarkWorker extends BenchmarkWorker {
	
	/**
	 Tasks to be executed
	 Tuple is constructed from task (first) and optimal solution or upper bound (second)
	 */
	private final List<Tuple<Task, Integer>> tasks;
	
	/**
	 Creates a new worker with given callback AUI and task to be executed
	 @param callback GUI callback AUI
	 @param tasks Tasks to be executed
	 */
	TaillardBenchmarkWorker(MainWindowAUI callback, List<Tuple<Task, Integer>> tasks) {
		super(callback, -1, -1, 10);
		this.tasks = tasks;
	}
	
	/**
	 Override method from AbstractWorker, that is invoked upon Thread.run() is called
	 Needs to generate algorithms from CLS, run tasks and evaluate results
	 */
	@Override
	void work() {
		Map<String, Double> values = new TreeMap<>();
		int i = 1;
		for(Tuple<Task, Integer> task : tasks) {
			if(!running)
				return;
			
			System.out.println("--- Iteration " + i + " von " + 10 + " ---");
			
			Map<String, Integer> localValues = runTask(task.getFirst()); //run heuristics
			
			callback.onBenchmarkProgress(i / 10F);
			
			//update values in results
			for(Map.Entry<String, Integer> entry : localValues.entrySet()) {
				//catch non-existing values
				Double oldValue = values.get(entry.getKey());
				oldValue = oldValue != null ? oldValue : 0;
				
				//aggregate results in corresponding map entry
				double newValue = (entry.getValue() - task.getSecond()) / (double) task.getSecond();
				
				values.put(entry.getKey(), newValue + oldValue);
			}
			i++;
		}
		
		//set results
		benchmarkResults = new TreeMap<>();
		System.out.println("--- RESULTS ---");
		for(Map.Entry<String, Double> entry : values.entrySet()) {
			//average values from numInstances and round down to two decimal places
			benchmarkResults.put(entry.getKey(), Math.round(entry.getValue() * 1000) / 100D);
			System.out.println(entry.getKey() + " : " + Math.round(entry.getValue() * 1000) / 100D + "%");
		}
		
		callback.onTaillardBenchmarkResult(benchmarkResults);
	}
}
