package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to model a job containing all available routes, the name, the deadline and the releaseDate
 */
public class Job implements IWritable, ICloneable<Job> {
	
	/**
	 * Available routes for this job
	 */
	@Getter
	private final LinkedList<Route> routes = new LinkedList<>();
	
	/**
	 * Unique name of the job
	 */
	@Getter
	@Setter
	@NonNull
	private String name;
	
	/**
	 * The job's deadline. -1 if the is no deadline
	 */
	@Getter
	@Setter
	private int deadline;
	
	/**
	 * The job's releaseDate. 0 if not specified
	 */
	@Getter
	@Setter
	private int releaseDate;
	
	/**
	 * Creates a job with given name, no deadline and releaseDate 0
	 *
	 * @param name The job's name
	 */
	public Job(String name) {
		this(name, -1, 0);
	}
	
	/**
	 * Creates a job with given name, deadline and releaseDate
	 *
	 * @param name The job's name
	 * @param deadline The job's deadline
	 * @param releaseDate The job's releaseDate
	 */
	public Job(String name, int deadline, int releaseDate) {
		this.name = name;
		this.deadline = deadline;
		this.releaseDate = releaseDate;
	}
	
	/**
	 * Creates a job with given name, deadline, releaseDate and routes
	 *
	 * @param name The job's name
	 * @param deadline The job's deadline
	 * @param releaseDate The job's releaseDate
	 * @param routes The available routes
	 */
	public Job(String name, int deadline, int releaseDate, Route... routes) {
		this.name = name;
		this.deadline = deadline;
		this.releaseDate = releaseDate;
		this.routes.addAll(Arrays.asList(routes));
	}
	
	/**
	 * Adds a route to the job
	 *
	 * @param route Route to be added
	 */
	public void addRoute(Route route) {
		this.routes.add(route);
	}
	
	/**
	 * Returns the scheduled route, that is the route at first position
	 *
	 * @return Scheduled route
	 */
	public Route getScheduledRoute() {
		return routes.get(0);
	}
	
	/**
	 * Returns an ordered list of the machines in the order to be scheduled. Only works for non-flexible job- and open shops
	 *
	 * @return All machines
	 */
	public List<Machine> getMachines() {
		return this.getScheduledRoute().getStages().stream().map(Stage::getScheduledMachine).collect(Collectors.toList());
	}
	
	/**
	 * Returns a string representation of the job
	 *
	 * @return String representation
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Returns a string containing all information saved in the object to be saved
	 *
	 * @return String representation
	 */
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('|').append(deadline).append('|');
		routes.get(0).getStages().forEach(stage -> builder.append(stage.getString()).append('|'));
		return builder.toString();
	}
	
	/**
	 * Deeply clones the object
	 *
	 * @return deeply cloned instance
	 */
	@Override
	public Job cloned() {
		Job job = new Job(this.name, this.deadline, 0);
		this.routes.forEach(route -> job.getRoutes().add(route.cloned()));
		return job;
	}
}
