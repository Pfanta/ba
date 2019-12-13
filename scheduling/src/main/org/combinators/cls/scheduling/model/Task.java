package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.ArrayList;

public class Task implements Writable {
	
	@Getter
	private ArrayList<Job> jobs = new ArrayList<>();
	
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
}
