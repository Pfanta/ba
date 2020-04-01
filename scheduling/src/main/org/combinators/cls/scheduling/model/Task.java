package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.OptionalInt;

/**
 * Class to model scheduling task containing all jobs and a result value
 */
public class Task implements IWritable, ICloneable<Task> {
	
	/**
	 * All jobs in this task
	 */
	@Getter
	private LinkedList<Job> jobs = new LinkedList<>();
	
	/**
	 * Result according to target function
	 */
	@Getter
	@Setter
	private int result;
	
	/**
	 * Creates an empty task
	 */
	public Task() {}
	
	/**
	 * Creates a task with given jobs
	 *
	 * @param jobs Jobs to be added to the task
	 */
	public Task(LinkedList<Job> jobs) {
		this.jobs.addAll(jobs);
	}
	
	/**
	 * Adds a job to the task at the end of the list. the order might change due to list sorting
	 *
	 * @param job Job to be added to the task
	 */
	public void add(Job job) {
		jobs.add(job);
	}
	
	/**
	 * Returns if at least one job has deadlines
	 *
	 * @return true if at least one job has a deadline, false if there is none
	 */
	public boolean hasDeadlines() {
		return jobs.stream().map(Job::getDeadline).anyMatch(d -> d >= 0);
	}
	
	/**
	 * returns the total makespan of the scheduled task, -1 if the task has not been scheduled yet
	 *
	 * @return Total makespan Cmax
	 */
	public int getMakespan() {
		OptionalInt makespan = jobs.stream().mapToInt(job -> job.getScheduledRoute().getStages().getLast().getScheduledMachine().getFinishTime()).max();
		
		return makespan.isPresent() ? makespan.getAsInt() : -1;
	}
	
	/**
	 * Returns a string containing all information saved in the object to be saved
	 *
	 * @return String representation
	 */
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		jobs.forEach(job -> builder.append(job.getString()).append("\n"));
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}
	
	/**
	 * Deeply clones the object
	 *
	 * @return deeply cloned instance
	 */
	@Override
	public Task cloned() {
		Task task = new Task();
		task.result = this.result;
		jobs.forEach(job -> task.add(job.cloned()));
		return task;
	}
}
