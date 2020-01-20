package org.combinators.cls.scheduling.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Job implements IWritable, ICloneable<Job> {
	
	@Getter
	private final LinkedList<Route> routes = new LinkedList<>();
	
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
	
	public Job(String name, int deadline, Route... routes) {
		this.name = name;
		this.deadline = deadline;
		this.routes.addAll(Arrays.asList(routes));
	}
	
	public void addRoute(Route route) {
		this.routes.add(route);
	}
	
	public Route getScheduledRoute() {
		return routes.get(0);
	}
	
	/**
	 @return returns an ordered list of the machines in the order to be scheduled. Only works for non-flexible job- and open shops
	 */
	public List<Machine> getMachines() {
		return this.getScheduledRoute().getStages().stream().map(Stage::getScheduledMachine).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public String getString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('|').append(deadline).append('|');
		routes.get(0).getStages().forEach(stage -> builder.append(stage.getString()).append('|'));
		return builder.toString();
	}
	
	@Override
	public Job cloned() {
		Job job = new Job(this.name, this.deadline);
		this.routes.forEach(route -> job.getRoutes().add(route.cloned()));
		return job;
	}
}
