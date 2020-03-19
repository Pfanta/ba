package org.combinators.cls.scheduling.view;

import org.combinators.cls.scheduling.model.Classification;

/**
 MainWindow GUI callback interface */
public interface MainWindowAUI {
	/**
	 * @param result Resulting ShopClass found from classification
	 */
	void onClassificationFinished(Classification result);
	
	/**
	 * @param results Result count found by CLS
	 */
	void onGenerationFinished(int results);
	
	/**
	 * @param progress benchmark progress
	 */
	void onBenchmarkProgress(float progress);
	
	/**
	 * @param progress runner progress
	 */
	void onRunnerProgress(float progress);
	
	/**
	 *
	 */
	void onRunnerFinished();
	
	/**
	 @param result result value for target Function e.g. finish time for function C_max
	 */
	void onEvaluationResult(int result);
	
	/**
	 Resets
	 */
	void onFinishedOrCanceled();
}
