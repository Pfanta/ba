package org.combinators.cls.scheduling.control;

import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.IOUtils;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 Class supporting methods to run workflow
 Generates Results from CLS and runs tasks on algorithms */
public class GenerationRunner {
	
	/**
	 AUI callback to GUI
	 */
	final MainWindowAUI mainWindowAUI;
	
	/**
	 Worker thread
	 */
	private AbstractWorker worker;
	
	/**
	 Creates a new instance
	 @param mainWindowAUI GUI callback AUI
	 */
	public GenerationRunner(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}
	
	/**
	 Runs one Task on the worker thread
	 @param task Task to be executed
	 */
	public void run(Task task) {
		worker = new Worker(mainWindowAUI, task);
		worker.start();
	}
	
	/**
	 Runs a benchmark on the algorithms in the repository with random instances of given problem size
	 @param numJobs Number of jobs to be generated
	 @param numMachines Number of machines to be generated
	 @param numInstances Number of instances to be generated and iterated through
	 */
	public void runBenchmark(int numJobs, int numMachines, int numInstances) {
		worker = new BenchmarkWorker(mainWindowAUI, numJobs, numMachines, numInstances);
		worker.start();
	}
	
	/**
	 Runs a benchmark on the algorithms in the repository on taillard's instances
	 @param inputFile File with task data
	 @param outputFile File to save results
	 */
	public void runTaillardBenchmark(File inputFile, File outputFile) throws InterruptedException, IOException {
		TaillardBenchmarkWorker benchmarkWorker = new TaillardBenchmarkWorker(mainWindowAUI, IOUtils.loadTaillard(inputFile));
		worker = benchmarkWorker;
		worker.start();
		worker.join();
		
		FileWriter writer = new FileWriter(outputFile);
		for(Map.Entry<String, Double> entry : benchmarkWorker.getBenchmarkResults().entrySet()) {
			writer.write(entry.getKey() + " : " + entry.getValue() + "%\n");
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 Cancels the execution of the current worker thread if there is any
	 The worker should stop execution after current step
	 */
	public void cancel() {
		if(worker != null)
			worker.cancel();
	}
}
