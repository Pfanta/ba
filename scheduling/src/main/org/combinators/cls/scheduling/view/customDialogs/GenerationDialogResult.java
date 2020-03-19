package org.combinators.cls.scheduling.view.customDialogs;

import lombok.Getter;
import org.combinators.cls.scheduling.model.ShopClass;

/**
 Container for generation dialog results */
public class GenerationDialogResult {
	/**
	 Number of machines
	 */
	@Getter
	private int numMachines;
	
	/**
	 Number of jobs
	 */
	@Getter
	private int numJobs;
	
	/**
	 True if at least one job has a deadline
	 */
	@Getter
	private boolean deadlines;
	
	/**
	 Shop class
	 */
	@Getter
	private ShopClass shopClass;
	
	/**
	 Creates a new container with given values
	 @param numMachines Number of machines
	 @param numJobs Number of jobs
	 @param deadlines wether the task has deadlines
	 @param shopClass The shop class
	 */
	public GenerationDialogResult(int numMachines, int numJobs, boolean deadlines, ShopClass shopClass) {
		this.numMachines = numMachines;
		this.numJobs = numJobs;
		this.deadlines = deadlines;
		this.shopClass = shopClass;
	}
}
