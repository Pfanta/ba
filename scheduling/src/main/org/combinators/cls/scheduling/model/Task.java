package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class Task implements IWritable, ICloneable<Task> {
	
	@Getter
	private LinkedList<Job> jobs = new LinkedList<>();
	
	@Getter
	@Setter
	private int result;
	
	public void add(Job job) {
		jobs.add(job);
	}
	
	public boolean hasDeadlines() {
		return jobs.stream().map(Job::getDeadline).anyMatch(d -> d >= 0);
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
