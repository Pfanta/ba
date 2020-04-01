package org.combinators.cls.scheduling.control;

import org.combinators.cls.scheduling.model.Classification;
import org.combinators.cls.scheduling.model.Task;
import org.combinators.cls.scheduling.utils.ApplicationUtils;
import org.combinators.cls.scheduling.utils.Tuple;
import org.combinators.cls.scheduling.utils.reflection.Reflect;
import org.combinators.cls.scheduling.utils.reflection.ReflectException;
import org.combinators.cls.scheduling.view.MainWindowAUI;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class for all workers
 */
abstract class AbstractWorker extends Thread {
	/**
	 * GUI callback field
	 */
	protected final MainWindowAUI callback;
	
	/**
	 * Flag to be set to true when worker ist working
	 * Set and false if the worker has finished or cancellation is scheduled
	 */
	protected volatile boolean running;
	
	/**
	 * Counts reflected classes to prevent overriding errors
	 */
	private static int generationCounter = 0;
	
	/**
	 * Creates a new Worker with given callback AUI
	 *
	 * @param callback GUI callback AUI
	 */
	AbstractWorker(MainWindowAUI callback) {
		this.callback = callback;
		this.setDaemon(true);
	}
	
	/**
	 * Override method inherited from java.lang.Thread
	 * Sets running lag and invokes work method
	 */
	@Override
	public void run() {
		this.running = true; //Set running state
		work();
		callback.onFinishedOrCanceled();
		this.running = false; //Set finished
	}
	
	/**
	 * Requests worker cancellation by setting running flag to false
	 */
	void cancel() {
		running = false;
	}
	
	/**
	 * Work method to be implemented in concrete classes
	 */
	abstract void work();
	
	/**
	 * Compiles given runners and runs given task on each of them
	 *
	 * @param classification Classification of task to be executed on algorithms
	 * @param runners algorithm source code to be compiled and run
	 * @param callback GUI callback
	 *
	 * @return Results of the scheduling
	 */
	public static List<Tuple<String, Task>> runResults(Classification classification, Map<String, String> runners, MainWindowAUI callback) {
		List<Tuple<String, Task>> results = new LinkedList<>();
		
		for(String heuristic : runners.keySet()) {
			try {
				Function<Classification, Task> function = Reflect.compile("org.combinators.cls.scheduling.Runner" + generationCounter, runners.get(heuristic).replace("class Runner", "class Runner" + generationCounter)).create().get();
				Task schedule = function.apply(classification.cloned()); //clone to prevent side-effects
				results.add(new Tuple<>(heuristic, schedule));
			} catch(ReflectException e) {
				ApplicationUtils.showException("Reflection exception occurred", "Exception while running results", e);
			}
			callback.onRunnerProgress((float) generationCounter / (float) runners.size());
			generationCounter++;
		}
		
		results.forEach(t -> System.out.println(t.getFirst() + " : " + t.getSecond().getResult()));
		return results;
	}
	
	/**
	 * Returns worker results
	 *
	 * @return Worker results
	 */
	abstract List<Tuple<String, Task>> getSchedulingResults();
}
