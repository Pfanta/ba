package org.combinators.cls.scheduling.view.customdialogs;

import lombok.Getter;

/**
 Container for benchmark dialog results */
public class BenchmarkDialogResult {
	/**
	 Number of machines
	 */
	@Getter
	private final int numMachines;
	
	/**
	 Number of jobs
	 */
	@Getter
	private final int numJobs;
	
	/**
	 Number of instances
	 */
	@Getter
	private final int numInstances;
	
	/**
	 Creates a new container with given values
	 @param numMachines Number of machines
	 @param numJobs Number of jobs
	 @param numInstances Number of instances
	 */
	public BenchmarkDialogResult(int numMachines, int numJobs, int numInstances) {
		this.numMachines = numMachines;
		this.numJobs = numJobs;
		this.numInstances = numInstances;
	}
}
