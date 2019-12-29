package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

public class Stage implements IWritable, ICloneable<Stage> {
	@Getter
	private Machine machine;
	
	/**
	 Time that the Jo has to be processed on this Machine
	 */
	@Getter
	@Setter
	private int duration;
	
	/**
	 Time when Job is scheduled at this machine
	 */
	@Getter
	@Setter
	private int scheduledTime;
	
	/**
	 How many identical machines there are to process this job
	 if machineCount is larger than 1 the job is considered flexible
	 */
	@Getter
	@Setter
	private int machineCount;
	
	public Stage(Machine machine, int machineCount, int duration, int scheduledTime) {
		this.machine = machine;
		this.machineCount = machineCount;
		this.duration = duration;
		this.scheduledTime = scheduledTime;
	}
	
	public Stage(Machine machine, int machineCount, int duration) {
		this(machine, machineCount, duration, -1);
	}
	
	public int getFinishTime() {
		return scheduledTime + duration;
	}
	
	@Override
	public String getString() {
		return machineCount + "x " + machine.getString() + "," + duration;
	}
	
	@Override
	public String toString() {
		return machineCount + "x " + machine + "(" + duration + ")";
	}
	
	@Override
	public Stage cloned() {
		return new Stage(this.machine.cloned(), this.machineCount, this.duration, this.scheduledTime);
	}
}
