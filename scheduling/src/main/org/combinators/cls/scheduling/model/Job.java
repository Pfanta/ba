package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;

public class Job implements IWritable, ICloneable<Job> {

	@Getter
	private final LinkedList<Stage> stages = new LinkedList<>();
	
	@Getter
	@Setter
	@NonNull
	private String name;
	
	@Getter
	@Setter
	private int deadline;
	
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
	
	public Job(String name, int deadline, Stage... stages) {
		this.name = name;
		this.deadline = deadline;
		this.stages.addAll(Arrays.asList(stages));
	}
	
	public void addStage(Stage stage) {
		stages.add(stage);
	}
	
	@Override
	public String toString() {
		return this.name;
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
		Job job = new Job(this.name, this.deadline);
		this.stages.forEach(stage -> job.getStages().add(stage.cloned()));
		return job;
	}
}
