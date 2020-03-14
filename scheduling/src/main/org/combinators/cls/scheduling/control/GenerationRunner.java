package org.combinators.cls.scheduling.control;

import lombok.Getter;
import lombok.Setter;
import org.combinators.cls.scheduling.model.ShopClass;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.scala.Scheduler;
import org.combinators.cls.scheduling.utils.*;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public void runTaillardBenchmark(File inputFile, File outputFile) throws InterruptedException, IOException {
		//instances.forEach(t -> System.out.println(t.getSecond() + " | " + t.getFirst().getString()));
		System.out.println("--------------------------------------------------");
		
		worker = new TaillardBenchmarkWorker(IOUtils.loadTaillard(inputFile));
		worker.start();
		worker.join();
		
		FileWriter writer = new FileWriter(outputFile);
		for(Map.Entry<String, Double> entry : benchmarkResults.entrySet()) {
			writer.write(entry.getKey() + " : " + entry.getValue() + "%\n");
		}
		writer.flush();
		writer.close();
	}
	
	public void cancel() {
		worker.cancel();
	}
	
	abstract static class AbstractWorker extends Thread {
		protected volatile boolean running;
		
		AbstractWorker() {
			this.setDaemon(true);
		}

		void cancel() {
			running = false;
		}
	}
	
	class Worker extends AbstractWorker {
		private final GenerationRunner generationRunner;
		private final Task task;
		
		Worker(Task task, GenerationRunner generationRunner) {
			super();
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
			super();
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
			boolean small = tasks.get(0).getJobs().size() < 10;
			int i = 1;
			Map<String, Integer> values = new TreeMap<>();
			Map<String, Double> relValues = new TreeMap<>();
			for (Task task : tasks) {
				if(!running)
					return;
				
				long t1 = System.currentTimeMillis();
				System.out.println("--- Iteration " + i + " von " + numInstances + " ---");
				
				//run heuristics
				Map<String, Integer> localValues = runTask(task);
				
				mainWindowAUI.onBenchmarkProgress((2F * i - 1) / (2F * numInstances));
				
				//calculate optimal schedule
				int opt = small ? BenchmarkUtils.getOptimalFlowShopSchedule(task) : -1;
				localValues.put("OPT", opt);
				
				mainWindowAUI.onBenchmarkProgress((2F * i) / (2F * numInstances));
				System.out.println("t = " + (System.currentTimeMillis() - t1) / 1000D + "s");
				
				//update Values in  results
				for(Map.Entry<String, Integer> entry : localValues.entrySet()) {
					Integer oldValue = values.get(entry.getKey());
					oldValue = oldValue != null ? oldValue : 0;
					
					Double oldRelValue = relValues.get(entry.getKey());
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
				System.out.print(entry.getKey() + " : " + (entry.getValue() / (double) numInstances));
				System.out.println(" : " + Math.round((relValues.get(entry.getKey()) / numInstances) * 1000) / 10D + "%");
			}
		}
		
		protected Map<String, Integer> runTask(Task task) {
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
	
	class TaillardBenchmarkWorker extends BenchmarkWorker {
		private final List<Tuple<Task, Integer>> tasks;
		
		TaillardBenchmarkWorker(List<Tuple<Task, Integer>> tasks) {
			super(-1, -1, 10);
			this.tasks = tasks;
		}
		
		@Override
		public void run() {
			this.running = true;
			work();
			mainWindowAUI.onFinishedOrCanceled();
		}
		
		private void work() {
			int i = 1;
			Map<String, Double> values = new TreeMap<>();
			for(Tuple<Task, Integer> task : tasks) {
				if(!running)
					return;
				
				System.out.println("--- Iteration " + i + " von " + 10 + " ---");
				
				//run heuristics
				Map<String, Integer> localValues = runTask(task.getFirst());
				
				mainWindowAUI.onBenchmarkProgress(i / 10F);
				
				//update Values in  results
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
		}
	}
}
