package org.combinators.cls.scheduling.control;

import lombok.Getter;
import lombok.Setter;
import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.*;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.*;
import java.util.stream.IntStream;

public class GenerationRunner {
	
	private final MainWindowAUI mainWindowAUI;
	private AbstractWorker worker;
	
	@Getter
	@Setter
	private volatile Tuple<String, Task> result;
	
	@Getter
	@Setter
	private volatile List<Tuple<String, Task>> results;
	
	@Getter
	@Setter
	private volatile Map<String, Double> benchmarkResults;
	
	public GenerationRunner(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}
	
	public void run(Task task) {
		worker = new Worker(task, this);
		worker.start();
	}
	
	public void runBenchmark(int numJobs, int numMachines, int numInstances) {
		worker = new BenchmarkWorker(numJobs, numMachines, numInstances);
		worker.start();
	}
	
	public void cancel() {
		worker.cancel();
	}
	
	abstract static class AbstractWorker extends Thread {
		protected volatile boolean running;
		
		void cancel() {
			running = false;
		}
	}
	
	class Worker extends AbstractWorker {
		private final GenerationRunner generationRunner;
		private final Task task;
		
		Worker(Task task, GenerationRunner generationRunner) {
			this.generationRunner = generationRunner;
			this.task = task;
		}
		
		@Override
		public void run() {
			this.running = true;
			work();
			mainWindowAUI.onFinishedOrCanceled();
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
	
	class BenchmarkWorker extends AbstractWorker {
		private final int numJobs;
		private final int numMachines;
		private final int numInstances;
		private final List<Task> tasks;
		
		BenchmarkWorker(int numJobs, int numMachines, int numInstances) {
			this.numJobs = numJobs;
			this.numMachines = numMachines;
			this.numInstances = numInstances;
			this.tasks = new LinkedList<>();
		}
		
		@Override
		public void run() {
			this.running = true;
			IntStream.range(0, numInstances).forEach(i -> this.tasks.add(GenerationUtils.generateRandomTask(new GenerationUtils.GenerationDialogResult(numMachines, numJobs, false, ShopClass.FS))));
			work();
			mainWindowAUI.onFinishedOrCanceled();
		}
		
		private void work() {
			int i = 1;
			Map<String, Integer> values = new TreeMap<>();
			for(Task task : tasks) {
				if(!running)
					return;
				
				System.out.println("--- Iteration " + i++ + " von " + numInstances + " ---");
				
				//run heuristics
				Map<String, Integer> localValues = runTask(task);
				
				//calculate optimal schedule
				localValues.put("OPT", BenchmarkUtils.getOptimalFlowShopSchedule(task));
				
				//update Values in  results
				for(Map.Entry<String, Integer> entry : localValues.entrySet()) {
					//catch non-existing values
					Integer val = values.get(entry.getKey());
					int oldValue = val != null ? val : 0;
					
					//aggregate results in corresponding map entry
					values.put(entry.getKey(), entry.getValue() + oldValue);
				}
				
			}
			
			//set results
			benchmarkResults = new TreeMap<>();
			System.out.println("--- RESULTS ---");
			for(Map.Entry<String, Integer> entry : values.entrySet()) {
				//average values from numInstances
				benchmarkResults.put(entry.getKey(), ((double) entry.getValue() / (double) numInstances));
				System.out.println(entry.getKey() + " : " + ((double) entry.getValue() / (double) numInstances));
			}
		}
		
		private Map<String, Integer> runTask(Task task) {
			//reclassify task
			ClassificationUtils.Classification classification = ClassificationUtils.classify(task);
			
			//generate results from CLS
			Map<String, String> runnerResults = Scheduler.run(classification);
			
			//run results
			List<Tuple<String, Task>> results = RunnerUtils.runResults(classification, runnerResults, mainWindowAUI);
			
			//put results into map
			Map<String, Integer> values = new TreeMap<>();
			results.forEach(t -> values.put(t.getFirst(), t.getSecond().getResult()));
			return values;
		}
	}
}
