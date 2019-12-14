package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.LinkedList;

public class NonFlexibleJob extends Job {
	@Getter
	protected final LinkedList<MachineTuple> machineTuples = new LinkedList<>();
	
	NonFlexibleJob() {
		super();
	}
	
	NonFlexibleJob(String name) {
		super(name);
	}
	
	NonFlexibleJob(String name, int deadline) {
		super(name, deadline);
	}
	
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('|').append(deadline).append('|');
		machineTuples.forEach(machineTuple -> builder.append(machineTuple.getString()).append('|'));
		return builder.toString();
	}
	
	@Override
	public Job cloned() {
		NonFlexibleJob job = new NonFlexibleJob(this.name, this.deadline);
		this.machineTuples.forEach(machineTuple -> job.getMachineTuples().add(machineTuple.cloned()));
		this.scheduledMachines.forEach(machineTuple -> job.getScheduledMachines().add(machineTuple.cloned()));
		return job;
	}
}
