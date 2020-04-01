package org.combinators.cls.scheduling.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Class to model a job's route containing all stages on this route
 */
public class Route implements ICloneable<Route> {
	
	/**
	 * All stages of this route sorted
	 */
	@Getter
	private LinkedList<Stage> stages;
	
	/**
	 * Creates an empty route
	 */
	public Route() {
		this.stages = new LinkedList<>();
	}
	
	/**
	 * Creates a route with gives stages
	 *
	 * @param stages Stages on this route
	 */
	public Route(Stage... stages) {
		this.stages = new LinkedList<>();
		this.stages.addAll(Arrays.asList(stages));
	}
	
	/**
	 * Adds a stage to the route at the end of the list
	 *
	 * @param stage Stage to be added
	 */
	public void addStage(Stage stage) {
		this.stages.add(stage);
	}
	
	/**
	 * Deeply clones the object
	 *
	 * @return deeply cloned instance
	 */
	@Override
	public Route cloned() {
		Route route = new Route();
		this.stages.forEach(stage -> route.getStages().add(stage.cloned()));
		return route;
	}
}
