package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedList;

public abstract class Job implements IWritable, ICloneable<Job> {
	@Getter
	protected final LinkedList<MachineTuple> scheduledMachines = new LinkedList<>();
	
	@Getter
	@Setter
	@NonNull
	protected String name;
	
	@Getter
	@Setter
	protected int deadline;
	
	public Job() {
		this("", -1);
	}

	public Job(String name) {
		this(name, -1);
	}
	
	public Job(String name, int deadline) {
		this.name = name;
		this.deadline = deadline;
	}
	
	public String getSchedule() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('|').append(deadline).append('|');
		scheduledMachines.forEach(machineTuple -> builder
				                                          .append(machineTuple.getMachine().getName())
				                                          .append("(t0=")
				                                          .append(machineTuple.getScheduledTime())
				                                          .append(" t=")
				                                          .append(machineTuple.getTime())
				                                          .append(")"));
		return builder.toString();
	}
}
