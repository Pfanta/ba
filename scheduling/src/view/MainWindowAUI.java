package view;

import utils.ClassificationUtils;

public interface MainWindowAUI {
	/**
	 @param result Resulting ShopClass found from classification
	 */
	void onClassificationFinished(ClassificationUtils.Classification result);
	
	/**
	 @param results Result count found by CLS
	 */
	void onGenerationFinished(int results);
	
	/**
	 @param result result value for target Function e.g. finish time for function C_max
	 */
	void onRunnerFinished(int result);
}
