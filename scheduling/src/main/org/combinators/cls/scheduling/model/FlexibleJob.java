package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.LinkedList;

public class FlexibleJob extends Job {
	@Getter
	protected final LinkedList<Stage> stages = new LinkedList<>();
	
	FlexibleJob() {
		super();
	}
	
	FlexibleJob(String name) {
		super(name);
	}
	
	FlexibleJob(String name, int deadline) {
		super(name, deadline);
	}
	
	public void addStage(Stage stage) {
		stages.add(stage);
	}
	
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('|').append(deadline).append('|');
		stages.forEach(stage -> builder.append(stage.getString()).append('|'));
		return builder.toString();
	}
	
	@Override
	public Job cloned() {
		FlexibleJob job = new FlexibleJob(this.name, this.deadline);
		this.stages.forEach(stage -> job.getStages().add(stage.cloned()));
		this.scheduledMachines.forEach(machineTuple -> job.getScheduledMachines().add(machineTuple.cloned()));
		return job;
	}
}
