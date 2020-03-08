package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.OptionalInt;

public class Task implements IWritable, ICloneable<Task> {
	
	@Getter
	private LinkedList<Job> jobs = new LinkedList<>();
	
	@Getter
	@Setter
	private int result;
	
	public Task() {}
	
	public Task(LinkedList<Job> jobs) {
		this.jobs.addAll(jobs);
	}
	
	public void add(Job job) {
		jobs.add(job);
	}
	
	public boolean hasDeadlines() {
		return jobs.stream().map(Job::getDeadline).anyMatch(d -> d >= 0);
	}
	
	public int getMakespan() {
		OptionalInt makespan = jobs.stream().mapToInt(job -> job.getScheduledRoute().getStages().getLast().getScheduledMachine().getFinishTime()).max();
		
		return makespan.isPresent() ? makespan.getAsInt() : -1;
	}
	
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		jobs.forEach(job -> builder.append(job.getString()).append("\n"));
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
	
	@Override
	public Task cloned() {
		Task task = new Task();
		task.result = this.result;
		jobs.forEach(job -> task.add(job.cloned()));
		return task;
	}
}
