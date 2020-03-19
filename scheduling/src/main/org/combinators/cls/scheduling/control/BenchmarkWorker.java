package org.combinators.cls.scheduling.control;

import lombok.Getter;
import org.combinators.cls.scheduling.model.Classification;
import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.BenchmarkUtils;
import org.combinators.cls.scheduling.utils.ClassificationUtils;
import org.combinators.cls.scheduling.utils.GenerationUtils;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.view.MainWindowAUI;
import org.combinators.cls.scheduling.view.customDialogs.GenerationDialogResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 Benchmark worker class for benchmarking */
class BenchmarkWorker extends AbstractWorker {
	
	/**
	 Number of instances to be generated
	 */
	private final int numInstances;
	
	/**
	 Tasks to be executed
	 */
	private final List<Task> tasks;
	
	/**
	 Benchmarking results
	 Maps heuristics to average makespan
	 */
	@Getter
	protected volatile Map<String, Double> benchmarkResults;
	
	/**
	 Creates a new worker with given callback AUI and task to be executed
	 @param callback GUI callback AUI
	 @param numJobs Number of jobs to be generated
	 @param numMachines Number of machines to be generated
	 @param numInstances Number of instances to be generated and iterated through
	 */
	BenchmarkWorker(MainWindowAUI callback, int numJobs, int numMachines, int numInstances) {
		super(callback);
		this.numInstances = numInstances;
		this.tasks = new LinkedList<>();
		
		IntStream.range(0, numInstances).forEach(i -> this.tasks.add(GenerationUtils.generateRandomTask(new GenerationDialogResult(numMachines, numJobs, false, ShopClass.FS)))); //Generate random instances
	}
	
	/**
	 Override method from AbstractWorker, that is invoked upon Thread.run() is called
	 Needs to generate algorithms from CLS, run tasks and evaluate results
	 */
	@Override
	void work() {
		boolean small = tasks.get(0).getJobs().size() < 10;
		int i = 1;
		Map<String, Integer> values = new TreeMap<>(); //average makespan
		Map<String, Double> relValues = new TreeMap<>(); //average deviation from optimal solution
		for(Task task : tasks) {
			if(!running)
				return;
			
			System.out.println("--- Iteration " + i + " von " + numInstances + " ---");
			
			Map<String, Integer> localValues = runTask(task); //run heuristics
			callback.onBenchmarkProgress((2F * i - 1) / (2F * numInstances));
			
			int opt = small ? BenchmarkUtils.getOptimalFlowShopSchedule(task) : -1; //calculate optimal schedule
			localValues.put("OPT", opt);
			callback.onBenchmarkProgress((2F * i) / (2F * numInstances));
			
			//update values in results
			for(Map.Entry<String, Integer> entry : localValues.entrySet()) {
				Integer oldValue = values.get(entry.getKey()); //old average makespan
				oldValue = oldValue != null ? oldValue : 0;
				
				Double oldRelValue = relValues.get(entry.getKey()); //old average deviation from optimal solution
				oldRelValue = oldRelValue != null ? oldRelValue : 0;
				
				//aggregate results in corresponding map entry
				values.put(entry.getKey(), entry.getValue() + oldValue);
				relValues.put(entry.getKey(), (entry.getValue() - opt) / (double) opt + oldRelValue);
			}
			i++;
		}
		
		//set results
		benchmarkResults = new TreeMap<>();
		System.out.println("--- RESULTS ---");
		for(Map.Entry<String, Integer> entry : values.entrySet()) {
			//average values from numInstances
			benchmarkResults.put(entry.getKey(), (entry.getValue() / (double) numInstances));
			System.out.println(entry.getKey() + " : " + (entry.getValue() / (double) numInstances) + " : " + Math.round((relValues.get(entry.getKey()) / numInstances) * 1000) / 10D + "%");
		}
	}
	
	/**
	 Runs a single task
	 Also used by TaillardBenchmarkWorker
	 @param task task to be executed
	 @return Map from heuristic to result
	 */
	protected Map<String, Integer> runTask(Task task) {
		Classification classification = ClassificationUtils.classify(task); //reclassify task
		
		Map<String, String> runnerResults = Scheduler.run(classification); //generate results from CLS
		
		List<Tuple<String, Task>> results = runResults(classification, runnerResults, callback); //run results
		
		Map<String, Integer> values = new TreeMap<>();
		results.forEach(t -> values.put(t.getFirst(), t.getSecond().getResult())); //put results into map
		return values;
	}
	
	/**
	 Not implemented in benchmark workers
	 @return Noting
	 */
	@Override
	List<Tuple<String, Task>> getSchedulingResults() {
		throw new IllegalStateException("No schedules available from benchmarks.");
	}
}