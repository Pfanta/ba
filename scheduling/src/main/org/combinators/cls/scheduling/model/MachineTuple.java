package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

public class MachineTuple implements IWritable, ICloneable<MachineTuple> {
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
	
	public MachineTuple(Machine machine, int time, int scheduledTime) {
		this.machine = machine;
		this.time = time;
		this.scheduledTime = scheduledTime;
	}
	
	public MachineTuple(Machine machine, int time) {
		this(machine, time, -1);
	}
	
	@Override
	public String getString() {
		return machine.getString() + "," + time;
	}
	
	@Override
	public String toString() {
		return machine + "(" + time + ")";
	}
	
	@Override
	public MachineTuple cloned() {
		return new MachineTuple(this.machine.cloned(), this.time, this.scheduledTime);
	}
}
