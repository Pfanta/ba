package org.combinators.cls.scheduling.view;

import org.combinators.cls.scheduling.model.Classification;

import java.util.Map;

/**
 * MainWindow GUI callback interface
 */
public interface MainWindowAUI {
	/**
	 * Called upon classification finished
	 *
	 * @param result Resulting ShopClass found from classification
	 */
	void onClassificationFinished(Classification result);
	
	/**
	 * Called upon generation finished
	 *
	 * @param result Result count found by CLS
	 */
	void onGenerationFinished(int result);
	
	/**
	 * Called upon benchmark progress
	 *
	 * @param progress benchmark progress
	 */
	void onBenchmarkProgress(float progress);
	
	/**
	 * Called upon runner progress
	 *
	 * @param progress runner progress
	 */
	void onRunnerProgress(float progress);
	
	/**
	 * Called upon runner finished
	 */
	void onRunnerFinished();
	
	/**
	 * Called upon evaluation finished
	 *
	 * @param result result value for target Function e.g. finish time for function C_max
	 */
	void onEvaluationResult(int result);
	
	/**
	 * Called upon benchmark finished
	 *
	 * @param benchmarkResults absolute values
	 * @param relValues relative values
	 */
	void onBenchmarkResult(Map<String, Double> benchmarkResults, Map<String, Double> relValues);
	
	/**
	 * Called upon taillard benchmark finished
	 *
	 * @param benchmarkResults results
	 */
	void onTaillardBenchmarkResult(Map<String, Double> benchmarkResults);
	
	/**
	 * Resets state
	 */
	void onFinishedOrCanceled();
}
