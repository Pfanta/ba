package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.ArrayList;

public class Task {

	@Getter
	private ArrayList<Job> jobs = new ArrayList<>();

	public void add(Job job) {
		jobs.add(job);
	}

	public boolean hasDeadlines() {
		return jobs.stream().map(Job::getDeadline).anyMatch(d -> d >= 0);
	}
}
