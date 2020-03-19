package org.combinators.cls.scheduling.model;

import lombok.Getter;

/**
 Classification of a task containing information about shop class, dimensions and other constraints */
public class Classification implements ICloneable<Classification> {
	
	/**
	 The current task that was classified
	 */
	@Getter
	private final Task task;
	
	/**
	 Number of machines
	 */
	@Getter
	private final int machineCount;
	
	/**
	 Number of jobs
	 */
	@Getter
	private final int jobCount;
	
	/**
	 Whether the Task has deadlines
	 */
	@Getter
	private final boolean deadlines;
	
	/**
	 The shop class of the task
	 */
	@Getter
	private final ShopClass shopClass;
	
	/**
	 Creates a new classification
	 @param task Task that was classified
	 @param machineCount Number of machines
	 @param jobCount Number of jobs
	 @param deadlines Whether the Task has deadlines
	 @param shopClass The shop class of the classified task
	 */
	public Classification(Task task, int machineCount, int jobCount, boolean deadlines, ShopClass shopClass) {
		this.task = task;
		this.machineCount = machineCount;
		this.jobCount = jobCount;
		this.deadlines = deadlines;
		this.shopClass = shopClass;
	}
	
	/**
	 Checks whether the shopClass equals Flow Shop or Flexible Flow Shop
	 @return true for shopClasses FS and FFS
	 */
	public boolean isStrictlyFlowShop() {
		return this.shopClass.equals(ShopClass.FS) || this.shopClass.equals(ShopClass.FFS);
	}
	
	/**
	 Checks whether the shopClass equals Job Shop or Flexible Job Shop
	 @return true for shopClasses JS and FJS
	 */
	public boolean isStrictlyJobShop() {
		return this.shopClass.equals(ShopClass.JS) || this.shopClass.equals(ShopClass.FJS);
	}
	
	/**
	 Returns a string representation of the job
	 @return String representation
	 */
	@Override
	public String toString() {
		return "Classification{machineCount=" + machineCount + ", jobCount=" + jobCount + ", shopClass=" + shopClass + "}";
	}
	
	/**
	 Deeply clones the object
	 @return deeply cloned instance
	 */
	@Override
	public Classification cloned() {
		return new Classification(task.cloned(), machineCount, jobCount, deadlines, shopClass);
	}
}
