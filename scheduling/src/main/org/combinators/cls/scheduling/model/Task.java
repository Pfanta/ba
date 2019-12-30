package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Task implements IWritable, ICloneable<Task> {
	
	@Getter
	private LinkedList<Job> jobs;
	
	@Getter
	@Setter
	private int result;
	
	public Task() {
		this.jobs = new LinkedList<>();
	}
	
	public Task(LinkedList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public void add(Job job) {
		jobs.add(job);
	}
	
	public boolean hasDeadlines() {
		return jobs.stream().map(Job::getDeadline).anyMatch(d -> d >= 0);
	}
	
	public List<Machine> getAllMachines() {
		return getMachines().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
	}
	
	public List<List<Machine>> getMachines() {
		return jobs.stream().map(job -> job.getStages().stream()
				                                .map(Stage::getMachine)
				                                .collect(Collectors.toList()))
				       .collect(Collectors.toList());
	}
	
	public int getMakespan() {
		OptionalInt makespan = jobs.stream().mapToInt(job -> job.getStages().getLast().getFinishTime()).max();
		
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
