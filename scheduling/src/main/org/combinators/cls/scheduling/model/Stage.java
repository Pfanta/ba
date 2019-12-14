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
	private int time;
	
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
	
	public Stage(Machine machine, int machineCount, int time, int scheduledTime) {
		this.machine = machine;
		this.machineCount = machineCount;
		this.time = time;
		this.scheduledTime = scheduledTime;
	}
	
	public Stage(Machine machine, int machineCount, int time) {
		this(machine, machineCount, time, -1);
	}
	
	@Override
	public String getString() {
		return machineCount + "x " + machine.getString() + "," + time;
	}
	
	@Override
	public String toString() {
		return machineCount + "x " + machine + "(" + time + ")";
	}
	
	@Override
	public Stage cloned() {
		return new Stage(this.machine.cloned(), this.time, this.scheduledTime);
	}
}
