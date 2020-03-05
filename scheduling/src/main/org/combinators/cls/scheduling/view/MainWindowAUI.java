package org.combinators.cls.scheduling.view;

import org.combinators.cls.scheduling.utils.ClassificationUtils;

public interface MainWindowAUI {
    /**
     * @param result Resulting ShopClass found from classification
     */
    void onClassificationFinished(ClassificationUtils.Classification result);

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
